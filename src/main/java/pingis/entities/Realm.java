package pingis.entities;

public enum Realm {
  BEGINNER, OBJECTORIENTED, DATASTRUCTURES;

  public static Realm getRealm(String realm) {
    switch (realm) {
      case "BEGINNER":
        return Realm.BEGINNER;
      case "OBJECTORIENTED":
        return Realm.OBJECTORIENTED;
      case "DATASTRUCTURES":
        return Realm.DATASTRUCTURES;
      default:
        return null;
    }
  }
}
