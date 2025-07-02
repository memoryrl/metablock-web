package cetus;

import lombok.Getter;
import lombok.Setter;
import cetus.bean.Message;

@Getter
@Setter
public class Response {

    private int code = 0;
    private String message;
    private Object data;

    public static Response ok() {
        return new Response();
    }

    public static Response ok(Object data) {
        Response r = new Response();
        r.data = data;
        return r;
    }
    public static Response ok(Object data, String message) {
        Response r = new Response();
        r.message = message;
        r.data = data;
        return r;
    }
    public static Response ok(Object data, String message, int code) {
        Response r = new Response();
        r.message = message;
        r.data = data;
        r.code = code;
        return r;
    }

    public static Response error(int code) {
        String message = Message.response(code);
        return new Response(code, message, null);
    }

    public static Response error(int code, Object... args) {
        String message = Message.response(code, args);
        return new Response(code, message, null);
    }

    public static Response error(String message) {
        return new Response(-1, message, null);
    }

    public static Response error(Object data, int code, Object... args) {
        String message = Message.response(code, args);
        return new Response(code, message, data);
    }

    public static Response error(Exception e) {
        return new Response(-1, e.getMessage(), null);
    }

    private Response() { }
    
    private Response(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
