package model;

import java.sql.Date;

public class PlanDetails {
    private int id;
    private PlanHeaders planHeader;
    private int shiftId;
    private Date date;
    private int quantity;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlanHeaders getPlanHeader() {
        return planHeader;
    }

    public void setPlanHeader(PlanHeaders planHeader) {
        this.planHeader = planHeader;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
