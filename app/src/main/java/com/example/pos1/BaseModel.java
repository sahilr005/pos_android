package com.example.pos1;

public class BaseModel {
    private int id;
    private String name;
    private boolean active;

    public BaseModel(int id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }
}
