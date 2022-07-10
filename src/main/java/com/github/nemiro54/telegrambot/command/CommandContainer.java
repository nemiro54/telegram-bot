package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import com.github.nemiro54.telegrambot.service.TelegramUserService;
import com.google.common.collect.ImmutableMap;

import static com.github.nemiro54.telegrambot.command.CommandName.*;

/**
 * Container of the {@link Command}'s which are using for handling telegram bot commands.
 */
public class CommandContainer {
    private final ImmutableMap<String, Command> commandImmutableMap;
    private final Command unknownCommand;

    public CommandContainer(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        commandImmutableMap = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, telegramUserService))
                .put(STOP.getCommandName(), new StopCommand(sendBotMessageService, telegramUserService))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(STAT.getCommandName(), new StatCommand(sendBotMessageService, telegramUserService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandImmutableMap.getOrDefault(commandIdentifier, unknownCommand);
    }
}
