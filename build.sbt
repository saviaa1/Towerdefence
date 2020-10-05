ThisBuild / scalaVersion	:= "2.12.3"
ThisBuild / version		:= "0.9"

lazy val root = (project in file("."))
	.settings(
		name := "Towerdefence",
		fork := true,
		EclipseKeys.withSource := true,
		retrieveManaged := true,
		libraryDependencies ++= Seq(
			"org.scalafx" %% "scalafx" % "8.0.192-R14",
			"com.typesafe.play" %% "play-json" % "2.6.9",
			"org.scalactic" %% "scalactic" % "3.1.1",
			"org.scalatest" %% "scalatest" % "3.1.1" % "test",
			//"de.codecentric.centerdevice" % "javafxsvg" % "1.3.0"
		)
	)
// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

// Add JavaFX dependencies
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m=>
  "org.openjfx" % s"javafx-$m" % "12.0.2" classifier osName
)
