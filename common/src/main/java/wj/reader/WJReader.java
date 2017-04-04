package wj.reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import wj.exceptions.WJException;


/**
 * Created by johan on 18/03/17.
 * Reader for messages using the WJ protocol
 */
public class WJReader {
    private InputStream inputStream;
    private byte[] binary;
    private String jsonString;

    public WJReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public WJType getResultType() throws IOException {
        byte[] headers = new byte[3];
        WJType type;
        short dataLength;

        try {
            int headerLen = this.inputStream.read(headers, 0, 3);

            if (headerLen == -1) {
                throw new IOException("Stream closed");
            }

            if (headerLen != 3) {
                throw new WJException("Invalid header, found only " + headerLen);
            }

            dataLength = (short) ((headers[1] & 0xFF) << 8 | (headers[2] & 0xFF));

            switch (headers[0]) {
                case (short) 0:
                    this.jsonString = readJsonString(dataLength);
                    type = WJType.JSON_STRING;
                    break;
                case (short) 1:
                    this.binary = readBinary(dataLength);
                    type = WJType.BINARY;
                    break;
                default:
                    throw new WJException("Invalid type: " + headers[0]);

            }

        } catch (WJException wjException) {
            System.err.println("WJException: " + wjException.getMessage());
            type = WJType.INVALID;
        }

        return type;
    }

    private String readJsonString(short dataLength) throws IOException, WJException {
        char[] string = new char[dataLength];
        Reader inputStreamReader = new InputStreamReader(this.inputStream, "UTF-8");
        int len = inputStreamReader.read(string, 0, dataLength);
        if (len != dataLength) {
            throw new WJException("Wrong data length");
        }

        return String.valueOf(string);
    }

    private byte[] readBinary(short dataLength) throws IOException, WJException {
        byte[] data = new byte[dataLength];
        int len = this.inputStream.read(data, 0, dataLength);
        if (len != dataLength) {
            throw new WJException("Wrong data length");
        }

        return data;
    }

    public String getJsonString() throws WJException {
        if (jsonString == null) {
            throw new WJException("JsonString not found");
        }
        return jsonString;
    }

    public byte[] getBinary() throws WJException {
        if (binary == null) {
            throw new WJException("Binary not found");
        }
        return binary;
    }
}
