/**
 * Created by VAWANIR on 5/14/2017.
 */
import java.util.*;
import java.io.*;

/**
 * This stores the method Info
 */
public class MethodInfo {
    private int methodCount;
    Method[] methodArray;

    /**
     * @param dis
     * @param constantPool
     * @param s
     * @throws IOException
     * @throws CodeParsingException
     * @throws InvalidConstantPoolIndex
     */
    public MethodInfo(DataInputStream dis, ConstantPool constantPool,String s)
            throws IOException, CodeParsingException, InvalidConstantPoolIndex {
        methodCount = dis.readUnsignedShort();
        methodArray = new Method[methodCount];
        for (int i=0; i<methodCount; i++){
            methodArray[i] = new Method(dis,constantPool,s,methodCount);
        }
    }

    /**
     * @return methodcount
     */
    public int getMethodCount() {
        return methodCount;
    }

    /**
     * @return methodArray
     */
    public Method[] getMethodArray() {
        return methodArray;
    }

    /**
     * @param i
     * @return i th value of methodArray
     */
    public Method getMethodArrayVal(int i) {
        return methodArray[i];
    }

}
