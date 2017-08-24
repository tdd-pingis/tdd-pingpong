package pingis.services;


import org.springframework.stereotype.Service;


@Service
public class LevelService {

  private int[] scoreLimits;
  private final int levels = 100;
  private final int firstLevel = 1000;
  private final float coefficient = 1.2f;

  public LevelService() {
    scoreLimits = new int[levels];
    generateLevels();
  }

  private void generateLevels() {
    scoreLimits[0] = 0;
    for (int i = 1; i < levels; i++) {
      int nextLevel = (int)(firstLevel * Math.pow(this.coefficient, i-1));
      this.scoreLimits[i] = nextLevel;
    }
  }

  public int getLevel(int score) {
    for (int i = 0; i < levels+1; i++) {
      if (score < scoreLimits[i]) {
        return scoreLimits[i - 1];
      }
    }
    return levels;
  }

  public Rank getRank(int level) {
    return null;
  }
  public int getPoints(int difficulty) {
    // TODO: figure this out
    return 0;
  }
}
