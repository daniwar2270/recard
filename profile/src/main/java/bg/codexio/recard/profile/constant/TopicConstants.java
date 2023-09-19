package bg.codexio.recard.profile.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopicConstants {

    public static final String GROUPS_PROFILE = "profile";
    public static final String TOPICS_USER_REGISTRATION = "UserRegistrationEvent_topic";
    public static final String TOPICS_USER_UPLOAD = "ImageUploadEvent_topic";
    public static final String TOPIC_SUFFIX = "_topic";
}