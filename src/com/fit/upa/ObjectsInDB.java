package com.fit.upa;

import java.util.ArrayList;
import java.util.Arrays;

public class ObjectsInDB {
    int type;
    String name;
    double[] ordinates;

    public ObjectsInDB(int type, String name, double[] ordinates){
        this.type = type;
        this.name = name;
        this.ordinates = ordinates;
    }
    public String info(){
        return type + " " + name +" "+ Arrays.toString(ordinates);
    }
}
