package ru.iskratel.server.spi;

import ru.iskratel.server.model.Context;
import ru.iskratel.server.model.Response;

public interface Operation {

    boolean isSupported(Context context);

    Response process(Context context);
}
