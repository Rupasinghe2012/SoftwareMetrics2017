/**
 * Created by VAWANIR on 5/14/2017.
 */
import java.io.*;
import java.util.*;

/**
 * This class stores the Field Info
 */
public class FieldInfo {
    private int fieldsCount;
    private int fieldAccessFlags;
    private int fieldNameIndex;
    private int fieldDescriptorIndex;
    private int fieldAttributeCount;
    private int fieldAttributeNameIndex;
    private long fieldAttributeLength;
    private int fieldAttributeInfo;

    /**
     * @param dis
     * @throws IOException
     */
    public FieldInfo(DataInputStream dis) throws IOException {
        fieldsCount = dis.readUnsignedShort();
        for (int i = 0; i < fieldsCount; i++) {
            fieldAccessFlags = dis.readUnsignedShort();
            fieldNameIndex = dis.readUnsignedShort();
            fieldDescriptorIndex = dis.readUnsignedShort();
            fieldAttributeCount = dis.readUnsignedShort();
            for (int j = 0; j < fieldAttributeCount; j++) {
                fieldAttributeNameIndex = dis.readUnsignedShort();
                fieldAttributeLength = (long) dis.readUnsignedShort() << 16 | dis.readUnsignedShort();
                for (int k = 0; k < fieldAttributeLength; k++) {
                    fieldAttributeInfo = dis.readByte();
                }
            }
        }
    }
}
