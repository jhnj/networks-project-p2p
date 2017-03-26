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

    public static SetupPortRequest parseSetupPortRequest(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, SetupPortRequest.class);
    }

    public static AddFileRequest parseAddFileRequest(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, AddFileRequest.class);
    }

    public static OKResponse parseOKResponse(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, OKResponse.class);
    }

    public static BlockListRequest parseBlockListRequest(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, BlockListRequest.class);
    }

    public static BlockListResponse parseBlockListResponse(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, BlockListResponse.class);
    }

    public static BlockRequest parseBlockRequest(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, BlockRequest.class);
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

    public static String stringifySetupPortRequest(SetupPortRequest setupPortRequest) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(setupPortRequest);
    }

    public static String stringifyAddFileRequest(AddFileRequest addFileRequest) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(addFileRequest);
    }

    public static String stringifyOKResponse(OKResponse okResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(okResponse);
    }

    public static String stringifyBlockListRequest(BlockListRequest blockListRequest) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(blockListRequest);
    }

    public static String stringifyBlockListResponse(BlockListResponse blockListResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(blockListResponse);
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
