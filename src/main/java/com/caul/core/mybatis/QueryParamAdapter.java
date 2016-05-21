package com.caul.core.mybatis;

import cn.easybuild.pojo.Paging;
import cn.easybuild.pojo.QueryParam;

/**
 * Created by xuwh on 15/8/15.
 */
public class QueryParamAdapter extends QueryParam {

    private Integer page = 1;

    private Integer rows = 20;

    public int getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if (page == null) {
            return;
        }
        if (getPaging() == null) {
            setPaging(new Paging());
        }
        getPaging().setCurrentPage(page);
        this.page = page;
    }

    public void setPageNo(Integer pageNo) {
        setPage(pageNo);
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        if (rows == null) {
            return;
        }
        getPaging().setPageSize(rows);
        this.rows = rows;
    }

    public Integer getOffset() {
        if (page < 1) {
            page = 1;
        }
        return (page - 1) * rows;
    }

    public Integer getResultTotal() {
        return getPaging().getRecords();
    }
}
