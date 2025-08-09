# TinyCompiler — A Mini Arithmetic Expression Compiler in Java

## Created this just for fun to know how the compiler is designed and it works

## Overview

TinyCompiler is a simple Java-based mini-compiler that takes a mathematical expression as input, compiles it into a Java class on the fly, and then executes it to produce a result.

It demonstrates the fundamental steps of compilation in a minimal and educational way:

- **Lexing** (tokenizing input string)
- **Parsing** (building an Abstract Syntax Tree - AST)
- **Code generation** (generating Java source code)
- **Runtime compilation** (compiling the generated Java code dynamically)
- **Loading and executing** the compiled class

---

## Features

- Supports basic arithmetic expressions with `+`, `-`, `*`, `/`, and parentheses
- Parses expressions with correct operator precedence
- Generates valid Java source code for the parsed expression
- Compiles Java code dynamically at runtime using Java Compiler API
- Executes the compiled code and prints the result

---

## Prerequisites

- Java Development Kit (JDK) 8 or higher  
  Make sure `javac` and `java` commands are available in your system PATH. 

---

## How to Build and Run

1. Clone or download this repository.

2. Compile the source:
    ```bash
    javac TinyCompiler.java
    ```

3. Run the program with an arithmetic expression as an argument (quote the expression):
    ```bash
    java TinyCompiler "(1+2)*3 - 4/2"
    ```

4. You should see the generated Java source printed, and the computed result:
    ```
    Generated Java source:
    public class CompiledExpr123456 implements java.util.concurrent.Callable<Double> {
      public Double call() {
        return (((1.0+2.0)*3.0)-(4.0/2.0));
      }
    }

    Result = 7.0
    ```

---

## How It Works (Technical Details)

### 1. Lexer (Tokenizer)
- Reads input string character-by-character
- Groups characters into meaningful tokens: numbers, operators, parentheses, EOF
- Ignores whitespace

### 2. Parser
- Implements a recursive descent parser based on simple arithmetic grammar
- Builds an **Abstract Syntax Tree (AST)** representing the expression hierarchy and operator precedence

### 3. AST Nodes
- `NumberExpr` represents number literals
- `BinaryExpr` represents binary operations (`+`, `-`, `*`, `/`)

### 4. Code Generation
- Converts AST back into Java source code that returns the computed value as a `Double`
- Wraps the generated expression into a Java class implementing `Callable<Double>`

### 5. Dynamic Compilation and Loading
- Uses `javax.tools.JavaCompiler` to compile generated Java source in-memory
- Saves compiled `.class` file to temporary directory
- Loads the compiled class dynamically with a `URLClassLoader`

### 6. Execution
- Creates an instance of the generated class via reflection
- Calls its `call()` method to compute and retrieve the result

---

## Concepts and Knowledge Used

- **Compiler Basics**: Lexical analysis, parsing, AST construction, code generation  
- **Recursive Descent Parsing**: Handwritten parser following operator precedence rules  
- **Java Compiler API**: Compiling Java code programmatically at runtime  
- **Reflection and Dynamic Class Loading**: Loading and instantiating classes compiled on the fly  
- **Java Generics and Type Safety**: Handling unchecked casts safely  
- **Expression Evaluation**: Converting math expressions into executable code

---

## Limitations and Future Work

- Only supports basic arithmetic with double precision numbers  
- No variables, functions, or complex language features  
- Error handling can be improved  
- Could be extended to support more operators, functions, or statements  

---

## License

MIT License — feel free to use, modify, and share!

---

## Contact

Created by [AKing-283](https://github.com/AKing-283)


