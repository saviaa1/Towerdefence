package Utils

import scala.io.Source._
import java.io.File
import java.io.FileNotFoundException
import play.api.libs.json._
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax
import GUI.{GameArea, Player}

/*
 * case class are used to define JSON data format
 */
case class GameInfo(
                Info:  Option[info],
                Path:  Option[Path],
                Field: Option[Field]
              )
case class info(
                fieldName: String,
                playerHealth: Int,
                playerStartMoney: Int,
                gridSize: Double,
                rows: Int,
                columns: Int,
                width: Int,
                height: Int,
                numberOfEnemies: Int,
                enemyMinHealth: Int,
                enemyMaxHealth: Int,
                enemyMinSpeed: Double,
                enemyMaxSpeed: Double,
                minSpawnRate: Double
              )
case class Path(
                pathPoints: Vector[Vector[Int]]
              )
case class Field(
                field: Vector[Vector[Int]],
                towers: Vector[Vector[Int]]
              )

/**
 * Loads game from JSON file.
 * And GameArea is initialised with values from file.
 */
class LoadGame {
  def initGameArea(selectedGame: String): GameArea = {
    val jsonString: String = readFile(selectedGame)
    readJSON(jsonString)
  }
  def readFile(file: String): String = {
    var ret: String = ""
    try {
      val source = scala.io.Source.fromFile(file)
      try {
        ret = source.mkString
      } catch {
        //ioexception
        case e: Exception => ret = ""; println("something else is wrong")
      } finally {
        source.close()
      }
    } catch {
      case ioe: FileNotFoundException => {
        println("file does not excist")
      }
    }
    ret
  }
  
  /**
   * https://alvinalexander.com/scala/how-to-list-files-in-directory-filter-names-scala/
   */
  def getListOfFiles(dir: File, extensions: List[String]): List[File] = {
    dir.listFiles.filter(_.isFile).toList.filter { file =>
      extensions.exists(file.getName.endsWith(_))
    }
  }
  def getMapName(files: List[File]): Map[String, File] = {
    implicit val infoReads = Json.reads[info]
    implicit val pathReads = Json.reads[Path]
    implicit val fieldReads= Json.reads[Field]
    implicit val GameInfoR = Json.reads[GameInfo]
    
    var mapNames: Map[String, File] = Map()
    for (file <- files) {
      val data: String = readFile(file.toString())
      if (!data.isEmpty()) {
        try {
          val inputJson = Json.parse(data)
          val JsonInstance: GameInfo = inputJson.as[GameInfo]
          val fieldName: Option[String] = JsonInstance.Info.map(_.fieldName)
          mapNames = mapNames ++ Map(fieldName.get -> file)
        } catch {
          case _ : Throwable => {
            println("JSON parse error")
          }
        }
      }
    }
    mapNames
  }
  
  private def readJSON(data: String): GameArea = {
    implicit val infoReads = Json.reads[info]
    implicit val pathReads = Json.reads[Path]
    implicit val fieldReads= Json.reads[Field]
    implicit val GameInfoR = Json.reads[GameInfo]
    
    val inputJson = Json.parse(data)
    
    val JsonInstance: GameInfo = inputJson.as[GameInfo]
    
    val playerHealth: Option[Int]            = JsonInstance.Info.map(_.playerHealth)
    val playerStartMoney: Option[Int]        = JsonInstance.Info.map(_.playerStartMoney)
    val player: Player                       = new Player(playerHealth.get, playerStartMoney.get)
    val fieldName: Option[String]            = JsonInstance.Info.map(_.fieldName)
    val gridSize: Option[Double]             = JsonInstance.Info.map(_.gridSize)
    val rows: Option[Int]                    = JsonInstance.Info.map(_.rows)
    val columns: Option[Int]                 = JsonInstance.Info.map(_.columns)
    val pathPoints: Option[Vector[Vector[Int]]]   = JsonInstance.Path.map(_.pathPoints)
    val path: GamePath                       = new GamePath(pathPoints.get.size, gridSize.get, pathPoints.get)
    
    val width: Option[Int]                   = JsonInstance.Info.map(_.width)
    val height: Option[Int]                  = JsonInstance.Info.map(_.height)
    val numberOfEnemies: Option[Int]         = JsonInstance.Info.map(_.numberOfEnemies)
    val minSpawnRate: Option[Double]         = JsonInstance.Info.map(_.minSpawnRate)
    val enemyMinHealth: Option[Int]          = JsonInstance.Info.map(_.enemyMinHealth)
    val enemyMaxHealth: Option[Int]          = JsonInstance.Info.map(_.enemyMaxHealth)
    val enemyMinSpeed: Option[Double]        = JsonInstance.Info.map(_.enemyMinSpeed)
    val enemyMaxSpeed: Option[Double]        = JsonInstance.Info.map(_.enemyMaxSpeed)
    
    val g: GameArea = new GameArea(
        width.get,
        height.get,
        200,
        player,
        path,
        gridSize.get,
        rows.get,
        columns.get,
        numberOfEnemies.get,
        minSpawnRate.get,
        enemyMinHealth.get,
        enemyMaxHealth.get,
        enemyMinSpeed.get,
        enemyMaxSpeed.get,
        fieldName.get
      )
    val fieldArr: Option[Vector[Vector[Int]]] = JsonInstance.Field.map(_.field)
    g.initField(fieldArr.get)
    val towerArr: Option[Vector[Vector[Int]]] = JsonInstance.Field.map(_.towers)
    g.initTowers(towerArr.get)
    g
  }
}
