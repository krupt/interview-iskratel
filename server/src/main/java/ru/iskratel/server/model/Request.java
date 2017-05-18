package ru.iskratel.server.model;

@SuppressWarnings("unused")
public class Request {

    private String username;

    private String content;

    private int index;

    private Operation operation;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "Request{" +
                "username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", index=" + index +
                ", operation=" + operation +
                '}';
    }
}
