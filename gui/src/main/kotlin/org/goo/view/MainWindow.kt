package org.goo.view


import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.Priority
import javafx.scene.text.TextAlignment
import org.goo.ColorProperties
import tornadofx.*
import kotlin.system.exitProcess

/**
 * Main Window
 */
class MainWindow : View() {

    override val root: Parent =
            vbox {
                title = "Guu debugger"
                vgrow = Priority.ALWAYS
                style {
                    backgroundColor += ColorProperties.secondColor
                    minWidth = Dimension(600.0, Dimension.LinearUnits.px)
                    minHeight = Dimension(400.0, Dimension.LinearUnits.px)
                }

                val path: String = app.parameters.unnamed.first()
                val editor: Editor = find(mapOf(Editor::path to path))

                add(editor)
                hbox(alignment = Pos.CENTER) {
                    label {
                        text = "Input your commands below"
                        textFill = ColorProperties.fontColor
                        style {
                            minHeight = Dimension(20.0, Dimension.LinearUnits.px)
                        }
                    }
                }

                val cliWindow = find<CLIWindow>(mapOf(CLIWindow::editor to editor))
                add(cliWindow)
                hbox(alignment = Pos.CENTER) {
                    label {
                        text = "Console Output"
                        textFill = ColorProperties.fontColor
                        style {
                            minHeight = Dimension(20.0, Dimension.LinearUnits.px)
                        }
                    }
                }
                add<Console>()
            }

    /**
     * Init after currentStage initialized
     */
    init {
        currentStage?.setOnCloseRequest {
            exitProcess(0)
        }
    }
}