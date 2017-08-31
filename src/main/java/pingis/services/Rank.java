package pingis.services;

public enum Rank {
  // TODO: Let's think these over. More ranks = more fun for the player.
  TOMATO(0),
  NOOB(1),
  WANNABE(10),
  NOVICE(20),
  GEEK(30),
  APPPRENTICE(40),
  PROGRAMMER(50),
  HACKER(60),
  NINJA(70),
  WIZARD(80),
  SUPERGURU(90),
  OUTOFBOUNDSEXCEPTION(100);
  private int limit;

  private Rank(int limit) {
    this.limit = limit;
  }

  public int getLimit(Rank rank) {
    return rank.limit;
  }
}
