package cetus.log;

import lombok.Setter;
import lombok.ToString;
import cetus.util.StringUtil;

@Setter @ToString
public class Logging {
    private boolean request;
    private boolean uri;
    private boolean params;
    private boolean controller;
    private boolean sql;
    private boolean view;
    private boolean elapsed;
    private String excludeUrls[];
    private String excludeParams[];

    public Logging() {
        logging = this;
    }

    public void setExcludeUrls(String urls) {
        if(StringUtil.isNotEmpty(urls)) {
            this.excludeUrls = StringUtil.commaToArray(urls);
        }
    }

    public void setExcludeParams(String params) {
        if(StringUtil.isNotEmpty(params)) {
            this.excludeParams = StringUtil.commaToArray(params);
        }
    }

    private static Logging logging;

    public static boolean isRequest() {
        return logging.request;
    }
    public static boolean isUri() {
        return logging.uri;
    }
    public static boolean isParams() {
        return logging.params;
    }
    public static boolean isController() {
        return logging.controller;
    }
    public static boolean isSql() {
        return logging.sql;
    }
    public static boolean isView() {
        return logging.view;
    }
    public static boolean isElapsed() {
        return logging.elapsed;
    }
    public static String[] getExcludeUrls() {
        return logging.excludeUrls;
    }
    public static String[] getExcludeParams() {
        return logging.excludeParams;
    }

    public static void all() {
        logging = new Logging();
        logging.setUri(true);
        logging.setRequest(true);
        logging.setSql(true);
        logging.setController(true);
        logging.setParams(true);
        logging.setElapsed(true);
        logging.setView(true);
    }

}
