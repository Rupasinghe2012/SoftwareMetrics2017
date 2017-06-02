/**
 * Created by VAWANIR on 5/14/2017.
 */
import java.io.*;

/**
 * This has the method Data
 */
public class Method {
    private int methodAccessFlag;
    private int methodNameIndex;
    private int methodDescriptorIndex;
    private  AttributesInfo attributesInfo;
    String name;

    /**
     * @param dis
     * @param constantPool
     * @param s
     * @param methodCount
     * @throws IOException
     * @throws CodeParsingException
     * @throws InvalidConstantPoolIndex
     */
    public Method(DataInputStream dis, ConstantPool constantPool, String s,int methodCount) throws IOException, CodeParsingException, InvalidConstantPoolIndex {
        name=s;
        methodAccessFlag = dis.readUnsignedShort();
        methodNameIndex = dis.readUnsignedShort();
        methodDescriptorIndex = dis.readUnsignedShort();
        attributesInfo = new AttributesInfo(dis,constantPool,methodCount);
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return methodaccesflag
     */
    public int getAccessFlag() {
        return methodAccessFlag;
    }

    /**
     * @return methodnameIndex
     */
    public int getNameIndex() {
        return methodNameIndex;
    }

    /**
     * @return descriptorIndex
     */
    public int getDescIndex() {
        return methodDescriptorIndex;
    }

    /**
     * @return Attributeinfo
     */
    public AttributesInfo getAttr() {
        return attributesInfo;
    }

}
