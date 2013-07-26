import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "AlgoService"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      "net.sourceforge.f2j" % "arpack_combined_all" % "0.1"
    )

    scalaVersion := "2.10.2"
      
    val main = play.Project(appName, appVersion, appDependencies).settings(
      resolvers ++= Seq(
        "Typesafe Repo" at "http://repo.typesafe.com/typesafe/repo/"
      )
    )

}
