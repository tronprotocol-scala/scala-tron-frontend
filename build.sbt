import Dependencies._

name := "scala-tron-frontend"
organization := "com.tron"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  guice,
) ++ akkaDeps ++ grpcDeps

//PB.targets in Compile := Seq(
//  scalapb.gen() -> (sourceManaged in Compile).value
//),
//fork in Test := true

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.tron.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.tron.binders._"
