/**
 * Created by VAWANIR on 5/15/2017.
 */
import java.util.*;

/**
 * Code Info
 */
public class CodeInfo {
    ArrayList methodD = new ArrayList();
    private int size;
    private  String methodSignature;
    private  int methodDetails;
    public CodeInfo
            (ArrayList<Instruction> inList ,ConstantPool constantPool)
            throws InvalidConstantPoolIndex {
        size=inList.size();
        int count =0;

        for (Instruction in:inList){
            int x= getDetails(in,constantPool);
            if(x!=0){
                methodD.add(x);
            }
        }

    }

    /**
     *
     * @param in
     * @param constantPool
     * @return
     * @throws InvalidConstantPoolIndex
     */
    public int getDetails(Instruction in, ConstantPool constantPool)
            throws InvalidConstantPoolIndex {
        int x=0;
        if (in.getOpcode().getMnemonic().
                matches("^invokestatic+")){
              x = getMethodDetails(in);
        }else if (in.getOpcode().getMnemonic().
                matches("^invokevirtual+")){
             x = getMethodDetails(in);
        }else if (in.getOpcode().getMnemonic().
                matches("^invokespecial+")){
              x= getMethodDetails(in);
        }else if (in.getOpcode().getMnemonic().
                matches("^invokeinterface+")){
              x = getMethodDetails(in);
        }else{
            x=0;
        }
            methodDetails = x;


        return methodDetails;
    }

    /**
     *
     * @return
     */
    public String getMethodSignature(){
        return methodSignature;
    }

    /**
     *
     * @return
     */
    public int getSize(){
        return size;
    }

    /**
     *
     * @return
     */
    public ArrayList getMethod(){
            return methodD;
    }

    /**
     *
     * @param in
     * @return
     */
    public int getMethodDetails(Instruction in ){
        String operand=in.getOperandString();
        String[] tokens=operand.split(" ");
        String[] index = new String[2];
        int number[]=new int[2];
        for (int i=0; i<tokens.length; i++){
            index=tokens[i].split("=");
            number[i]=Integer.
                    parseInt(index[index.length-1],16);
        }
        int indexByte =(short)(number[0]<< 8 | number[1]);
        return indexByte;
    }

}
