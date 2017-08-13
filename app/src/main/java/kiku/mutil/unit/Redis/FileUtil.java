package kiku.mutil.unit.Redis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
    static String outputFile = "outputRedis";

    static Transactions mTransactions = new Transactions();
    static Operation mOperations = new Operation();

    private final static String BEGIN = "BEGIN";
    private final static String SET = "SET";
    private final static String GET = "GET";
    private final static String UNSET = "UNSET";
    private final static String NUMEQUALTO = "NUMEQUALTO";
    private final static String ROLLBACK = "ROLLBACK";
    private final static String COMMIT = "COMMIT";
    private final static String END = "END";

    public static void readFileByLineToRedis(String path) {
        String line = null;
        try {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals(END)) break;
                processLine(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateOutputFile(String filename, List<String> lines) {
        Path parent = Paths.get("src/main/res/input").getParent();
        String absoPath = parent.toString()+ "/" + filename;
        Charset utf8 = StandardCharsets.UTF_8;
        try {
            Files.write(Paths.get(absoPath)
                    , lines
                    , utf8
                    , StandardOpenOption.CREATE
                    , StandardOpenOption.APPEND); //overwrite existing content -TRUNCATE_EXISTING
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void processLine(String line) throws IOException {
        int value;
        boolean isProblem = false;
        String[] commands = line.split("\\s+");
        if (commands.length <= 0) {
            throw new IOException("Error: cannot have empty input");
        }
        try {
            switch (commands[0].toUpperCase()) {
                case GET:
                    if (commands.length == 2) {
                        mOperations.GET(commands[1]);
                    } else {
                        isProblem = true;
                    }
                    break;
                case UNSET:
                    if (commands.length == 2) {
                        mOperations.UNSET(commands[1]);
                    } else {
                        isProblem = true;
                    }
                    break;
                case SET:
                    if (commands.length == 3) {
                        Integer val = Integer.parseInt(commands[2]);
                        mOperations.SET(commands[1], val);
                    } else {
                        isProblem = true;
                    }
                    break;
                case NUMEQUALTO:
                    if (commands.length == 2) {
                        Integer val = Integer.parseInt(commands[1]);
                        mOperations.NUMEQUALTO(val);
                    } else {
                        isProblem = true;
                    }
                    break;
                case ROLLBACK:
                    if (commands.length == 1) {
                        mOperations = mTransactions.ROLLBACK(mOperations);
                    } else {
                        isProblem = true;
                    }
                    break;
                case COMMIT:
                    if (commands.length == 1) {
                        mOperations = mTransactions.COMMIT(mOperations);
                    } else {
                        isProblem = true;
                    }
                    break;
                case BEGIN:
                    if (commands.length == 1) {
                        mOperations = mTransactions.BEGIN(mOperations);
                    } else {
                        isProblem = true;
                    }
                    break;
                default:
                    System.out.println("Command cannot be recognized " + commands[0]);
                    break;
            }
            if (isProblem) {
                System.out.println("Invalid parameters for " + mOperations );
                throw new IOException("Command is invalid. Exiting");
            }
        } catch(NumberFormatException e){
                System.out.println("Invalid number format: ");
                Arrays.stream(commands).forEach(cmd -> System.out.print(cmd + " "));
        }
    }
}
