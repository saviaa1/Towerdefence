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
import scalafx.stage.Screen
import java.io.File

object MainMenu extends Scene(1200, 800) {
  val gHeight = this.getHeight
  val loadGame: LoadGame = new LoadGame
  val files: List[File]  = loadGame.getListOfFiles(new File("./gameFiles/mapFiles/"), List("json"))
  val mapNames = loadGame.getMapName(files)
  
  val nameLabel = new Label(Language.getWord(2)) {
    font = new Font("Arial", 30)
    textFill = White
  }
  
  val startButton = new Button(Language.getWord(1)) {
    textFill = Black
    prefHeight = gHeight * 0.02
    maxWidth = 200
    style = "-fx-background-color: white; -fx-font-size: 120%;"
  }
  startButton.onAction = (e: ActionEvent) => {
    menuVBox.children = List(chooseMapLabel, fileListView, fileListButton, backButton)
  }
  val chooseMapLabel = new Label() {
    if (!mapNames.isEmpty) text = Language.getWord(34)
    else text = Language.getWord(35)
    font = new Font("Arial", 20)
    textFill = White
  }
  var selectedMap: String = ""
  val c = mapNames.keySet.toList
  val fileListView = new ListView(c) {
    prefHeight = gHeight * 0.3
    prefWidth = 200
    maxWidth = 300
  }
  fileListView.selectionModel.apply.selectedItems.onChange {
    selectedMap = fileListView.selectionModel.apply.getSelectedItems.head
  }
  val fileListButton = new Button(Language.getWord(36)) {
    textFill = Black
    prefHeight = gHeight * 0.02
    prefWidth = 200
    maxWidth = 300
    style = "-fx-background-color: white; -fx-font-size: 120%;"
  }
  fileListButton.onAction = (e: ActionEvent) => {
    if(!selectedMap.isEmpty() && !mapNames.isEmpty && mapNames.contains(selectedMap)) {
      val g: GameArea = loadGame.initGameArea(mapNames(selectedMap).toString())
      TowerDefence.stage.scene = g
    }
  }
  val backButton = new Button(Language.getWord(5)) {
    style = "-fx-background-color: white; -fx-font-size: 120%;"
    textFill = Black
    prefWidth = 200
    maxWidth = 300
  }
  backButton.onAction = (e: ActionEvent) => {
    menuVBox.children = List(nameLabel, startButton, scoresButton, exitButton)
  }
  val scoresButton = new Button(Language.getWord(3)) {
    textFill = Black
    prefHeight = gHeight * 0.02
    maxWidth = 200
    style = "-fx-background-color: white; -fx-font-size: 120%;"
  }
  scoresButton.onAction = (e: ActionEvent) => {
    HighScoreFile.read()
    new HighScoreMenu()
  }
  val exitButton = new Button("Exit") {
    style = "-fx-background-color: white; -fx-font-size: 120%;"
    textFill = Black
    maxWidth = 200
  }
  exitButton.onAction = (e: ActionEvent) => {
    sys.exit(0)
  }
  val menuVBox = new VBox {
    spacing = gHeight * 0.02
    children = List(nameLabel, startButton, scoresButton, exitButton)
    alignment = Pos.Center
    style = "-fx-background-color: black"
  }
  root = menuVBox
  def clearMenu() {
    menuVBox.children = List(nameLabel, startButton, scoresButton, exitButton)
  }
}

class HighScoreMenu() {
  val backButton = new Button(Language.getWord(5)) {
    style = "-fx-background-color: white"
    textFill = Black
    minWidth = (200); maxWidth = (200); prefWidth = (200)
  }
  backButton.onAction = (e: ActionEvent) => {
    MainMenu.root = MainMenu.menuVBox
  }
  val resetButton = new Button(Language.getWord(6)) {
    style = "-fx-background-color: white"
    textFill = Black
    minWidth = (200); maxWidth = (200); prefWidth = (200)
  }
  resetButton.onAction = (e: ActionEvent) => {
    HighScoreFile.reset()
  }
  val table = new TableView(HighScoreFile.ScoreBuffer) {
    minHeight = 200; prefHeight = 300; maxHeight = 700
    minWidth = 750; prefWidth = 750; maxWidth = 800
    alignmentInParent = Pos.Center
    autosize()
  }
  val indexC = new TableColumn[HighScore, String] (Language.getWord(7)) {
    sortable = false
    cellValueFactory = cdf => StringProperty(cdf.value.fieldName)
    prefWidth = 250
  }
  val nameC = new TableColumn[HighScore, String](Language.getWord(8)) {
    cellValueFactory = cdf => StringProperty(cdf.value.name)
    sortable = false
    prefWidth = 250
  }
  val scoreC = new TableColumn[HighScore, Int](Language.getWord(9)) {
    cellValueFactory = cdf => ObjectProperty(cdf.value.score)
    sortable = false
    prefWidth = 250
  }
  table.columns ++= List(indexC, nameC, scoreC)
  
  val buttonsContent = new HBox(50) {
    children = List(backButton, resetButton)
    alignment = (Pos.Center)
    minWidth = (450); maxWidth = (450); prefWidth = (450)
  }
  val HiScMenuContent = new VBox(20) {
    children = List(table, buttonsContent)
    alignment = Pos.Center
    style = "-fx-background-color: black"
  }
  MainMenu.root = HiScMenuContent
}