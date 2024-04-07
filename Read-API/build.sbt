
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.18"

lazy val root = (project in file("."))
  .settings(
    name := "Read-API"
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.0",
  "org.apache.spark" %% "spark-sql" % "3.5.0" % "provided",
  "org.apache.httpcomponents" % "httpclient" % "4.5.13",
  "org.scalaj" %% "scalaj-http" % "2.4.2"
)

