package com.multidisciplinar.docsecurepro.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
public class DocSecureProUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String prettyJson(Object obj) {
        String value = StringUtils.EMPTY;
        try {
            value = OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Error prettyJson: {}", e);
        }
        return StringUtils.toEncodedString(value.getBytes(), StandardCharsets.UTF_8);
    }

}
