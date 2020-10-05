package Objects

import scalafx.scene.image.Image
import GUI.GameArea
 
/**
 * Enemy that has location, health, direction, speed, (target?)
 */
class Enemy1(g: GameArea, x0: Double, y0: Double, val minSpeed: Double, val maxSpeed: Double, val minHealth: Int, val maxHealth: Int, val gridSize: Double) extends Enemy(g: GameArea, new Image("file:./gameFiles/images/Enemy1.png", gridSize, gridSize, true, false)) {
  x = x0
  y = y0
  def enemyTypeHealthFactor = 0.6
  def enemyTypeSpeedFactor = 1.0
  var pathPoint: Int = 0
  val path = g.Path  
}

class Enemy2(g: GameArea, x0: Double, y0: Double, val minSpeed: Double, val maxSpeed: Double, val minHealth: Int, val maxHealth: Int, val gridSize: Double) extends Enemy(g: GameArea, new Image("file:./gameFiles/images/Enemy2.png", gridSize, gridSize, true, false)) {
  x = x0
  y = y0
  def enemyTypeHealthFactor = 1.0
  def enemyTypeSpeedFactor = 0.7
  var pathPoint: Int = 0
  val path = g.Path
  
}
