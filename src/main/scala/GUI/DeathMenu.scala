package GUI

import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color._
import scalafx.geometry.Pos
import scalafx.event.ActionEvent
import Utils.LoadGame
import scalafx.scene.text.Font
import Utils.{HighScore, HighScoreFile}
import scalafx.collections.ObservableBuffer
import scalafx.beans.property.{StringProperty, ObjectProperty}

class DeathMenu(g: GameArea) {
  val deathLabel = new Label(Language.getWord(10) + g.Player.score) {
    textFill = Red
    font = new Font("Arial", 20)
    style = "-fx-background-color: black;"
  }
  val saveHighScoreButton = new Button(Language.getWord(11)) {
    textFill = Black
    prefHeight = MainMenu.getHeight() * 0.02
    maxWidth = 300
    style = "-fx-background-color: white; -fx-font-size: 120%;"
  }
  saveHighScoreButton.onAction = (e: ActionEvent) => {
    deathVBox.children = List(nameLabel, textField, saveScoreButton, exitButton)
  }
  val exitButton = new Button(Language.getWord(12)) {
    style = "-fx-background-color: white; -fx-font-size: 120%;"
    textFill = Black
    maxWidth = 300
  }
  exitButton.onAction = (e: ActionEvent) => {
    sys.exit(0)
  }
  val backToMainMenuButton = new Button(Language.getWord(13)) {
    textFill = Black
    prefHeight = MainMenu.getHeight() * 0.02
    maxWidth = 300
    style = "-fx-background-color: white; -fx-font-size: 120%;"
  }
  backToMainMenuButton.onAction = (e: ActionEvent) => {
    TowerDefence.stage.scene = MainMenu
  }
  
  val nameLabel = new Label(Language.getWord(14)) {
    alignment = Pos.Center
    textFill = White
    font = new Font("Arial", 15)
    style = "-fx-background-color: black"
  }
  val textField = new TextField() {
    alignment = Pos.Center
    maxWidth = 300
  }
  val saveScoreButton = new Button(Language.getWord(15)) {
    style = "-fx-background-color: white; -fx-font-size: 120%;"
    textFill = Black
    maxWidth = 300
  }
  saveScoreButton.onAction = (e: ActionEvent) => {
    if (!textField.getText().isEmpty()) {
      HighScoreFile.saveHighScore(
        g.fieldName,
        textField.getText(),
        g.Player.score
      )
      TowerDefence.stage.scene = MainMenu
    }
  }
  
  val deathVBox = new VBox(5) {
    spacing = g.getHeight() *0.02
    layoutX = 0
    layoutY = 0
    minWidth = g.MenuWidth + g.gameFieldWidth
    maxWidth = g.MenuWidth + g.gameFieldWidth
    minHeight = g.gameFieldHeight
    prefHeight = g.gameFieldHeight
    maxHeight = g.gameFieldHeight
    alignment = Pos.Center
    style = "-fx-background-color: black;"
    children = List(deathLabel, saveHighScoreButton, backToMainMenuButton, exitButton)
  }
  g.content = deathVBox
}