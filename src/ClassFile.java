/**
 * Created by VAWANIR on 5/14/2017.
 */
import java.io.*;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.stream.Stream;

/**
 * This stores the Lot of data about the Class File
 */
public class ClassFile
{
    private String filename;
    private long magic;
    private int minorVersion;
    private int majorVersion;
    private ConstantPool constantPool;
    private int accessFlags;
    private int thisClass;
    private int superClass;
    private Interfaces interfaces;
    private FieldInfo fieldInfo;
    private MethodInfo methodInfo;
    private HashMap<Method,ArrayList>    methodNames;
    private int mCount;
    private HashMap<String,String>  methodHashTable;
    ArrayList methodD = new ArrayList();
    private String thisClassName;
    private String classFilePath=
            "C:\\Users\\vawanir\\IdeaProjects\\SoftwareMetrics2017\\out\\production";
    private int classesCount = 0;
    /**
     * Parses a class file an constructs a ClassFile object. At present, this
     * only parses the header and constant pool.
     */
    /**
     *
     * @param filename
     * @param lol
     * @throws ClassFileParserException
     * @throws IOException
     */
    public ClassFile(String filename,String lol)
            throws ClassFileParserException,
            IOException {
        DataInputStream dis =
                new DataInputStream(new FileInputStream(filename));

        this.filename = filename;
        magic =
                (long) dis.readUnsignedShort() << 16 | dis.readUnsignedShort();

        minorVersion = dis.readUnsignedShort();

        majorVersion = dis.readUnsignedShort();

        constantPool = new ConstantPool(dis);

        accessFlags = dis.readUnsignedShort();

        thisClass = dis.readUnsignedShort();


        thisClassName =
                ((ConstantClass)constantPool.getEntry(thisClass)).getName();

        superClass = dis.readUnsignedShort();

        interfaces = new Interfaces(dis);

        fieldInfo = new FieldInfo(dis);

        String x = filename.substring(0, filename.length() - 6);
        methodInfo = new MethodInfo(dis, constantPool, x);

        methodNames = new HashMap<>();

        methodHashTable = new HashMap<>();

        mCount = methodInfo.getMethodCount();

        for (int p = 0; p < mCount; p++) {

            int aCount =
                    methodInfo.getMethodArrayVal(p).getAttr()
                            .getAttributesCount();
            for (int q = 0; q < aCount; q++) {

                methodD = methodInfo.getMethodArrayVal(p)
                        .getAttr().getAttrVal(q)
                        .getCodeAttribute().getCodeInfo().getMethod();

                Method metodName;
                metodName = methodInfo.getMethodArrayVal(p);

                methodNames.put(metodName, methodD);

            }
        }

        for (Map.Entry m : methodNames.entrySet()) {

            ArrayList calleeL = new ArrayList();

            Method mVal = (Method) m.getKey();

            String mNam =getMethodSig(mVal, constantPool);

            String mSig = thisClassName+"-"+ mNam;

            calleeL = (ArrayList) m.getValue();

            String calleName=null;

            if(calleeL.isEmpty()){
                String a = mSig.concat(String.valueOf(0));
                methodHashTable.put(a,calleName);
            }

            for (int z = 0; z < calleeL.size(); z++) {

                int r = (int) calleeL.get(z);

                String Mcname = getClassName(r, constantPool);

                String Mname= getCalleeMSig(r, constantPool);
                calleName = Mcname+"-"+Mname;

                String a = mSig.concat(Integer.toString(z));

                methodHashTable.put(a, calleName);

            }

        }
        String Caller= thisClassName+"-"+lol;
        System.out.println( Caller);
        printTree(Caller);



    }

