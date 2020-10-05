package GUI

import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color._
import scalafx.geometry.Pos
import scalafx.event.ActionEvent

/**
 * Stores different languages.
 * Currently stores and reads them off array.
 * 
 */
object Language {
  val langFI = Array[String]("Suomi", "Aloita uusi peli", "Tornipuolustus", "Tilastot", "Jatka tallennettua peliä",
      "Palaa", "Resetöi kaikki", "Kartan nimi", "Nimi", "Pisteet",
      "Peli ohi\nPisteesi: ", "Tallenna pisteesi", "Poistu", "Päävalikkoon", "Nimesi:",
      "Tallenna", "Elämät: ", "Pisteet: ", "Raha: ", "Aloita",
      "Pysäytä", "Poistu", "Tornin tiedot", "Viilentymisaika ", "Ammuksen vahinko: ",
      "Kantama: ", "Parantaa ominaisuuksia 20%", "Parannus kulu", "Osta uusi torni", "Osta laaser torni ",
      "Jatka Peliä", "Lopeta", "Osta ohjus torni ", "Aalto: ", "Valitse kenttä",
      "Virhe: ei kenttä tiedostoja satavilla", "Valitse kartta"
      
  )
  val langEN = Array[String]("English", "Start new game", "Towerdefence", "Highscores", "Continue saved game",
      "Return", "Reset All", "Map name", "Name", "Score",
      "Game over\nYour score: ", "Save your score", "Exit", "To main Menu", "Your name:",
      "Save", "Health: ", "Score: ", "Money: ", "Start",
      "Pause", "Exit", "Tower info", "Cooldown ", "Ammo damage: ",
      "Range: ", "Upgrades stats by 20%", "Upgrade cost ", "Buy a new Tower", "Buy Laser for ",
      "Continue game", "Quit", "Buy Missile for ", "Wave: ", "Choose Map",
      "Error: no map files", "Select a map"
      
  )
  
  def getWord(i: Int): String = {
    this.chosenLang(TowerDefence.LangChoice, i)
  }
  
  def chosenLang(choise: String, word: Int): String = {
    choise match {
      case "FI" => langFI(word)
      case "EN" => langEN(word)
      case _        => "err"
    }
  }
}

/**
 * Menu scene where you choose your preferred language.
 * Saves that selection at TowerDefence.LangChoice as a String.
 */
object LanguageMenu extends Scene(1200, 800) {
  val buttonFI = new Button(Language.chosenLang("FI", 0)) {
    textFill = Black
    prefHeight = LanguageMenu.getHeight() * 0.02
    maxWidth = 200
    style = "-fx-background-color: white; -fx-font-size: 120%;"
  }
  val buttonEN = new Button(Language.chosenLang("EN", 0)) {
    textFill = Black
    prefHeight = LanguageMenu.getHeight() * 0.02
    maxWidth = 200
    style = "-fx-background-color: white; -fx-font-size: 120%;"
  }
  root = new VBox {
    spacing = LanguageMenu.getHeight() * 0.02
    children = List(buttonFI, buttonEN)
    alignment = Pos.Center
    style = "-fx-background-color: black"
  }
  
  buttonFI.onAction = (e: ActionEvent) => {
    TowerDefence.LangChoice = "FI"
    TowerDefence.stage.scene = MainMenu
  }
  buttonEN.onAction = (e: ActionEvent) => {
    TowerDefence.LangChoice = "EN"
    TowerDefence.stage.scene = MainMenu
  }
}