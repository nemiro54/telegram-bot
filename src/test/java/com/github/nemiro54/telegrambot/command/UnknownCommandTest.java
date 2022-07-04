package com.github.nemiro54.telegrambot.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.nemiro54.telegrambot.command.UnknownCommand.UNKNOWN_MESSAGE;

@DisplayName("Unit-level testing for UnknownCommand")
public class UnknownCommandTest extends AbstractCommandTest {
    @Override
    String getCommandName() {
        return "/xfghjas";
    }

    @Override
    String getCommandMessage() {
        return UNKNOWN_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new UnknownCommand(sendBotMessageService);
    }
}
