package pl.msitko

import org.dhallj.core.Expr

import scala.meta._
import org.dhallj.ast.{RecordLiteral, RecordType}

object DhallToScalaGenerator {

  def generate(dhallExpr: Expr): Seq[Defn.Class] = ???

//  {
//    // format: off
//    List(
//    Defn.Class(List(Mod.Final(), Mod.Case()), Type.Name("Explained"), Nil,
//      Ctor.Primary(Nil, Name(""),
//        List(List(Term.Param(Nil, Term.Name("title"), Some(Type.Name("String")), None), Term.Param(Nil, Term.Name("code"), Some(Type.Name("String")), None), Term.Param(Nil, Term.Name("explanation"), Some(Type.Apply(Type.Name("Option"), List(Type.Name("String")))), None)))),
//      Template(Nil, Nil, Self(Name(""), None), Nil)),
//    Defn.Class(List(Mod.Final(), Mod.Case()), Type.Name("TopLevel"), Nil,
//      Ctor.Primary(Nil, Name(""),
//        List(List(Term.Param(Nil, Term.Name("comparisonName"), Some(Type.Name("String")), None), Term.Param(Nil, Term.Name("scalaCode"), Some(Type.Name("String")), None), Term.Param(Nil, Term.Name("haskell"), Some(Type.Apply(Type.Name("List"), List(Type.Name("Explained")))), None)))),
//      Template(Nil, Nil, Self(Name(""), None), Nil)))
//    // format: on
//  }
}
