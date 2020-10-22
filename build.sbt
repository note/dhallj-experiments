import Common._
import Dependencies._

lazy val generic = (project in file("generic"))
  .commonSettings("dhallj-generic", "0.1.0")
  .settings(
    libraryDependencies ++= (dhall ++ magnolia(scalaVersion.value) ++ munit)
  )

lazy val generateScala = (project in file("generate-scala"))
  .commonSettings("generate-scala", "0.1.0")
  .settings(
    libraryDependencies ++= (dhall ++ dhallJavaGen ++ scalameta ++ munit)
  )
