package com.example.jobSeaching.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public enum MembershipType {
    BASIC(0, 0, 0),
    SILVER(30, 3, 747500),
    GOLD(90, 10, 1997500),
    PLATINUM(180, 30, 3747500),
    DIAMOND(365, 80, 6997500);

    private final int durationDays;
    private final int postLimit;
    private final int price;

    MembershipType(int durationDays, int postLimit, int price) {
        this.durationDays = durationDays;
        this.postLimit = postLimit;
        this.price = price;
    }

    public int getDurationDays() { return durationDays; }
    public int getPostLimit() { return postLimit; }
    public int getPrice() { return price; }
}
