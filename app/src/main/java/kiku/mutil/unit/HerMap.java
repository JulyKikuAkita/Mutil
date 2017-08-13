package kiku.mutil.unit;

import java.util.HashMap;

public class HerMap extends PMap {
    HashMap<Integer, HashMap> version_map=new HashMap<Integer, HashMap>();
    static Integer max_version=0;

    public Integer get(String key, int version_int){
        if(key==null) {
            System.out.println("GET "+key+" = <NULL>");
            return null;
        }
        // might throw exception if version_int not in keySet (version_int provided by input)
        if(version_map.get(version_int).containsKey(key)){
            int value=(int) version_map.get(version_int).get(key);
            System.out.println("GET "+key +"(#"+version_int+") = "+value);
            return value;
        }else{
            int temp=version_int;
            while(temp>=0){
                if(version_map.get(temp).containsKey(key)){
                    int value=(int) version_map.get(temp).get(key);
                    System.out.println("GET "+key +"(#"+version_int+") = "+value);
                    return value;
                }
                temp--;
            }
        }
        return null;
    }

    public Integer put(String key, int value){
        max_version++;
        HashMap<String, Integer> value_map=new HashMap<String, Integer>();
        value_map.put(key, value);
        version_map.put(max_version, value_map);
        System.out.println("PUT (#"+max_version+") "+key+" = "+value);
        return max_version;
    }

    public Integer get(String key){
        if(key==null) {
            System.out.println("GET "+key+" = <NULL>");
            return null;
        }
        if(version_map.get(max_version).containsKey(key)){
            System.out.println("GET "+key +"= "+version_map.get(max_version).get(key));
            return (Integer)version_map.get(max_version).get(key);
        }
        else{
            int temp=max_version;
            while(temp>0){
                if(version_map.get(temp).containsKey(key)){
                    Integer value=(Integer) version_map.get(temp).get(key);
                    System.out.println("GET "+key +" = "+value);
                    return value;
                }
                temp--;
            }
        }
        System.out.println("GET "+key+" = <NULL>");
        //get key4
        return null;
    }
}
