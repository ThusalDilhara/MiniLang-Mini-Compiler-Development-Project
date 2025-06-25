import java.util.List;

public class Parser {
  private final List<Token> tokens;
  private final SymbolTable symbolTable = new SymbolTable();
  private int current = 0;

  public Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  public void parseProgram() {
    while (!isAtEnd()) {
      parseStatement();
    }
    System.out.println("Parsing + Semantic Analysis successful.");
  }

  private void parseStatement() {
    if (match("KEYWORD", "int")) {
      String name = expect("IDENTIFIER").value;
      symbolTable.declare(name);
      expect("SYMBOL", ";");
    } else if (check("IDENTIFIER")) {
      String name = peek().value;
      if (!symbolTable.isDeclared(name)) {
        error("Undeclared variable: " + name);
      }
      parseAssignment();
    } else if (match("KEYWORD", "if")) {
      expect("SYMBOL", "(");
      parseCondition();
      expect("SYMBOL", ")");
      parseBlock();
      if (match("KEYWORD", "else")) {
        parseBlock();
      }
    } else if (match("KEYWORD", "while")) {
      expect("SYMBOL", "(");
      parseCondition();
      expect("SYMBOL", ")");
      parseBlock();
    } else if (match("KEYWORD", "print")) {
      expect("SYMBOL", "(");
      String name = expect("IDENTIFIER").value;
      if (!symbolTable.isDeclared(name)) {
        error("Undeclared variable in print: " + name);
      }
      expect("SYMBOL", ")");
      expect("SYMBOL", ";");
    } else if (check("SYMBOL", "{")) {
      parseBlock();
    } else {
      error("Unexpected token: " + peek().value);
    }
  }

  private void parseAssignment() {
    expect("IDENTIFIER");
    expect("OPERATOR", "=");
    parseExpression();
    expect("SYMBOL", ";");
  }

  private void parseBlock() {
    expect("SYMBOL", "{");
    while (!check("SYMBOL", "}")) {
      parseStatement();
    }
    expect("SYMBOL", "}");
  }

  private void parseExpression() {
    parseTerm();
    while (match("OPERATOR", "+") || match("OPERATOR", "-")) {
      parseTerm();
    }
  }

  private void parseTerm() {
    parseFactor();
    while (match("OPERATOR", "*") || match("OPERATOR", "/")) {
      parseFactor();
    }
  }

  private void parseFactor() {
    if (match("IDENTIFIER") || match("NUMBER")) {
      return;
    } else if (match("SYMBOL", "(")) {
      parseExpression();
      expect("SYMBOL", ")");
    } else {
      error("Expected identifier, number, or expression in parentheses");
    }
  }

  private void parseCondition() {
    parseExpression();
    if (match("OPERATOR", "==") || match("OPERATOR", "!=") || match("OPERATOR", "<")
        || match("OPERATOR", ">") || match("OPERATOR", "<=") || match("OPERATOR", ">=")) {
      parseExpression();
    } else {
      error("Expected a relational operator in condition");
    }
  }

  private boolean match(String type) {
    if (check(type)) {
      advance();
      return true;
    }
    return false;
  }

  private boolean match(String type, String value) {
    if (check(type, value)) {
      advance();
      return true;
    }
    return false;
  }

  private Token expect(String type) {
    if (!check(type)) {
      error("Expected token type: " + type);
    }
    return advance();
  }

  private Token expect(String type, String value) {
    if (!check(type, value)) {
      error("Expected token: " + value);
    }
    return advance();
  }

  private boolean check(String type) {
    if (isAtEnd())
      return false;
    return peek().type.equals(type);
  }

  private boolean check(String type, String value) {
    if (isAtEnd())
      return false;
    Token token = peek();
    return token.type.equals(type) && token.value.equals(value);
  }

  private Token peek() {
    return tokens.get(current);
  }

  private Token advance() {
    if (!isAtEnd())
      current++;
    return previous();
  }

  private Token previous() {
    return tokens.get(current - 1);
  }

  private boolean isAtEnd() {
    return current >= tokens.size();
  }

  private void error(String message) {
    throw new RuntimeException("Syntax/Semantic Error: " + message);
  }
}
