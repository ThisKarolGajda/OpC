package me.opkarol.opc.api.gui.misc;

public enum OpInventoryRows {
    ROWS_1(9),
    ROWS_2(18),
    ROWS_3(27),
    ROWS_4(36),
    ROWS_5(45),
    ROWS_6(54);

    private final int i;

    OpInventoryRows(int i) {
        this.i = i;
    }

    public int getSize() {
        return i;
    }
}
