package org.dhallj.generic

import org.dhallj.codec.syntax._
import org.dhallj.generic.example._
import org.dhallj.syntax._
import GenericDecoder._
import org.dhallj.codec.DecodingFailure
import org.dhallj.generic.example.akka.{Akka, Http, OnOrOff, Preview, Server}

class GenericDecoderSpec extends munit.FunSuite {
  test("Load nested case classes") {
    val input =
      """
        |let topLevel = "com"
        |let somePort = 5000
        |in {
        |  db = {
        |    host = "host.${topLevel}",
        |    port = somePort + 432
        |  },
        |  api1 = {
        |    endpoint = {
        |      host = "some.host"
        |    }
        |  },
        |  api2 = {
        |    endpoint = {
        |      scheme = "https",
        |      host = "some.host2",
        |      -- Optionals are handled in non-consistent way currently:
        |      -- 1. To construct None you need to either omit field or set it to None explicitly
        |      -- 2. To construct Some you need to explicitly call Some
        |      path = Some "/",
        |      port = 8080
        |    }
        |  }
        |}
        |""".stripMargin

    val parsed  = input.parseExpr.getOr("Parsing failed").normalize()
    val decoded = parsed.as[AppConfig].getOr("Decoding failed")
    val expected = AppConfig(
      db = DbConfig(host = "host.com", port = 5432),
      api1 = ApiConfig(endpoint = EndpointConfig(host = "some.host")),
      api2 = ApiConfig(
        endpoint = EndpointConfig(
          scheme = "https",
          host = "some.host2",
          port = 8080,
          path = Some("/")
        )),
    )

    assertEquals(decoded, expected)
  }

  test("Load sealed trait") {
    val input =
      """
        |let Error = < Error1 : { msg : Text } | Error2 : { code : Natural, code2 : Natural } >
        |in { errors = [Error.Error1 { msg = "abc"}, Error.Error2 { code = 123, code2 = 456 }] }
        |""".stripMargin

    val parsed = input.parseExpr.getOr("Parsing failed").normalize()

    val decoded  = parsed.as[Errors].getOr("Decoding failed")
    val expected = Errors(List(Error1("abc"), Error2(code = 123, code2 = 456)))
    assertEquals(decoded, expected)
  }

  test("Load union without parameters") {
    val input =
      """
        |let OnOrOff = < On: {} | Off: {} >
        |in { http = { server = { preview = { enableHttp2 = OnOrOff.Off } } } }
        |""".stripMargin

    val parsed = input.parseExpr.getOr("Parsing failed").normalize()

    val decoded = parsed.as[Akka].getOr("Decoding failed")

    val expected = Akka(Http(Server(Preview(OnOrOff.Off))))

    assertEquals(decoded, expected)
  }

  test("Load union without parameters 2") {
    val input =
      """
        |let OnOrOff = < On | Off >
        |in OnOrOff.Off
        |""".stripMargin

    val parsed = input.parseExpr.getOr("Parsing failed").normalize()

    val decoded = parsed.as[OnOrOff].getOr("Decoding failed")

    assertEquals(decoded, OnOrOff.Off)
  }

  test("Decoding error should be comprehensible for deeply nested case classes") {
    val input =
      """
        |let OnOrOff = < On: {} | Off: {} >
        |in { http = { server = { preview = { enableHttp = OnOrOff.Off } } } }
        |""".stripMargin

    val parsed = input.parseExpr.getOr("Parsing failed").normalize()

    val decoded = parsed.as[Akka]

    println(s"decoded: $decoded")
    val expectedMsg = "Missing field http.server.preview.[enableHttp2] when decoding org.dhallj.generic.example.akka.Preview"

    val expected = Left(new DecodingFailure("", null))

    assertEquals(decoded, expected)
  }

  implicit class EitherOps[L, R](v: Either[L, R]) {
    def getOr(clue: String): R = v.fold(l => fail(s"Unexpected Left when $clue: $l"), r => r)
  }
}
