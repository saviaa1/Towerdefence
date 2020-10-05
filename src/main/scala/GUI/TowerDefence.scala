package GUI

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene._
import scalafx.stage.Screen

/**
 * Main object of TowerDefence.
 * The App is started here.
 */
object TowerDefence extends JFXApp {
  var LangChoice: String = "EN"
  stage = new JFXApp.PrimaryStage {
    resizable = true
    centerOnScreen()
    title = Language.getWord(2)
    scene = LanguageMenu
    //scene = MainMenu
  }
  stage.setMaximized(true)
  //val screenW: Double = Screen.primary.visualBounds.getWidth
  //val screenH: Double = Screen.primary.visualBounds.getHeight
}