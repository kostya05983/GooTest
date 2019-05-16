package org.goo.controllers

import org.fxmisc.richtext.CodeArea
import tornadofx.Controller
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import java.util.stream.Collectors

/**
 * EditorController - modelView
 * data of codeArea in this class is model
 * @author kostya05983
 */
class EditorController : Controller() {
    val codeArea: CodeArea by param()

    /**
     * Mark debug line with blue colored highlight, just for beauty
     * and check for bounds for prevent exceptions
     * @param line - paragraph to color
     */
    fun markDebugLine(line: Int) {
        if (line < codeArea.paragraphs.size)
            codeArea.setStyle(line, Collections.singleton("parameter"))
    }

    /**
     * Reset all colors to default
     * color all codeArea to default color
     */
    fun toDefaultColor() {
        for (i in 0 until codeArea.paragraphs.size) {
            codeArea.setStyle(i, 0, codeArea.paragraphs[i].length(), Arrays.asList("default"))
        }
    }

    /**
     * Load text from transfer file
     * @param path - where file exists
     */
    fun loadText(path: String): String {
        val result: String
        val br = BufferedReader(FileReader(path))
        result = br.lines().collect(Collectors.toList()).joinToString("\n")
        br.close()
        return result
    }

    /**
     * Write text to file
     * @param text - text to write
     * @param path - where to write
     */
    fun writeTextToFile(text: String, path: String) {
        val br = BufferedWriter(FileWriter(path, false))
        br.write(text)
        br.flush()
        br.close()
    }
}