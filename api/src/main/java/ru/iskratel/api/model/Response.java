package ru.iskratel.api.model;

public class Response {

    private String message;

    private String content;

    public Response() {
    }

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

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Response{" +
                "message='" + message + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
