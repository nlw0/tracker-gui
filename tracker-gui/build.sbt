name := "tracker-gui"

version := "1.0"

scalaVersion := "2.12.1"

val akkaVersion = "2.4.16"

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= List(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "org.scalafx" %% "scalafx" % "8.0.102-R11"
)