package ru.iskratel.server.spi.impl;

import ru.iskratel.server.model.Request;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.service.SessionService;
import ru.iskratel.server.spi.Operation;
import ru.iskratel.server.util.Session;

public class SaveOperationImpl implements Operation {

    public String getName() {
        return "save";
    }

    public Response process(Request request) {
        Session session = SessionService.getSession();
        return null;
    }
}
