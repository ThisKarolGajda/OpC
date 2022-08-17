package me.opkarol.opc.api.database.mysql;

public enum MySqlAttribute {
    NOTNULL(" NOT NULL"),
    PRIMARY(" PRIMARY KEY (%s)"),
    LENGTH(" (%s)"),
    AUTOINCREMENT(" AUTO INCREMENT"),
    ;

    private final String text;

    MySqlAttribute(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
