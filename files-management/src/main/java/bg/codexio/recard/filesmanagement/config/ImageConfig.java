package bg.codexio.recard.filesmanagement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "image.avatar")
@Data
public class ImageConfig {

    private Integer thumbnailSize;
    private String dir;
    private Integer compressionRatio;
    private String baseUrl;
}