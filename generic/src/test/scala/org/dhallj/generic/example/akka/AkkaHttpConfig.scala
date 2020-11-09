package org.dhallj.generic.example.akka

final case class Akka(
    http: Http
)

final case class Http(
    server: Server
)

final case class Server(
    preview: Preview
)

final case class Preview(
    enableHttp2: OnOrOff
)

sealed trait OnOrOff

object OnOrOff {
  final case object On  extends OnOrOff
  final case object Off extends OnOrOff
}
