package kiku.mutil.unit.Redis;

import java.util.Arrays;
import java.util.HashMap;

public class Operation {
    HashMap<String, Integer> mKeyValMap;
    HashMap<Integer, Integer> mValCountMap;

    public Operation() {
        mKeyValMap = new HashMap();
        mValCountMap = new HashMap<>();
    }

    //create new operation with updated map val
    public Operation(Operation operation) {
        mKeyValMap = new HashMap(operation.getKeyValMap());
        mValCountMap = new HashMap<>(operation.getValCountMap());
    }

    //key not exist -add k-v pair, valCount++
    //key exist - overwrite, original count--, new count ++
    public void SET(String key, Integer val) {
        if (mKeyValMap.containsKey(key)) {
            Integer origVal = mKeyValMap.get(key);
            mValCountMap.put(origVal, mValCountMap.getOrDefault(origVal, 1) - 1);
        }
        mKeyValMap.put(key, val);
        mValCountMap.put(val, mValCountMap.getOrDefault(val, 0) + 1);
    }

    public Integer GET(String key) {
        if (mKeyValMap.containsKey(key)) {
            Integer val = mKeyValMap.get(key);
            FileUtil.generateOutputFile(FileUtil.outputFile, Arrays.asList(new String[]{""+ val}));
            return val;
        }
        FileUtil.generateOutputFile(FileUtil.outputFile, Arrays.asList(new String[]{"NULL"}));
        return null;
    }

    //set the current key value to null
    public void UNSET(String key) {
        if (mKeyValMap.containsKey(key)) {
            Integer val = mKeyValMap.get(key);
            mValCountMap.put(val, mValCountMap.getOrDefault(val, 1)- 1);
            mKeyValMap.remove(key);
        }
    }

    public Integer NUMEQUALTO(Integer key) {
        Integer val = mValCountMap.getOrDefault(key, 0);
        FileUtil.generateOutputFile(FileUtil.outputFile, Arrays.asList(new String[]{"" + val}));
        return val;
    }

    //######################## below for v2 #############################
    public HashMap<String, Integer> getKeyValMap() {
        return mKeyValMap;
    }

    public HashMap<Integer, Integer> getValCountMap() {
        return mValCountMap;
    }

    //abstract map equals compares entrySet of 2 maps
    public boolean equals(Operation that) {
        if ( !this.getKeyValMap().equals(that.getKeyValMap())) {
            return false;
        }

        if (! this.getValCountMap().equals(that.getValCountMap())) {
            return false;
        }
        return true;
    }
}
