import java.util.*;

public class Main {
  public static void main(String[] args) {
    List<Token> tokens = MiniLangLexer.tokenize("input.minilang");

    try {
      Parser parser = new Parser(tokens);
      parser.parseProgram();

      IntermediateCodeGenerator icg = new IntermediateCodeGenerator();

      // Example output for demonstration
      String t1 = icg.newTemp();
      icg.add(t1 + " = 5 + 3");
      icg.add("a = " + t1);
      String l1 = icg.newLabel();
      String l2 = icg.newLabel();
      String l3 = icg.newLabel();
      icg.add("if a > 0 goto " + l1);
      icg.add("goto " + l2);
      icg.add(l1 + ": print a");
      icg.add("goto " + l3);
      icg.add(l2 + ": a = 0");
      icg.add(l3 + ":");

      icg.printCode();

    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }
  }
}
