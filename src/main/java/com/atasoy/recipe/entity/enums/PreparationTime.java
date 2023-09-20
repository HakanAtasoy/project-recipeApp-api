package com.atasoy.recipe.entity.enums;

public enum PreparationTime {
    LESS_THAN_15("15 dakikadan az"),
    BETWEEN_15_AND_30("15-30 dakika"),
    BETWEEN_30_AND_60("30 dakika - 1 saat"),
    MORE_THAN_60("1 saatten fazla");


    private final String displayName;

    PreparationTime(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PreparationTime fromDisplayTime(String displayName) {
        for (PreparationTime PreparationTime : PreparationTime.values()) {
            if (PreparationTime.getDisplayName().equals(displayName)) {
                return PreparationTime;
            }
        }
        throw new IllegalArgumentException("No enum constant found for display name: " + displayName);
    }

}

