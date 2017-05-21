package ru.iskratel.server.spi.impl;

import ru.iskratel.server.Application;
import ru.iskratel.server.model.Request;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.repository.InMemoryStorage;
import ru.iskratel.server.repository.Row;
import ru.iskratel.server.service.SessionService;
import ru.iskratel.server.spi.Operation;
import ru.iskratel.server.util.Session;

public class GetOperationImpl implements Operation {

    private static final String OPERATION_NAME = "get";

    public boolean isSupported(Request request) {
        return OPERATION_NAME.equalsIgnoreCase(request.getOperationName());
    }

    public Response process(Request request) {
        Integer index = request.getIndex();
        if (index == null) {
            return new Response("Line index is required", null);
        }
        InMemoryStorage<String> storage = Application.getInstance().getStorage();
        Row<String> row = storage.get(index);
        Session session = SessionService.getSession();
        session.setLastRowId(index);
        return new Response(null, row.getContent());
    }
}
