package org.goo.view

import javafx.beans.binding.Bindings
import javafx.geometry.Insets
import javafx.scene.input.KeyCombination
import org.fxmisc.richtext.CodeArea
import org.goo.ColorProperties
import org.goo.controllers.CommandEvent
import org.goo.controllers.EditorController
import org.goo.styles.CodeAreaStyles
import org.goo.styles.EditorStyles
import org.goo.ConsoleCommand
import tornadofx.*
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.layout.*
import javafx.scene.paint.Color
import java.util.function.IntFunction
import org.fxmisc.richtext.LineNumberFactory


/**
 * View contains our editor with code
 * @author kostya05983
 */
class Editor : View() {
    val path: String by param()
    val codeArea = CodeArea()
    private val controller: EditorController = find(mapOf(EditorController::codeArea to codeArea))

    init {
        importStylesheet(EditorStyles::class)
        loadSubscriptions()
        loadShortCut()
        codeArea.replaceText(0, 0, controller.loadText(path))
    }

    override val root: VBox = vbox {
        hgrow = Priority.ALWAYS
        vgrow = Priority.ALWAYS

        loadCodeAreaStyles()
        initGraphicFactory()
        add(codeArea)
    }

    private fun initGraphicFactory() {
        val numberFactory = LineNumberFactory.get(codeArea)

        codeArea.setParagraphGraphicFactory {
            val node = numberFactory.apply(it - 1)
            node.style {
                backgroundColor += ColorProperties.primaryColor
            }
            val hbox = HBox(node)
            hbox
        }
    }

    private fun loadCodeAreaStyles() {
        codeArea.hgrow = Priority.ALWAYS
        codeArea.vgrow = Priority.ALWAYS

        codeArea.stylesheets.add(CodeAreaStyles().base64URL.toExternalForm())
        codeArea.style {
            backgroundColor += ColorProperties.primaryColor
        }
        val css = this@Editor.javaClass.getResource("/test.css").toExternalForm()
        codeArea.stylesheets.add(css)
    }

    private fun loadSubscriptions() {
        subscribe<DebugLineEvent> {
            controller.markDebugLine(it.line)
        }
        subscribe<RestoreColor> {
            controller.toDefaultColor()
        }
    }

    private fun loadShortCut() {
        shortcut(KeyCombination.valueOf("Ctrl+S")) {
            controller.writeTextToFile(codeArea.text, path)
        }
        shortcut(KeyCombination.valueOf("F7")) {
            fire(CommandEvent(ConsoleCommand.STEP_INTO.text))
        }
        shortcut(KeyCombination.valueOf("F8")) {
            fire(CommandEvent(ConsoleCommand.STEP_OVER.text))
        }
    }
}

class DebugLineEvent(val line: Int) : FXEvent()
class RestoreColor : FXEvent()