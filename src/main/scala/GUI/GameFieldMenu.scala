package GUI

import scalafx.scene.shape.Circle
import scalafx.scene.paint.Color._
import scalafx.scene.Scene
import scalafx.scene.text.Font
import scalafx.Includes._
import scalafx.scene.control._
import scalafx.geometry._
import scalafx.event.ActionEvent
import scalafx.scene.layout._
import Objects._
import Utils.{HighScore, HighScoreFile}

class GameFieldMenu(g: GameArea, gameFieldWidth: Int, MenuWidth: Int, gameFieldHeight: Int) {
  var drawSelection: Boolean = false
  var selectionCircle = drawCircle((0,0))
  
  val waveText = new Label{
    textFill = White
    text = Language.getWord(33) + g.wave
    prefWidth = MenuWidth
    margin = Insets(10,10,0,10)
    font = new Font("Arial", 15)
  }
  val healthText = new Label{
    textFill = White
    text = Language.getWord(16) + g.Player.health
    prefWidth = MenuWidth
    margin = Insets(10,10,0,10)
    font = new Font("Arial", 15)
  }
  val scoreText = new Label{
    textFill = White
    prefWidth = MenuWidth
    text = Language.getWord(17) + g.Player.score
    margin = Insets(10,10,0,10)
    font = new Font("Arial", 15)
  }
  val moneyText = new Label{
    textFill = White
    prefWidth = MenuWidth
    text = Language.getWord(18) + g.Player.money
    margin = Insets(10,10,0,10)
    font = new Font("Arial", 15)
  }
  val scoreVBox = new VBox() {
    layoutX = gameFieldWidth
    layoutY = 0
    prefWidth = MenuWidth
    //maxWidth = MenuWidth
    //minHeight = gameFieldHeight*0.15
    prefHeight = gameFieldHeight*0.15
    //maxHeight = gameFieldHeight*0.15
    alignment = Pos.TopLeft
    style = "-fx-background-color: black;"
    children = List(waveText, healthText, scoreText, moneyText)
  }
  val towerVBox = new VBox(5) {
    layoutX = gameFieldWidth
    layoutY = gameFieldHeight*0.15
    prefWidth = MenuWidth
    //maxWidth = MenuWidth
    //minHeight = gameFieldHeight*0.70
    prefHeight = gameFieldHeight*0.70
    //maxHeight = gameFieldHeight*0.70
    alignment = Pos.Center
    style = "-fx-background-color: black;"
    children = List()
  }
  val menuVBox = new VBox(5) {
    layoutX = gameFieldWidth
    layoutY = gameFieldHeight*0.85
    prefWidth = MenuWidth
    //maxWidth = MenuWidth
    //minHeight = gameFieldHeight*0.15
    prefHeight = gameFieldHeight*0.15
    //maxHeight = gameFieldHeight*0.15
    style = "-fx-background-color: black;"
    children = List()
  }
  val startButton = new Button(Language.getWord(19)) {
    textFill = Black
    prefHeight = gameFieldHeight * 0.04
    //maxHeight = gameFieldHeight * 0.04
    prefWidth = MenuWidth * 0.9
    style = "-fx-background-color: white; -fx-font-size: 120%;"
    margin = Insets(10,10,10,10)
  }
  startButton.onAction = (e: ActionEvent) => {
    g.timer.startTimer
    menuVBox.children = List(pauseButton)
  }
  val pauseButton = new Button(Language.getWord(20)) {
    textFill = Black
    prefHeight = gameFieldHeight * 0.04
    //maxHeight = gameFieldHeight * 0.04
    prefWidth = MenuWidth * 0.9
    style = "-fx-background-color: white; -fx-font-size: 120%;"
    margin = Insets(10,10,10,10)
  }
  pauseButton.onAction = (e: ActionEvent) => {
    g.timer.stopTimer
    menuVBox.children = List(exitButton, startButton)
  }
  val continueGameButton = new Button(Language.getWord(30)) {
    textFill = Black
    prefHeight = gameFieldHeight * 0.04
    //maxHeight = gameFieldHeight * 0.04
    prefWidth = MenuWidth * 0.9
    style = "-fx-background-color: white; -fx-font-size: 120%;"
    margin = Insets(0,10,10,10)
  }
  continueGameButton.onAction = (e: ActionEvent) => {
    g.timer.startTimer
    menuVBox.children = List(pauseButton)
  }
  val exitButton = new Button(Language.getWord(31)) {
    textFill = Black
    prefHeight = gameFieldHeight * 0.04
    //maxHeight = gameFieldHeight * 0.04
    prefWidth = MenuWidth * 0.9
    style = "-fx-background-color: white; -fx-font-size: 120%;"
    margin = Insets(10,10,10,10)
  }
  exitButton.onAction = (e: ActionEvent) => {
    towerVBox.children.clear()
    menuVBox.children = List(askToSaveHighScoreButton, backToMainMenuButton, continueGameButton)
  }
  val askToSaveHighScoreButton = new Button(Language.getWord(11)) {
    textFill = Black
    prefHeight = gameFieldHeight * 0.04
    //maxHeight = gameFieldHeight * 0.04
    prefWidth = MenuWidth * 0.9
    style = "-fx-background-color: white; -fx-font-size: 120%;"
    margin = Insets(0,10,0,10)
  }
  askToSaveHighScoreButton.onAction = (e: ActionEvent) => {
    exitButton.style = "-fx-background-color: white; -fx-font-size: 120%;"
    exitButton.textFill = Black
    exitButton.prefWidth = 300
    deathVBox.children = List(nameLabel, textField, saveScoreButton, backToMainMenuButton, quitButton)
    g.content = deathVBox
  }
  val quitButton = new Button(Language.getWord(31)) {
    textFill = Black
    prefHeight = gameFieldHeight * 0.04
    maxHeight = gameFieldHeight * 0.04
    prefWidth = 300
    style = "-fx-background-color: white; -fx-font-size: 120%;"
    margin = Insets(0,10,0,10)
  }
  quitButton.onAction = (e: ActionEvent) => {
    System.exit(0)
  }
  val backToMainMenuButton = new Button(Language.getWord(13)) {
    textFill = Black
    prefHeight = gameFieldHeight * 0.04
    maxHeight = gameFieldHeight * 0.04
    prefWidth = 300
    style = "-fx-background-color: white; -fx-font-size: 120%;"
    margin = Insets(0,10,0,10)
  }
  backToMainMenuButton.onAction = (e: ActionEvent) => {
    MainMenu.clearMenu()
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
    prefWidth = 300
    maxWidth = 300
  }
  val saveScoreButton = new Button(Language.getWord(15)) {
    style = "-fx-background-color: white; -fx-font-size: 120%;"
    textFill = Black
    prefHeight = gameFieldHeight * 0.04
    maxHeight = gameFieldHeight * 0.04
    prefWidth = 300
    margin = Insets(0,10,0,10)
  }
  saveScoreButton.onAction = (e: ActionEvent) => {
    if (!textField.getText().isEmpty()) {
      HighScoreFile.saveHighScore(
        g.fieldName,
        textField.getText(),
        g.Player.score
      )
      MainMenu.clearMenu()
      TowerDefence.stage.scene = MainMenu
    }
  }
  
