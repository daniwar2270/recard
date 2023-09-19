package bg.codexio.recard.profile.service;

import bg.codexio.recard.profile.constant.TopicConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicNamingService {

    public static String getTopicName(Class<?> clazz) {
        return clazz.getSimpleName() + TopicConstants.TOPIC_SUFFIX;
    }
}