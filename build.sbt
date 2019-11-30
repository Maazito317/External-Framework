name := "Command"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "com.typesafe" % "config" % "1.3.4"
libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.5", "org.slf4j" % "slf4j-simple" % "1.7.5")
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