  val deathVBox = new VBox(5) {
    spacing = g.getHeight() *0.02
    layoutX = 0
    layoutY = 0
    prefWidth = g.MenuWidth + g.gameFieldWidth
    //maxWidth = g.MenuWidth + g.gameFieldWidth
    //minHeight = g.gameFieldHeight
    prefHeight = g.gameFieldHeight
    //maxHeight = g.gameFieldHeight
    alignment = Pos.Center
    style = "-fx-background-color: black;"
  }
  
  
  
  
  def scoreMenu(): VBox = {
    scoreVBox
  }
  
  def menuMenu(): VBox = {
    menuVBox.alignment = Pos.BottomCenter
    menuVBox.children = startButton
    menuVBox
  }
  //Road is pressed -> empty menu
  def gameMenu(): VBox = {
    checkSelectionCircle()
    towerVBox.children = List()
    towerVBox
  }
  
  def towerMenu(mouseGrid: (Int, Int), towerPressed: Option[Tower]): VBox = {
    checkSelectionCircle()
    val t = towerPressed.get
    val towerInfo = new Label{
      textFill = White
      prefWidth = MenuWidth
      text = Language.getWord(22)
      margin = Insets(0,10,0,10)
      font = new Font("Arial", 15)
    }
    val stat1 = new Label{
      textFill = White
      prefWidth = MenuWidth
      text = Language.getWord(23) + t.getStats._1
      margin = Insets(10,10,0,10)
      font = new Font("Arial", 13)
    }
    val stat2 = new Label{
      textFill = White
      prefWidth = MenuWidth
      text = Language.getWord(24) + t.getStats._2
      margin = Insets(10,10,0,10)
      font = new Font("Arial", 13)
    }
    val stat3 = new Label{
      textFill = White
      prefWidth = MenuWidth
      text = Language.getWord(25) + t.getStats._3
      margin = Insets(10,10,0,10)
      font = new Font("Arial", 13)
    }
    val upgradePercent = new Label{
      textFill = White
      prefWidth = MenuWidth
      text = Language.getWord(26)
      margin = Insets(10,10,0,10)
      font = new Font("Arial", 13)
    }
    val updateButton = new Button() {
      textFill = Black
      prefHeight = gameFieldHeight * 0.04
      prefHeight = gameFieldHeight * 0.04
      prefWidth = MenuWidth * 0.9
      margin = Insets(10,10,0,10)
    }
    def updateTowerMenuText() {
      updateButton.text = Language.getWord(27) + t.upgradeCost
      updateButton.style = "-fx-background-color: white; -fx-font-size: 120%;"
      stat1.text = Language.getWord(23) + "%1.3f".format(t.getStats._1)
      stat2.text = Language.getWord(24) + t.getStats._2
      stat3.text = Language.getWord(25) + t.getStats._3.toInt
    }
    updateTowerMenuText()
    
    //Draw circle around selected Tower
    drawSelection = true
    selectionCircle = drawCircle(mouseGrid)
    g.content += selectionCircle
    
    updateButton.onAction = (e: ActionEvent) => {
      if(g.Player.money >= t.upgradeCost) {
        g.Player.money -= t.upgradeCost
        updateScoreTexts()
        t.upgrade()
        updateTowerMenuText()
      }
    }
    towerVBox.children = List(towerInfo, stat1, stat2, stat3, updateButton, upgradePercent)
    towerVBox
  }
  
