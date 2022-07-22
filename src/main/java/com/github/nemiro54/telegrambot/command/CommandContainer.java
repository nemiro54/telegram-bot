package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.command.annotation.AdminCommand;
import com.github.nemiro54.telegrambot.javarushclient.JavaRushGroupClient;
import com.github.nemiro54.telegrambot.service.GroupSubService;
import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import com.github.nemiro54.telegrambot.service.TelegramUserService;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Objects;

import static com.github.nemiro54.telegrambot.command.CommandName.*;
import static java.util.Objects.nonNull;

/**
 * Container of the {@link Command}'s which are using for handling telegram bot commands.
 */
public class CommandContainer {
    private final ImmutableMap<String, Command> commandImmutableMap;
    private final Command unknownCommand;
    private final List<String> admins;

    public CommandContainer(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService,
                            JavaRushGroupClient javaRushGroupClient, GroupSubService groupSubService,
                            List<String> admins) {
        this.admins = admins;
        commandImmutableMap = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), new StartCommand(sendBotMessageService, telegramUserService))
                .put(STOP.getCommandName(), new StopCommand(sendBotMessageService, telegramUserService))
                .put(HELP.getCommandName(), new HelpCommand(sendBotMessageService))
                .put(ADMIN_HELP.getCommandName(), new AdminHelpCommand(sendBotMessageService))
                .put(NO.getCommandName(), new NoCommand(sendBotMessageService))
                .put(STAT.getCommandName(), new StatCommand(sendBotMessageService, telegramUserService))
                .put(ADD_GROUP_SUB.getCommandName(),
                        new AddGroupSubCommand(sendBotMessageService, javaRushGroupClient, groupSubService))
                .put(LIST_GROUP_SUB.getCommandName(),
                        new ListGroupSubCommand(sendBotMessageService, telegramUserService))
                .put(DELETE_GROUP_SUB.getCommandName(),
                        new DeleteGroupSubCommand(sendBotMessageService, telegramUserService, groupSubService))
                .build();

        unknownCommand = new UnknownCommand(sendBotMessageService);
    }

    public Command retrieveCommand(String commandIdentifier, String userName) {
        Command orDefault = commandImmutableMap.getOrDefault(commandIdentifier, unknownCommand);
        if (isAdminCommand(Objects.requireNonNull(orDefault))) {
            if (admins.contains(userName)) {
                return orDefault;
            } else {
                return unknownCommand;
            }
        }
        return orDefault;
    }

    private boolean isAdminCommand(Command command) {
        return nonNull(command.getClass().getAnnotation(AdminCommand.class));
    }
}
