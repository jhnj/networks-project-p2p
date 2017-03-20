package wj.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by johan on 18/03/17.
 * Write WJ messages
 */
public class WJWriter {
    private OutputStream outputStream;

    public WJWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeBinary(byte[] data) throws IOException {
        int length = data.length;
        if (length > Short.MAX_VALUE) {
            throw new IOException("Data too large");
        }

        // Write header, data length big endian
        outputStream.write((byte) 0x01);
        outputStream.write((byte) (length >> 8) & 0xFF);
        outputStream.write((byte) length & 0xFF);

        outputStream.write(data);
        outputStream.flush();
    }

    public void writeJsonString(String jsonString) throws IOException {
        int length = jsonString.length();
        if (length > Short.MAX_VALUE) {
            throw new IOException("String too large");
        }

        byte[] data = new byte[3 + jsonString.length()];

        // Write header, data length big endian
        data[0] = (byte) 0x00;
        data[1] = (byte) ((length >> 8) & 0xFF);
        data[2] = (byte) (length & 0xFF);

        int i = 3;
        for (char ch : jsonString.toCharArray()) {
            data[i] = (byte) ch;
            i++;
        }

        outputStream.write(data);
        outputStream.flush();
    }
}
