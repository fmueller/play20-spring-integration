import sbt._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "play20-spring-integration"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.springframework" % "spring-context" % "3.1.2.RELEASE"
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
    // Add your own project settings here
  )
}
