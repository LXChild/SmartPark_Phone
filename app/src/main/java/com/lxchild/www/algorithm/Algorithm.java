package com.lxchild.www.algorithm;

import android.content.Context;
import android.os.AsyncTask;

import com.lxchild.www.util.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

public class Algorithm extends AsyncTask<Integer, Void, HashMap<String, int[][]>> {//算法类
    private boolean pathFlag = false;//true 找到了路径
    private Context cxt;

    private int[] target = new int[]{0, 0};
    private int[] source = new int[]{11, 3};
    private ArrayList<int[][]> searchProcess = new ArrayList<>();//搜索过程

    //记录到每个点的最短路径 for Dijkstra
    private HashMap<String, ArrayList<int[][]>> hmPath = new HashMap<>();
    private int[][] map = MapList.map_f1;

    //A*用优先级队列
    private PriorityQueue<int[][]> astarQueue = new PriorityQueue<int[][]>(100, new AStarComparator(this));
    private Stack<int[][]> stack = new Stack<>();//深度优先所用栈
    private HashMap<String, int[][]> hm = new HashMap<>();//结果路径记录
    private LinkedList<int[][]> queue = new LinkedList<>();//广度优先所用队列
    //记录路径长度 for Dijkstra
    private int[][] length = new int[MapList.map_f1.length][MapList.map_f1[0].length];
    private int[][] visited = new int[MapList.map_f1.length][MapList.map_f1[0].length];//0 未去过 1 去过
    private int[][] sequence = {
            {0, 1}, {0, -1},
            {-1, 0}, {1, 0},
            {-1, 1}, {-1, -1},
            {1, -1}, {1, 1}
    };
    private int timeSpan = 10;//时间间隔

//    private static Algorithm algorithm;

//    public static Algorithm instance() {
//        if (algorithm == null) {
//            algorithm = new Algorithm();
//        }
//        return algorithm;
//    }

    public void setCxt(Context cxt) {
        this.cxt = cxt;
    }

    public boolean isPathFlag() {
        return pathFlag;
    }

    public void setPathFlag(boolean pathFlag) {
        this.pathFlag = pathFlag;
    }

    public void setTarget(int[] target) {
        this.target = target;
    }

    public int[] getTarget() {
        return target;
    }

    public void setSource(int[] source) {
        this.source = source;
    }

    public int[] getSource() {
        return source;
    }

    public HashMap<String, int[][]> getHm() {
        return hm;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MyUtil.ShowProgressDialog(cxt);
    }

    @Override
    protected HashMap<String, int[][]> doInBackground(Integer... params) {
        clearState();
        return setWay(source[0], source[1], target[0], target[1]);
    }

    @Override
    protected void onPostExecute(HashMap<String, int[][]> stringHashMap) {
        super.onPostExecute(stringHashMap);
        MyUtil.DismissProgressDialog();
        this.cancel(true);
    }

    public void clearState() {//清空所有状态与列表
        pathFlag = false;
        searchProcess.clear();
        stack.clear();
        queue.clear();
        astarQueue.clear();
        hm.clear();
        visited = new int[MapList.map_f1.length][MapList.map_f1[0].length];
        hmPath.clear();
        for (int i = 0; i < length.length; i++) {
            for (int j = 0; j < length[0].length; j++) {
                length[i][j] = 9999;//初始路径长度为最大距离都不可能的那么大
            }
        }
    }

    public HashMap<String, int[][]> setWay(final int sx, final int sy, final int tx, final int ty) {

        boolean flag = true;
        int[][] start = {//开始状态
                {sx, sy},
                {sx, sy}
        };
        astarQueue.offer(start);
        while (flag) {
            int[][] currentEdge = astarQueue.poll();//从队首取出边
            int[] tempTarget = currentEdge[1];//取出此边的目的点
            //判断目的点是否去过，若去过则直接进入下次循环
            if (visited[tempTarget[1]][tempTarget[0]] != 0) {
                continue;
            }
            //标识目的点为访问过
            visited[tempTarget[1]][tempTarget[0]] = visited[currentEdge[0][1]][currentEdge[0][0]] + 1;
            searchProcess.add(currentEdge);//将临时目的点加入搜索过程中
            //记录此临时目的点的父节点
            hm.put(tempTarget[0] + ":" + tempTarget[1], new int[][]{currentEdge[1], currentEdge[0]});
            //    MapBitmap.canDrawPath = true;
            try {
                Thread.sleep(timeSpan);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //判断有否找到目的点
            if (tempTarget[0] == tx && tempTarget[1] == ty) {
                break;
            }
            int currCol = tempTarget[0];//将所有可能的边入优先级队列
            int currRow = tempTarget[1];
            for (int[] rc : sequence) {
                int i = rc[1];
                int j = rc[0];
                if (i == 0 && j == 0) {
                    continue;
                }
                if (currRow + i >= 0 && currRow + i < MapList.map_f1.length && currCol + j >= 0
                        && currCol + j < MapList.map_f1[0].length &&
                        map[currRow + i][currCol + j] != 1) {
                    int[][] tempEdge = {
                            {tempTarget[0], tempTarget[1]},
                            {currCol + j, currRow + i}
                    };
                    astarQueue.offer(tempEdge);
                }
            }
        }
        pathFlag = true;
        //MapBitmap.canDrawPath = true;

        return hm;
    }

}




