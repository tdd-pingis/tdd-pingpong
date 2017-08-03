package pingis.services;

import java.util.Scanner;

public class DataImporterIO implements IO {

    private Scanner s;
    public DataImporterIO() {
        s = new Scanner(getClass().getClassLoader().getResourceAsStream("exampledata/dummychallenges.json"));
    }
    @Override
    public boolean hasNext() {
        return s.hasNext();
    }

    @Override
    public String nextLine() {
        return s.nextLine();
    }
    
}
