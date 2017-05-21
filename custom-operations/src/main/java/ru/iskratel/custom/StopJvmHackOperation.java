package ru.iskratel.custom;

import ru.iskratel.server.model.Request;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.spi.Operation;

public class StopJvmHackOperation implements Operation {

    private static final String OPERATION_NAME = "crash";

    public boolean isSupported(Request request) {
        return OPERATION_NAME.equalsIgnoreCase(request.getOperationName());
    }

    public Response process(Request request) {
        System.exit(1);
        return null;
    }
}
