package com.lxchild.www.search;

/**
 * Created by LXChild on 2015/7/5.
 */
public class StallEntity {
    private int id;
    private String status;
    private int[] pos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int[] getPos() {
        return pos;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }
}
