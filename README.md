### About project
This is interpreter and debugger of guu language

The project has separate modules:

* gui - view part, user interface

* core - scanner, syntaxAnalyzer, semanticAnalyzer, 
interpreter and debugger

* common - constants which is used between modules

Features:
 
* you can use step_into or step_over

* check current memory

* check current stacktrace

* add multiple lines to debug

* empty lines will be skipped

### Build project

1. You need java 8 oracle and gradle 5.4 to build this project

2. Go to root gradle directory

3. Run gradle build

4. Next run jar with oracle java8, java -jar ./gui/build/libs/gui-1.0-SNAPSHOT.jar path_to_file

### Interface

In project three fields.
First contains your code, second is used for interpret your commands
and third for output

### Commands

#### Debugger commands

* i - step_into to next debug line it's similar to step_into

* o - step_over to next line , skip debug of calls

* var - output current memory

* trace - output current stackTrace

### ShortCuts commands

* Ctrl + S save to file

* F7 - step into

* F8 - step_over

#### Console commands

* add <number> - add stop points to line

* remove <number> - delete stop point at number

* points - output current stop points

* stop - stop currentSession of debug or run

* debug - start debug code

* run - execute code

* clear - clear console output

### Instruction to add new operator

1 Add new token in /core/src/main/kotlin/org/goo/scanner/Token.kt

2 Add new token in scanner tokenDeterminer /core/src/main/kotlin/org/goo/scanner/TokenDeterminer.kt

3 Next add new token in /core/src/main/kotlin/org/goo/syntax/ContinuousStartState.kt
and in /core/src/main/kotlin/org/goo/syntax/WaitFirstOperatorState.kt

4 Realize new operator in /core/src/main/kotlin/org/goo/interpreter/operators

5 And add it to mapping in /core/src/main/kotlin/org/goo/interpreter/Interpreter.kt

### Available operators

Example 
````
sub test
    print b
sub main
    set a 5
    random b
    print a
    call test
````
* print <var> - print variable

* set <var> <numeric> - set to var numeric value

* call <var> - call sub with name var

* random <var> - set random numeric to var

### For test

For test you can run with example.txt file


### Technologies

* Kotlin [https://github.com/JetBrains/kotlin]

* Junit5 [https://github.com/junit-team/junit5]

* Tornadofx [https://github.com/edvin/tornadofx]

### Feature versions

* Change to socket connection, between debugger and gui

* add gui buttons for debug
