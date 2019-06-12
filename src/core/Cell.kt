package core

class Cell(val x: Int, val y: Int) {

    operator fun plus(other: Cell): Cell {
        return Cell(x + other.x, y + other.y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is Cell) {
            val cell = other as Cell?
            return x == cell!!.x && y == cell.y
        }
        return false
    }

    override fun hashCode(): Int {
        var result = 11
        result = 19 * result + x
        return 19 * result + y
    }

    override fun toString(): String {
        return "$x:$y"
    }
}