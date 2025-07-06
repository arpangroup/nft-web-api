package com.trustai.income_service.rank.entity;

import java.util.Arrays;

public enum Rank {
    RANK_0(0, "Rank 0"),
    RANK_1(1, "Rank 1"),
    RANK_2(2, "Rank 2"),
    RANK_3(3, "Rank 3"),
    RANK_4(4, "Rank 4"),
    RANK_5(5, "Rank 5"),
    RANK_6(6, "Rank 6"),
    RANK_7(7, "Rank 7"),
    RANK_8(8, "Rank 8");

    private final int value;
    private final String displayName;

    Rank(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Rank fromValue(int value) {
        return Arrays.stream(values())
                .filter(rank -> rank.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid rank value: " + value));
    }
}
