package ru.iskratel.server.spi.impl;

import ru.iskratel.server.Application;
import ru.iskratel.server.model.Request;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.repository.InMemoryStorage;
import ru.iskratel.server.service.SessionService;
import ru.iskratel.server.spi.Operation;
import ru.iskratel.server.util.StringUtils;

public class DeleteOperationImpl implements Operation {

    public String getName() {
        return "delete";
    }

    public Response process(Request request) {
        final Integer index = request.getIndex();
        final long lastCommitId = SessionService.getSession().getLastCommitId();
        final InMemoryStorage<String> storage = Application.getInstance().getStorage();
        if (storage.isRowChanged(index, lastCommitId)) {
            return new Response("Content of line changed. New content is: ", storage.get(index));
        } else {
            storage.remove(index);
            return new Response("Delete successful", StringUtils.joinWithIndexByNewLineCharacter(storage.getAll()));
        }
    }
}
