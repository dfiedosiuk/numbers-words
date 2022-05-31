ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

lazy val root = (project in file("."))
  .settings(
    name := "kafka-streams-testing"
  )

libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "3.1.0"
libraryDependencies += "org.apache.kafka" % "kafka-streams-test-utils" % "3.1.0"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.36"
libraryDependencies += "org.slf4j" % "slf4j-reload4j" % "1.7.36"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.12"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % Test

libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.1"