import java.util.*;

public class SymbolTable {
  private final Set<String> declaredVariables = new HashSet<>();

  public void declare(String name) {
    declaredVariables.add(name);
  }

  public boolean isDeclared(String name) {
    return declaredVariables.contains(name);
  }
}