package pingis.entities;

public enum Realm {
  BEGINNER("Beginner"),
  OBJECTORIENTED("Object Oriented"),
  DATASTRUCTURES("Data Structures");

  private String name;

  private Realm(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
