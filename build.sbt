import play.Project._

name := "PlayGround"

version := "1.0"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  filters,
  "mysql" % "mysql-connector-java" % "5.1.21",
  "org.ocpsoft.prettytime" % "prettytime" % "3.1.0.Final"
)


playJavaSettings