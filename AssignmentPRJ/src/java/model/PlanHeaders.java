package model;

public class PlanHeaders {
    private int id;
    private Plans plan;
    private Products product;
    private int quantity;
    private float estimatedEffort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Plans getPlan() {
        return plan;
    }

    public void setPlan(Plans plan) {
        this.plan = plan;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getEstimatedEffort() {
        return estimatedEffort;
    }

    public void setEstimatedEffort(float estimatedEffort) {
        this.estimatedEffort = estimatedEffort;
    }
}
