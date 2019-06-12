package javafx

import javafx.application.Application
import javafx.stage.Stage
import tornadofx.App

class DotsAndBoxesApp : App(DotsAndBoxesView::class) {

    override fun start(stage: Stage) {
        super.start(stage)
    }
}

fun main(args: Array<String>) {
    Application.launch(DotsAndBoxesApp::class.java, *args)
}