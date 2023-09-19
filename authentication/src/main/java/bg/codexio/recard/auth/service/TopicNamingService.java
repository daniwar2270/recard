package bg.codexio.recard.auth.service;

import bg.codexio.recard.auth.constants.TopicConstants;

public class TopicNamingService {

    public static String getTopicName(Class<?> clazz) {
        return clazz.getSimpleName() + TopicConstants.TOPIC_SUFFIX;
    }

    private TopicNamingService(){
    }
}