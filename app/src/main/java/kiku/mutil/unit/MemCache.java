package kiku.mutil.unit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

public class MemCache extends PMap{
    //TreeMap saves <count, val>
    HashMap<String, TreeMap<Integer, Integer>> map = new HashMap<>();
    String outputFileName = "memOut.txt";
    private static Integer COUNT = 0;

    // put key value:return count
    public Integer put(String key, int value) {
        map.putIfAbsent(key, new TreeMap<Integer, Integer>());
        map.get(key).put(++COUNT, value);
        generateOutputFile(outputFileName
                , Arrays.asList(new String[]{"PUT (#" + COUNT + ") " + key + " = " + value}));
        System.out.println("PUT (#"+ COUNT+") "+key+" = "+value);
        return COUNT;
    }

    //get key: return last val map to key
    public Integer get(String key) {
        if(map.containsKey(key)){
            TreeMap valMap = map.get(key);
            Integer res = (Integer) valMap.lastEntry().getValue();
            generateOutputFile(outputFileName, Arrays.asList(new String[]{"GET "+key +"= "+ res}));
            System.out.println("GET "+key +"= "+ res);
            return res;
        }
        generateOutputFile(outputFileName, Arrays.asList(new String[]{"GET "+key+" = <NULL>"}));
        System.out.println("GET "+key+" = <NULL>");
        return null;
    }

    //get key version: return val corresponding to count and key
    //if version <= current count, return the value of the closet count when key is created
    //if version > current count ? the same as above or throw exception?
    public Integer get(String key, int count) {
        if(map.containsKey(key)){
            TreeMap valMap = map.get(key);
            if (valMap.containsKey(count)) {
                Integer res = (Integer) valMap.get(count);
                generateOutputFile(outputFileName
                        , Arrays.asList(new String[]{"GET "+key +"(#"+count+") = " + res}));
                System.out.println("GET "+key +"(#"+count+") = " + res);
                return res;
            } else {
                Integer latestCount = (Integer) valMap.floorKey(count);
                if (latestCount != null) {
                    Integer val = (Integer) valMap.get(latestCount);
                    generateOutputFile(outputFileName
                            , Arrays.asList(new String[]{"GET "+key +"(#"+count+") = " + val}));
                    System.out.println("GET "+key +"(#"+count+") = " + val);
                    return val;
                }
            }
        }
        System.out.println("GET "+key +"(#"+count+") = " + "null");
        generateOutputFile(outputFileName
                , Arrays.asList(new String[]{"GET "+key +"(#"+count+") = " + "null"}));
        return null;
    }
}
