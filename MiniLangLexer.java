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
      System.out.println("❌ File not found: " + filePath);
      return tokens;
    }

    Matcher matcher = tokenPatterns.matcher(code.toString());

    while (matcher.find()) {
      if (matcher.group("KEYWORD") != null) {
        tokens.add(new Token("KEYWORD", matcher.group("KEYWORD")));
      } else if (matcher.group("IDENTIFIER") != null) {
        tokens.add(new Token("IDENTIFIER", matcher.group("IDENTIFIER")));
      } else if (matcher.group("NUMBER") != null) {
        tokens.add(new Token("NUMBER", matcher.group("NUMBER")));
      } else if (matcher.group("OPERATOR") != null) {
        tokens.add(new Token("OPERATOR", matcher.group("OPERATOR")));
      } else if (matcher.group("SYMBOL") != null) {
        tokens.add(new Token("SYMBOL", matcher.group("SYMBOL")));
      } else if (matcher.group("MISMATCH") != null) {
        System.out.println("❌ ERROR: Unrecognized token '" + matcher.group("MISMATCH") + "'");
      }
    }

    return tokens;
  }
}
