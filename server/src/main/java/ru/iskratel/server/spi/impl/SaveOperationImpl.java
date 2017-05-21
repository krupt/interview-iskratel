package ru.iskratel.server.spi.impl;

import ru.iskratel.server.Application;
import ru.iskratel.api.model.Request;
import ru.iskratel.api.model.Response;
import ru.iskratel.server.repository.InMemoryStorage;
import ru.iskratel.server.service.SessionService;
import ru.iskratel.server.spi.Operation;

public class SaveOperationImpl implements Operation {

    public String getName() {
        return "save";
    }

    public Response process(Request request) {
        final Integer index = request.getIndex();
        final long lastCommitId = SessionService.getSession().getLastCommitId();
        final InMemoryStorage<String> storage = Application.getInstance().getStorage();
        if (storage.isRowChanged(index, lastCommitId)) {
            return new Response("Content of line changed. New content is: ", storage.get(index));
        } else {
            storage.set(index, request.getContent());
            return new Response("Save successful", request.getContent());
        }
    }
}
