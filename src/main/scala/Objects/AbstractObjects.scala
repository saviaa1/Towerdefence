package Objects

import scalafx.scene.image.{ImageView, Image}
import scalafx.Includes._
import GUI.GameArea
import Utils.GamePath
import scala.math
import scala.util.Random

/**
 * This is the parent class for all objects in the game
 */
abstract class GameObject(img: Image) extends ImageView(img)

abstract class Surface(img: Image) extends GameObject(img)

abstract class Tower(g: GameArea, img: Image) extends GameObject(img) {
  val gridSize: Double
  val Grid: (Int, Int)
  var timesUpgraded: Int = 1
  var firingSpeed: Double
  var ammoDamage: Int
  var range: Double
  var secondsSince: Double = 0
  
  def shoot(delta: Double): Unit
  def upgradeCost: Int
  def getStats: (Double, Int, Double) = (firingSpeed, ammoDamage, range)
  def upgrade()
  def findClosestEnemy(range: Double): Option[Enemy] = {
    var closestEnemy: Option[Enemy] = None
    var tempDistance: Double = 0
    for (e <- g.enemyBuffer) {
      var distance2: Double = ((this.x.value - e.x.value) * (this.x.value - e.x.value)) + ((this.y.value - e.y.value) * (this.y.value - e.y.value))
      if ((range*range) >= distance2 && (tempDistance == 0 || tempDistance > distance2)) {
        closestEnemy = Some(e)
        tempDistance = distance2
      }
    }
    closestEnemy
  }
}

/**
 * This is the parent class of all moving objects in the game
 */
abstract class MovingObject(g: GameArea, img: Image) extends GameObject(img) {
  var speed: Double
  var isAlive: Boolean = true
  
  def move(delta: Double): Unit
  def checkOutOfBounds(): Unit = {
    if (x.value <= 0 - image.value.width.value || x.value > g.gameFieldWidth || y.value <= 0 - image.value.height.value || y.value > g.gameFieldHeight) {
      isAlive = false
    }
  }
  
  def distance(e: GameObject): Double = {
    val dx: Double = e.x.value - this.x.value
    val dy: Double = e.y.value - this.y.value
    val length: Double = Math.sqrt(dx*dx + dy*dy)
    length
  }
}

/**
 * This is the parent class of all Ammo objects in the game.
 * Excluding Laser's ammo
 */
abstract class Ammo(g: GameArea, img: Image) extends MovingObject(g, img) {
  val ammoDamage: Int
  def hit(enemy: Enemy): Unit = enemy.damage(ammoDamage)
}

/**
 * This is the parent class of all enemy objects in the game.
 */
abstract class Enemy(g: GameArea, img: Image) extends MovingObject(g, img) {
  val minHealth: Int
  val maxHealth: Int
  val minSpeed: Double
  val maxSpeed: Double
  var pathPoint: Int
  val path: GamePath
  val gridSize: Double
  
  val r = new Random()
  var health: Int = minHealth + r.nextInt((enemyTypeHealthFactor * ((minHealth + maxHealth) + 1)).toInt)
  val originalHealth: Int = health
  var speed: Double = minSpeed + (enemyTypeSpeedFactor * r.nextDouble() * (minSpeed + maxSpeed + 1))
  
  def enemyTypeHealthFactor: Double
  def enemyTypeSpeedFactor: Double
  def location(): (Double, Double) = (this.x.value, this.y.value)
  def damage(damage: Int): Unit = {
    this.health -= damage
    if (this.health <= 0) {
      this.isAlive = false
      g.Player.money += (originalHealth*0.2).ceil.toInt
    }
  }
  
  def move(delta: Double): Unit = {
    if (pathPoint < path.pathPoints.size) {
      path.pathPoints(pathPoint)(2) match {
        //Up
        case 0 =>     if(this.y.value > path.pathPoints(pathPoint)(1)*gridSize) {
                        this.y = this.y.value - speed*delta
                      } else {
                        this.y = path.pathPoints(pathPoint)(1)*gridSize
                        pathPoint += 1
                      }
        //Right
        case 1 =>     if(this.x.value < path.pathPoints(pathPoint)(0)*gridSize) {
                        this.x = this.x.value + speed*delta
                      } else {
                        this.x = path.pathPoints(pathPoint)(0)*gridSize
                        pathPoint += 1
                      }
        //Down         
        case 2 =>     if(this.y.value < path.pathPoints(pathPoint)(1)*gridSize) {
                        this.y = this.y.value + speed*delta
                      } else {
                        this.y = path.pathPoints(pathPoint)(1)*gridSize
                        pathPoint += 1
                      }
        case _ =>     println("Error, move.case_"); this.isAlive = false //failsafe
      }
    } else {
      this.x = this.x.value + speed*delta
    }
    checkOutOfBounds()
  }
  
  override def checkOutOfBounds() = {
    if (x.value <= 0 - image.value.width.value || y.value <= 0 - image.value.height.value || y.value > g.gameFieldHeight) {
      isAlive = false
    }
    else if (x.value > g.gameFieldWidth) {
      isAlive = false
      if (g.Player.health - 1 <= 0) {
        g.timer.stopTimer
        println("Player is dead")
        g.Player.die(g)
        
      } else {
        g.Player.health -= 1
      }
    }
  }
}
