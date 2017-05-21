package ru.iskratel.custom;

import ru.iskratel.server.model.Request;
import ru.iskratel.server.model.Response;
import ru.iskratel.server.spi.Operation;

public class StopJvmHackOperation implements Operation {

    public String getName() {
        return "crash";
    }

    public Response process(Request request) {
        System.exit(1);
        return null;
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
