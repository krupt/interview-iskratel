package ru.iskratel.server.spi.impl;

import ru.iskratel.server.model.Request;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.service.SessionService;
import ru.iskratel.server.spi.Operation;
import ru.iskratel.server.util.Session;

public class AddOperationImpl implements Operation {

    private static final String OPERATION_NAME = "add";

    public boolean isSupported(Request request) {
        return OPERATION_NAME.equalsIgnoreCase(request.getOperationName());
    }

    public Response process(Request request) {
        Integer index = request.getIndex();
        if (request.getIndex() == null) {
            return new Response("Line index is required", null);
        }
        Session session = SessionService.getSession();
        if (session.getLastRowId() != index) {
            return new Response("You can't change line that was not read", null);
        }

        return null;
    }
}
