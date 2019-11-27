package com.fit.upa;

import java.util.ArrayList;
import java.util.Arrays;

public class ObjectsInDB {
    public int type;
    public String objType;
    public String name;
    public double[] ordinates;

    public ObjectsInDB(int type, String objType, String name, double[] ordinates){
        this.type = type;
        this.objType = objType;
        this.name = name;
        this.ordinates = ordinates;
    }
    public String info(){
        return type + " " + name +" "+ Arrays.toString(ordinates);
    }
}