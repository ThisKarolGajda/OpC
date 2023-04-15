package me.opkarol.opc.api.database.mysql.types;

public record SqlVariable(String name,
                          SqlVariableType variableName) {

    @Override
    public String toString() {
        return name();
    }
}
