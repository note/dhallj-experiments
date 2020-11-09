package pl.msitko

import org.dhallj.core.Expr

import scala.meta.{Pkg, _}
import org.dhallj.ast.{RecordLiteral, RecordType}

import scala.meta.internal.semanticdb.Scala.Names.TermName

object DhallToScalaGenerator {

  // take dhall type and generate scala
  // implement as ExternalVisitor?
  def generate(dhallExpr: Expr, pkg: Option[Term.Ref]): Seq[Defn.Class] = ???

  def generate(dhallExpr: Expr, pkg: List[String]): Seq[Defn.Class] =
    generate(dhallExpr, toTermSelect(pkg))

  def generate(dhallExpr: Expr, pkg: String): Seq[Defn.Class] = generate(dhallExpr, pkg.split('.').toList)

  def generate(dhallExpr: Expr): Seq[Defn.Class] =
    generate(dhallExpr, List.empty)

  private def toTermSelect(packageComponents: List[String]): Option[Term.Ref] =
    if (packageComponents.nonEmpty) {
      Some(
        packageComponents.tail.foldLeft(Term.Name(packageComponents.head): Term.Ref) { (acc, curr) =>
          Term.Select(acc, Term.Name(curr))
        }
      )
    } else {
      None
    }

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
