package bg.codexio.recard.auth.config;

import bg.codexio.recard.auth.annotations.KafkaPayload;
import bg.codexio.recard.auth.constants.SingleSymbolConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaUtils {

    public static String extractTypeMapping() {
        final var reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(KafkaPayload.class.getPackageName())));
        final var allClasses = reflections.getTypesAnnotatedWith(KafkaPayload.class);

        return allClasses.stream()
                .map(cls -> cls.getSimpleName() + SingleSymbolConstants.COLON + cls.getName())
                .collect(Collectors.joining(SingleSymbolConstants.COMMA));
    }
}