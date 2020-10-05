package Objects

import scalafx.scene.image.Image

/**
 * Block where Towers are allowed but Enemies aren't
 */
class Ground(x0: Double, y0: Double, gridSize: Double) extends Surface(new Image("file:./gameFiles/images/Ground.png", gridSize, gridSize, false, false)) {
  x = x0
  y = y0
}

/**
 * Block where Enemies are allowed but Towers aren't
 */
class Road(x0: Double, y0: Double, gridSize: Double) extends Surface(new Image("file:./gameFiles/images/Path.png", gridSize, gridSize, false, false)) {
  x = x0
  y = y0
}