package com.github.nemiro54.telegrambot.service;

import com.github.nemiro54.telegrambot.javarushclient.JavaRushPostClient;
import com.github.nemiro54.telegrambot.javarushclient.dto.PostInfo;
import com.github.nemiro54.telegrambot.repository.entity.GroupSub;
import com.github.nemiro54.telegrambot.repository.entity.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link FindNewPostsService} interface.
 */
@Service
public class FindNewPostsServiceImpl implements FindNewPostsService {

    private static final String JAVARUSH_WEB_POST_FORMAT = "https://javarush.ru/groups/posts/%s";

    private final GroupSubService groupSubService;
    private final JavaRushPostClient javaRushPostClient;
    private final SendBotMessageService sendBotMessageService;

    @Autowired
    public FindNewPostsServiceImpl(GroupSubService groupSubService,
                                   JavaRushPostClient javaRushPostClient,
                                   SendBotMessageService sendBotMessageService) {
        this.groupSubService = groupSubService;
        this.javaRushPostClient = javaRushPostClient;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void findNewPosts() {
        groupSubService.findAll().forEach(groupSub -> {
            List<PostInfo> newPosts = javaRushPostClient.findNewPosts(groupSub.getId(), groupSub.getLastPostId());
            setNewLastPostId(groupSub, newPosts);
            notifySubscribersAboutNewPosts(groupSub, newPosts);
        });
    }

    private void notifySubscribersAboutNewPosts(GroupSub groupSub, List<PostInfo> newPosts) {
        Collections.reverse(newPosts);
        List<String> messagesWithNewPosts = newPosts.stream()
                .map(post -> String.format("""
                                Вышла новая статья <b>%s</b> в группе <b>%s</b>.
                                                        
                                Описание: <b>%s</b>
                                
                                Ссылка: <b>%s</b>""",
                        post.getTitle(), groupSub.getTitle(), post.getDescription(), getPostUrl(post.getKey())))
                .toList();

        groupSub.getUsers().stream()
                .filter(TelegramUser::isActive)
                .forEach(it -> sendBotMessageService.sendMessage(it.getChatId(), messagesWithNewPosts));
    }

    private void setNewLastPostId(GroupSub groupSub, List<PostInfo> newPosts) {
        newPosts.stream()
                .mapToInt(PostInfo::getId)
                .max()
                .ifPresent(id -> {
                    groupSub.setLastPostId(id);
                    groupSubService.save(groupSub);
                });
    }

    private String getPostUrl(String key) {
        return String.format(JAVARUSH_WEB_POST_FORMAT, key);
    }
}
