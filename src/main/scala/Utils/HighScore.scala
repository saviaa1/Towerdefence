package Utils

import scala.io.Source
import java.io.PrintWriter
import scalafx.collections.ObservableBuffer

//Saves players' results as HighScore-class
case class HighScore(fieldName: String, val name: String, val score: Int)

//Reads and modifies HighScore file's data and passes it elsewhere.
object HighScoreFile {
  //Filename of the file where all HighScores are saved
  private val fileName = "./gameFiles/highScore/highscores.txt"
  
  //Buffers where HighScore classes are stored and which HighScoreMenu reads
  var ScoreBuffer = ObservableBuffer[HighScore] ()
  
  //Overrides old file and writes what's on buffers there.
  private def write() = {
    val file = new PrintWriter(fileName)
    try {
      putInOrder()
      file.println("//Don't change this file, fieldName|name|score")
      //goes through all buffers and prints them to the file
      for (i <- this.ScoreBuffer) {
        file.println(i.fieldName + "|" + i.name + "|" + i.score)
      }
    }
    finally {
      file.close()
    }
  }
  
  //Method which reads file and creates and add all HighScore classes to buffers
  def read() = {
    val file = Source.fromFile(fileName)
    try {
      val fileVector = file.getLines.toVector
      
      resetBuffer()
      //Goes through every line except the first line in the file and extracts data from lines and creates HighScore class and adds it to correct buffer
      for (i <- fileVector.tail) {
        val array = i.split('|')
        this.ScoreBuffer += HighScore(array(0), array(1), array(2).toInt)
      }
      putInOrder()
    }
    catch {
      //In case of file error, resets file and all HighScore buffers
      case ioException: Throwable => reset(); println("FileError, file reset")
    }
    finally {
      file.close()
    }
  }
  
  //Private method which puts all buffers in order from largest score to smallest score
  private def putInOrder() = {
    this.ScoreBuffer = this.ScoreBuffer.sortBy(_.score)
    this.ScoreBuffer = this.ScoreBuffer.reverse
  }
  
  //Resets buffers and file
  def reset() {
    resetBuffer()
    val file = new PrintWriter(fileName)
    try {
      file.println("//Don't change this file, fieldName|name|score")
    }
    finally {
      file.close()
    }
  }
  
  //Resets only buffers
  private def resetBuffer() {
    this.ScoreBuffer.clear()
  }
  
  //Method is used to add new highscore to buffers and file
  def saveHighScore(fieldName: String, name: String, score: Int) = {
    this.ScoreBuffer += HighScore(fieldName, name, score)
    write()
  }
}