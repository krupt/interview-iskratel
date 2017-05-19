package ru.iskratel.server.spi.impl;

import ru.iskratel.server.model.Context;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.spi.Operation;

public class GetAllOperationImpl implements Operation {

    private static final String OPERATION_NAME = "getAll";

    public boolean isSupported(Context context) {
        return OPERATION_NAME.equalsIgnoreCase(context.getRequest().getOperationName())
                && context.getRequest().getIndex() == null;
    }

    public Response process(Context context) {
        return new Response(null, "asddsaadsadsdasdas");
    }
}
