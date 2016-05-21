package com.caul.sys.view;

import cn.easybuild.pojo.DataSet;
import cn.easybuild.pojo.StringPojo;

/**
 * 负责将 DateSet 转换为 Grid
 * Created by yuanyouz on 2016/3/11.
 */
public class GridAdapter<T extends StringPojo> extends Grid<T> {

    public GridAdapter(DataSet<T> dataSet) {
        super(dataSet.getData(), dataSet.getPaging().getRecords());
    }
}
