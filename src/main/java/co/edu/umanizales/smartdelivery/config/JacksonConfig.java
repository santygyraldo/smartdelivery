package co.edu.umanizales.smartdelivery.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(
                    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, // significa que no se escribiran los datos en formato timestamp
                    SerializationFeature.FAIL_ON_EMPTY_BEANS, //
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
                )
                .modules(new JavaTimeModule()) //
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .build()
                .findAndRegisterModules();
    }
}
