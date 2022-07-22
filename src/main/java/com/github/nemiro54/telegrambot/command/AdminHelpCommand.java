package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.nemiro54.telegrambot.command.CommandName.STAT;

/**
 * Admin Help {@link Command}
 */
public class AdminHelpCommand implements Command {

    public static final String ADMIN_HELP_MESSAGE = String.format("""
            <b>Доступные команды админа:</b>
            
            <b>Получить статистику</b>
            %s - статистика бота""", STAT.getCommandName());

    private final SendBotMessageService sendBotMessageService;

    public AdminHelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), ADMIN_HELP_MESSAGE);
    }
}
