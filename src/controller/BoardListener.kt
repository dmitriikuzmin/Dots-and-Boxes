package controller

import core.Cell

interface BoardListener {
    fun turnMade(cell: Cell, captured: Array<Cell?>?)
}