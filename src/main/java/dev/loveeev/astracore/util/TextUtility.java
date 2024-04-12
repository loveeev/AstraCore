package dev.loveeev.astracore.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Zimoxy DEV: loveeev
 */

@UtilityClass
public class TextUtility {


    public String colorize(String message) {
        if (message == null) {
            return null;
        }
        return translateColorCodes(message);
    }


    public List<String> colorize(Collection<String> lines) {
        List<String> coloredLines = new ArrayList<>();
        for (String line : lines) {
            coloredLines.add(colorize("&7" + line));
        }
        return coloredLines;
    }


    public String uncolorize(String message) {
        return message == null ? null : ChatColor.stripColor(colorize(message));
    }


    public List<String> uncolorize(Collection<String> lines) {
        List<String> uncoloredLines = new ArrayList<>();
        for (String line : lines) {
            uncoloredLines.add(uncolorize(line));
        }
        return uncoloredLines;
    }



        @SneakyThrows(ArrayIndexOutOfBoundsException.class)
        public String translateColorCodes(String text) {
            String[] texts = text.split(String.format("((?<=%1$s)|(?=%1$s))", "&"));
            StringBuilder finalText = new StringBuilder();
            for (int i = 0; i < texts.length; i++) {
                if (texts[i].equalsIgnoreCase("&")) {
                    i++;
                    if (texts[i].charAt(0) == '#') {
                        finalText.append(ChatColor.of(texts[i].substring(0, 7))).append(texts[i].substring(7));
                    } else {
                        finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                    }
                    continue;
                }
                finalText.append(texts[i]);
            }
            return finalText.toString();
        }


    public String getMultilineMessage(Collection<String> message) {
        StringBuilder builder = new StringBuilder();
        boolean firstLine = true;
        for (String line : message) {
            if (line.isEmpty()) {
                line = " ";
            }
            if (!firstLine) {
                builder.append("\n§r").append(line);
            } else {
                firstLine = false;
                builder.append(line);
            }
        }
        return builder.toString();
    }


    public String getCenteredColoredMessage(String message) {
        int maxWidth = 80, spaces = (int) Math.round((maxWidth - 1.4 * ChatColor.stripColor(TextUtility.colorize(message)).length()) / 2);
        return StringUtils.repeat(" ", spaces) + TextUtility.colorize(message);
    }
}