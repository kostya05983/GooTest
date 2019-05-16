package org.goo.view

enum class ConsoleCommand(val text: String) {
    VAR("var"), TRACE("trace"),
    STEP_INTO("step_into"), STEP_OVER("step_over"),
    CLEAR("clear"),
    DEBUG("debug"),
    STOP("stop"),
    ADD("add"),
    REMOVE("remove"),
    OUTPUT_POINTS("points")
}