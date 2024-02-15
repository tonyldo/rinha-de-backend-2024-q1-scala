ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "rinha-de-backend-2024-q1-scala"
  )

libraryDependencies += "org.scalatest" %% "scalatest-funsuite" % "3.2.18" % "test"
