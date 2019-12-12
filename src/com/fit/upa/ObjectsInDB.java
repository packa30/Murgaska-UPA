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
        if(type == 6){
            this.type = type;
            this.objType = objType;
            this.name = name;
            this.ordinates = usefulOrdinates(ordinates);
            this.eleminfo = eleminfo;
        }
        else {
            this.type = type;
            this.objType = objType;
            this.name = name;
            this.ordinates = ordinates;
            this.eleminfo = eleminfo;
        }
    }
    public String info(){
        return type + " " + name +" "+ Arrays.toString(ordinates);
    }

    public double[] usefulOrdinates(double[] ord){
        int j = 0;
        double[] resOrd = new double[ord.length-2]; // problem ak bude viacej bodov ako 4
        for(int i = 0; i < resOrd.length;i++){
            resOrd[i] = ord[j];
            j++;
            if(j==3){
                j+=2;
            }
        }
        System.out.println(">>>>>>"+Arrays.toString(resOrd));
        return resOrd;
    }
}