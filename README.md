
# 🎯 MiniLang Compiler – Java Project

A simple compiler for the **MiniLang** language built in Java, supporting:

✅ Variable declarations
✅ Assignments
✅ Arithmetic expressions
✅ Conditional statements (`if`, `else`)
✅ Loops (`while`)
✅ Print statements

---

## 📁 Folder Structure

```
MiniLang/
│
├── Main.java
├── Token.java
├── MiniLangLexer.java
├── Parser.java
├── IntermediateCodeGenerator.java
├── SymbolTable.java
├── input.minilang
└── README.md
```

---

## 🛠️ How to Compile and Run

### ✅ Prerequisites

* Java JDK installed
* All `.java` files and `input.minilang` in the same folder

### ⚙️ Compilation

```bash
javac *.java
```

### 🚀 Run the Compiler

```bash
java Main
```

---

## ✏️ How to Change the Input

1. Open the `input.minilang` file.
2. Write your MiniLang code. Example:

```c
int x;
x = 10;
print(x);
```

3. Save and rerun:

```bash
java Main
```

---

## 🧾 Output

When you run the compiler, you will see:

* ✅ A confirmation message: `Parsing + Semantic Analysis successful.`
* 📜 The generated **Intermediate 3-address Code** printed to the terminal

---

## 📚 Example Output

```text
t0 = 10
x = t0
print x
```

---

## 📌 License

This project is for educational use only.

