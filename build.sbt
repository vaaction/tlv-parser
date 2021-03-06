import sbt.Keys._

val appName = "tlv-parser"

name := appName

herokuAppName in Compile := appName

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.7"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.typesafe.akka" % "akka-http-experimental_2.11" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
)

WebKeys.packagePrefix in Assets := "assets/"

(managedClasspath in Runtime) += (packageBin in Assets).value

mainClass in Compile := Some("mvp.volvo.tlv.WebServer")

lazy val root = (project in file(".")).enablePlugins(SbtWeb, JavaAppPackaging)