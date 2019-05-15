package org.goo.view

//import controllers.SyntaxAnalyzerImpl
import javafx.scene.control.TextArea
import javafx.scene.input.KeyCombination
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.fxmisc.richtext.CodeArea
import org.goo.ColorProperties
import org.goo.styles.EditorStyles
import tornadofx.*
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import java.util.stream.Collectors

class Editor : View() {
    val path: String by param()

    val codeArea = CodeArea()

    init {
        importStylesheet(EditorStyles::class)
    }


    override val root: VBox = vbox {
        hgrow = Priority.ALWAYS
        vgrow = Priority.ALWAYS
        codeArea.replaceText(0, 0, loadText())

        loadSubscriptions()
        loadShortCut()

        //TODO styles in other place
        codeArea.hgrow = Priority.ALWAYS
        codeArea.vgrow = Priority.ALWAYS

        codeArea.style {
            backgroundColor += ColorProperties.primaryColor
        }

        codeArea.stylesheet {
            Stylesheet.content {
                backgroundColor += ColorProperties.primaryColor
                borderWidth += box(Dimension(0.0, Dimension.LinearUnits.px))
                baseColor = ColorProperties.primaryColor
            }

            Stylesheet.text {
                fill = ColorProperties.fontColor
            }
            this.addSelection(CssSelection(CssSelector(CssRuleSet(CssRule.c("debug")))) {
                fill = ColorProperties.selectionColor
            })
        }
        add(codeArea) //TODO this wrong example of adding
    }

    private fun markDebugLine(line: Int) {
        codeArea.setStyle(line, Arrays.asList("debug"))
    }

    /**
     * Reset all colors to default
     */
    private fun toDefaultColor() {
        for (i in 0 until codeArea.paragraphs.size) {
            codeArea.setStyle(i, 0, codeArea.paragraphs[i].length(), Arrays.asList("default"))
        }
    }

    private fun loadSubscriptions() {
        subscribe<WriteEvent> {
            writeTextToFile(codeArea.text)
        }
        subscribe<DebugLineEvent> {
            markDebugLine(it.line)
        }
        subscribe<RestoreColor> {
            toDefaultColor()
        }
    }

    private fun loadShortCut() {
        shortcut(KeyCombination.valueOf("Ctrl+S")) {
            writeTextToFile(codeArea.text)
        }
    }

    //TODO interface for loading and and writing in seperate class
    //TODO when need to make higlighting process stream for highlight
    private fun loadText(): String {
        val result: String
        val br = BufferedReader(FileReader(path))
        result = br.lines().collect(Collectors.toList()).joinToString("\n")
        br.close()
        return result
    }

    private fun writeTextToFile(text: String) {
        val br = BufferedWriter(FileWriter(path, false))
        br.write(text)
        br.flush()
        br.close()
    }
}

class WriteEvent : FXEvent()

class DebugLineEvent(val line: Int) : FXEvent()
class RestoreColor() : FXEvent()

fun TextArea.deleteSelectedText() {
    text.replace(selectedText, "")
}