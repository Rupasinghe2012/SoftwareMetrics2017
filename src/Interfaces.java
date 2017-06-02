/**
 * Created by VAWANIR on 5/14/2017.
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * This has the Interface Data
 */
public class Interfaces {
    private int interfaceCount;

    /**
     * @param dis
     * @throws IOException
     */
    public Interfaces(DataInputStream dis) throws IOException {
        interfaceCount = dis.readUnsignedShort();
        dis.skipBytes(interfaceCount * 2);
    }
}
