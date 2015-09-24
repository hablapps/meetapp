name := """aula-virtual"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

mainClass in Compile := Some("SimpleExample")

// scalaSource in Compile := baseDirectory.value / "app" / "org"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "com.typesafe.play" %% "play-slick" % "0.8.0",
  // "com.typesafe.slick" %% "slick" % "2.1.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  // "com.h2database" % "h2" % "1.3.170",
  "mysql" % "mysql-connector-java" % "5.1.26",
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "com.hablapps" %% "funplay" % "0.1-SNAPSHOT"
)

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.0-M3"
