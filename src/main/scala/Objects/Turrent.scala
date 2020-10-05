package Objects

import scalafx.scene.image.{ImageView, Image}
import scalafx.Includes._
import scalafx.scene.shape.Line
import scalafx.scene.paint.Color
import GUI.GameArea
import scala.math


/**
 * Tower that has location, bulletDamage, firerate, (target?)
 */
class Laser(g: GameArea, x0: Double, y0: Double, val gridSize: Double) extends Tower(g: GameArea, new Image("file:./gameFiles/images/Turrent1.png", gridSize, gridSize, false, false)) {
  x = x0
  y = y0
  
  var firingSpeed: Double = 0.5
  var ammoDamage = 20
  var range: Double = 200
  
  val Grid: (Int, Int) = ((x/gridSize).toInt, (y/gridSize).toInt)
  def shoot(delta: Double) = {
    secondsSince += delta
    if (secondsSince >= firingSpeed) {
      secondsSince = 0
      if (g.enemyBuffer.size > 0) {
        val target: Option[Enemy] = findClosestEnemy(range)
        if (target.isDefined) {
          val L: LaserLine = new LaserLine(g, target.get, this, ammoDamage)
          g.LaserLineBuffer += L
          g.content += L 
        }
      }
    }
  }
  def upgrade() = {
    timesUpgraded += 1
    firingSpeed = firingSpeed*0.8
    ammoDamage = (ammoDamage*1.2).ceil.toInt
    range = range*1.2
  }
  def upgradeCost: Int = (75*timesUpgraded*0.5).toInt
}

/**
 * Bullet that Turrent fires, has speed, direction, damage, target.
 */
class LaserLine(g: GameArea, target: Enemy, shooter: Laser, ammoDamage: Int) extends Line {
  val x: Double = shooter.x.value
  val y: Double = shooter.y.value
  var isAlive: Boolean = true
  val lineDrawTime: Double = 0.2
  var seconds: Double = 0
  
  stroke = Color.web("RED", 0.5)
  startX = x + 25
  startY = y + 25
  endX = target.x.value + 25
  endY = target.y.value + 25
  strokeWidth = 3
  
  target.damage(ammoDamage)
  
  def lineTime(delta: Double): Unit = {
    seconds += delta
    if (seconds >= lineDrawTime) {
      isAlive = false
    }
  }
}

/**
 * Rocketlauncher tower.
 */
class RocketLauncher(g: GameArea, x0: Double, y0: Double, val gridSize: Double) extends Tower(g: GameArea, new Image("file:./gameFiles/images/Turrent2.png", gridSize, gridSize, false, false)) {
  x = x0
  y = y0
  
  var firingSpeed: Double = 3.0
  var ammoDamage = 50
  var range: Double = 400
  var splashDamage: Int = 25
  var splashRange: Int = 100
  var rocketSpeed: Double = 200
  
  val Grid: (Int, Int) = ((x/gridSize).toInt, (y/gridSize).toInt)
  def shoot(delta: Double) = {
    secondsSince += delta
    if (secondsSince >= firingSpeed) {
      secondsSince = 0
      if (g.enemyBuffer.size > 0) {
        val target: Option[Enemy] = findClosestEnemy(range)
        if (target.isDefined) {
          val shell = new Rocket(g, target.get, this, ammoDamage, splashDamage, splashRange, rocketSpeed, gridSize)
          g.ammoBuffer += shell
          g.content += shell
        }
      }
    }
  }
  def upgrade() = {
    timesUpgraded += 1
    firingSpeed = firingSpeed*0.8
    ammoDamage = (ammoDamage*1.2).ceil.toInt
    range = range*1.2
    splashDamage = (splashDamage*1.2).ceil.toInt
    splashRange = (splashRange*1.2).ceil.toInt
    rocketSpeed *= 1.2 
  }
  def upgradeCost: Int = (100*timesUpgraded*0.5).toInt
}

/**
 * Rocket fired by Rocketlauncher -tower.
 */
class Rocket(g: GameArea, target: Enemy, shooter: RocketLauncher, val ammoDamage: Int, val splashDamage: Int, val splashRange: Int, var speed: Double, val gridSize: Double) extends Ammo(g: GameArea, new Image("file:./gameFiles/images/Turrent1Ammo.png", gridSize*0.4, gridSize*0.4, false, false)) {
  x = shooter.x.value + gridSize/2 - gridSize*0.2
  y = shooter.y.value + gridSize/2 - gridSize*0.2
  
  def move(delta: Double): Unit = {
    val dx: Double = (target.x.value + gridSize / 2 - gridSize*0.2) - this.x.value
    val dy: Double = (target.y.value + gridSize / 2 - gridSize*0.2) - this.y.value
    val length: Double = Math.sqrt(dx*dx + dy*dy)

    if (!this.intersects(target.boundsInLocal.value) && length > delta*speed) {
      this.x = this.x.value + (dx/length) * delta * speed
      this.y = this.y.value + (dy/length) * delta * speed
    }
    else {
      this.isAlive = false
      hit(target): Unit
    }
  }
  /**
   * Enemy directly hit receives ammoDamage which is largest and other enemies who are inside splahRange receive splash damage.
   */
  override def hit(taget: Enemy): Unit = {
    g.enemyBuffer.foreach((e: Enemy) => {
      if (!e.eq(target)) {
        if(this.distance(e) <= splashRange) {
          e.damage(splashDamage)
        }
      }
      else {
        e.damage(ammoDamage)
      }
    })
  }
}
