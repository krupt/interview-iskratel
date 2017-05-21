package ru.iskratel.server.spi;

import ru.iskratel.server.model.Request;
import ru.iskratel.server.model.Response;

public interface Operation {

    String getName();

    Response process(Request request);

    /**
     * Is line index required for execute operation
     */
    default boolean isIndexRequired() {
        return true;
    }

    /**
     * Is any read operation required before execute operation
     */
    default boolean isReadRequired() {
        return true;
    }
}
