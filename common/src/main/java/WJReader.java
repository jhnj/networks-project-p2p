import java.io.IOException;
import java.io.InputStream;


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

    public WJType getResultType() {
        byte[] headers = new byte[3];
        WJType type;
        short dataLength;

        try {
            int headerLen = this.inputStream.read(headers, 0, 3);

            if (headerLen != 3) {
                throw new WJException("Invalid header");
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
                default:
                    throw new WJException("Invalid type");

            }

        } catch (IOException ioException) {
            System.err.println("IOException: " + ioException.getMessage());
            type = WJType.INVALID;
        } catch (WJException wjException) {
            System.err.println("WJException: " + wjException.getMessage());
            type = WJType.INVALID;
        }

        return type;
    }

    private String readJsonString(short dataLength) {
        String string = "string";
        return string;
    }

    private byte[] readBinary(short dataLength) {
        byte[] data = new byte[19];
        return data;
    }

    public String getJsonString() throws WJException{
        return jsonString;
    }

    public byte[] getBinary() throws WJException{
        return binary;
    }
}
