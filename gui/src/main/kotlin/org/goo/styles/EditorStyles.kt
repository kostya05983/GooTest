package org.goo.styles

import org.goo.ColorProperties
import tornadofx.Dimension
import tornadofx.Stylesheet
import tornadofx.box

/**
 * Default styles for code editor
 * more about type css you can find
 * if google type css tornadofx
 * @author kostya05983
 */
class EditorStyles : Stylesheet() {

    init {
        Stylesheet.textArea {
            Stylesheet.content {
                backgroundColor += ColorProperties.primaryColor
                borderWidth += box(Dimension(0.0, Dimension.LinearUnits.px))
            }
            Stylesheet.focused {
                backgroundColor += ColorProperties.primaryColor
            }

            backgroundInsets += box(Dimension(0.0, Dimension.LinearUnits.px))
            baseColor = ColorProperties.primaryColor
            focusColor = ColorProperties.primaryColor
            textFill = ColorProperties.fontColor
        }
    }
}