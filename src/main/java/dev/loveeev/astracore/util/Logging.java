package dev.loveeev.astracore.util;


import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

/**
 * @author Zimoxy DEV: loveeev
 */
@UtilityClass
public class Logging {

    private static final String PREFIX = "[AstraWorld]";
    private static final ConsoleCommandSender console = Bukkit.getConsoleSender();

    public void debug(String message) {
        withExtra("[DEBUG]", message);
    }

    public void error(String message) {
        withExtra("[ERROR]", message);
    }

    public void warning(String message) {
        withExtra("[WARN]", message);
    }

    public void info(String message) {
        withExtra("[INFO]", message);
    }

    public void withExtra(String extra, String message) {
        console.sendMessage(PREFIX + " " + extra + " => " + message);
    }

    public void log(String message) {
        console.sendMessage(PREFIX + " | " + message);
    }

}