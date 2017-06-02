/**
 * Created by VAWANIR on 5/14/2017.
 */
import java.io.*;

/**
 * This has the LinesTable Data
 */
class Lines {

    private byte[]table;

    /**
     * @param dis
     * @param attributNameInedx
     * @throws IOException
     */
    public Lines(DataInputStream dis, int attributNameInedx) throws IOException {
        int nameIndex = attributNameInedx;
        long lineALength=(long)dis.readUnsignedShort()<<16|dis.readUnsignedShort();
        table = new byte[(int)lineALength];
        dis.readFully(table);
    }

}
