package org.goo

import org.goo.view.MainWindow
import tornadofx.App
import tornadofx.UIComponent
import tornadofx.launch
import tornadofx.reloadStylesheetsOnFocus
import kotlin.reflect.KClass

class MyApp() : App() {
    override val primaryView: KClass<out UIComponent> = MainWindow::class

    init {
        reloadStylesheetsOnFocus()
    }
}

fun main(args: Array<String>) {
    launch<MyApp>(args)
}