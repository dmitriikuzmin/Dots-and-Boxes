package core

import controller.BoardListener

class Board constructor(private val width: Int, private val height: Int) {

    private val chips = mutableMapOf<Cell, Player>()

    var turn = Player.BLUE

    var redPoints = 0

    var bluePoints = 0

    private var listener: BoardListener? = null

    fun clear() {
        chips.clear()
        bluePoints = 0
        redPoints = 0
        turn = Player.BLUE
    }

    fun registerListener(listener: BoardListener) {
        this.listener = listener
    }

    operator fun get(x: Int, y: Int): Player? {
        return get(Cell(x, y))
    }

    operator fun get(cell: Cell): Player? {
        return chips[cell]
    }

    fun makeTurn(cell: Cell): Cell? {
        return makeTurn(cell, true)
    }

    private fun moveType(cell: Cell): MoveType {
        return when {
            cell.x == 0 -> MoveType.LEFT
            cell.y == 0 -> MoveType.BOTTOM
            cell.x == width - 1 -> MoveType.RIGHT
            cell.y == height - 1 -> MoveType.TOP
            cell.x % 2 == 1 -> MoveType.CENTER_H
            else -> MoveType.CENTER_V
        }
    }

    private fun captured(cell: Cell): Array<Cell?> {
        val type = moveType(cell)

        val dir = type.dir(type)

        val addition = type.capturedCell(type)

        var first: Cell? = Cell(0,0)

        var second: Cell? = Cell(0,0)

        if (addition.second == null) {
            dir.forEach {
                val newCell = cell.plus(it)
                if (chips[newCell] == null) {
                    return arrayOf(null, null)
                }
            }
            first = cell.plus(addition.first)
            return arrayOf(first, null)

        } else {
            if (type == MoveType.CENTER_H || type == MoveType.CENTER_V) {

                for (i in 0..5) {
                    val newCell = cell.plus(dir[i])
                    val free = chips[newCell] == null
                    when {
                        free && i in 0..2 -> first = null
                        free && i in 3..5 -> second = null
                    }
                }

                if (first != null) {
                    first = cell + addition.first
                }

                if (second != null) {
                    second = cell + addition.second!!
                }

                return arrayOf(first, second)
            }
        }
        return arrayOf(null, null)
    }

    private fun makeTurn(cell: Cell, withEvent: Boolean): Cell? {
        val success = captured(cell)

        if (!chips.containsKey(cell)) {
            chips[cell] = turn
            when {

                success[0] == null && success[1] == null -> {
                    turn = turn.opposite()
                }

                success[0] == null || success[1] == null -> {
                    if (turn == Player.RED) {
                        redPoints++
                    } else {
                        bluePoints++
                    }
                }

                else -> {
                    if (turn == Player.RED) {
                        redPoints += 2
                    } else {
                        bluePoints += 2
                    }
                }
            }

            if (listener != null && withEvent) {
                listener!!.turnMade(cell, success)
            }

            return cell
        }
        return null
    }

    private fun hasFreeCells(): Boolean {
        for (x in 0 until width) {
            for (y in 0 until height) {
                if ((x + y) % 2 == 1) {
                    val cell = get(x, y)
                    cell ?: return true
                }
            }
        }
        return false
    }

    fun winner(): Player? {
        if (!hasFreeCells()) {
            return when {
                bluePoints > redPoints -> Player.BLUE
                else -> Player.RED
            }
        }
        return null
    }
}