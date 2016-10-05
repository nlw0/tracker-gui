name := "tracker-www"

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.10"

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= List(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
  "org.scalafx" %% "scalafx" % "8.0.92-R10"
)

javaOptions in run += "-Djava.library.path=/usr/local/share/OpenCV/java"
