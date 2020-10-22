package org.dhallj.generic.example

final case class DbConfig(
    host: String,
    port: Int
)

final case class ApiConfig(
    endpoint: EndpointConfig
)

final case class EndpointConfig(
    scheme: String = "http",
    host: String,
    port: Int = 80,
    path: Option[String] = None
)

final case class AppConfig(
    db: DbConfig,
    api1: ApiConfig,
    api2: ApiConfig
)

final case class Errors(errors: List[Error])

sealed trait Error

final case class Error1(msg: String) extends Error

final case class Error2(code: Int, code2: Long) extends Error

final case class Abcs(s: List[Abc])
final case class Abc(l: String)
//final case class Abc2(msg: Int, msg2: Long)   extends Abc
//final case class Abc3(code: Int, code2: Long) extends Error
