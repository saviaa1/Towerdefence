package GUI

import scalafx.animation.AnimationTimer
import scalafx.scene.layout.GridPane
import scalafx.Includes._
import Objects.{Ammo, Enemy, LaserLine}

/**
   * Timer class of the game.
   */
class GameTimer(g: GameArea) {
  var oldTime: Long = 0L
  var second: Double = 0
  var scoreTime = 0.0
  var waveTime = 0.0
  
  val mainTimer = AnimationTimer(t => {
    if (oldTime > 0) {
      val delta = (t - oldTime)/1e9
      doActions(delta)
      scoreTime += delta
      //Adds one score every second
      if (scoreTime >= 1) {
        g.Player.score += 1
        g.Player.money += 1
        scoreTime = 0
      }
    }
    oldTime = t
  })
  
  /**
   * Actions that need to happen every tick
   */
  private def doActions(delta: Double) {
    g.spawnEnemies(delta)
    g.enemyBuffer.foreach(_.move(delta))
    
    g.towerBuffer.foreach(_.shoot(delta))
    g.LaserLineBuffer.foreach(_.lineTime(delta))
    g.ammoBuffer.foreach(_.move(delta))
    removeDeadObjects()
    
    //Update texts in GameMenu
    g.gameFieldMenu.updateScoreTexts()
    
    //Keep menu on top of scene
    g.menuVBox.toFront()
    g.towerVBox.toFront()
    g.scoreVBox.toFront()
    isWaveEnded(delta)
  }
  
  private def isWaveEnded(delta: Double) = {
    val timeBetweenWaves: Double = 3.0
    if (g.enemiesToSpawn <= 0 && g.enemyBuffer.size <= 0) {
      if (timeBetweenWaves <= waveTime) {
        waveTime = 0.0
        g.enemiesToSpawn = g.originalEnemiesToSpawn + g.wave
        g.wave += 1
      }
      else {
        waveTime = waveTime + delta
      }
    }
  }
  private def removeDeadObjects() = {
    val enemiesToRemove = g.enemyBuffer.filter(!_.isAlive)
    g.enemyBuffer.foreach((e: Enemy) => if (enemiesToRemove.contains(e)) {
      g.enemyBuffer.remove(g.enemyBuffer.indexOf(e))
      if (g.content.contains(e)) {
        g.content.remove(e)
      }
    })
    val laserLineToRemove = g.LaserLineBuffer.filter(!_.isAlive)
    g.LaserLineBuffer.foreach((e: LaserLine) => if (laserLineToRemove.contains(e)) {
      g.LaserLineBuffer.remove(g.LaserLineBuffer.indexOf(e))
      if (g.content.contains(e)) {
        g.content.remove(e)
      }
    })
    val ammoToRemove = g.ammoBuffer.filter(!_.isAlive)
    g.ammoBuffer.foreach((e: Ammo) => if (ammoToRemove.contains(e)) {
      g.ammoBuffer.remove(g.ammoBuffer.indexOf(e))
      if (g.content.contains(e)) {
        g.content.remove(e)
      }
    })
  }
  def startTimer = {
    mainTimer.start
  }
  def stopTimer = {
    mainTimer.stop
    oldTime = 0L
  }
}