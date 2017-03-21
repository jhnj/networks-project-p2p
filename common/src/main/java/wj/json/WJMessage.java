package wj.json;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    public static AddFileRequest parseAddFileRequest(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, AddFileRequest.class);
    }

    public static FileClientsRequest parseFileClientsRequest(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, FileClientsRequest.class);
    }

    public static FileClientsResponse parseFileClientsResponse(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, FileClientsResponse.class);
    }

    public static FileListRequest parseFileListRequest(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, FileListRequest.class);
    }

    public static FileListResponse parseFileListResponse(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, FileListResponse.class);
    }

    public static String stringifyAddFileRequest(AddFileRequest addFileRequest) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(addFileRequest);
    }

    public static String stringifyAddFileResponse(AddFileResponse addFileResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(addFileResponse);
    }

    public static String stringifyFileClientsRequest(FileClientsRequest fileClientsRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(fileClientsRequest);
    }

    public static String stringifyFileClientsResponse(FileClientsResponse fileClientsResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(fileClientsResponse);
    }

    public static String stringifyFileListRequest(FileListRequest fileListRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(fileListRequest);
    }

    public static String stringifyFileListResponse(FileListResponse fileListResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(fileListResponse);
    }

}
