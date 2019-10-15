import sbt._

object Dependencies {
  val resolutionRepos = Seq(
    "Sonatype OSS Releases"   at "http://oss.sonatype.org/content/repositories/releases/",
    "Typesafe"                at "http://repo.typesafe.com/typesafe/releases/",
    "Artima Maven Repository" at "http://repo.artima.com/releases"
  )

  object V {
    val logback   = "1.2.3"
    val logging   = "3.9.2"
    val zio       = "1.0.0-RC13"
    val zioKafka  = "0.2.0"
    
    // Test
    val scalatest = "3.0.5"
  }

  val Libraries = Seq(
    "ch.qos.logback"              % "logback-classic" % V.logback,
    "com.typesafe.scala-logging" %% "scala-logging"   % V.logging,
    "dev.zio"                    %% "zio"             % V.zio,
    "dev.zio"                    %% "zio-streams"     % V.zio,
    "dev.zio"                    %% "zio-kafka"       % V.zioKafka,

    // Test
    "org.scalatest"              %% "scalatest"       % V.scalatest % "test"
  )
}
