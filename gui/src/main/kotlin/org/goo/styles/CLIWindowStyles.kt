package org.goo.styles

import org.goo.ColorProperties
import tornadofx.Stylesheet

class CLIWindowStyles : Stylesheet() {
    init {
        Stylesheet.textField {
            backgroundColor += ColorProperties.primaryColor
            Stylesheet.focused {
                backgroundColor += ColorProperties.primaryColor
            }
            baseColor = ColorProperties.primaryColor
            focusColor = ColorProperties.primaryColor
            textFill = ColorProperties.fontColor
        }
    }
}