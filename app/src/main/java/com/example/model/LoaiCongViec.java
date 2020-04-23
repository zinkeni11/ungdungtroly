package com.example.model;

public class LoaiCongViec {
    private long id;
    private String TenLoaiCV;
    private String MoTaLoaiCV;

    public LoaiCongViec(long id, String tenLoaiCV, String moTaLoaiCV) {
        this.id = id;
        this.TenLoaiCV = tenLoaiCV;
        this.MoTaLoaiCV = moTaLoaiCV;
    }

    public LoaiCongViec(String tenLoaiCV, String moTaLoaiCV) {
        this.TenLoaiCV = tenLoaiCV;
        this.MoTaLoaiCV = moTaLoaiCV;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTenLoaiCV() {
        return TenLoaiCV;
    }

    public void setTenLoaiCV(String tenLoaiCV) {
        TenLoaiCV = tenLoaiCV;
    }

    public String getMoTaLoaiCV() {
        return MoTaLoaiCV;
    }

    public void setMoTaLoaiCV(String moTaLoaiCV) {
        MoTaLoaiCV = moTaLoaiCV;
    }
}
