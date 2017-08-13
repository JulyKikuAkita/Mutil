package kiku.mutil.unit.RE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

/*
 * A DB contains a sequence of uncommitted Transaction Blocks.
 */
public class Database {
    private LinkedList<TransactionBlock> blocks;

    // Initialize db with an initial transaction block to hold transaction history
    public Database(){
        blocks = new LinkedList<TransactionBlock>();
        blocks.add(new TransactionBlock());
    }

    // Set the variable name to the value.
    public void set(String name, Integer value){
        blocks.getLast().set(name, value);
    }

    // Get the value of the variable name, or NULL if that variable is not set.
    public Integer get(String name){
        return blocks.getLast().get(name);
    }

    // Return the number of variables that are currently set to value. If no variables equal that value, print 0.
    public Integer numEqualTo(Integer value){
        return blocks.getLast().numEqualTo(value);
    }

    // Close all open transaction blocks, permanently applying the changes made in them. Return false if no transaction is in progress.
    @SuppressWarnings("unchecked")
    public boolean commit() {
        if (blocks.size() <= 1) return false;

        HashMap<String, Integer> name_value = new HashMap<String, Integer>();
        HashMap<Integer, Integer> value_counter = new HashMap<Integer, Integer>();

        ListIterator<TransactionBlock> iterator = blocks.listIterator();
        while (iterator.hasNext()) {
            TransactionBlock block = iterator.next();
            name_value.putAll((Map<? extends String, ? extends Integer>) block.getNameValue());
        }

        for (Entry<String, Integer> entry : name_value.entrySet()) {
            Integer value = entry.getValue();
            if(value_counter.get(value) == null){
                value_counter.put(value, new Integer(1));
            }
            else{
                value_counter.put(value, new Integer(value_counter.get(value) + 1));
            }
            name_value.put(entry.getKey(),entry.getValue());
        }

        blocks = new LinkedList<TransactionBlock>();
        blocks.add(new TransactionBlock(name_value, value_counter));

        return true;
    }

    // Undo all of the commands issued in the most recent transaction block, and discard the block. Return false if no previous block to roll back to.
    public boolean rollBack(){
        if (blocks.size() <= 1) return false;
        blocks.removeLast();
        return true;
    }

    // Open a new transaction block.Transaction blocks can be nested; a BEGIN can be issued inside of an existing block.
    public void begin(){
        TransactionBlock block = new TransactionBlock();
        block.setPrev(blocks.getLast());
        blocks.add(block);
    }

    //public static void main(String[] args) throws IOException {
    public static void runDB() throws IOException {
        Database db = new Database();
	/*	Scanner scanner = new Scanner(System.in);
		scanner.useDelimiter("\\s+"); // space delimited
		String cmdLine; // cmdLine is typically 'cmd' followed by 'key' followed by 'value'
		*/
        File file = new File( "src/main/res/values/inputRedis");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String cmdLine;

        while ((cmdLine = bufferedReader.readLine()) != null) {
            //cmdLine = scanner.nextLine();
            String[] tokens = cmdLine.split("\\s+");
            String cmd = tokens[0];
            String name;
            Integer value;
            try {
                switch (cmd) {
                    case "GET":
                        name = tokens[1];
                        System.out.println(db.get(name) != null ? db.get(name):"NULL");
                        break;
                    case "SET":
                        name = tokens[1];
                        value = Integer.parseInt(tokens[2]);
                        db.set(name, value);
                        break;
                    case "UNSET":
                        name = tokens[1];
                        db.set(name, null);
                        break;
                    case "NUMEQUALTO":
                        value = Integer.parseInt(tokens[1]);
                        System.out.println(db.numEqualTo(value));
                        break;
                    case "BEGIN":
                        db.begin();
                        break;
                    case "ROLLBACK":
                        if (!db.rollBack()) System.out.println("NO TRANSACTION");
                        break;
                    case "COMMIT":
                        if (!db.commit()) System.out.println("NO TRANSACTION");
                        break;
                    case "END":
                        return;
                    case "":
                        break;
                    default:
                        System.out.println("Invalid command: " + cmd );
                }
            } catch (NumberFormatException e) {			// SET n a
                System.out.println("Invalid number format: " + cmdLine );
            } catch (ArrayIndexOutOfBoundsException e) {// GET
                System.out.println("Possibly missing operand: " + cmdLine );
            }
        }
        fileReader.close();
    }
}

/**
 * A transaction block conceptually includes all past uncommitted transactions accessible through traversing to the previous transaction block.
 */
class TransactionBlock {
    private TransactionBlock prev;	// point to the immediate past TransactionBlock

    // Delta only. Not the full transaction history.
    private HashMap<String, Integer> name_value = new HashMap<String, Integer>();
    private HashMap<Integer, Integer> value_counter = new HashMap<Integer, Integer>();

    public TransactionBlock(){}

    public void setPrev(TransactionBlock block) {
        prev = block;
    }

    public TransactionBlock(HashMap<String, Integer>nameValue, HashMap<Integer, Integer>valueCounter){
        name_value = nameValue;
        value_counter = valueCounter;
    }

    public HashMap<String, Integer> getNameValue(){
        return name_value;
    }

    // Set the variable name to the value and maintain delta counter.
    public void set(String name, Integer currentValue){

        // maintain delta value_counter, decrease counter of old 'name' value
        Integer prevValue = get(name);
        if (prevValue != null){
            Integer prevValueCounter = numEqualTo(prevValue);
            value_counter.put(prevValue, --prevValueCounter);
        }

        // maintain delta value_counter, increase counter of new 'name' value
        Integer currentValueCounter = numEqualTo(currentValue);
        if (currentValue != null) {
            if (currentValueCounter != null) {
                value_counter.put(currentValue, ++currentValueCounter);
            } else {
                value_counter.put(currentValue, new Integer(1));
            }
        }

        name_value.put(name, currentValue);
    }

    // Get the value for the given name
    public Integer get(String name) {
        TransactionBlock block = this;
        Integer value = block.name_value.get(name);
        while(!block.name_value.containsKey(name) && block.prev != null){
            block = block.prev;
            value = block.name_value.get(name);
        }
        return value;
    }

    // Print out the number of variables that are currently set to value. If no variables equal that value, print 0.
    public Integer numEqualTo(Integer value){
        if (value == null) return 0;

        TransactionBlock block = this;
        Integer counter = block.value_counter.get(value);
        while(counter == null && block.prev != null){
            block = block.prev;
            counter = block.value_counter.get(value);
        }

        if (counter == null)
            return 0;
        else{
            return counter;
        }
    }
}

