package com.example.model;

public class CongViec {
    private long id;
    private String TenCV;
    private String MoTa;
    private long Thoigian;
    private String DiaDiem;
    private int MaLoaiCV;
    private int thoiGianLap;

    public CongViec(long id, String tenCV, String moTa, long thoigian, String diaDiem, int maLoaiCV, int thoiGianLap) {
        this.id = id;
        this.TenCV = tenCV;
        this.MoTa = moTa;
        this.Thoigian = thoigian;
        this.DiaDiem = diaDiem;
        this.MaLoaiCV = maLoaiCV;
        this.thoiGianLap = thoiGianLap;
    }

    public CongViec(String tenCV, String moTa, long thoigian, String diaDiem, int maLoaiCV, int thoiGianLap) {
        this.TenCV = tenCV;
        this.MoTa = moTa;
        this.Thoigian = thoigian;
        this.DiaDiem = diaDiem;
        this.MaLoaiCV = maLoaiCV;
        this.thoiGianLap = thoiGianLap;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTenCV() {
        return TenCV;
    }

    public void setTenCV(String tenCV) {
        TenCV = tenCV;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public long getThoigian() {
        return Thoigian;
    }

    public void setThoigian(long thoigian) {
        Thoigian = thoigian;
    }

    public String getDiaDiem() {
        return DiaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        DiaDiem = diaDiem;
    }

    public int getMaLoaiCV() {
        return MaLoaiCV;
    }

    public void setMaLoaiCV(int maLoaiCV) {
        MaLoaiCV = maLoaiCV;
    }

    public int getThoiGianLap() {
        return thoiGianLap;
    }

    public void setThoiGianLap(int thoiGianLap) {
        this.thoiGianLap = thoiGianLap;
    }
}
