package com.osp.core.utils;

import com.google.common.collect.Lists;
import java.util.*;
import java.util.stream.*;

public class UtilsCollection<T> {

    /*Collections.sort(list, new Comparator<LogDataView>() {
        public int compare(LogDataView o1, LogDataView o2) {
            return o1.getCreation_date().compareTo(o2.getCreation_date());
        }
    });*/

    public static Set<String> union(Set<String> s1, Set<String> s2) {
        // Sets.union(s1, s2);
        return Stream.concat(s1.stream(),s2.stream()).collect(Collectors.toSet());
    }
    public static Set<String> intersection(Set<String> s1, Set<String> s2) {
        // Sets.intersection(s1, s2);
        return s1.stream().filter(s2::contains).collect(Collectors.toSet());
    }

    public static <T> List<List<T>> partition(List<T> list, int size, Class<T> classOfT) {
        List<List<T>> newList = Lists.partition(list, size);
        return newList;
    }

    public static void main(String[] args) {
        List<String> OLD_LIST = Arrays.asList("Tang Monk,name of a fictitious monkey with supernatural powers,Bajie,Monk Sha,Cao Cao,Liu Bei,Sun Quan".split(","));
        List<List<String>> newList = Lists.partition(OLD_LIST, 3);

        List<Integer> LIST = Arrays.asList(1, 2, 3, 4, 5, 6);
        Map<Boolean, List<Integer>> newMap = LIST.stream().collect(Collectors.partitioningBy(i -> i > 3));

        System.out.println("done");
    }
}
