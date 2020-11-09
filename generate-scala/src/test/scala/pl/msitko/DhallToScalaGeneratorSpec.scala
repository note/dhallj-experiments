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
               |""".stripMargin.parseExpr.getOrElse(fail("parsing dhall failed")).normalize()

    val expected =
      """
        |//package abc.fgh.xyz
        |final case class Explained(title: String, code: String, explanation: Option[String])
        |final case class TopLevel(comparisonName: String, scalaCode: String, haskell: List[Explained])
        |""".stripMargin.parse[Source].get

    println(expected.structure)

    expected.structure

    val res = DhallToScalaGenerator.generate(in, "abc.xyz")

//    assertEquals(res, expected.structure)
  }

  test("fragments of dhall-openapi") {
    // Taken from https://github.com/atcol/dhall-openapi
    val in = """
               |let Example   = \(a : Type) ->
               |               { summary        : Text
               |               , description    : Text
               |               , value          : a
               |               , externalValue  : Text
               |               }
               |let Header    = { description      : Text
               |, required         : Bool
               |, deprecated       : Bool
               |, allowEmptyValue  : Bool
               |}
               |let Reference = { `$ref` : Text }
               |let HeaderRef = < Req : Header | Ref : Reference >
               |let Encoding  =  { contentType    : Text
               |                 , headers        : Text
               |                 , style          : List { mapKey : Text, mapValue : ./HeaderRef }
               |                 , explode        : Bool
               |                 , allowReserved  : Bool
               |                 }
               |let SchemaRef = < Schema : List { mapKey : Text, mapValue : Text } | Ref : Reference >
               |let MediaType = \(a : Type) ->
               |  < Single : { schema   : SchemaRef
               |             , example  : Example a
               |             , encoding : Optional Encoding
               |             }
               |  | Multiple : { schema   : Optional SchemaRef
               |               , examples : (List { mapKey : Text, mapValue : (Example a) })
               |               , encoding : Optional Encoding
               |               }
               |  >
               |in MediaType
               |""".stripMargin

    // it's crucial to NOT normalize
    val parsed = in.parseExpr.getOrElse(fail("parsing dhall failed"))
    println("PARSED")
    println(parsed)

    // How to represent union types?
    val expected =
      """package abc.fgh.xyz
        |final case class Example[A](summary: String, description: String, value: A, externalValue: String)
        |sealed trait HeaderRef
        |final case class Header(description: String, required: Boolean, deprecated: Boolean, allowEmptyValue: Boolean) extends HeaderRef
        |final case class Reference(ref: String) extends HeaderRef
        |final case class Encoding(
        |  contentType: String,
        |  headers: String,
        |  style: List[(String, HeaderRef)], // not sure?
        |  explode: Boolean,
        |  allowReserved: Boolean
        |)
        |sealed trait SchemaRef
        |final case class
        |sealed trait MediaType[A]
        |object MediaType {
        |  final case class Single(schema:
        |""".stripMargin.parse[Source].get
  }
}
