package org.goo.view

import javafx.scene.control.TextArea
import javafx.scene.input.KeyCombination
import org.goo.controllers.CLIWindowController
import tornadofx.View
import tornadofx.textarea

/**
 * Mini Window for input commands
 */
class CLIWindow : View() {
    val editor: Editor by param()
    private val controller: CLIWindowController = find(mapOf(CLIWindowController::editor to editor))

    override val root: TextArea = textarea {
        loadShortCut()
        maxHeight = 20.0
    }

    private fun loadShortCut() {
        shortcut(KeyCombination.valueOf("Ctrl+ Enter")) {
            val lastLine = root.text.split("\n").last()
            controller.cli(lastLine)
            root.text = ""
        }
    }
}