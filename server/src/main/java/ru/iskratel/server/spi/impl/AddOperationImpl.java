package ru.iskratel.server.spi.impl;

import ru.iskratel.server.Application;
import ru.iskratel.api.model.Request;
import ru.iskratel.api.model.Response;
import ru.iskratel.server.repository.InMemoryStorage;
import ru.iskratel.server.service.SessionService;
import ru.iskratel.server.spi.Operation;
import ru.iskratel.server.util.StringUtils;

public class AddOperationImpl implements Operation {

    public String getName() {
        return "add";
    }

    public Response process(Request request) {
        final Integer index = request.getIndex();
        final long lastCommitId = SessionService.getSession().getLastCommitId();
        final InMemoryStorage<String> storage = Application.getInstance().getStorage();
        if (storage.isStructChanged(index, lastCommitId)) {
            return new Response("Content of lines changed. New content is: ",
                    StringUtils.joinWithIndexByNewLineCharacter(storage.getAll())
            );
        } else {
            storage.add(index, request.getContent());
            return new Response("Add successful", StringUtils.joinWithIndexByNewLineCharacter(storage.getAll()));
        }
    }
}
