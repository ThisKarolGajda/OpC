package me.opkarol.opc.api.command.types;

import java.io.Serializable;

public class TypeKeys {

    enum StringKeys implements MetaDataKey<Integer> {
        S1(0),
        S2(1),
        S3(2),
        S4(3),
        S5(4),
        S6(5),
        S7(6),
        S8(7);

        private final Integer value;

        StringKeys(Integer value) {
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return value;
        }
    }

    enum IntegerKeys implements MetaDataKey<Integer> {
        I1(0),
        I2(1),
        I3(2),
        I4(3),
        I5(4),
        I6(5),
        I7(6),
        I8(7);
        private final Integer value;

        IntegerKeys(Integer value) {
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return value;
        }
    }

    public interface MetaDataKey<T extends Serializable> extends Serializable {
        T getValue();
    }
}