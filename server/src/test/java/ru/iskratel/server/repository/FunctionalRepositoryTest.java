package ru.iskratel.server.repository;

import org.junit.Before;
import org.junit.Test;
import ru.iskratel.api.model.Request;
import ru.iskratel.server.service.SessionService;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class FunctionalRepositoryTest {

    private final InMemoryStorage<String> storage = new InMemoryStorage<>();

    @Before
    public void init() throws IllegalAccessException, NoSuchFieldException {
        SessionService.init(5, TimeUnit.MINUTES);
        Request request = new Request();
        request.setUsername("kovalev");
        SessionService.initSession(request);
        storage.init(Arrays.asList("One", "Two"));
    }

    @Test
    public void testChanging() {
        storage.add(2, "Three");
        assertEquals(true, storage.isRowChanged(2, 1));
        assertEquals(false, storage.isRowChanged(1, 1));
        assertEquals(false, storage.isRowChanged(0, 1));

        assertEquals(false, storage.isRowChanged(2, 2));
        assertEquals(false, storage.isRowChanged(1, 2));
        assertEquals(false, storage.isRowChanged(0, 2));

        storage.set(1, "Two!");
        assertEquals(false, storage.isRowChanged(2, 2));
        assertEquals(true, storage.isRowChanged(1, 2));
        assertEquals(false, storage.isRowChanged(0, 2));

        assertEquals(false, storage.isRowChanged(2, 3));
        assertEquals(false, storage.isRowChanged(1, 3));
        assertEquals(false, storage.isRowChanged(0, 3));
    }
}
