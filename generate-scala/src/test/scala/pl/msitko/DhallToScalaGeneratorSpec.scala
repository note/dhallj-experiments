package pl.msitko

import scala.meta._
import org.dhallj.syntax._

class DhallToScalaGeneratorSpec extends munit.FunSuite {
  test("Generate Scala for product type") {
    """{ name : Text, account : Text, age : Natural }"""
  }

  test("Generate Scala for product type") {
    val in = """
               |let Comparison = { name : Text, scalaCode : Text, haskellCode : Text }
               |let Explained = { title : Text, code : Text, explanation : Optional Text }
               |in { Comparison = Comparison
               |   , Explained = Explained
               |   }
               |""".stripMargin.parseExpr.getOrElse(fail("parsing dhall failed"))

    println(in.normalize())

//    println("next")
//    println(org.dhallj.javagen.toJavaCode(in.normalize(), "abc.xyz", "some"))

    val expected =
      """
        |package abc.xyz
        |final case class Explained(title: String, code: String, explanation: Option[String])
        |final case class TopLevel(comparisonName: String, scalaCode: String, haskell: List[Explained])
        |""".stripMargin.parse[Source].get

    println(expected.structure)
  }
}
