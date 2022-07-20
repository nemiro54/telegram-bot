package com.github.nemiro54.telegrambot.javarushclient;

import com.github.nemiro54.telegrambot.javarushclient.dto.PostInfo;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link JavaRushGroupClient} interface
 */
@Component
public class JavaRushPostClientImpl implements JavaRushPostClient {

    private final String javaRushApiPostPath;

    public JavaRushPostClientImpl(@Value("${javarush.api.path}") String javaRushApiPostPath) {
        this.javaRushApiPostPath = javaRushApiPostPath + "/posts";
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public List<PostInfo> findNewPosts(Integer groupId, Integer lastPostId) {
        List<PostInfo> lastPostsByGroup = Unirest.get(javaRushApiPostPath)
                .queryString("order", "NEW")
                .queryString("groupKid", groupId)
                .queryString("limit", 15)
                .asObject(new GenericType<List<PostInfo>>() {
                }).getBody();

        List<PostInfo> newPosts = lastPostsByGroup.stream()
                .takeWhile(post -> !lastPostId.equals(post.getId()))
                .collect(Collectors.toList());

        return newPosts;
    }
}