    /**
     * @param lol
     * @throws ClassFileParserException
     * @throws IOException
     */
    public void printTree(String lol)
            throws ClassFileParserException, IOException {

            for(Map.Entry m:methodHashTable.entrySet()){
                String Caller = (String) m.getKey();

                String Callee= (String) m.getValue();


                String x= Caller.substring(0,Caller.length()-1);

                String lop=null;

                if(Callee!=null){
                    String  toks[] = Callee.split("-");
                    lop=toks[0];
                }
                if(thisClassName.equalsIgnoreCase(lop)) {

                    if (lol.equalsIgnoreCase(x)){

                        if (Callee!=null){

                            if (lol!=Callee){

                                System.out.println("\t"+Callee);
                                getCaller(Callee);
                            }else {

                                System.out.println
                                        ("\t\tThis is Recursive Method "+ Callee);
                            }

                        }
                    }
                }else {
                    if (lop=="null") {

                    }else{

                    if ("java/lang/Object".equalsIgnoreCase(lop)){

                        if (lol.equalsIgnoreCase(x)){

                            if (Callee!=null){

                                System.out.println("\tObject Constructor");
                                getCaller(Callee);
                            }
                        }
                    }else {
                        if (Callee!=null){

                            String file = classFilePath + lop + ".class";

                            File f = new File(file);
                            if (f.exists() && !f.isDirectory()){

                                String  toks[] = Callee.split("-")
                                        ;
                                Callee=toks[1];
                                ClassFileParser cf = new ClassFileParser(file, Callee);

                                classesCount++;
                            }else {
                                System.out.println
                                        (Callee +" --> Missing Class file...<--");
                            }
                        }
                    }
                }
                }
            }
        }


    /**
     * @param a
     */
    public void getCaller(String a){

        for(Map.Entry m:methodHashTable.entrySet()) {

            String Caller = (String) m.getKey();

            String Callee = (String) m.getValue();

            String x = Caller.substring(0, Caller.length() - 1);

            String lop = x.substring(0,1);

            if (a.equalsIgnoreCase(x) && Callee!=null){

                if ( a!=Callee ){

                    System.out.println("\t\t"+Callee);
                    getCaller(Callee);

                }else {

                    System.out.println
                            ("\t\tRecursive Method Calling "+Callee);
                }

            }
        }
    }

    /**
     * @return
     * @throws InvalidConstantPoolIndex
     */
    public String[] getMethodD()
            throws InvalidConstantPoolIndex {

        Method[] meth = new Method[mCount];

        String[] methd = new String [mCount];

        meth = methodInfo.getMethodArray();

        for (int i=0; i<meth.length; i++){


            Method mValue = meth[i];

            int nameIndex = mValue.getNameIndex();

            int descIndex = mValue.getDescIndex();

            String mSig =
                    (((ConstantUtf8)constantPool.
                            getEntry(nameIndex)).getBytes())+
                    (((ConstantUtf8)constantPool.
                            getEntry(descIndex)).getBytes());
            methd[i]=thisClassName+"."+mSig;

        }

        return methd;
}

    /**
     * @return mcount
     */
    public int getMCount(){

        return mCount;
    }

    /**
     * @return methodInfo
     */
    public MethodInfo getMethods()
    {

        return methodInfo;
    }

    /**
     * @return constantpool
     */
    public ConstantPool getConstantpool() {
        return constantPool;
    }

    /**
     * @param mVal
     * @param constantPool
     * @return msig
     * @throws InvalidConstantPoolIndex
     */
    public String getMethodSig
    (Method mVal ,ConstantPool constantPool)
            throws InvalidConstantPoolIndex {
        String Msig ;

        int nameIndex = mVal.getNameIndex();

        int descIndex = mVal.getDescIndex();
        String Mname =
                (((ConstantUtf8)constantPool.
                        getEntry(nameIndex)).getBytes());

        if(Mname.matches("<init>")){
            Mname="Constructor";
        }

        String Mpara =(((ConstantUtf8)constantPool.
                getEntry(descIndex)).getBytes());

        String Para = getParameterValues(Mpara);

        Msig= Mname+Para;

        return Msig;
    }

    /**
     * @param r
     * @param constantPool
     * @return Cname
     * @throws InvalidConstantPoolIndex
     */
    public String getClassName(int r, ConstantPool constantPool)
            throws InvalidConstantPoolIndex {

        String cName =((ConstantMethodRef)

                constantPool.getEntry(r)).getClassName();
        return cName;
    }

