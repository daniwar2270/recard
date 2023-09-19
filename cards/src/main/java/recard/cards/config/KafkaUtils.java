package recard.cards.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import recard.cards.annotation.KafkaPayload;
import recard.cards.constant.SingleSymbolConstants;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaUtils {

    public static String extractTypeMappings() {
        final var reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(KafkaPayload.class.getPackageName())));
        final var allClasses = reflections.getTypesAnnotatedWith(KafkaPayload.class);

        return allClasses.stream()
                .map(cls -> cls.getSimpleName() + SingleSymbolConstants.COLON + cls.getName())
                .collect(Collectors.joining(SingleSymbolConstants.COMMA));
    }
}
