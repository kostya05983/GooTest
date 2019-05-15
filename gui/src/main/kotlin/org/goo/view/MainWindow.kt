package org.goo.view


import javafx.scene.Parent
import javafx.scene.layout.Priority
import org.goo.ColorProperties
import tornadofx.*

class MainWindow : View() {

    override val root: Parent =
            vbox {
                vgrow = Priority.ALWAYS
                style {
                    backgroundColor += ColorProperties.secondColor
                    minWidth = Dimension(600.0, Dimension.LinearUnits.px)
                    minHeight = Dimension(400.0, Dimension.LinearUnits.px)
                }

                val path: String = app.parameters.unnamed.first()
                val editor: Editor = find(mapOf(Editor::path to path))

                add(editor)
                region {
                    style {
                        minHeight = Dimension(20.0, Dimension.LinearUnits.px)
                    }
                }
                add<CLIWindow>()
                region {
                    style {
                        minHeight = Dimension(20.0, Dimension.LinearUnits.px)
                    }
                }
                add<Console>()
            }
}