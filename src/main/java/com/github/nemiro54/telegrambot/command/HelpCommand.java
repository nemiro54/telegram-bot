package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.nemiro54.telegrambot.command.CommandName.*;

/**
 * Help {@link Command}.
 */
public class HelpCommand implements Command {

    private final SendBotMessageService sendBotMessageService;

    public static final String HELP_MESSAGE = String.format("""
            <b> Доступные команды</b>
            
            <b> Начать/закончить работу с ботом:</b>
            %s - начать работу
            %s - приостановить работу
            
            <b> Действия с подписками: </b>
            %s - подписаться на группу статей
            %s - отписаться от группы статей
            %s - получить список групп, на которые подписан
            
            %s - получить помощь в работе""",
            START.getCommandName(), STOP.getCommandName(), ADD_GROUP_SUB.getCommandName(),
            DELETE_GROUP_SUB.getCommandName(), LIST_GROUP_SUB.getCommandName(), HELP.getCommandName());

    public HelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), HELP_MESSAGE);
    }
}
