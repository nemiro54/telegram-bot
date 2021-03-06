package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.javarushclient.JavaRushGroupClient;
import com.github.nemiro54.telegrambot.service.GroupSubService;
import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import com.github.nemiro54.telegrambot.service.StatisticService;
import com.github.nemiro54.telegrambot.service.TelegramUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static java.util.Collections.singletonList;

@DisplayName("Unit-level testing for CommandContainer")
class CommandContainerTest {
    private CommandContainer commandContainer;

    @BeforeEach
    public void init() {
        SendBotMessageService sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);
        JavaRushGroupClient javaRushGroupClient = Mockito.mock(JavaRushGroupClient.class);
        GroupSubService groupSubService = Mockito.mock(GroupSubService.class);
        StatisticService statisticService = Mockito.mock(StatisticService.class);
        commandContainer = new CommandContainer(sendBotMessageService, telegramUserService, javaRushGroupClient,
                groupSubService, statisticService, singletonList("username"));
    }

    @Test
    public void shouldGetAllTheExistingCommands() {
//        when-then
        Arrays.stream(CommandName.values())
                .forEach(commandName -> {
                    Command command = commandContainer.retrieveCommand(commandName.getCommandName(), "username");
                    Assertions.assertNotEquals(UnknownCommand.class, command.getClass());
                });
    }

    @Test
    public void shouldReturnUnknownCommand() {
//        given
        String unknownCommand = "/ghjk";

//        when
        Command command = commandContainer.retrieveCommand(unknownCommand, "username");

//        then
        Assertions.assertEquals(UnknownCommand.class, command.getClass());
    }
}