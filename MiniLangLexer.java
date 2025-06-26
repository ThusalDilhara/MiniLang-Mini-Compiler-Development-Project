import java.io.*;
import java.util.*;
import java.util.regex.*;

public class MiniLangLexer {
  static String[] keywords = { "int", "if", "else", "while", "print" };
  static String[] operators = { "==", "!=", "<=", ">=", "=", "\\+", "-", "\\*", "/", "<", ">" };
  static String[] symbols = { "\\(", "\\)", ";", "\\{", "\\}" };

  static Pattern tokenPatterns;

  static {
    StringBuilder tokenPatternBuilder = new StringBuilder();

    tokenPatternBuilder.append("(?<KEYWORD>\\b(" + String.join("|", keywords) + ")\\b)");
    tokenPatternBuilder.append("|(?<IDENTIFIER>[a-zA-Z_]\\w*)");
    tokenPatternBuilder.append("|(?<NUMBER>\\b\\d+\\b)");
    tokenPatternBuilder.append("|(?<OPERATOR>" + String.join("|", operators) + ")");
    tokenPatternBuilder.append("|(?<SYMBOL>" + String.join("|", symbols) + ")");
    tokenPatternBuilder.append("|(?<WHITESPACE>[ \\t\\r\\n]+)");
    tokenPatternBuilder.append("|(?<MISMATCH>.)");

    tokenPatterns = Pattern.compile(tokenPatternBuilder.toString());
  }

  public static List<Token> tokenize(String filePath) {
    StringBuilder code = new StringBuilder();
    List<Token> tokens = new ArrayList<>();

    try (Scanner scanner = new Scanner(new File(filePath))) {
      while (scanner.hasNextLine()) {
        code.append(scanner.nextLine()).append("\n");
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found: " + filePath);
      return tokens;
    }

    Matcher matcher = tokenPatterns.matcher(code.toString());

    while (matcher.find()) {
      if (matcher.group("KEYWORD") != null) {
        String token = matcher.group("KEYWORD");
        tokens.add(new Token("KEYWORD", token));
        System.out.println("KEYWORD: " + token);
      } else if (matcher.group("IDENTIFIER") != null) {
        String token = matcher.group("IDENTIFIER");
        tokens.add(new Token("IDENTIFIER", token));
        System.out.println("IDENTIFIER: " + token);
      } else if (matcher.group("NUMBER") != null) {
        String token = matcher.group("NUMBER");
        tokens.add(new Token("NUMBER", token));
        System.out.println("NUMBER: " + token);
      } else if (matcher.group("OPERATOR") != null) {
        String token = matcher.group("OPERATOR");
        tokens.add(new Token("OPERATOR", token));
        System.out.println("OPERATOR: " + token);
      } else if (matcher.group("SYMBOL") != null) {
        String token = matcher.group("SYMBOL");
        tokens.add(new Token("SYMBOL", token));
        System.out.println("SYMBOL: " + token);
      } else if (matcher.group("MISMATCH") != null) {
        System.out.println("ERROR: Unrecognized token '" + matcher.group("MISMATCH") + "'");
      }
    }

    return tokens;
  }

}
