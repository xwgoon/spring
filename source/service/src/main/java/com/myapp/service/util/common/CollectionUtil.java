package com.myapp.service.util.common;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtil {

    public static <T> List<List<T>> split(Collection<T> collection) {
        return split(collection, 2000);
    }

    public static <T> List<List<T>> split(Collection<T> collection, int threshold) {
        List<List<T>> list1 = new ArrayList<>();
        if (CollectionUtils.isEmpty(collection)) return list1;

        int n = 0;
        List<T> list2 = new ArrayList<>();
        for (T t : collection) {
            if (n < threshold) {
                list2.add(t);
                n++;
            } else {
                list1.add(list2);
                list2 = new ArrayList<>();
                n = 0;
            }
        }

        if (list2.size() > 0) {
            list1.add(list2);
        }

        return list1;
    }
}
