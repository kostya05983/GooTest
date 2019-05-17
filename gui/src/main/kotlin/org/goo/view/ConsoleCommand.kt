package org.goo.view

/**
 * Enum contains available commands for CLIWindow
 * which can be prompted  by user
 */
enum class ConsoleCommand(val text: String) {
    VAR("var"), TRACE("trace"),
    STEP_INTO("step"), STEP_OVER("step_over"),
    CLEAR("clear"),
    DEBUG("debug"),
    STOP("stop"),
    ADD("add"),
    REMOVE("remove"),
    OUTPUT_POINTS("points"),
    RUN("run"),
    HELP("help")
}