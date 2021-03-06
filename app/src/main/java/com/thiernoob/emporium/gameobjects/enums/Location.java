package com.thiernoob.emporium.gameobjects.enums;

public enum Location {

    ALETHKAR {
        public String toString() {
            return "Alethkar";
        }
    },
    JAH_KEVED{
        public String toString() {
            return "Jah Keved";
        }
    },
    AZIR{
        public String toString() {
            return "Azir";
        }
    },
    NARAK{
        public String toString() {
            return "Narak";
        }
    },
    RESHI{
        public String toString() {
            return "Reshi";
        }
    },
    SHINOVAR{
        public String toString() {
            return "Shinovar";
        }
    },
    AIMIA{
        public String toString() {
            return "Aimia";
        }
    },
    BONTA{
        public String toString() {
            return "Bonta";
        }
    },
    BRAKMAR{
        public String toString() {
            return "Brakmar";
        }
    },
    ASTRUB{
        public String toString() {
            return "Astrub";
        }
    },
    TRAVELING{
        public String toString() {
            return "Traveling";
        }
    };

    public static Location value(String v) {
        if (v.equals("Aimia")) {
            return AIMIA;
        }
        else if (v.equals("Reshi")) {
            return RESHI;
        }
        else if (v.equals("Shinovar")) {
            return SHINOVAR;
        }
        else if (v.equals("Traveling")) {
            return TRAVELING;
        }
        else if (v.equals("Jah Keved")) {
            return JAH_KEVED;
        }
        else if (v.equals("Alethkar")) {
            return ALETHKAR;
        }
        else if (v.equals("Azir")) {
            return AZIR;
        }
        else if (v.equals("Narak")) {
            return NARAK;
        }
        else if (v.equals("Bonta")) {
            return BONTA;
        }
        else if (v.equals("Brakmar")) {
            return BRAKMAR;
        }
        else if (v.equals("Astrub")) {
            return ASTRUB;
        }
        else{
            return AZIR;
        }
    }
}
