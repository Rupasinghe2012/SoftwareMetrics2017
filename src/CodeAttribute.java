/**
 * Created by VAWANIR on 5/14/2017.
 */
import java.io.*;
import java.util.*;

/**
 * This is where the codeAttribute is handled
 */
class CodeAttribute {
    private long attributeLength;
    private int codeMaxStack;
    private int codeMaxLocals;
    private int codeLength;
    private byte[] code;
    private int exceptionTableLength;
    private byte[] exceptioneTable;
    private AttributesInfo attributesInfo;
    private ArrayList<Instruction>List;
    private CodeInfo codeInfo;

    /**
     * @param dis
     * @param constantPool
     * @param nameIndex
     * @param methodCount
     * @throws IOException
     * @throws CodeParsingException
     * @throws InvalidConstantPoolIndex
     */
    public CodeAttribute
    (DataInputStream dis, ConstantPool constantPool,
     int nameIndex,int methodCount)
            throws IOException,
            CodeParsingException, InvalidConstantPoolIndex {
        attributeLength=
                (long)dis.readUnsignedShort()<<16|dis.readUnsignedShort();
        codeMaxStack = dis.readUnsignedShort();
        codeMaxLocals = dis.readUnsignedShort();
        codeLength =
                dis.readUnsignedShort() << 16 | dis.readUnsignedShort();
        code = new byte[codeLength];
        dis.readFully(code);
        exceptionTableLength=dis.readUnsignedShort();
        exceptioneTable=new byte[exceptionTableLength*8];
        dis.readFully(exceptioneTable);
        attributesInfo=new AttributesInfo(dis, constantPool,methodCount);
        AddInstruction();
        ArrayList<Instruction> inList= getList();
        int size = inList.size();
        codeInfo = new CodeInfo(inList,constantPool);
    }

    /**
     * @return codeinfo
     */
    public CodeInfo getCodeInfo(){
        return  codeInfo;
    }

    /**
     * In this Add the instructions to the Arrray List
     * @return
     * @throws CodeParsingException
     */
    private ArrayList<Instruction> AddInstruction()
            throws CodeParsingException {
        int offset=0;
        List=new ArrayList<Instruction>();
        do
        {
            Instruction ins;
            ins = new Instruction(code, offset);
            offset+=ins.getSize();
            List.add(ins);


        }while(offset<code.length);
        return List;
    }

    /**
     * @return List array list
     */
    public ArrayList<Instruction> getList() {
        return List;
    }

    /**
     * @param List
     */
    public void setList(ArrayList<Instruction> List) {
        this.List = List;
    }

    /***
     * @return attributeLength
     */
    public long getAttrLength() {
        return attributeLength;
    }


}
