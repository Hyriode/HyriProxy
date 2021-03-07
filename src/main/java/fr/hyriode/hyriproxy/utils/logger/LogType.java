package fr.hyriode.hyriproxy.utils.logger;

public class LogType {

    public static final LogType INFO = new LogType("info", null);
    public static final LogType WARN = new LogType("warn", LogColor.YELLOW);
    public static final LogType ERROR = new LogType("error", LogColor.RED);

    private String name;
    private LogColor color;

    public LogType(String name, LogColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LogColor getColor() {
        return color;
    }

    public void setColor(LogColor color) {
        this.color = color;
    }
}
