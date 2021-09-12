package com.company;

public class FileInfo {
    private String name;
    private double size;
    private String unit;
    private boolean isService = false;

    public FileInfo(String name, double size) {
        this.name = name;
        this.size = size;
        unit = "";
    }

    public FileInfo(String name, double size, boolean isService){
        this(name, size);
        this.isService = isService;
    }

    public String getName() {
        return name;
    }

    public double getSize() {
        return size;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isService() {
        return isService;
    }

    public void convertUnit(int base){
        unit = "Б";

        if (size < 1000)
            return;

        if (size >= 1000 && size < 1000000) {
            size = size / base;
            unit = "КБ";
        }
        else if (size >= 1000000 && size < 1e+9){
            size = size / (base * base);
            unit = "МБ";
        }
        else {
            size = size / (base * base * base);
            unit = "ГБ";
        }

        size =  Math.round(size * 100.0) / 100.0;
    }

    public void convertDefaultUnit(int base) {
        size = Math.round(getSize() / base * 100.0) / 100.0;
    }
}
