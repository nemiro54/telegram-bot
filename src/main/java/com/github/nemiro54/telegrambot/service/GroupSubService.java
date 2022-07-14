package com.github.nemiro54.telegrambot.service;

import com.github.nemiro54.telegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.nemiro54.telegrambot.repository.entity.GroupSub;

/**
 * Service for manipulating with {@link GroupSub}.
 */
public interface GroupSubService {

    GroupSub save(String chatId, GroupDiscussionInfo groupDiscussionInfo);
}
