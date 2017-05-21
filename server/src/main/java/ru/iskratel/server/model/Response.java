package ru.iskratel.server.model;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class Response {

    private final String message;

    private final String content;

    public Response(String message) {
        this.message = message;
        content = null;
    }

    public Response(String message, String content) {
        this.message = message;
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Response{" +
                "message='" + message + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
