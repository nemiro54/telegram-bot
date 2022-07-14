package com.github.nemiro54.telegrambot.command;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Utils class for Commands.
 */
public class CommandUtils {

    /**
     * Get ChatID from {@link Update} object
     *
     * @param update provided {@link Update}
     * @return ChatID from the provided {@link Update} object
     */
    public static String getChatId(Update update) {
        return String.valueOf(update.getMessage().getChatId());
    }

    /**
     * Get text of the Message from {@link Update} object
     * @param update provided {@link Update}
     * @return the text of the Message from the provided {@link Update} object
     */
    public static String getMessage(Update update) {
        return update.getMessage().getText();
    }
}
