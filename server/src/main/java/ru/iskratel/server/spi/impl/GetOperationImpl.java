package ru.iskratel.server.spi.impl;

import ru.iskratel.server.Application;
import ru.iskratel.server.model.Context;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.repository.Row;
import ru.iskratel.server.spi.Operation;

import java.util.UUID;

public class GetOperationImpl implements Operation {

    private static final String OPERATION_NAME = "get";

    public boolean isSupported(Context context) {
        return OPERATION_NAME.equalsIgnoreCase(context.getRequest().getOperationName())
                && context.getRequest().getIndex() != null;
    }

    public Response process(Context context) {
        Row<String, UUID> row = Application.getInstance().getStorage().get(context.getRequest().getIndex());
        return new Response(null, row.getContent());
    }
}
