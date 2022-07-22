package com.github.nemiro54.telegrambot.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.nemiro54.telegrambot.command.CommandName.ADMIN_HELP;
import static com.github.nemiro54.telegrambot.command.AdminHelpCommand.ADMIN_HELP_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit-level testing for AdminHelpCommand")
class AdminHelpCommandTest extends AbstractCommandTest {
    @Override
    String getCommandName() {
        return ADMIN_HELP.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return ADMIN_HELP_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new AdminHelpCommand(sendBotMessageService);
    }
}