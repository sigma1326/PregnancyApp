package com.simorgh.reportutil;

import com.simorgh.database.Date;

public final class ReportState {
    public boolean drugs = true;
    public boolean bloodPressure = true;
    public boolean motherWeight = true;
    public boolean fever = true;
    public boolean cigarette = true;
    public boolean alcohol = true;
    public boolean sleepTime = true;
    public boolean exerciseTime = true;

    public Date startDate;
    public Date endDate;

    public ReportState(boolean drugs, boolean bloodPressure, boolean motherWeight, boolean fever, boolean cigarette, boolean alcohol, boolean sleepTime, boolean exerciseTime) {
        this.drugs = drugs;
        this.bloodPressure = bloodPressure;
        this.motherWeight = motherWeight;
        this.fever = fever;
        this.cigarette = cigarette;
        this.alcohol = alcohol;
        this.sleepTime = sleepTime;
        this.exerciseTime = exerciseTime;
    }

    public ReportState() {

    }


}
