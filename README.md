### Build project

1. You need java 8 and gradle 4.4.1 to build this project

2. Go to ./gui module

3. Run gradle build

4. Next run jar, java -jar ./build/libs/gui-1.0-SNAPSHOT-all.jar path_to_file

### Interface

In project three fields.
First contains your code, second is used for interpret your commands
and third for output

### Commands

#### Debugger commands

step - step to next debug line it's similar to step_into

step_over - step to next line , skip debug of calls

var - output current memory

trace - output current stackTrace

#### Console commands

* add <number> - add stop points to line

* remove <number> - delete stop point at number

* points - output current stop points

* stop - set to debug and run session isRunning false

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
### Technologies

* Kotlin
* Junit5
* Tornadofx
