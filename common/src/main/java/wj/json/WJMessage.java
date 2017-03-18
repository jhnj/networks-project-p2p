package wj.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

/**
 * Created by walter on 2017-03-18.
 */
public class WJMessage {

    public static String getAction(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.readValue(jsonString, ObjectNode.class);
        if (objectNode.has("action")) {
            return objectNode.get("action").asText();
        } else {
            return "";
        }
    }

    public static FileListRequest parseFileListRequest(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, FileListRequest.class);
    }

}
