import Common._
import Dependencies._

lazy val root = (project in file("."))
  .commonSettings("dhallj-experiments", "0.1.0")
  .settings(
    libraryDependencies ++= (dhall ++ magnolia(scalaVersion.value) ++ munit)
  )