  def shopMenu(mouseGrid: (Int, Int)): VBox = {
    checkSelectionCircle()
    drawSelection = true
    selectionCircle = drawCircle(mouseGrid)
    g.content += selectionCircle
    val shopInfo = new Label{
      textFill = White
      prefWidth = MenuWidth
      text = Language.getWord(28)
      margin = Insets(0,10,0,10)
      font = new Font("Arial", 15)
    }
    val buyLaserButton = new Button(Language.getWord(29) + "75") {
      textFill = Black
      prefHeight = gameFieldHeight * 0.04
      //maxHeight = gameFieldHeight * 0.04
      prefWidth = MenuWidth * 0.9
      style = "-fx-background-color: white; -fx-font-size: 120%;"
      margin = Insets(10,10,10,10)
    }
    buyLaserButton.onAction = (e: ActionEvent) => {
      if (g.Player.money >= 75) {
        g.Player.money -= 75
        updateScoreTexts()
        val newLaser = new Laser(g, mouseGrid._1*g.gridSize, mouseGrid._2*g.gridSize, g.gridSize)
        g.content += newLaser
        g.towerBuffer += newLaser
        towerVBox.children = List()
      }
    }
    val buyMissileLauncherButton = new Button(Language.getWord(32) + "100") {
      textFill = Black
      prefHeight = gameFieldHeight * 0.04
      //maxHeight = gameFieldHeight * 0.04
      prefWidth = MenuWidth * 0.9
      style = "-fx-background-color: white; -fx-font-size: 120%;"
      margin = Insets(10,10,10,10)
    }
    buyMissileLauncherButton.onAction = (e: ActionEvent) => {
      if (g.Player.money >= 100) {
        g.Player.money -= 100
        updateScoreTexts()
        val newRocketL = new RocketLauncher(g, mouseGrid._1*g.gridSize, mouseGrid._2*g.gridSize, g.gridSize)
        g.content += newRocketL
        g.towerBuffer += newRocketL
        towerVBox.children = List()
      }
    }
    
    towerVBox.children = List(shopInfo, buyLaserButton, buyMissileLauncherButton)
    towerVBox
  }
  
  def updateScoreTexts() {
    moneyText.text = Language.getWord(18) + g.Player.money
    healthText.text = Language.getWord(16) + g.Player.health
    scoreText.text = Language.getWord(17) + g.Player.score
    waveText.text = Language.getWord(33) + g.wave
  }
  def drawCircle(mouseGrid: (Int, Int)): Circle = {
    val grid = g.gridSize
    val c = Circle(mouseGrid._1 * grid + grid/2, mouseGrid._2 * grid + grid/2, grid/2+10, Red)
    c.opacity = 0.2
    c
  }
  def checkSelectionCircle() {
    if(drawSelection) {
      drawSelection = false
      if(g.content.contains(selectionCircle)) g.content.remove(selectionCircle)
    }
  }
}