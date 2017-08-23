package pingis.services;


import java.util.Map;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;


@Service
public class LevelService {

  private Map<Integer, Integer> levels;
  private final int baseMinimum = 1000;
  private final float coefficient = 1.2f;

  public LevelService() {
    levels = new LinkedHashMap();
    generateLevels();
  }

  public void generateLevels() {
    this.levels.put(0,0);
    for (int i = 1; i < 100; i++) {
      int nextLevel = (int)(baseMinimum * Math.pow(this.coefficient, i-1));
      this.levels.put(nextLevel, i);
    }
  }

  public int getLevel(int score) {
    int previous = 0;
    for (Integer limit : this.levels.keySet()) {
      if (score < limit) {
        return levels.get(previous);
      }
      else previous = limit;
    }
    return 100;
  }

  public int getPoints(int difficulty) {
    // TODO: figure this out
  }
}
