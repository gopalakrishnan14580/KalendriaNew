package com.kalendria.kalendria.model;

/**
 * Created by mansoor on 02/03/16.
 */
public class FilerModel {
    private String parent_id;
    private String id;
    private String name;
    private String level;
    public boolean isVisible;
    public boolean isSearch;
    public   String sector;//added by magesh
    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
