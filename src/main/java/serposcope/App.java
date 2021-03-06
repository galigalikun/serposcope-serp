/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package serposcope;

import java.util.Base64;
import java.util.Arrays;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

public class App {
    static LZ4Factory factory = LZ4Factory.fastestInstance();
    static LZ4Compressor compressor = factory.fastCompressor();
    static LZ4FastDecompressor decompressor = factory.fastDecompressor();

    static final String source = "todo";

    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        byte[] data = Base64.getDecoder().decode(source.getBytes());
        byte[] compressedData = decompress(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);

        try {
            DataInputStream dis = new DataInputStream(bais);
            byte version = dis.readByte();
            System.out.format("version:%d\n", version);
            int entrySize = dis.readShort();
            System.out.format("entrySize:%d\n", entrySize);
            for (int i = 0; i < entrySize; i++) {
                String url = dis.readUTF();
                System.out.format("url:%s\n", url);
                byte mapSize = dis.readByte();
                for (int j = 0; j < mapSize; j++) {
                    short key = dis.readShort();
                    short value = dis.readShort();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(new App().getGreeting());
    }

    protected static byte[] decompress(byte[] compressed) {
        if (compressed == null || compressed.length < 5) {
            return null;
        }

        int decompressedLength = ByteBuffer.wrap(compressed, 0, 4).getInt();
        byte[] decompressed = new byte[decompressedLength];
        decompressor.decompress(compressed, 4, decompressed, 0, decompressedLength);

        return decompressed;
    }
}
