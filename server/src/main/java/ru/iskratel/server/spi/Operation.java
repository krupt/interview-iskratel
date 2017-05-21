package ru.iskratel.server.spi;

import ru.iskratel.api.model.Request;
import ru.iskratel.api.model.Response;

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
