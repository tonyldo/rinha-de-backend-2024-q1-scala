ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "rinha-de-backend-2024-q1-scala"
  )

libraryDependencies += "com.typesafe.slick" %% "slick" % "3.4.1"
libraryDependencies += "org.scalatest" %% "scalatest-funsuite" % "3.2.18" % "test"
libraryDependencies += "com.h2database" % "h2" % "2.2.224" % "test"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.7" % Runtime
libraryDependencies += "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1"


Test / parallelExecution := false
