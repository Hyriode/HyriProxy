package fr.hyriode.hyriproxy.utils.logger;

public class LogType {

    public static final LogType INFO = new LogType("info");
    public static final LogType WARN = new LogType("warn");
    public static final LogType ERROR = new LogType("error");

    private final String name;

    public LogType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
