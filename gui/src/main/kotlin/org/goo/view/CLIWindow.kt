package org.goo.view

import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import org.goo.controllers.CLIWindowController
import org.goo.styles.CLIWindowStyles
import tornadofx.*

/**
 * Mini Window for input commands
 */
class CLIWindow : View() {
    val editor: Editor by param()
    private val controller: CLIWindowController = find(mapOf(CLIWindowController::editor to editor))


    override val root: TextField = textfield {
        maxHeight = 20.0

    }

    init {
        importStylesheet(CLIWindowStyles::class)
        loadShortCut()
    }

    private fun loadShortCut() {
        root.setOnKeyPressed {
            if (it.code == KeyCode.ENTER) {
                val lastLine = root.text.split("\n").last()
                controller.cli(lastLine)
                root.text = ""
                root.positionCaret(0)
            }
        }
    }
}