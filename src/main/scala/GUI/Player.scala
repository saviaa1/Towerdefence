package GUI

class Player(var health: Int, var money: Int) {
  var score: Int = 0
  
  def die(g: GameArea) {
    g.mouseActive = false
    new DeathMenu(g)
  }
}