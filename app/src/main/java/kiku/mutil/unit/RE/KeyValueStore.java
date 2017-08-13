package kiku.mutil.unit.RE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class KeyValueStore {

    // https://www.thumbtack.com/challenges/simple-database
    // http://www.hsufengko.com/home/thumbtacks-simple-database-challenge
    //https://github.com/dianapojar/simpledatabase/blob/master/src/db/data/TransactionManager.java

    LinkedList<Block> blocks;
    public KeyValueStore(){
        blocks=new LinkedList<Block>();
        blocks.add(new Block());
    }

    public void begin(){
        Block block=new Block();
        blocks.add(block);
    }

    public void rollback(){
    }
    public void commit(){

    }
    public Integer get(String name){
        return blocks.getLast().get(name);
    }
    public void set(String name, Integer value){
        blocks.getLast().set(name, value);
    }

    public void unset(String name){
        this.set(name, null);
    }

    public Integer numwithValue(String name){
        return blocks.getLast().numwithValue(name);
    }


    public static void main(String[] args) {
        KeyValueStore store = new KeyValueStore();
        String input = "src/Keyvaluedb/kvdb-test-input.txt";

        try {
            File file = new File(input);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line.trim();
                processLine(line, store);
            }
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    StringBuilder sb = new StringBuilder();

    public static void processLine(String line, KeyValueStore store) throws IOException {
        String command;

        // for demonstration purposes only
        String[] commands = line.split("\\s+");
        if (commands.length <= 0) {
            throw new IOException("Error: cannot have empty input");
        }
        command = commands[0];

        try {
            switch (command) {
                case "GET":
                    if (commands.length != 2) {
                        throw new IOException("GET command is invalid. Please provide argument for get");
                    }

                    System.out.println(store.get(commands[1]));
                    break;
                case "SET":
                    if (commands.length != 3) {
                        throw new IOException("SET command is invalid. Please provide argument for set");
                    }
                    store.set(commands[1], new Integer(commands[2]));
                    break;
                case "UNSET":
                    if (commands.length != 2) {
                        throw new IOException("UNSET command is invalid. Please provide argument for unset");
                    }
                    store.unset(commands[1]);
                    break;
                case "NUMWITHVALUE":
                    if (commands.length != 2) {
                        throw new IOException("NUMWITHVALUE command is invalid. Please provide argument for NUMWITHVALUE");
                    }
                    System.out.println(store.numwithValue(commands[1]));
                    break;
                case "BEGIN":
                    store.begin();
                    break;
                case "ROLLBACK":
                    store.rollback();
                    break;

                case "commit":
                    store.commit();
                    System.out.println(store.numwithValue(commands[1]));
                case "END":
                    System.exit(0);
                default:
                    System.out.println("Command cannot be recognized");
            }
        } catch (NumberFormatException e) { // SET n a
            System.out.println("Invalid number format: " + command);
        }
    }

}
