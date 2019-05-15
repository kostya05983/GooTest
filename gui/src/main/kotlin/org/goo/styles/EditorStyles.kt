package org.goo.styles

import org.goo.ColorProperties
import tornadofx.Dimension
import tornadofx.Stylesheet
import tornadofx.box

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