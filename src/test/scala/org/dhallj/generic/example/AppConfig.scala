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
