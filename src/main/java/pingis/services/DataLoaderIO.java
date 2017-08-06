/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.services;

import java.util.Scanner;

/**
 *
 * @author lauri
 */
public class DataLoaderIO implements IO {

    private Scanner s;
    public DataLoaderIO() {
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
