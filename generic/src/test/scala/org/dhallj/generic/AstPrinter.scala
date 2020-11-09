package org.dhallj.generic

import org.dhallj.syntax._

object AstPrinter {

  def main(args: Array[String]): Unit = {
//    val input =
//      """
//        |let OnOrOff = < On: {} | Off: {} >
//        |in { http = { server = { preview = { enableHttp2 = OnOrOff.Off } } } }
//        |""".stripMargin

    val input =
      """
        |let OnOrOff = < On: {} | Off: {} >
        |in OnOrOff.Off
        |""".stripMargin

//    val input =
//      """
//        |let OnOrOff = < On: { t: Text } | Off: {t: Text} >
//        |in OnOrOff.Off { t = "abc" }
//        |""".stripMargin

    val expr = input.parseExpr.getOrElse(throw new RuntimeException("cannot parse")).normalize()

    println(org.dhallj.javagen.toJavaCode(expr, "example", "A"))
  }
}
