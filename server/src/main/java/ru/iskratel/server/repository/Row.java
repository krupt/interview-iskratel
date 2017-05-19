package ru.iskratel.server.repository;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Objects;

@ThreadSafe
@Immutable
public class Row<T, U> {

    private final T content;

    private final U sign;

    public Row(T content, U sign) {
        this.content = content;
        this.sign = sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Row row = (Row) o;
        return Objects.equals(content, row.content) &&
                Objects.equals(sign, row.sign);
    }

    public T getContent() {
        return content;
    }

    public U getSign() {
        return sign;
    }
}
