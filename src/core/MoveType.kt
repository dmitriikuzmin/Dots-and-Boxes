package core

enum class MoveType {
    LEFT,
    RIGHT,
    TOP,
    BOTTOM,
    CENTER_V,
    CENTER_H;


    fun dir(type: MoveType): Array<Cell> {
        return when (type) {
            LEFT -> arrayOf(Cell(1,1),Cell(2,0),Cell(1,-1))
            RIGHT -> arrayOf(Cell(-1,1),Cell(-2,0), Cell(-1,-1))
            TOP -> arrayOf(Cell(-1,-1),Cell(0,-2), Cell(1,-1))
            BOTTOM -> arrayOf(Cell(-1,1),Cell(0,2), Cell(1,1))
            CENTER_V -> arrayOf(Cell(1,1),Cell(2,0),Cell(1,-1),Cell(-1,1),Cell(-2,0), Cell(-1,-1))
            CENTER_H -> arrayOf(Cell(-1,-1),Cell(0,-2), Cell(1,-1),Cell(-1,1),Cell(0,2), Cell(1,1))
        }
    }

    fun capturedCell(type: MoveType): Pair<Cell,Cell?> {
        return when (type) {
            LEFT -> Pair(Cell(1,0),null)
            RIGHT -> Pair(Cell(-1,0),null)
            TOP -> Pair(Cell(0,-1),null)
            BOTTOM -> Pair(Cell(0,1),null)
            CENTER_V -> Pair(Cell(1,0),Cell(-1,0))
            CENTER_H -> Pair(Cell(0,-1),Cell(0,1))
        }
    }
}