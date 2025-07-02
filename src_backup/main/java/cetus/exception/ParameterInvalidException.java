

package cetus.exception;

import org.springframework.web.multipart.MaxUploadSizeExceededException;

import cetus.Response;

public class ParameterInvalidException extends CetusException {

    /**
     *
     */
    private static final long serialVersionUID = -2586358325579528007L;

    public static ResponseException CSRF = new MoveToBackException(Response.error(1000));

    public static MoveToBackException REQUIRED(String fieldLabel, String fieldName) {
        return new MoveToBackException(Response.error(fieldLabel, 1001, fieldName));
    }

    public static MoveToBackException MAX_LENGTH(String fieldLabel, String fieldName, int length) {
        return new MoveToBackException(Response.error(fieldLabel, 1002, fieldName, length));
    }

    public static MoveToBackException PATTERN(String fieldLabel, String fieldName, String name) {
        return new MoveToBackException(Response.error(fieldLabel, 1003, fieldName, name));
    }

    public static MoveToBackException RANGE(String fieldLabel, String fieldName, long min, long max) {
        return new MoveToBackException(Response.error(fieldLabel, 1004, fieldName, min, max));
    }

    public static MoveToBackException MAX_UPLOAD_SIZE_EXCEEDED(MaxUploadSizeExceededException e) {
        //String maxSize = FileUtil.toHumanSize(e.getMaxUploadSize());
        String maxSize = "";
        return new MoveToBackException(Response.error(1006, maxSize));
    }

    public static MoveToBackException LENGTH(String fieldLabel, String fieldName, int length) {
        return new MoveToBackException(Response.error(fieldLabel, 1007, fieldName, length));
    }

    public static MoveToBackException ONLY_NUMBER(String fieldLabel, String fieldName) {
        return new MoveToBackException(Response.error(fieldLabel, 1008, fieldName));
    }

    public static MoveToBackException DENY_FILE_EXTENSION(String fieldName) {
        return new MoveToBackException(Response.error(1009, fieldName));
    }

}