package com.github.nemiro54.telegrambot.javarushclient;

import com.github.nemiro54.telegrambot.javarushclient.dto.PostInfo;

import java.util.List;

/**
 * Client for JavaRush Open API corresponds to Posts.
 */
public interface JavaRushPostClient {

    /**
     * Find new Posts since lastPostId in provided groups.
     *
     * @param groupId provided group ID
     * @param lastPostId provided last post ID
     * @return the collection of the new {@link PostInfo}
     */
    List<PostInfo> findNewPosts(Integer groupId, Integer lastPostId);
}
