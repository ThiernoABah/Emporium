package com.thiernoob.emporium.gameobjects.enums;

public enum Rarity {
    COMMON{
        public String toString() {
            return "Common";
        }
    },
    RARE{
        public String toString() {
            return "Rare";
        }
    },
    EPIC{
        public String toString() {
            return "Epic";
        }
    },
    LEGENDARY{
        public String toString() {
            return "Legendary";
        }
    };

    public static Rarity value(String v) {
        if (v.equals("Common")) {
            return COMMON;
        }
        else if (v.equals("Rare")) {
            return RARE;
        }
        else if (v.equals("Epic")) {
            return EPIC;
        }else if (v.equals("Legendary")) {
            return LEGENDARY;
        }
        else{
            return COMMON;
        }
    }
}
