package Objects

import org.scalatest._
import scala.math

class AbstractObjectsTest extends FlatSpec with Matchers{
  val mockEnemy1 = new Enemy1(null, 50, 60, 10, 10, 10, 10, 50)
  val mockEnemy2 = new Enemy2(null, 25, 35, 10, 10, 10, 10, 50)
  assert(mockEnemy1.distance(mockEnemy2) == Math.sqrt((50-25)*(50-25) + (60-35)*(60-35)))
}