package javafx

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import controller.BoardBasedCellListener
import controller.BoardListener
import core.*
import tornadofx.*

class DotsAndBoxesView : View(), BoardListener {

    private val columnsNumber = 9

    private val rowsNumber = 9

    private val board = Board(columnsNumber, rowsNumber)

    private val buttons = mutableMapOf<Cell, Button>()

    private var inProcess = true

    private lateinit var statusLabel: Label

    override val root = BorderPane()

    private val gridSize = 70.px

    init {
        title = "Dots and Boxes"
        val listener = BoardBasedCellListener(board)
        board.registerListener(this)

        with(root) {
            style {
                padding = box(50.px)
            }
            top {
                vbox {
                    menubar {
                        menu("Game") {
                            item("Restart").action {
                                restartGame()
                            }
                        }
                    }
                }
            }
            center {
                gridpane {
                    for (row in 0 until rowsNumber) {
                        row {
                            for (column in 0 until columnsNumber) {

                                val cell = Cell(column, rowsNumber - 1 - row)

                                val button = button {
                                    style {
                                        backgroundColor += Color.TRANSPARENT
                                        minWidth = gridSize
                                        minHeight = gridSize
                                    }
                                }
                                val rightCell = (row + column) % 2 == 0

                                when {

                                    rightCell && row % 2 == 0 -> {
                                        button.apply {
                                            graphic = circle(radius = 20.0) {
                                                fill = Color.BLACK
                                            }
                                            style {
                                                backgroundColor += Color.TRANSPARENT
                                            }
                                        }
                                    }

                                    !rightCell -> {
                                        button.action {
                                            if (inProcess) {
                                                listener.cellClicked(cell)
                                            }
                                        }
                                        buttons[cell] = button
                                    }

                                    rightCell -> {
                                        buttons[cell] = button
                                    }
                                }
                            }
                        }
                    }
                }
            }
            bottom {
                statusLabel = label("")
            }

        }
        updateBoard()
        updateStatus()
    }

    private fun restartGame() {
        board.clear()
        for (x in 0 until columnsNumber) {
            for (y in 0 until rowsNumber) {
                updateBoard(Cell(x, y))
                updateStatus()
            }
        }
        inProcess = true
    }

    override fun turnMade(cell: Cell, captured: Array<Cell?>?) {
        updateBoard(cell,captured)
        updateStatus()
    }

    private fun updateBoard(cell: Cell? = null, captured: Array<Cell?>? = null) {
        if (cell == null) return

        val chip = board[cell]

        buttons[cell]?.apply {
            style {
                minWidth = gridSize
                minHeight = gridSize
                backgroundInsets = multi(box(15.px,30.px))
                if (cell.x % 2 != 0 ) {
                    rotate = 90.deg
                }
                backgroundColor += when (chip) {
                    null -> Color.TRANSPARENT
                    Player.BLUE -> Color.BLUE
                    Player.RED -> Color.RED
                }
            }
        }

        captured?.forEach {
            if (it != null) {
                buttons[it]?.apply {
                    style {
                        minWidth = gridSize
                        minHeight = gridSize
                        backgroundColor += when (chip) {
                            null -> Color.TRANSPARENT
                            Player.BLUE -> Color.BLUE
                            Player.RED -> Color.RED
                        }
                    }
                }
            }
        }
    }

    private fun updateStatus() {
        val winner = board.winner()
        val blue = board.bluePoints
        val red = board.redPoints

        statusLabel.text = when {
            winner == Player.BLUE -> {
                inProcess = false
                "Blue wins! ($blue vs $red) Press 'Restart' to continue"
            }
            winner == Player.RED -> {
                inProcess = false
                "Red wins! ($red vs $blue) Press 'Restart' to continue"
            }
            board.turn == Player.BLUE ->
                "Game in process: Blues turn Points: $blue"
            else ->
                "Game in process: Reds turn Points: $red"
        }
    }

}