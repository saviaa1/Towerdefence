package Utils

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import scala.io.Source._
import java.io.{File, FileNotFoundException}

class LoadGameTest extends FlatSpec with Matchers {
  val c = new LoadGame
  "Method LoadGame.readFile(non excisting file)" should "return empty string" in {
    assert(c.readFile("empty") == "")
  }
  "Method LoadGame.getListOfFiles" should "return empty List() when non excisting directory" in {
    assertThrows[java.lang.NullPointerException] {
      c.getListOfFiles(new File("non_excisting"), List("json"))
    }
  }
  "Method LoadGame.getListOfFiles" should "return List(..HighScore) when dir highscore" in {
    assert(c.getListOfFiles(new File("./gameFiles/highScore/"), List()) == List())
  }
  "Method LoadGame.getListOfFiles" should "return List(..HighScore) when dir highscore and file extension" in {
    assert(c.getListOfFiles(new File("./gameFiles/highScore/"), List("txt")).head.toString == "./gameFiles/highScore/highscores.txt")
  }
  
  "Method LoadGame.getListOfFiles()" should "return empty Map" in {
    assert(c.getListOfFiles(new File("./gameFiles/highScore/"), List("json")).isEmpty)
  }
  "Method LoadGame.getListOfFiles()" should "return non empty Map" in {
    assert(!c.getListOfFiles(new File("./gameFiles/mapFiles/"), List("json")).isEmpty)
  }
}