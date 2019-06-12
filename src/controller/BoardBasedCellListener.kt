package controller

import core.*

class BoardBasedCellListener(private val board: Board) {

    fun cellClicked(cell: Cell) {
        if (board.winner() == null) {
            board.makeTurn(cell)
        }
    }
}