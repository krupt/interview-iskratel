package ru.iskratel.server.spi.impl;

import ru.iskratel.server.Application;
import ru.iskratel.api.model.Request;
import ru.iskratel.api.model.Response;
import ru.iskratel.server.spi.Operation;
import ru.iskratel.server.util.StringUtils;

import java.util.Collection;

public class GetAllOperationImpl implements Operation {

    public String getName() {
        return "getAll";
    }

    public Response process(Request request) {
        Collection<String> lines = Application.getInstance().getStorage().getAll();
        return new Response(null, StringUtils.joinWithIndexByNewLineCharacter(lines));
    }

    @Override
    public boolean isIndexRequired() {
        return false;
    }

    @Override
    public boolean isReadRequired() {
        return false;
    }
}
