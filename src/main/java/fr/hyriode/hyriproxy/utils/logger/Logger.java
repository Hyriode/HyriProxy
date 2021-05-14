package fr.hyriode.hyriproxy.utils.logger;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private String loggerName;
    private File file;
    private PrintWriter writer;

    public Logger(String loggerName, File file){
        this.loggerName = loggerName.endsWith(" ") ? loggerName : loggerName + " ";
        this.file = file;
        if (this.file != null) {
            try {
                if (!this.file.exists()){
                    this.file.getParentFile().mkdirs();
                    this.file.createNewFile();
                }
                this.writer = new PrintWriter(this.file);
                Runtime.getRuntime().addShutdownHook(new Thread(this.writer::close));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void log(String message, LogType type){
        final String date = String.format("[%s] ", new SimpleDateFormat("dd/MM/yyyy kk:mm:ss").format(new Date()));
        String msg = date + this.loggerName + "[" + type.getName().toUpperCase() + "] " + message;

        System.out.println(msg);
        this.save(msg);
    }

    private void save(String message){
        if (this.file != null){
            try {
                if (!this.file.exists()) {
                    this.file.getParentFile().mkdirs();
                    this.file.createNewFile();
                }

                if (this.writer != null) {
                    this.writer.println(message);
                    this.writer.flush();
                }

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }
}
