package com.lxchild.www.algorithm;

import java.util.Comparator;

/**
 * Created by Administrator on 2015/6/30.
 */
public class AStarComparator implements Comparator<int[][]>{
    Algorithm algorithm;
    public  AStarComparator(Algorithm algorithm){
        this.algorithm=algorithm;

    }
    public int compare(int[][] o1,int[][] o2){
        int[] t1=o1[1];
        int[] t2=o2[1];
        int[] target=algorithm.getTarget();
        int a=(t1[0]-target[0])*(t1[0]-target[0])+(t1[1]-target[1])*(t1[1]-target[1]);
        int b=(t2[0]-target[0])*(t2[0]-target[0])+(t2[1]-target[1])*(t2[1]-target[1]);
        return a-b;
    }
    public boolean equals(Object obj){
        return false;
    }
}
