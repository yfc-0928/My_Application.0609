package com.example.myapplication0609;

public class DevResource {
    private int id;
    private String url;
    private boolean isSaved;

    public DevResource() {
    }

    public DevResource(int id, String url, boolean isSaved) {
        this.id = id;
        this.url = url;
        this.isSaved = isSaved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }
}
