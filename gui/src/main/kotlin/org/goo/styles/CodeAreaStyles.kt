package org.goo.styles

import org.goo.ColorProperties
import tornadofx.Dimension
import tornadofx.Stylesheet
import tornadofx.box

class CodeAreaStyles : Stylesheet() {

    init {
        Stylesheet.content {
            backgroundColor += ColorProperties.primaryColor
            borderWidth += box(Dimension(0.0, Dimension.LinearUnits.px))
            baseColor = ColorProperties.primaryColor
        }
        Stylesheet.text {
            fill = ColorProperties.fontColor
        }
    }
}