import sbt._

object Dependencies {
	object Versions {
		val Dhall = "0.4.0"
	}

	lazy val dhall 							= Seq("org.dhallj" %% "dhall-scala-codec" % "0.4.0")
	lazy val dhallJavaGen 			= Seq("org.dhallj" %% "dhall-javagen" % "0.4.0")
	def magnolia(scalaVersion: String) 	= Seq(
		"com.propensive" %% "magnolia" % "0.16.0",
		"org.scala-lang" % "scala-reflect" % scalaVersion % Provided
	)

	lazy val munit = Seq("org.scalameta" %% "munit" % "0.7.12" % Test)

	lazy val scalameta = Seq("org.scalameta" %% "scalameta" % "4.3.22")

}
