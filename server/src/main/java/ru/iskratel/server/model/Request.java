package ru.iskratel.server.model;

import java.util.Objects;

@SuppressWarnings("unused")
public class Request {

    private String username;

    private String content;

    private Integer index;

    private String operationName;

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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Request request = (Request) o;
        return Objects.equals(username, request.username) &&
                Objects.equals(content, request.content) &&
                Objects.equals(index, request.index) &&
                Objects.equals(operationName, request.operationName);
    }

    @Override
    public String toString() {
        return "Request{" +
                "username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", index=" + index +
                ", operationName=" + operationName +
                '}';
    }
}
