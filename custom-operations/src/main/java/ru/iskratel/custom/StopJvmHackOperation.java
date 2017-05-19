package ru.iskratel.custom;

import ru.iskratel.server.model.Context;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.spi.Operation;

public class StopJvmHackOperation implements Operation {

    private static final String OPERATION_NAME = "crash";

    public boolean isSupported(Context context) {
        return OPERATION_NAME.equalsIgnoreCase(context.getRequest().getOperationName());
    }

    public Response process(Context context) {
        System.exit(1);
        return null;
    }
}
