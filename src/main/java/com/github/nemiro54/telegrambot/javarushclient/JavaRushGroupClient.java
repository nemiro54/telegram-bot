package com.github.nemiro54.telegrambot.javarushclient;

import com.github.nemiro54.telegrambot.javarushclient.dto.GroupCountRequestArgs;
import com.github.nemiro54.telegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.nemiro54.telegrambot.javarushclient.dto.GroupInfo;
import com.github.nemiro54.telegrambot.javarushclient.dto.GroupRequestArgs;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * Client for JavaRush Open API corresponds to Groups.
 */
public interface JavaRushGroupClient {

    /**
     * Get all the {@link GroupInfo} filtered by provided {@link GroupRequestArgs}
     *
     * @param requestArgs provided {@link GroupRequestArgs}
     * @return the collection  of the {@link GroupInfo} objects
     */
    List<GroupInfo> getGroupList(GroupRequestArgs requestArgs);

    /**
     * Get all the {@link GroupDiscussionInfo} filtered by provided {@link GroupRequestArgs}
     * @param requestArgs provided {@link GroupRequestArgs}
     * @return the collection of the {@link GroupDiscussionInfo} objects
     */
    List<GroupDiscussionInfo> getGroupDiscussionList(GroupRequestArgs requestArgs);

    /**
     * Get count of groups filtered by provided {@link GroupRequestArgs}
     *
     * @param countRequestArgs provided {@link GroupCountRequestArgs}
     * @return the count of the group
     */
    Integer getGroupCount(GroupCountRequestArgs countRequestArgs);

    /**
     * Get {@link GroupDiscussionInfo} by provided ID
     * @param id provided ID
     * @return {@link GroupDiscussionInfo} objects
     */
    GroupDiscussionInfo getGroupById(Integer id);

    Integer findLastPostId(Integer groupSub);
}
