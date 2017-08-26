package pingis.utils;

public class SyntaxError {
  public final int row;
  public final int column;
  public final String text;
  public final String type;

  public SyntaxError(int row, int column, String text) {
    this.row = row;
    this.column = column;
    this.text = text;
    this.type = "error";
  }
}
