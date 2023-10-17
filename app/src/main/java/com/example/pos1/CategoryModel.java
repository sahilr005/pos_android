package com.example.pos1;

public class CategoryModel {
    private int catid;
    private String catname;

    public CategoryModel(int catid, String catname) {
        this.catid = catid;
        this.catname = catname;
    }

    public int getCatid() {
        return catid;
    }

    public String getCatname() {
        return catname;
    }
}
