package kiku.mutil;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import kiku.mutil.unit.HerMap;
import kiku.mutil.unit.MemCache;
import kiku.mutil.unit.PMap;
import kiku.mutil.unit.RE.Database;
import kiku.mutil.unit.RE.Trie9;
import kiku.mutil.unit.Redis.FileUtil;
import kiku.mutil.unit.Redis.Operation;
import kiku.mutil.unit.Redis.Transactions;
import kiku.mutil.unit.Trie;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 *
 */
public class TestUtil {
    /**
     * https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it-in-java
     * http://winterbe.com/posts/2015/03/25/java8-examples-string-number-math-files/
     */
    public static void generateNewOutputFile(String filename, List<String> res) {
        Path parent = Paths.get("src/main/res/input").getParent();
        String absoPath = parent.toString()+ "/" + filename;
        Charset utf8 = StandardCharsets.UTF_8;
        try {
            Files.write(Paths.get(absoPath)
                    , res
                    , utf8
                    , StandardOpenOption.CREATE
                    , StandardOpenOption.APPEND); //overwrite existing content -TRUNCATE_EXISTING
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTrieT9() {
        String dictionary = "src/main/res/t9Words.txt";
        String findMatchInput = "src/main/res/input";
        Trie trie = new Trie();
        Util.readFileByLineToTrie(trie, dictionary);
        String[] t9_4355 = new String[]{"gell", "hell"};
        String[] t9_4332 = new String[]{"idea"};
        String[] t9_43556 = new String[]{"gekko", "hello"};

        String[] t9_968 = new String[]{"wot", "you"};
        String[] t9_63 = new String[]{"me","ne","od","oe","of"};
        String[] t9_627427482 = new String[]{"margarita"};

        System.out.println(trie.getPossibleWordWithNumberMapping("968"));
        System.out.println(trie.getPossibleWordWithNumberMapping("63").toString());
        System.out.println(trie.getPossibleWordWithNumberMapping("627427482").toString());
        System.out.println(trie.getPossibleWordWithNumberMapping("727456").toString()); //<No Result>
        Util.generateTrieSearchResultToFile(trie, findMatchInput, "t9out.txt");
        //assertEquals(Arrays.asList(t9_4355).toString(), trie.getPossibleWordWithNumberMapping("4355").toString());
        //assertEquals(Arrays.asList(t9_4332).toString(), trie.getPossibleWordWithNumberMapping("4332").toString());
        //assertEquals(Arrays.asList(t9_43556).toString(), trie.getPossibleWordWithNumberMapping("43556").toString());
        Log.printHorizontalLine();

        Trie9 test = new Trie9(dictionary, findMatchInput);
        test.loadDictionary();
        test.getOutput();
    }

    @Test
    // https://github.com/shirleyyoung0812/Google-interview-questions/blob/master/src/Trie/AutoCompleteTrie.java
    public void testTrieMatchPrefix() {
        String path = "src/main/res/text.txt";
        Trie trie = new Trie();
        Util.readFileByLineToTrie(trie, path);

        List<String> res = trie.getAllMatchFreqWithPrefix("", 5);
        //generateNewOutputFile("sampleOut.txt" , res);
        System.out.println(res.toString());

        //TrieHer trieHer = new TrieHer(path);
        //System.out.println(trieHer.getAutoComplete("r"));
    }

    @Test
    public void testMemCachePut() {
        String path = "src/main/res/values/inputKV";
        PMap map = new MemCache();
        PMap hemap = new HerMap();
        MemCache.readFileByLineToMap(map, path);
        Log.printHorizontalLine();
        hemap.readFileByLineToMap(hemap, path);
    }

    @Test
    public void testRedis(){
        Operation opt = new Operation();
        opt.SET("a", 10);
        opt.SET("b", 10);
        assertTrue(opt.NUMEQUALTO(10) == 2);
        assertTrue(opt.NUMEQUALTO(20) == 0);
        opt.SET("b", 30);
        assertTrue(opt.NUMEQUALTO(10) == 1);
        opt.UNSET("b");
        assertNull(opt.GET("B"));
    }

    @Test
    public void testRedisTransaction1(){
        Transactions transactions = new Transactions();
        Operation ops = new Operation();
        ops = transactions.BEGIN(ops);
        ops.SET("a", 10);
        assertTrue(ops.GET("a") == 10);
        ops = transactions.BEGIN(ops);
        ops.SET("a", 20);
        assertTrue(ops.GET("a") == 20);
        ops = transactions.ROLLBACK(ops);
        assertTrue(ops.GET("a") == 10);
        ops = transactions.ROLLBACK(ops);
        assertNull(ops.GET("a"));
    }

    @Test
    public void testRedisTransaction2(){
        Transactions transactions = new Transactions();
        Operation ops = new Operation();
        ops.SET("a", 50);
        ops = transactions.BEGIN(ops);
        assertTrue(ops.GET("a") == 50);
        ops.SET("a", 60);
        ops = transactions.BEGIN(ops);
        ops.UNSET("a");
        assertNull(ops.GET("a"));
        ops = transactions.ROLLBACK(ops);
        assertTrue(ops.GET("a") == 60);
        ops = transactions.COMMIT(ops);
        assertTrue(ops.GET("a") == 60);
    }

    @Test
    public void testRedisTransaction3(){
        Transactions transactions = new Transactions();
        Operation ops = new Operation();
        ops.SET("a", 10);
        ops = transactions.BEGIN(ops);
        assertTrue(ops.NUMEQUALTO(10) == 1);
        ops = transactions.BEGIN(ops);
        ops.UNSET("a");
        assertTrue(ops.NUMEQUALTO(10) == 0);
        ops = transactions.ROLLBACK(ops);
        assertTrue(ops.NUMEQUALTO(10) == 1);
        ops = transactions.ROLLBACK(ops);
        ops = transactions.ROLLBACK(ops);
        ops = transactions.COMMIT(ops);
        ops = transactions.BEGIN(ops);
        assertTrue(ops.NUMEQUALTO(10) == 1);
        ops = transactions.COMMIT(ops);
        ops = transactions.BEGIN(ops);
        ops.UNSET("a");
        assertNull(ops.GET("a"));
        ops = transactions.BEGIN(ops);
    }

    @Test
    public void testRedisReadFile() {
        String path = "src/main/res/values/inputRedis";
        FileUtil db = new FileUtil();
        db.readFileByLineToRedis(path);
    }

    @Test
    public void testReDB() {
        Database db = new Database();
        try {
            db.runDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}