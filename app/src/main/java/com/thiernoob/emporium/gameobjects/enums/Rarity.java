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
    }
}
