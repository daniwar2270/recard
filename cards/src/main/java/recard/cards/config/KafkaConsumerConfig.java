package recard.cards.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import recard.cards.annotation.KafkaPayload;
import recard.cards.constant.TopicConstants;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class KafkaConsumerConfig {

    private String bootstrapServersConfig;

    public KafkaConsumerConfig(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapServersConfig) {
        this.bootstrapServersConfig = bootstrapServersConfig;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        final Map<String, Object> configProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServersConfig,
                ConsumerConfig.GROUP_ID_CONFIG, TopicConstants.GROUPS_CARD,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                JsonDeserializer.TYPE_MAPPINGS, KafkaUtils.extractTypeMappings()
        );

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        return new ConcurrentKafkaListenerContainerFactory<>() {{
            this.setConsumerFactory(consumerFactory());
        }};
    }
}