package ru.iskratel.server.spi.impl;

import ru.iskratel.server.Application;
import ru.iskratel.api.model.Request;
import ru.iskratel.api.model.Response;
import ru.iskratel.server.repository.InMemoryStorage;
import ru.iskratel.server.service.SessionService;
import ru.iskratel.server.spi.Operation;
import ru.iskratel.server.util.StringUtils;

public class SetOperationImpl implements Operation {

    public String getName() {
        return "set";
    }

    public Response process(Request request) {
        final Integer index = request.getIndex();
        final long lastCommitId = SessionService.getSession().getLastCommitId();
        final InMemoryStorage<String> storage = Application.getInstance().getStorage();
        if (storage.isRowChanged(index, lastCommitId)) {
            return new Response("Content of line doesn't changed. " + FAIL_MESSAGE_FOOTER,
                    StringUtils.joinWithIndexByNewLineCharacter(storage.getAll()));
        } else {
            storage.set(index, request.getContent());
            return new Response("Save successful", StringUtils.joinWithIndexByNewLineCharacter(storage.getAll()));
        }
    }
}
