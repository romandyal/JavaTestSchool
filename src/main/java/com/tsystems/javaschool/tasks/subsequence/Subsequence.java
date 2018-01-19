package com.tsystems.javaschool.tasks.subsequence;

import java.io.ObjectInputStream;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public static boolean find(List x, List y) {
        if (x == null || y == null) throw new IllegalArgumentException();
        try{
            int lastPos = 0;
            for (int i = 0; i < x.size(); i++) {
                if (!y.contains(x.get(i))) return false;
                int curPos = y.indexOf(x.get(i));
                if(lastPos > curPos)return false;
                lastPos = curPos;
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static void main(String[] args) {
        boolean res;
        List x = Stream.of("B", "A", "D", "C").collect(toList());
        List y = Stream.of("BD", "A", "ABC", "B", "M", "D", "M", "C", "DC", "D").collect(toList());
        res = find(x, y);
        System.out.println(res);
    }
}
