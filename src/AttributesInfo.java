/**
 * Created by VAWANIR on 5/14/2017.
 */
import java.util.*;
import java.io.*;

/**
 * This Stores the Attributes Info
 */
public class AttributesInfo {
    private Attributes[] attributes;
    private int attributesCount;

    /**
     *
     * @param dis
     * @param constantPool
     * @param methodCount
     * @throws IOException
     * @throws InvalidConstantPoolIndex
     * @throws CodeParsingException
     */
    public AttributesInfo
    (DataInputStream dis, ConstantPool constantPool,int methodCount)
            throws IOException, InvalidConstantPoolIndex, CodeParsingException {

        attributesCount = dis.readUnsignedShort();

        attributes = new Attributes[attributesCount];

        for (int i=0; i<attributesCount; i++){
            attributes[i]=new Attributes(dis,constantPool,methodCount);
        }
    }

    /**
     * @param i
     * @return i th value of attributes[]
     */
    public Attributes getAttrVal(int i)
    {
        return attributes[i];
    }

    /**
     * @return attributesCount
     */
    public int getAttributesCount() {
        return attributesCount;
    }
}
