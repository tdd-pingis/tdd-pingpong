package pingis.entities;

public enum ChallengeType {
  PROJECT, MIXED, ARCADE;
  public static ChallengeType getType(String type) {
    if (type.equals("PROJECT")) {
      return ChallengeType.PROJECT;
    } else if (type.equals("MIXED")) {
      return ChallengeType.MIXED;
    } else if (type.equals("ARCADE")) {
      return ChallengeType.ARCADE;
    }
    return null;
  }
}
