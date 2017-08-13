package kiku.mutil.unit.RE;

import java.util.HashMap;

public class Block {
    Block previous;
    HashMap<String, Integer> mapvalue=new HashMap<String, Integer>();
    HashMap<Integer, Integer> mapcounter=new HashMap<Integer, Integer>();

    public int numwithValue(Integer value) {
        return 0;
    }

    public Integer get(String name) {
        //starting from the last block to find the value
        Block current=this;
        Integer value=current.mapvalue.get(name);

        while(!current.mapvalue.containsKey(name)&&current.previous!=null){
            current=current.previous;
            value=current.mapvalue.get(name);
        }
        return value;
    }

    public Integer numwithValue(String value){
        if(value==null) return 0;
        Block current=this;
        Integer num=current.mapcounter.get(value);
        while(!current.mapcounter.containsKey(value)&&current.previous!=null){
            current=current.previous;
            num=current.mapcounter.get(value);
        }
        if(num==null) return 0;
        return num;

    }
    public void set(String name, Integer value) {
        //first check if we have the value
        Integer old_value=this.get(name);
        System.out.println("name and value are "+name+" "+value);
        if(old_value!=null){
            //decrease the counter
            Integer temp=numwithValue(old_value);
            temp--;
            mapcounter.put(old_value, temp);
        }

        //update the new value
        Integer count=numwithValue(value);
        System.out.println("count is "+count);
        System.out.println("value is "+value);
        //if value is null it means unset
        if(value!=null){
            if(count!=null){
                count++;
                mapcounter.put(value, count);
            }else{
                System.out.println("put value into 1 "+value);
                mapcounter.put(value, new Integer(1));


            }
        }
        mapvalue.put(name,value);

    }


}

