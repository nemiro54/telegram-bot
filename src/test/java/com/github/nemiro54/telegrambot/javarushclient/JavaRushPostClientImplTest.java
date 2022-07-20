package com.github.nemiro54.telegrambot.javarushclient;

import com.github.nemiro54.telegrambot.javarushclient.dto.PostInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.nemiro54.telegrambot.javarushclient.JavaRushGroupClientImplTest.JAVARUSH_API_PATH;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Integration-level testing for JavaRushPostClient")
class JavaRushPostClientImplTest {

    private final JavaRushPostClient javaRushPostClient = new JavaRushPostClientImpl(JAVARUSH_API_PATH);

    @Test
    public void shouldProperlyGetNew15Posts() {
//        when
        List<PostInfo> newPosts = javaRushPostClient.findNewPosts(30, 2935);

//        then
        Assertions.assertEquals(15, newPosts.size());
    }
}