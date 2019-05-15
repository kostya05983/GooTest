package org.goo.view

import javafx.scene.control.TextArea
import javafx.scene.input.KeyCombination
import org.goo.debugger.Debugger
import org.goo.interpreter.Interpreter
import org.goo.scanner.Scanner
import org.goo.semantic.SemanticAnalyzer
import org.goo.syntax.SyntaxAnalyzer
import tornadofx.FXEvent
import tornadofx.View
import tornadofx.textarea
import java.io.PipedInputStream
import java.io.PipedOutputStream

class Console : View() {

    private val editor: Editor by inject()

    override val root: TextArea = textarea {
        isEditable = false
        loadSubscriptions()
    }


    private fun loadSubscriptions() {
        subscribe<OutputEvent> {
            root.text += it.line;
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

class ClearOutputEvent() : FXEvent()