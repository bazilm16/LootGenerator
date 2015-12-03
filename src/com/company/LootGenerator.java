package com.company;
import java.util.*;
import java.io.*;

/**
 * Created by mupisiri on 12/1/15.
 */
public class LootGenerator {

    //leads data from a file and loads it into a list
    public List<String> loadFile(String filename) throws IOException{
        List<String> data = new LinkedList<>();
        Scanner str = new Scanner(new File(filename));
        while(str.hasNextLine()){
            data.add(str.nextLine());
        }
        str.close();
        return data;
    }//loadFile

    public void printWords(String filename) throws IOException {
        Scanner text = new Scanner(new File(filename));
        while (text.hasNext()) {
            System.out.println(text.next());
        }
    }

    public void pickMonster(){

    }

    public void fetchTreasureClass(){

    }

    public void generateBaseItem(){

    }

    public void generateBaseStats(){

    }

    public void generateAffix(){

    }

    public static void main(String[] args){
        System.out.println("bazil");
        System.out.println("bazil");
        System.out.println("bazil");
    }

}
