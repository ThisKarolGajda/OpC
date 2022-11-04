package me.opkarol.opc.api.commands.types;

public enum OPS8 implements IType {
    S1(1),
    S2(2),
    S3(3),
    S4(4),
    S5(5),
    S6(6),
    S7(7),
    S8(8);

    private final int i;

    OPS8(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }
}
