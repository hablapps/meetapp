name := """meetapp"""

scalaVersion := "2.11.8-tl-201604190743" // compile succeeds

scalaBinaryVersion := "2.11"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

mainClass in Compile := Some("SimpleExample")

scalaSource in Test := baseDirectory.value / "app" / "fun" / "testing"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  // "-Xprint:typer",
  // "-Xlog-implicit-conversions",
  "-language:postfixOps",
  "-Yhigher-order-unification",
  "-language:higherKinds")

resolvers ++= Seq(
  "scalatl" at "http://milessabin.com/scalatl"
)

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.typesafe.play" %% "play-slick" % "0.8.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "mysql" % "mysql-connector-java" % "5.1.26",
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.scalaz" %% "scalaz-core" % "7.2.0-M3",
  "org.specs2" %% "specs2-core" % "3.6.5",
  "org.specs2" %% "specs2-junit" % "3.6.5" % "test",
  "org.specs2" %% "specs2-mock" % "3.6.5" % "test",
  "org.scalatest" %% "scalatest" % "3.0.0-M1"
)
