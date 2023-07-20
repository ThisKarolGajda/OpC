package me.opkarol.opc.api.misc.listeners;

import java.io.Serializable;

public interface IListener extends Serializable {

    void runListener();

    void stopListener();

}