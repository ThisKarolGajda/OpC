package me.opkarol.opc.api.database.mysql.types;

public record MySqlVariable(String name,
                            MySqlVariableType variableName) {

    @Override
    public String toString() {
        return name();
    }
}
