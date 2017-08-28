package pingis.utils;

public class EditorTabData {

  public final String title;
  public final String code;

  public EditorTabData(String title, String code) {
    this.title = title;
    this.code = code;
  }

  public String getTitle() {
    return title;
  }

  public String getCode() {
    return code;
  }
}
