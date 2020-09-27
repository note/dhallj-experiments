package org.dhallj.generic

import magnolia.{CaseClass, Magnolia, SealedTrait}
import cats.Traverse
import org.dhallj.ast.RecordLiteral
import org.dhallj.codec.{Decoder, DecodingFailure}
import org.dhallj.core.Expr
import org.dhallj.codec.Decoder.Result
import cats.instances.list._
import cats.instances.either._

final case class MissingRecordField(override val target: String, missingFieldName: String, override val value: Expr)
    extends DecodingFailure(target, value)

object GenericDecoder {
  type Typeclass[T] = Decoder[T]

  def combine[T](caseClass: CaseClass[Decoder, T]): Decoder[T] = new Decoder[T] {

    override def decode(expr: Expr): Result[T] = expr match {
      case RecordLiteral(recordMap) =>
        Traverse[List]
          .traverse(caseClass.parameters.toList) { param =>
            recordMap.get(param.label) match {
              case Some(expr) =>
                param.typeclass.decode(expr)
              case None =>
                param.default match {
                  case Some(default) => Right(default)
                  case None          => Left(MissingRecordField(caseClass.typeName.full, param.label, expr))
                }
            }
          }
          .map(ps => caseClass.rawConstruct(ps))

      case other =>
        Left(new DecodingFailure(caseClass.typeName.full, other))
    }

    override def isValidType(typeExpr: Expr): Boolean = ???

    override def isExactType(typeExpr: Expr): Boolean = ???
  }

  def dispatch[T](sealedTrait: SealedTrait[Decoder, T]): Decoder[T] = new Decoder[T] {
    override def decode(expr: Expr): Result[T] = ???

    override def isValidType(typeExpr: Expr): Boolean = ???

    override def isExactType(typeExpr: Expr): Boolean = ???
  }

  implicit def gen[T]: Typeclass[T] = macro Magnolia.gen[T]
}
