import java.util.*;

public class IntermediateCodeGenerator {
  private final List<String> instructions = new ArrayList<>();
  private int tempVarCount = 0;
  private int labelCount = 0;

  public String newTemp() {
    return "t" + (tempVarCount++);
  }

  public String newLabel() {
    return "L" + (labelCount++);
  }

  public void add(String instruction) {
    instructions.add(instruction);
  }

  public void printCode() {
    System.out.println("\nIntermediate 3-address code:");
    for (String instr : instructions) {
      System.out.println(instr);
    }
  }
}