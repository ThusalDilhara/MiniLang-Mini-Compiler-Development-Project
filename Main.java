import java.util.*;

public class Main {
  public static void main(String[] args) {
    List<Token> tokens = MiniLangLexer.tokenize("input.minilang");

    try {
      Parser parser = new Parser(tokens);
      parser.parseProgram(); // This alone handles everything now
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }
  }
}
