package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.repository.entity.GroupSub;
import com.github.nemiro54.telegrambot.repository.entity.TelegramUser;
import com.github.nemiro54.telegrambot.service.GroupSubService;
import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import com.github.nemiro54.telegrambot.service.TelegramUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Optional;

import static com.github.nemiro54.telegrambot.command.AbstractCommandTest.prepareUpdate;
import static com.github.nemiro54.telegrambot.command.CommandName.DELETE_GROUP_SUB;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit-level testing for DeleteGroupSubCommand")
class DeleteGroupSubCommandTest {

    private Command command;
    private SendBotMessageService sendBotMessageService;
    GroupSubService groupSubService;
    TelegramUserService telegramUserService;

    @BeforeEach
    public void init() {
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        groupSubService = Mockito.mock(GroupSubService.class);
        telegramUserService = Mockito.mock(TelegramUserService.class);

        command = new DeleteGroupSubCommand(sendBotMessageService, telegramUserService, groupSubService);
    }

    @Test
    public void shouldProperlyReturnSubscriptionsList() {
//        given
        Long chatId = 23456L;
        Update update = prepareUpdate(chatId, DELETE_GROUP_SUB.getCommandName());
        TelegramUser telegramUser = new TelegramUser();
        GroupSub groupSub = new GroupSub();
        groupSub.setId(123);
        groupSub.setTitle("GS1 Title");
        telegramUser.setGroupSubs(singletonList(groupSub));
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId)))
                .thenReturn(Optional.of(telegramUser));

        String expectedMessage = String.format("""
                    Чтобы удалить подписку на группу - передай комадну вместе с ID группы.\s
                    Например: /deleteGroupSub 16\s

                    Я подготовил список всех групп, на которые ты подписан:)\s

                    имя группы - ID группы\s

                    %s - %s \n""", groupSub.getTitle(), groupSub.getId());

//        when
        command.execute(update);

//        then
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }

    @Test
    public void shouldRejectByInvalidGroup() {
//        given
        Long chatId = 23456L;
        Update update = prepareUpdate(chatId, String.format("%s %s", DELETE_GROUP_SUB.getCommandName(), "groupSubId"));
        TelegramUser telegramUser = new TelegramUser();
        GroupSub groupSub = new GroupSub();
        groupSub.setId(123);
        groupSub.setTitle("GS1 Title");
        telegramUser.setGroupSubs(singletonList(groupSub));
        Mockito.when(telegramUserService.findByChatId(String.valueOf(chatId)))
                .thenReturn(Optional.of(telegramUser));

        String expectedMessage = """
                    Неправильный формат ID группы.
                    ID должен быть целым положительным числом.""";

//        when
        command.execute(update);

//        then
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }

    @Test
    public void shouldProperlyDeleteByGroupId() {
//        given
        Long chatId = 23456L;
        Integer groupId = 1234;
        Update update = prepareUpdate(chatId, String.format("%s %s", DELETE_GROUP_SUB.getCommandName(), groupId));

        GroupSub groupSub = new GroupSub();
        groupSub.setId(123);
        groupSub.setTitle("GS1 Title");
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setChatId(chatId.toString());
        telegramUser.setGroupSubs(singletonList(groupSub));
        ArrayList<TelegramUser> users = new ArrayList<>();
        users.add(telegramUser);
        groupSub.setUsers(users);
        Mockito.when(groupSubService.findById(groupId)).thenReturn(Optional.of(groupSub));
        Mockito.when(telegramUserService.findByChatId(chatId.toString()))
                .thenReturn(Optional.of(telegramUser));

        String expectedMessage = "Удалил подписку на группу GS1 Title";

//        when
        command.execute(update);

//        then
        users.remove(telegramUser);
        Mockito.verify(groupSubService).save(groupSub);
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }

    @Test
    public void shouldDoesNotExistsByGroupId() {
//        given
        Long chatId = 23456L;
        Integer groupId = 123;
        Update update = prepareUpdate(chatId, String.format("%s %s", DELETE_GROUP_SUB.getCommandName(), groupId));

        Mockito.when(groupSubService.findById(groupId)).thenReturn(Optional.empty());

        String expectedMessage = "Не нашел такую группу :(";

//        when
        command.execute(update);

//        then
        Mockito.verify(groupSubService).findById(groupId);
        Mockito.verify(sendBotMessageService).sendMessage(chatId.toString(), expectedMessage);
    }
}