    /**
     * @param r
     * @param constantPool
     * @return cmsig
     * @throws InvalidConstantPoolIndex
     */
    public String getCalleeMSig
    (int r,ConstantPool constantPool)
            throws InvalidConstantPoolIndex {

        String Mname =
                ((ConstantMethodRef)
                        constantPool.getEntry(r)).getName();
        if(Mname.matches("<init>"))
        {
            Mname="Constructor";
        }
        String Mpara=
                ((ConstantMethodRef)
                        constantPool.getEntry(r)).getType();
        String Para = getParameterValues(Mpara);
        String cMsig= Mname+Para;
        return  cMsig;
    }

    /**
     * @return mkeys
     * @throws InvalidConstantPoolIndex
     * @throws CodeParsingException
     * @throws IOException
     */
    public Method[] getKeys()
            throws InvalidConstantPoolIndex,
            CodeParsingException, IOException {

        Method [] mKeys =new Method[methodInfo.getMethodCount()];

        for(Map.Entry m:methodNames.entrySet()) {

            for (int i=0; i<methodNames.size(); i++){

                mKeys[i] = (Method) m.getKey();
            }

        }
        return mKeys;
    }

    /**
     * @return mVal
     * @throws InvalidConstantPoolIndex
     * @throws CodeParsingException
     * @throws IOException
     */
    public ArrayList getValueList()
            throws InvalidConstantPoolIndex,
            CodeParsingException, IOException {

        ArrayList calleeL = new ArrayList();

        ArrayList<String> mVal = new ArrayList<String>();

        for (Map.Entry m : methodNames.entrySet()) {

            calleeL = (ArrayList) m.getValue();

            for (int i = 0; i < calleeL.size(); i++) {

                int r = (int) calleeL.get(i);

               mVal.add(getCalleeMSig(r,constantPool));
            }

        }
        return mVal;
    }

    /**
     * @return count
     * @throws InvalidConstantPoolIndex
     * @throws CodeParsingException
     * @throws IOException
     */
    public int uniqueset()
            throws InvalidConstantPoolIndex,
            CodeParsingException, IOException {

        int count=0;

        ArrayList<String> mVal = new ArrayList<String>();
        mVal=getValueList();

        String[] array = new String[mVal.size()];

        for (int i = 0; i < mVal.size(); i++) {

            array[i]=mVal.get(i);
        }
        count= (int) Stream.of(array)
                .distinct().count();
        return count;
    }

    /**
     * @return thiClassname
     */
    public String getThisClassName(){
        return thisClassName;
}

    /**
     * @return ClassesCount
     */
    public int getClassesCount(){
        return classesCount;
    }

    /**
     * @param Mpara
     * @return Para
     */
    public String getParameterValues(String Mpara){
        String d ="";
        String Para="";
        for (int i=0; i<Mpara.length();i++){
            if(Character.isUpperCase(Mpara.charAt(i))){
                char w = Mpara.charAt(i);
                d = d + w ;
            }
        }
        for (int j=0; j<d.length(); j++){
            String x= String.valueOf(d.charAt(j));
            if (x.matches("I")){
                Para=Para+"int,";

            }else if (x.matches("D")){
                Para=Para+"double,";

            }else if(x.matches("L")){
                Para=Para+"String,";

            }else if (x.matches("C")){
                Para=Para+"char,";

            }else if (x.matches("B")){
                Para=Para+"byte,";

            }else if (x.matches("F")){
                Para=Para+"float,";

            }else if (x.matches("J")){
                Para=Para+"long,";

            }else if (x.matches("Z")){
                Para=Para+"boolean,";

            }else {
//                Para=Mpara;
            }

        }
        Para="("+Para+")";
        return Para;
    }
    /** Returns the contents of the class file as a formatted String. */
//    public String toString()
//    {
//        return String.format(
//                "Filename: %s\n" +
//                        "Magic: 0x%08x\n" +
//                        "Class file format version: %d.%d\n\n" +
//                        "Constant pool:\n\n%s",
//                filename, magic, majorVersion, minorVersion, constantPool);
//    }
}
