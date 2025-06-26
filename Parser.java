import java.util.*;

public class Parser {
  private final List<Token> tokens;
  private int pos = 0;
  private final SymbolTable symbolTable = new SymbolTable();
  private final IntermediateCodeGenerator icg = new IntermediateCodeGenerator();

  public Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  public void parseProgram() {
    while (pos < tokens.size()) {
      parseStatement();
    }
    System.out.println("Parsing + Semantic Analysis successful.");
    icg.printCode();
  }

  private void parseStatement() {
    Token current = peek();

    if (current.type.equals("KEYWORD") && current.value.equals("int")) {
      advance();
      Token var = expect("IDENTIFIER", null);
      symbolTable.declare(var.value);
      expect("SYMBOL", ";");
    } else if (current.type.equals("IDENTIFIER")) {
      Token var = advance();
      if (!symbolTable.isDeclared(var.value)) {
        error("Variable '" + var.value + "' not declared");
      }
      expect("OPERATOR", "=");
      String expr = parseExpression();
      expect("SYMBOL", ";");
      icg.add(var.value + " = " + expr);
    } else if (current.type.equals("KEYWORD") && current.value.equals("print")) {
      advance();
      expect("SYMBOL", "(");
      String expr = parseExpression();
      expect("SYMBOL", ")");
      expect("SYMBOL", ";");
      icg.add("print " + expr);
    } else if (current.type.equals("KEYWORD") && current.value.equals("if")) {
      advance();
      expect("SYMBOL", "(");
      String cond = parseBooleanExpression();
      expect("SYMBOL", ")");
      String l1 = icg.newLabel();
      String l2 = icg.newLabel();
      icg.add("if " + cond + " goto " + l1);
      icg.add("goto " + l2);
      icg.add(l1 + ":");
      expect("SYMBOL", "{");
      while (!peek().value.equals("}"))
        parseStatement();
      expect("SYMBOL", "}");
      if (peek().type.equals("KEYWORD") && peek().value.equals("else")) {
        String l3 = icg.newLabel();
        icg.add("goto " + l3);
        icg.add(l2 + ":");
        advance();
        expect("SYMBOL", "{");
        while (!peek().value.equals("}"))
          parseStatement();
        expect("SYMBOL", "}");
        icg.add(l3 + ":");
      } else {
        icg.add(l2 + ":");
      }
    } else if (current.type.equals("KEYWORD") && current.value.equals("while")) {
      advance();
      String start = icg.newLabel();
      String end = icg.newLabel();
      icg.add(start + ":");
      expect("SYMBOL", "(");
      String cond = parseBooleanExpression();
      expect("SYMBOL", ")");
      icg.add("if not " + cond + " goto " + end);
      expect("SYMBOL", "{");
      while (!peek().value.equals("}"))
        parseStatement();
      expect("SYMBOL", "}");
      icg.add("goto " + start);
      icg.add(end + ":");
    } else {
      error("Unexpected token: " + current.value);
    }
  }

  private String parseBooleanExpression() {
    String left = parseExpression();
    if (peek().type.equals("OPERATOR") && Arrays.asList("==", "!=", "<", "<=", ">", ">=").contains(peek().value)) {
      String op = advance().value;
      String right = parseExpression();
      String temp = icg.newTemp();
      icg.add(temp + " = " + left + " " + op + " " + right);
      return temp;
    }
    return left;
  }

  private String parseExpression() {
    String term = parseTerm();
    while (peek().type.equals("OPERATOR") && (peek().value.equals("+") || peek().value.equals("-"))) {
      String op = advance().value;
      String next = parseTerm();
      String temp = icg.newTemp();
      icg.add(temp + " = " + term + " " + op + " " + next);
      term = temp;
    }
    return term;
  }

  private String parseTerm() {
    String factor = parseFactor();
    while (peek().type.equals("OPERATOR") && (peek().value.equals("*") || peek().value.equals("/"))) {
      String op = advance().value;
      String next = parseFactor();
      String temp = icg.newTemp();
      icg.add(temp + " = " + factor + " " + op + " " + next);
      factor = temp;
    }
    return factor;
  }

  private String parseFactor() {
    Token token = peek();
    if (token.type.equals("NUMBER") || token.type.equals("IDENTIFIER")) {
      if (token.type.equals("IDENTIFIER") && !symbolTable.isDeclared(token.value)) {
        error("Variable '" + token.value + "' not declared");
      }
      return advance().value;
    } else if (token.type.equals("SYMBOL") && token.value.equals("(")) {
      advance();
      String expr = parseExpression();
      expect("SYMBOL", ")");
      return expr;
    } else {
      error("Unexpected token in expression: " + token.value);
      return "";
    }
  }

  private Token peek() {
    return tokens.get(pos);
  }

  private Token advance() {
    return tokens.get(pos++);
  }

  private Token expect(String type, String value) {
    Token token = advance();
    if (!token.type.equals(type) || (value != null && !token.value.equals(value))) {
      error("Expected " + (value != null ? value : type) + " but found " + token.value);
    }
    return token;
  }

  private void error(String message) {
    throw new RuntimeException("PARSE ERROR: " + message);
  }
}
