package org.goo.view

enum class ConsoleCommand(val text: String) {
    VAR("var"), TRACE("trace"),
    STEP_INTO("step into"), STEP_OVER("step over"),
    CLEAR("clear"),
    START("start"),
    ADD("add"),
    REMOVE("remove"),
    OUTPUT_POINTS("OUTPUT_POINTS")
}