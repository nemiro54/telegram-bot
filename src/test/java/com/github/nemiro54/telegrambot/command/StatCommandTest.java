package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.dto.GroupStatDTO;
import com.github.nemiro54.telegrambot.dto.StatisticDTO;
import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import com.github.nemiro54.telegrambot.service.StatisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static com.github.nemiro54.telegrambot.command.AbstractCommandTest.prepareUpdate;
import static com.github.nemiro54.telegrambot.command.StatCommand.STAT_MESSAGE;
import static java.util.Collections.singletonList;

@DisplayName("Unit-level testing for StatCommand")
public class StatCommandTest {

    private SendBotMessageService sendBotMessageService;
    private StatisticService statisticService;
    private Command statCommand;

    @BeforeEach
    public void init() {
        sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        statisticService = Mockito.mock(StatisticService.class);
        statCommand = new StatCommand(sendBotMessageService, statisticService);
    }

    @Test
    public void shouldProperlySendStatMessage() {
//        given
        Long chatId = 23456L;
        GroupStatDTO groupStatDTO = new GroupStatDTO(1, "group", 1);
        StatisticDTO statisticDTO = new StatisticDTO(1, 1,  singletonList(groupStatDTO), 2.5);
        Mockito.when(statisticService.countBotStatistic()).thenReturn(statisticDTO);

//        when
        statCommand.execute(prepareUpdate(chatId, CommandName.STAT.getCommandName()));

//        then
        Mockito.verify(sendBotMessageService).sendMessage(String.valueOf(chatId), String.format(STAT_MESSAGE,
                statisticDTO.getActiveUserCount(),
                statisticDTO.getInactiveUserCount(),
                statisticDTO.getAverageGroupCountByUser(),
                String.format("%s (id = %s) - %s подписчиков", groupStatDTO.getTitle(), groupStatDTO.getId(), groupStatDTO.getActiveUserCount())));
    }
}
