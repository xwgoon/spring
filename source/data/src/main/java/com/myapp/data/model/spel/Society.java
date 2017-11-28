package com.myapp.data.model.spel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Society {

    private String name;

    public static String Advisors = "advisors";
    public static String President = "president";

    private List<Inventor> members = new ArrayList<>();
    private Map<String, Integer> officers = new HashMap<>();
    private int[] ints = {1, 2, 3};

    {
        members.add(new Inventor("张三", "中国"));
        members.add(new Inventor("Tom", "美国"));
        members.add(new Inventor("李四", "中国"));

        officers.put("a", 1);
        officers.put("b", 2);
        officers.put("c", 3);
    }

    public int[] getInts() {
        return ints;
    }

    public List<Inventor> getMembers() {
        return members;
    }

    public Map getOfficers() {
        return officers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMember(String name) {
        for (Inventor inventor : members) {
            if (inventor.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
