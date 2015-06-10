package org.apereo.finaid.mvc.models;

public class Award {

    private String status;
    private String offeredAmt;
    private String paidAmt;
    private String fund;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOfferedAmt(double offeredAmt) {
        this.offeredAmt = String.format("$%.2f", offeredAmt);
    }

    public void setPaidAmt(double paidAmt) {
        this.paidAmt = String.format("$%.2f", paidAmt);
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getStatus() {
        return status;
    }

    public String getOfferedAmt() {
        return offeredAmt;
    }

    public String getPaidAmt() {
        return paidAmt;
    }

    public String getFund() {
        return fund;
    }

}
