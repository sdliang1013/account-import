package com.caul.sys.view;

import cn.easybuild.pojo.StringPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuwh on 15/8/13.
 */
public class Grid<T extends StringPojo> {

    private int total = 0;

    private List<T> rows = new ArrayList<T>();

    public Grid() {
    }

    public Grid(List<T> rows, int total) {
        this.total = total;
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
