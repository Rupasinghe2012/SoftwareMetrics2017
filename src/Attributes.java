/**
 * Created by VAWANIR on 5/14/2017.
 */
import java.io.*;
import java.util.*;
public class Attributes {
    private int attributeNameIndex;
    private CodeAttribute codeAttribute;
    private Lines lines;

    /**
     * @param dis
     * @param constantPool
     * @param methodCount
     * @throws IOException
     * @throws InvalidConstantPoolIndex
     * @throws CodeParsingException
     */
    public  Attributes (DataInputStream dis, ConstantPool constantPool,int methodCount)
            throws IOException, InvalidConstantPoolIndex, CodeParsingException {

        String attributeName;

        attributeNameIndex= dis.readUnsignedShort();
        attributeName =
                ((ConstantUtf8)constantPool.getEntry(attributeNameIndex)).getBytes();

        if(attributeName.equalsIgnoreCase("code")){
            codeAttribute =
                    new CodeAttribute(dis,constantPool,attributeNameIndex,methodCount);
        }else{
           lines= new Lines(dis,attributeNameIndex);
        }
    }

    /**
     * @return attributeNameIndex
     */
    public int getNameIndex() {
            return attributeNameIndex;
        }

    /**
     * @return codeAttribut
     */
    public CodeAttribute getCodeAttribute(){
        return codeAttribute;
    }

}
