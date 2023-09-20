package com.atasoy.recipe.entity.enums;

public enum ServingSize {
    LESS_THAN_2("1 kişilik"),
    BETWEEN_2_AND_4("2-4 kişilik"),
    BETWEEN_4_AND_6("4-6 kişilik"),
    MORE_THAN_6("6+ kişilik");
    // Add more ranges as needed

    private final String displayName;

    ServingSize(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ServingSize fromDisplayName(String displayName) {
        for (ServingSize servingSize : ServingSize.values()) {
            if (servingSize.getDisplayName().equals(displayName)) {
                return servingSize;
            }
        }
        throw new IllegalArgumentException("No enum constant found for display name: " + displayName);
    }

}

