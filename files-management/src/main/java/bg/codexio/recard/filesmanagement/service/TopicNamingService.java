package bg.codexio.recard.filesmanagement.service;

import bg.codexio.recard.filesmanagement.constant.TopicConstants;

public class TopicNamingService {

    public static String getTopicName(Class<?> clazz) {
        return clazz.getSimpleName() + TopicConstants.TOPIC_SUFFIX;
    }
}