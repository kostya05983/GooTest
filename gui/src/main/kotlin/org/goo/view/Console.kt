package org.goo.view

import javafx.scene.control.TextArea
import tornadofx.FXEvent
import tornadofx.View
import tornadofx.textarea

/**
 * Output Window for user
 */
class Console : View() {
    init {
        loadSubscriptions()
    }

    override val root: TextArea = textarea {
        isEditable = false
    }

    /**
     * loadSubscriptions for events
     */
    private fun loadSubscriptions() {
        subscribe<OutputEvent> {
            root.text += it.line
        }

        subscribe<ClearOutputEvent> {
            root.text = ""
        }

        subscribe<OutputEventLn> {
            root.text = root.text + it.line + "\n"
        }
    }
}

class OutputEvent(val line: String) : FXEvent()

class OutputEventLn(val line: String) : FXEvent()

class ClearOutputEvent : FXEvent()