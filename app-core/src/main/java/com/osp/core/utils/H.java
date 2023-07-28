package com.osp.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class H {

    public static <T> void each(List<T> list, Each<T> each) throws RuntimeException {

        if (!H.isTrue(list)) return;

        for (int index = 0; index < list.size(); index++)
            each.do_(index, list.get(index));
    }

    public static <T> List<T> findAll(List<T> list, final Find<T> findAll_) throws RuntimeException {

        final List<T> newList = new ArrayList<T>();

        if (!H.isTrue(list)) return newList;

        try {
            each(list, (index, item) -> {
                if (findAll_.do_(index, item)) {
                    newList.add(item);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return newList;
    }

    public static <T> T find(List<T> list, final Find<T> findAll_) {

        if (!H.isTrue(list)) return null;

        for (int index = 0; index < list.size(); index++) {
            if (findAll_.do_(index, list.get(index))) return list.get(index);
        }

        return null;
    }

    public static <T> Boolean exists(List<T> list, final Find<T> findAll_) {

        if (!H.isTrue(list)) return false;

        for (int index = 0; index < list.size(); index++) {
            if (findAll_.do_(index, list.get(index))) return true;
        }

        return false;
    }

    public static <T1, T2> List<T2> collect(List<T1> list, Collect<T1, T2> collect) {

        List<T2> newList = new ArrayList<T2>();

        if (!H.isTrue(list)) return newList;

        for (int index = 0; index < list.size(); index++)
            newList.add(collect.do_(index, list.get(index)));

        return newList;
    }

    public static String join(Collection<String> strs, String delimiter) {

        if (strs == null || strs.isEmpty()) return null;

        StringBuilder sb = new StringBuilder();

        Iterator<String> iterator = strs.iterator();

        while (iterator.hasNext()) {

            sb.append(iterator.next());

            if (iterator.hasNext()) sb.append(delimiter);
        }

        return sb.toString();
    }

    public static String join(String[] strs, String delimiter) {

        if (strs == null || strs.length == 0) return null;

        StringBuilder sb = new StringBuilder();

        for (int index = 0; index < strs.length; index++) {

            sb.append(strs[index]);

            if (index < strs.length - 1) sb.append(delimiter);
        }

        return sb.toString();
    }

    public static Boolean isTrue(Object value) {

        if (value == null) return false;

        if (value instanceof String) return !((String) value).trim().isEmpty();

        if (value instanceof Number) return !((Number) value).equals(Long.valueOf(0));

        if (value instanceof Boolean) return (Boolean) value;

        if (value instanceof Collection) return !((Collection) value).isEmpty();

        if (value instanceof Object[]) return ((Object[]) value).length > 0;

        return true;
    }

    public static <T> T nvl(T object, NVL<T>... nvls) {
        int index = 0;
        if (nvls != null) {
            while (object == null && index < nvls.length) {
                object = nvls[index].getDefaultValue();
                index++;
            }
        }
        return object;
    }

    public interface Each<T> {

        void do_(int index, T item) throws RuntimeException;
    }

    public interface Collect<T1, T2> {

        T2 do_(int index, T1 item) throws RuntimeException;
    }

    public interface Find<T> {

        Boolean do_(int index, T item) throws RuntimeException;
    }

    public interface NVL<T> {
        T getDefaultValue() throws RuntimeException;
    }
}
