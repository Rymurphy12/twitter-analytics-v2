name := "twitter-analytics"

organization := "com.github.rymurphy12"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.12.4" // Also supports 2.11.x

val http4sVersion = "0.18.0"

// Only necessary for SNAPSHOT releases
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "io.circe" %% "circe-generic" % "0.9.1",
  "io.circe" %% "circe-literal" % "0.9.1"
)

scalacOptions ++= Seq("-Ypartial-unification")
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)