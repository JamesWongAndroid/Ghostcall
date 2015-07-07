package com.tapfury.ghostcall;

public class GhostPackage {

    private String packagePrice;
    private String packageName;
    private String packageTime;

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {

        return packageName;
    }

    public String getPackageTime() {
        return packageTime;
    }

    public void setPackageTime(String packageTime) {
        this.packageTime = packageTime;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }
}
