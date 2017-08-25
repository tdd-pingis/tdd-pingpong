package pingis.services;


import org.springframework.stereotype.Service;


@Service
public class LevelService {

  private final int levels = 100;
  private final int firstLevel = 1000;
  private final float base = 1.2f;

  public LevelService() {

  }

  public int getLevel(int score) {
    for (int i = 1; i <= levels; i++) {
      if (score < (int)(firstLevel * Math.pow(this.base, i - 1))) {
        return i - 1;
      }
    }
    return levels;
  }

  public Rank getRank(int level) {
    return null;
  }

}
