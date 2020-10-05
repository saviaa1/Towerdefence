package GUI

import scala.collection.mutable.Buffer
import scalafx.scene._
import scalafx.scene.paint.Color._
import scalafx.scene.layout.GridPane._
import scalafx.scene.layout.{ColumnConstraints, RowConstraints}
import scalafx.scene.paint.Color._
import scala.collection.mutable.Map
import scalafx.scene.control._
import scalafx.Includes._
import scala.util.Random
import scalafx.scene.text.Font
import scalafx.scene.layout._
import scalafx.geometry._
import scala.util.Random
import scalafx.event.ActionEvent
import scalafx.scene.input.MouseEvent
import scalafx.event.EventHandler
import Objects._
import Utils.GamePath
import scalafx.stage.Screen

class GameArea( val gameFieldWidth: Int,
                val gameFieldHeight: Int,
                val MenuWidth: Int,
                val Player: Player,
                val Path: GamePath,
                val gridSize: Double,
                val rows: Int,
                val columns: Int,
                var enemiesToSpawn: Int,
                var minSpawnRate: Double,
                val EminHealth: Int,
                val EmaxHealth: Int,
                val EminSpeed: Double,
                val EmaxSpeed: Double,
                val fieldName: String
              )
      extends Scene(gameFieldWidth + MenuWidth, gameFieldHeight)
{
  var wave: Int = 1
  val originalEnemiesToSpawn: Int = enemiesToSpawn
  var timer = new GameTimer(this)
  var towerBuffer = Buffer[Tower]()
  var enemyBuffer = Buffer[Enemy]()
  var ammoBuffer = Buffer[Ammo]()
  var LaserLineBuffer = Buffer[LaserLine]()
  var surfaceArr: Vector[Vector[Int]] = Vector[Vector[Int]]()
  var mouseActive: Boolean = true
  
  val r = new Random(352)
  var timeSinceLastSpawn: Double = 0
  
  val gameFieldMenu = new GameFieldMenu(this, gameFieldWidth, MenuWidth, gameFieldHeight)
  if (mouseActive) {
    onMouseClicked = (a: MouseEvent) => {
      if (a.x >= 0 && a.x < gameFieldWidth && a.y >= 0 && a.y < gameFieldHeight) {
        mousePressed(((a.x/gridSize).toInt, (a.y/gridSize).toInt))
      }
    }
  }
  
  val scoreVBox: VBox =  gameFieldMenu.scoreMenu()
  val towerVBox: VBox =  gameFieldMenu.gameMenu()
  val menuVBox: VBox = gameFieldMenu.menuMenu()
  content += scoreVBox
  content += towerVBox
  content += menuVBox
    
  def mousePressed(mouseGrid: (Int, Int)) = {
    var towerAtPressed: Boolean = false
    var towerPressed: Option[Tower] = None: Option[Tower]
    var groundAtPressed: Boolean = false
    var roadAtPressed: Boolean = false
    //For Towers
    for(t <- towerBuffer){
      if (t.Grid == mouseGrid) {
        towerAtPressed = true
        towerPressed = Some(t)
      }
    }
    //For Ground
    for(g <- surfaceArr) {
      if ((g(0), g(1)) == mouseGrid) {
        if (g(2) == 0) {
          groundAtPressed = true
        }
        else {
          roadAtPressed = true
        }
      }
    }
    menuLogic(towerAtPressed, groundAtPressed, roadAtPressed, mouseGrid, towerPressed)
  }
  
  /**
   * Determines what happens when gameArea is mouse clicked.
   */
  def menuLogic(towerAtPressed: Boolean, groundAtPressed: Boolean, roadAtPressed: Boolean, mouseGrid: (Int, Int), towerPressed: Option[Tower]) {
    //If Tower is pressed
    if (towerAtPressed && groundAtPressed && !roadAtPressed && towerPressed.isDefined) gameFieldMenu.towerMenu(mouseGrid, towerPressed)
    //If empty ground is pressed
    else if (!towerAtPressed && groundAtPressed && !roadAtPressed) gameFieldMenu.shopMenu(mouseGrid)
    //If road is pressed
    else if (roadAtPressed) gameFieldMenu.gameMenu()
    else println("menuLogic unknown combination " + (towerAtPressed, groundAtPressed, roadAtPressed))
  }
  
  def initField(arr: Vector[Vector[Int]]) = {
    surfaceArr = arr
    for (square <- arr) {
      if (square.size == 3) {
        square(2) match {
          case 0 => this.content += new Ground(square(0)*gridSize, square(1)*gridSize, gridSize-1)
          case 1 => this.content += new Road(square(0)*gridSize, square(1)*gridSize, gridSize-1)
        }
      }
    }
  }
  def initTowers(arr: Vector[Vector[Int]]) = {
    for (i <- arr) {
      i(2) match {
        case 1 => {
          val tower = new Laser(this, i(0)*gridSize, i(1)*gridSize, gridSize)
          this.content += tower 
          this.towerBuffer += tower
        }
        case 2 => {
          val tower = new RocketLauncher(this, i(0)*gridSize, i(1)*gridSize, gridSize)
          this.content += tower
          this.towerBuffer += tower
        }
      }
    }
  }
  def spawnEnemies(delta: Double) = {
    timeSinceLastSpawn += delta
    if (enemiesToSpawn > 0 && timeSinceLastSpawn >= minSpawnRate) {
      if (r.nextInt(10) <= 8) {
        if (r.nextInt(3) < 2) {
          val enemy = new Enemy1(this, Path.pathPoints(0)(0)*gridSize, Path.pathPoints(0)(1)*gridSize, EminSpeed, EmaxSpeed, EminHealth, EmaxHealth, gridSize)
          this.content += enemy
          this.enemyBuffer += enemy
          this.enemiesToSpawn -= 1
        }
        else {
          val enemy = new Enemy2(this, Path.pathPoints(0)(0)*gridSize, Path.pathPoints(0)(1)*gridSize, EminSpeed, EmaxSpeed, EminHealth, EmaxHealth, gridSize)
          this.content += enemy
          this.enemyBuffer += enemy
          this.enemiesToSpawn -= 1
        }
        
      }
      timeSinceLastSpawn = 0
    }
  }
}

/**
 * Greates grid lines, NOT currently used.
 * Intended as helper class.
 * Grid lines currently created by tweaking surface objects size
 */
class createGrid(gridSize: Double, rows: Int, columns: Int) {
  def init: GridPane = {
    var grid: GridPane = new GridPane()
    grid.gridLinesVisible = true
    for(i <- 0 until columns) {
      grid.getColumnConstraints().add(new ColumnConstraints(gridSize))
    }
    for(j <- 0 until rows) {
      grid.getRowConstraints().add(new RowConstraints(gridSize))
    }
    grid.setStyle("-fx-grid-lines-visible: true;")
    return grid
  }
}