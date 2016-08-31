package com.auais.note.pojo;

public class Source {
    private Integer id;

    private String userId;

    private String soruceId;

    private Float size;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getSoruceId() {
        return soruceId;
    }

    public void setSoruceId(String soruceId) {
        this.soruceId = soruceId == null ? null : soruceId.trim();
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }
}