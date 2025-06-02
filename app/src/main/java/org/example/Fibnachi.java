package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fibnachi {
    private Map<Integer,Integer> mem = new HashMap<>(){{
        put(0,0);
        put(1,1);
    }};

    private List<Integer> dp = new ArrayList<>(List.of(0,1));

    public int fib(int n) {
        return fibBottomUp(n);
    }

    private int fibRecursion(int n) {
        if(n == 0) return 0;
        if(n == 1) return 1;
        return fibRecursion(n-1) + fibRecursion(n-2);
    }

    private int fibMeoization(int n) {
        if(mem.containsKey(n)) return mem.get(n);
        int f = fibMeoization(n-1) + fibMeoization(n-2);
        mem.put(n, f);
        return mem.get(n);
    }

    private int fibTable(int n) {  //bottom with
        for (int i = 2; i <= n; i++) {
            var f = dp.get(i-2) + dp.get(i-1);
            dp.add(f);
        }
        return dp.get(n);
    }

    private int fibBottomUp(int n) {
        if( n < 2) return n;
        int prev = 0, cur=1;
        for (int i = 2; i <= n; i++) {
            int next = prev + cur;
            prev = cur;
            cur = next;
        }
        return cur;
    }

}
