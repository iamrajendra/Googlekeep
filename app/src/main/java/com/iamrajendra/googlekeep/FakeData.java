package com.iamrajendra.googlekeep;

import android.view.Display;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FakeData {
    public  static List<Model> getItems(){
        List<Model> list = new ArrayList<>();
        list.add(new Model(0,"A"));
        list.add(new Model(1,"B"));
        list.add(new Model(0,"C"));
        list.add(new Model(0,"D"));
        list.add(new Model(0,"E"));
        list.add(new Model(1,"F"));
        list.add(new Model(0,"G"));
        list.add(new Model(0,"H"));
        list.add(new Model(0,"I"));
        list.add(new Model(1,"J"));
        list.add(new Model(0,"K"));
        list.add(new Model(0,"L"));
        list.add(new Model(0,"M"));
        list.add(new Model(1,"N"));
        list.add(new Model(0,"O"));
        list.add(new Model(0,"P"));
        list.add(new Model(0,"Q"));
        list.add(new Model(1,"R"));
        list.add(new Model(0,"S"));
        list.add(new Model(0,"T"));
        list.add(new Model(0,"U"));
        list.add(new Model(1,"V"));
        list.add(new Model(0,"W"));
        list.add(new Model(0,"X"));
        list.add(new Model(0,"Y"));
        list.add(new Model(0,"Z"));
        Collections.sort(list);
return  list;
    }
}
