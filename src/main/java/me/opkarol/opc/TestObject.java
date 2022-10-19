package me.opkarol.opc;

import me.opkarol.opc.api.database.mysql.reflection.*;

@OpMTable(name = "test2")
public class TestObject {
    @OpMValue
    @OpMIdentificationObject
    private int id;

    @OpMConstructor
    public TestObject(int id) {
        this.id = id;
    }

    @OpMIdentification
    public void setId(int id) {
        this.id = id;
    }
}
