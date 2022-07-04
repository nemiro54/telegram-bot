package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Start {@link Command}.
 */
public class StartCommand  implements Command {

    private final SendBotMessageService sendBotMessageService;

    public final static String START_MESSAGE = "Привет! Это первая реализация телеграм-бота";

    public StartCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), START_MESSAGE);
    }
}
