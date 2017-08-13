package kiku.mutil.unit.Redis;

import java.util.Arrays;
import java.util.Stack;

/**
 * https://www.thumbtack.com/challenges/simple-database
 */
public class Transactions {
    private Stack<Operation> mStack;
    private boolean mIsBegin;

    public Transactions() {
        mStack = new Stack<>();
        mIsBegin = false;
    }

    public Operation BEGIN(Operation operation) {
        mIsBegin = true;
        mStack.push(operation);
        return new Operation(operation);
    }

    /**
     * ROLLBACK one transaction if applicable (ex: set, unset)
     */
    public Operation ROLLBACK(Operation operation) {
        if (!mStack.isEmpty() && !mStack.peek().equals(operation)) {
            Operation lastCommitted = mStack.pop();
            if (mStack.isEmpty()) {
                mIsBegin = false;
            }
            return lastCommitted;
        } else {
            FileUtil.generateOutputFile(FileUtil.outputFile, Arrays.asList(new String[]{"NO TRANSACTION"}));
            System.out.println("NO TRANSACTION");
            return operation;
        }
    }

    public Operation COMMIT(Operation operation) {
        if (!mIsBegin) {
            FileUtil.generateOutputFile(FileUtil.outputFile, Arrays.asList(new String[]{"NO TRANSACTION"}));
            System.out.println("Cannot COMMIT without BEGIN");
            return operation;
        }
        if (mStack.isEmpty() || mStack.peek().equals(operation)) {
            FileUtil.generateOutputFile(FileUtil.outputFile, Arrays.asList(new String[]{"NO TRANSACTION"}));
            System.out.println("NO TRANSACTION");
            return operation;
        }
        mStack.clear(); //remove all operations
        mStack.push(operation);
        return new Operation(operation);
    }
/*
    public class NestedOps {
        HashMap<String, Integer> mKeyValMap;
        HashMap<Integer, Integer> mValCountMap;

        public NestedOps() {
            mKeyValMap = new HashMap();
            mValCountMap = new HashMap<>();
        }

        //create new operation with updated map val
        public NestedOps(NestedOps nestedOps) {
            mKeyValMap = new HashMap(nestedOps.getKeyValMap());
            mValCountMap = new HashMap<>(nestedOps.getValCountMap());
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
                return mKeyValMap.get(key);
            }
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
            return mValCountMap.getOrDefault(key, 0);
        }

        public HashMap<String, Integer> getKeyValMap() {
            return mKeyValMap;
        }

        public HashMap<Integer, Integer> getValCountMap() {
            return mValCountMap;
        }

        //abstract map equals comapares entrySet of 2 maps
        public boolean equal(NestedOps o1, NestedOps o2) {
            if ( !o1.getKeyValMap().equals(o2.getKeyValMap())) {
                return false;
            }

            if (! o1.getValCountMap().equals(o2.getValCountMap())) {
                return false;
            }
            return true;
        }
    }
*/
}
