import sbt._

object Dependencies {
	lazy val dhall 			= Seq("org.dhallj" %% "dhall-scala-codec" % "0.4.0")
	def magnolia(scalaVersion: String) 	= Seq(
		"com.propensive" %% "magnolia" % "0.16.0",
		"org.scala-lang" % "scala-reflect" % scalaVersion % Provided
	)

	lazy val munit = Seq("org.scalameta" %% "munit" % "0.7.12" % Test)
}
