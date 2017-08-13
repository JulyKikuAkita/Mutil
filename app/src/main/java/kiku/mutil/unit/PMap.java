package kiku.mutil.unit;

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

public abstract class PMap {
    // put key value:return count
    public abstract Integer put(String key, int value);

    //get key: return last val map to key
    public abstract Integer get(String key);

    //get key version: return val corresponding to count and key
    public abstract Integer get(String key, int count);

    public static void readFileByLineToMap(PMap map, String path) {
        String line = null;
        try {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                line.trim();
                processLine(line, map);
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


    public static void processLine(String line, PMap map) throws IOException {
        String command;

        // for demonstration purposes only
        String[] commands = line.split("\\s+");
        if (commands.length <= 0) {
            throw new IOException("Error: cannot have empty input");
        }
        try {
            switch (commands[0]) {
                case "GET":
                    if(commands.length==2)
                    {
                        map.get(commands[1]);
                    }else if(commands.length==3){
                        map.get(commands[1], Integer.parseInt(commands[2]));
                    }

                    break;
                case "PUT":
                    if (commands.length != 3) {
                        throw new IOException("SET command is invalid. Please provide argument for set");
                    }
                    map.put(commands[1], new Integer(commands[2]));
                    break;
                default:
                    System.out.println("Command cannot be recognized");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: ");
            Arrays.stream(commands).forEach(cmd -> System.out.print(cmd + " "));
        }
    }

    //##############################################################################################
    /**
     * Just a helper to format string to be ready to insert to Trie Node
     * Here limit to ASCII encoding, not considering UNICODE
     * https://stackoverflow.com/questions/18304804/what-is-the-difference-between-character-isalphabetic-and-character-isletter-in
     */
    public String alterToLettersOnly(String testName) {
        //use regex
        String result = testName.trim().replaceAll("[^a-zA-Z0-9\\s]", "");
        /*
        StringBuilder sb = new StringBuilder();
        for (char c : testName.toCharArray()) {
            if (Character.isLetter(c)) {
                sb.append(c);
            }
        }
        return sb.toString().toLowerCase();
        */
        return result.toLowerCase();
    }
    //##############################################################################################
}
