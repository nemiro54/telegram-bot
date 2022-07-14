package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.repository.entity.GroupSub;
import com.github.nemiro54.telegrambot.repository.entity.TelegramUser;
import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import com.github.nemiro54.telegrambot.service.TelegramUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.nemiro54.telegrambot.command.CommandName.LIST_GROUP_SUB;
import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Unit-level testing for ListGroupSubCommand")
class ListGroupSubCommandTest {
    @Test
    public void shouldProperlyShowsListGroupSub() {
//        given
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setActive(true);
        telegramUser.setChatId("1");

        List<GroupSub> groupSubList = new ArrayList<>();
        groupSubList.add(populateGroupSub(1, "gs1"));
        groupSubList.add(populateGroupSub(2, "gs2"));
        groupSubList.add(populateGroupSub(3, "gs3"));
        groupSubList.add(populateGroupSub(4, "gs4"));

        telegramUser.setGroupSubs(groupSubList);

        SendBotMessageService sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserService.class);

        Mockito.when(telegramUserService.findByChatId(telegramUser.getChatId())).thenReturn(Optional.of(telegramUser));
        ListGroupSubCommand listGroupSubCommand = new ListGroupSubCommand(sendBotMessageService, telegramUserService);

        Update update = new Update();
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(Long.valueOf(telegramUser.getChatId()));
        Mockito.when(message.getText()).thenReturn(LIST_GROUP_SUB.getCommandName());
        update.setMessage(message);

        String collectedGroups = String.format("Вот группы, на которые ты подписан: \n\n%s",
                telegramUser.getGroupSubs().stream()
                        .map(it -> String.format("%s, ID = %s\n", it.getTitle(), it.getId()))
                        .collect(Collectors.joining()));

//        when
        listGroupSubCommand.execute(update);

//        then
        Mockito.verify(sendBotMessageService).sendMessage(telegramUser.getChatId(), collectedGroups);
    }

    private GroupSub populateGroupSub(Integer id, String title) {
        GroupSub groupSub = new GroupSub();
        groupSub.setId(id);
        groupSub.setTitle(title);
        return groupSub;
    }
}