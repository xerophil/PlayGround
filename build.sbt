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

//play.Keys.lessEntryPoints <<= baseDirectory { base =>
//  (base / "app" / "assets" / "stylesheets" / "bootstrap" * "bootstrap.less") +++
//   (base / "app" / "assets" / "stylesheets" * "*.less")
//}
playJavaSettings

lessEntryPoints <<= baseDirectory(_ / "app" / "assets" / "stylesheets" * "main.less")
