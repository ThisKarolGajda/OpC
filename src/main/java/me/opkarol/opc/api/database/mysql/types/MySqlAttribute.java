package me.opkarol.opc.api.database.mysql.types;

public enum MySqlAttribute {
    NOTNULL(" NOT NULL"),
    PRIMARY(" PRIMARY KEY (%s)"),
    LENGTH(" (%s)"),
    IGNORE_IN_SEARCH(""),
    IGNORE_IN_ALL_SEARCH(""),
    CHARACTER_SET(" CHARACTER SET utf8 COLLATE utf8_unicode_ci");

    private final String text;

    MySqlAttribute(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
