package pingis.services;

import java.util.Scanner;

/**
 * @author lauri
 */
public class DataloaderIo implements Io {

  private final Scanner reader;

  public DataloaderIo() {
    reader = new Scanner(
        getClass().getClassLoader().getResourceAsStream("exampledata/dummychallenges.json"));
  }

  @Override
  public boolean hasNext() {
    return reader.hasNext();
  }

  @Override
  public String nextLine() {
    return reader.nextLine();
  }

}
