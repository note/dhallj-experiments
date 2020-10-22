package org.dhallj.generic

import org.dhallj.codec.syntax._
import org.dhallj.generic.example.{Abc, Abcs, ApiConfig, AppConfig, DbConfig, EndpointConfig, Error1, Error2, Errors}
import org.dhallj.syntax._
import GenericDecoder._
import org.dhallj.codec.Decoder

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
        |in { errors = [Error.Error1 { msg = "abc"}, Error.Error2 { code = 123 , code2 = 456 }] }
        |""".stripMargin

    val parsed = input.parseExpr.getOr("Parsing failed").normalize()

    val decoded  = parsed.as[Errors].getOr("Decoding failed")
    val expected = Errors(List(Error1("abc"), Error2(code = 123, code2 = 456)))
    assertEquals(decoded, expected)
  }

  test("Load sealed trait 2") {
    val input =
      """
        |let Error = < Error1 : { msg : Text } | Error2 : { code : Natural, code2 : Natural } >
        |in { errors = [{ msg = "abc"}, { code = 123 , code2 = 456 }] }
        |""".stripMargin

    //    val input =
    //      """
    //        |let Error = < Error1 { msg : Text } | Error2 { code : Natural, code2 : Natural } >
    //        |in [Error.Error1 { msg = "abc"}, Error.Error2 { code = 123 , code2 = 456 }]
    //        |""".stripMargin

    val parsed = input.parseExpr.getOr("Parsing failed").normalize()

    val decoded  = parsed.as[Errors].getOr("Decoding failed")
    val expected = Errors(List(Error1("abc"), Error2(code = 123, code2 = 456)))
    assertEquals(decoded, expected)
  }

  implicit class EitherOps[L, R](v: Either[L, R]) {
    def getOr(clue: String): R = v.fold(l => fail(s"Unexpected Left when $clue: $l"), r => r)
  }
}
