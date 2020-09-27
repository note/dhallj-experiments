package org.dhallj.generic

import org.dhallj.codec.syntax._
import org.dhallj.generic.example.{ApiConfig, AppConfig, DbConfig, EndpointConfig}
import org.dhallj.syntax._

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

    import GenericDecoder._
    val parsed = input.parseExpr.getOr("Parsing failed").normalize()
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

  implicit class EitherOps[L, R](v: Either[L, R]) {
    def getOr(clue: String): R = v.fold(l => fail(s"Unexpected Left when $clue: $l"), r => r)
  }
}
