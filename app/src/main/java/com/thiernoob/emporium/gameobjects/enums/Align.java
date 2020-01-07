package com.thiernoob.emporium.gameobjects.enums;

public enum Align {

    CHAOTIC {
        public String toString() {
            return "Chaotic";
        }
    },

    NEUTRAL{
        public String toString() {
            return "Neutral";
        }
    },

    LAWFUL {
        public String toString() {
            return "Lawful";
        }
    };


    public static Align value(String v) {
        if (v.equals("Neutral")) {
            return NEUTRAL;
        }
        else if (v.equals("Lawful")) {
            return LAWFUL;
        }
        else if (v.equals("Chaotic")) {
            return CHAOTIC;
        }
        else{
            return NEUTRAL;
        }
    }

}
