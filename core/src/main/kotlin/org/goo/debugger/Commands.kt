package org.goo.debugger

enum class Commands(val text: String) {
    STEP_INTO("step"),
    STEP_OVER("step_over"),
    TRACE("trace"),
    VAR("var")
}