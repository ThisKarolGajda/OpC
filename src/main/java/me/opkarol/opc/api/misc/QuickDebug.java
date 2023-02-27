package me.opkarol.opc.api.misc;

import me.opkarol.opc.OpAPI;

public class QuickDebug {

    public QuickDebug(Exception exception) {
        StackTraceElement traceElement = exception.getStackTrace()[0];
        OpAPI.logInfo(traceElement.getClassName() + "/" + traceElement.getMethodName() + ":" + traceElement.getLineNumber());
    }
}
