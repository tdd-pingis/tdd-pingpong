package pingis.entities;

public enum CodeStatus {
  DONE("Done"),
  IN_PROGRESS("In progress"),
  DROPPED("Dropped");

  private final String name;

  private CodeStatus(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
