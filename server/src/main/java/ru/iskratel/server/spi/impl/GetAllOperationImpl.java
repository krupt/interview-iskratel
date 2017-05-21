package ru.iskratel.server.spi.impl;

import ru.iskratel.server.Application;
import ru.iskratel.server.model.Request;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.spi.Operation;

import java.util.Collection;
import java.util.stream.Collectors;

public class GetAllOperationImpl implements Operation {

    public String getName() {
        return "getAll";
    }

    public Response process(Request request) {
        Collection<String> all = Application.getInstance().getStorage().getAll();
        int[] index = {0};
        String content = all.stream()
                .map(line -> index[0]++ + ". " + line)
                .collect(Collectors.joining("\n"));
        return new Response(null, content);
    }
}
