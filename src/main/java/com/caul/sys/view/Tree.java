package com.caul.sys.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuwh on 15/8/13.
 */
public class Tree {

    public static final String STATE_OPEN = "open";

    public static final String STATE_CLOSED = "closed";

    public static final String ATTR_URL =   "url";

    private String id;

    private String text;

    private String iconCls;

    private String state;

    private boolean checked;

    private List<Tree> children = null;

    private Map<String, Object> attributes = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }

    public void addChild(Tree comboTree) {
        if (this.children == null) {
            this.children = new ArrayList<Tree>();
        }
        children.add(comboTree);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void putAttribute(String key, Object value) {
        if (this.attributes == null) {
            attributes = new HashMap<String, Object>();
        }
        attributes.put(key, value);
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
