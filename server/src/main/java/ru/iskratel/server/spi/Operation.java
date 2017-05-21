package ru.iskratel.server.spi;

import ru.iskratel.server.model.Request;
import ru.iskratel.server.model.Response;

public interface Operation {

    boolean isSupported(Request request);

    Response process(Request request);
}
