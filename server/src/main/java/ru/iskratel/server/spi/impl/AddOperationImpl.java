package ru.iskratel.server.spi.impl;

import ru.iskratel.server.model.Request;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.service.SessionService;
import ru.iskratel.server.spi.Operation;
import ru.iskratel.server.util.Session;

public class AddOperationImpl implements Operation {

    public String getName() {
        return "add";
    }

    public Response process(Request request) {
        Integer index = request.getIndex();
        if (request.getIndex() == null) {
            return new Response("Line index is required", null);
        }
        Session session = SessionService.getSession();
        long lastCommitId = session.getLastCommitId();
        if (lastCommitId == 0) {
            return new Response("You must read lines before add new line", null);
        }
        return null;
    }
}
