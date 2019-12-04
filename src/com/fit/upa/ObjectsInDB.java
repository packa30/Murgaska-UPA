package com.fit.upa;

import java.util.ArrayList;
import java.util.Arrays;

public class ObjectsInDB {
    public int type;
    public String objType;
    public String name;
    public double[] ordinates;
    public int[] eleminfo;

    public ObjectsInDB(int type, String objType, String name, double[] ordinates, int[] eleminfo){
        this.type = type;
        this.objType = objType;
        this.name = name;
        this.ordinates = ordinates;
        this.eleminfo = eleminfo;
    }
    public String info(){
        return type + " " + name +" "+ Arrays.toString(ordinates);
    }
}