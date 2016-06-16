name := "tlv-parser"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.typesafe.akka" % "akka-http-experimental_2.11" % "2.4.7"
)

WebKeys.packagePrefix in Assets := "assets/"

(managedClasspath in Runtime) += (packageBin in Assets).value

lazy val tlv = (project in file(".")).enablePlugins(SbtWeb)