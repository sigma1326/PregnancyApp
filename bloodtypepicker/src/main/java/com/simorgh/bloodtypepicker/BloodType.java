package com.simorgh.bloodtypepicker;

public class BloodType {
    private String bloodType;
    private boolean isNegative;

    public BloodType(String bloodType, boolean isNegative) {
        this.bloodType = bloodType;
        this.isNegative = isNegative;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public boolean isNegative() {
        return isNegative;
    }

    public void setNegative(boolean negative) {
        isNegative = negative;
    }
}
