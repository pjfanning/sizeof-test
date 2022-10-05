ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.11.12"

lazy val root = (project in file("."))
  .settings(
    name := "sizeof-test",
    libraryDependencies := Seq(
      "com.madhukaraphatak" %% "java-sizeof" % "0.1",
      "org.slf4j" % "slf4j-api" % "2.0.3",
      "org.slf4j" % "slf4j-simple" % "2.0.3",
      "com.google.guava" % "guava" % "31.1-jre"
    )
  )
