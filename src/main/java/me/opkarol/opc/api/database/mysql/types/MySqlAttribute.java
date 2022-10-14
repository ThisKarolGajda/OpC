package me.opkarol.opc.api.database.mysql.types;

public enum MySqlAttribute {
    NOTNULL(" NOT NULL"),
    PRIMARY(" PRIMARY KEY (%s)"),
    LENGTH(" (%s)"),
    IGNORE_IN_SEARCH("");

    private final String text;

    MySqlAttribute(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
