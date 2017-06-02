/**
 * Created by VAWANIR on 5/14/2017.
 */
import java.io.*;
public abstract class CPEntry
{
    /**
     * Parses a constant pool entry from a DataInputStream, returning an
     * instance of the appropriate class. Any references to other entries
     * remain unresolved until the resolveReferences() method is called.
     */
    public static CPEntry parse(DataInputStream dis) throws IOException,
            InvalidTagException
    {
        byte tag = dis.readByte();
        CPEntry entry;

        switch(tag)
        {
            case  1: entry = new ConstantUtf8(dis);               break;
            case  3: entry = new ConstantInteger(dis);            break;
            case  4: entry = new ConstantFloat(dis);              break;
            case  5: entry = new ConstantLong(dis);               break;
            case  6: entry = new ConstantDouble(dis);             break;
            case  7: entry = new ConstantClass(dis);              break;
            case  8: entry = new ConstantString(dis);             break;
            case  9: entry = new ConstantFieldRef(dis);           break;
            case 10: entry = new ConstantMethodRef(dis);          break;
            case 11: entry = new ConstantInterfaceMethodRef(dis); break;
            case 12: entry = new ConstantNameAndType(dis);        break;
            case 15: entry = new ConstantMethodHandle(dis);       break;
            case 16: entry = new ConstantMethodType(dis);         break;
            case 18: entry = new ConstantInvokeDynamic(dis);      break;

            default:
                throw new InvalidTagException(
                        String.format("Invalid tag: 0x%02x", tag));

        }
        return entry;
    }

    /**
     * Resolves references between constant pool entries, once the entire
     * constant pool has been parsed.
     */
    public void resolveReferences(ConstantPool cp)
            throws InvalidConstantPoolIndex {}

    /** Returns a string indicating the type of entry. */
    public abstract String getTagString();

    /**
     * Returns a string containing the raw (unresolved) contents of the entry.
     */
    public abstract String getValues();

    /**
     * Returns the number of entries "taken up" by this entry. This caters for
     * a quirk in the class file format, whereby a Long or Double entry counts
     * as two entries.
     */
    public int getEntryCount() { return 1; }
}


/** Represents a CONSTANT_Utf8 entry (tag == 1). */
class ConstantUtf8 extends CPEntry
{
    private String bytes;

    /**
     *
     * @param dis
     * @throws IOException
     */
    public ConstantUtf8(DataInputStream dis) throws IOException
    {
        int length = dis.readUnsignedShort();
        byte[] b = new byte[length];
        dis.readFully(b);
        this.bytes = new String(b);
    }

    /**
     *
     * @return
     */
    public String getBytes()     { return bytes; }

    /**
     *
     * @return
     */
    public String getTagString() { return "Utf8"; }

    /**
     *
     * @return
     */
    public String getValues()
    {
        return String.format("length=%d, bytes=\"%s\"",
                bytes.length(), bytes);
    }
}


/** Represents a CONSTANT_Integer entry (tag == 3). */
class ConstantInteger extends CPEntry
{
    private int value;

    /**
     * @param dis
     * @throws IOException
     */
    public ConstantInteger(DataInputStream dis) throws IOException
    {
        this.value = dis.readInt();
    }

    /**
     * @return
     */
    public int getIntValue()     { return value; }

    /**
     *
     * @return
     */
    public String getTagString() { return "Integer"; }

    /**
     *
     * @return
     */
    public String getValues()    { return String.format("value=%d", value); }
}


/** Represents a CONSTANT_Float entry (tag == 4). */
class ConstantFloat extends CPEntry
{
    private float value;

    /**
     * @param dis
     * @throws IOException
     */
    public ConstantFloat(DataInputStream dis) throws IOException
    {
        this.value = dis.readFloat();
    }

    /**
     * @return
     */
    public float getFloatValue() { return value; }

    /**
     * @return
     */
    public String getTagString() { return "Float"; }

    /**
     *
     * @return
     */
    public String getValues()    { return String.format("value=%f", value); }
}


/**
 * Represents a CONSTANT_Long entry (tag == 5). This class overrides
 * getEntryCount() to indicate that a Long entry counts for two entries in the
 * constant pool.
 */
class ConstantLong extends CPEntry
{
    private long value;

    /**
     *
     * @param dis
     * @throws IOException
     */
    public ConstantLong(DataInputStream dis) throws IOException
    {
        this.value = dis.readLong();
    }

    /**
     *
     * @return
     */
    public long getLongValue()   { return value; }

    /**
     *
     * @return
     */
    public String getTagString() { return "Long"; }

    /**
     *
     * @return
     */
    public int getEntryCount()   { return 2; }

    /**
     *
     * @return
     */
    public String getValues() { return String.format("value=%d", value); }
}


/**
 * Represents a CONSTANT_Double entry (tag == 6). This class overrides
 * getEntryCount() to indicate that a Double entry counts for two entries in
 * the constant pool.
 */
class ConstantDouble extends CPEntry
{
    private double value;

    /**
     *
     * @param dis
     * @throws IOException
     */
    public ConstantDouble(DataInputStream dis) throws IOException
    {
        this.value = dis.readDouble();
    }

    /**
     *
     * @return
     */
    public double getDoubleValue() { return value; }

    /**
     *
     * @return
     */
    public String getTagString()   { return "Double"; }

    /**
     *
     * @return
     */
    public int getEntryCount()     { return 2; }

    /**
     *
     * @return
     */
    public String getValues() { return String.format("value=%f", value); }
}

/**
 * Represents a CONSTANT_Class entry (tag == 7). This holds a reference to a
 * Utf8 entry containing a class name (just a raw index until
 * resolveReferences() is called).
 */
class ConstantClass extends CPEntry
{
    private int nameIndex;
    private ConstantUtf8 nameEntry = null;

    /**
     *
     * @param dis
     * @throws IOException
     */
    public ConstantClass(DataInputStream dis) throws IOException
    {
        this.nameIndex = dis.readUnsignedShort();
    }

    /**
     *
     * @param cp
     * @throws InvalidConstantPoolIndex
     */
    public void resolveReferences(ConstantPool cp)
            throws InvalidConstantPoolIndex
    {
        this.nameEntry = (ConstantUtf8)cp.getEntry(nameIndex);
    }

    /**
     *
     * @return
     */
    public int getNameIndex()    { return nameIndex; }

    /**
     *
     * @return
     */
    public String getName()      { return nameEntry.getBytes(); }

    /**
     *
     * @return
     */
    public String getTagString() { return "Class"; }

    /**
     *
     * @return
     */
    public String getValues()
    {
        return String.format("name_index=0x%02x", nameIndex);
    }
}


/**
 * Represents a CONSTANT_String entry (tag == 8). This holds a reference to a
 * Utf8 entry containing the string itself (just a raw index until
 * resolveReferences() is called).
 */
class ConstantString extends CPEntry
{
    private int stringIndex;
    private ConstantUtf8 stringEntry = null;

    /**
     *
     * @param dis
     * @throws IOException
     */
    public ConstantString(DataInputStream dis) throws IOException
    {
        this.stringIndex = dis.readUnsignedShort();
    }

    /**
     *
     * @param cp
     * @throws InvalidConstantPoolIndex
     */
    public void resolveReferences(ConstantPool cp)
            throws InvalidConstantPoolIndex
    {
        this.stringEntry = (ConstantUtf8)cp.getEntry(stringIndex);
    }

    /**
     *
     * @return
     */
    public int getStringIndex()  { return stringIndex; }

    /**
     *
     * @return
     */
    public String getString()    { return stringEntry.getBytes(); }

    /**
     *
     * @return
     */
    public String getTagString() { return "String"; }

    /**
     *
     * @return
     */
    public String getValues()
    {
        return String.format("string_index=0x%02x", stringIndex);
    }
}


/**
 * Abstract superclass for the three CONSTANT_xxxref entry types. These
 * contain references to a ConstantClass entry and to a ConstantNameAndType
 * entry (both just raw indexes until resolveReferences() is called).
 */
abstract class ConstantRef extends CPEntry
{
    private int classIndex;
    private int nameAndTypeIndex;

    private ConstantClass classEntry = null;
    private ConstantNameAndType nameAndTypeEntry = null;

    /**
     * @param dis
     * @throws IOException
     */
    public ConstantRef(DataInputStream dis) throws IOException
    {
        this.classIndex = dis.readUnsignedShort();
        this.nameAndTypeIndex = dis.readUnsignedShort();
    }

    /**
     * @param cp
     * @throws InvalidConstantPoolIndex
     */
    public void resolveReferences(ConstantPool cp)
            throws InvalidConstantPoolIndex
    {
        this.classEntry = (ConstantClass)cp.getEntry(classIndex);
        this.nameAndTypeEntry =
                (ConstantNameAndType)cp.getEntry(nameAndTypeIndex);
    }

    /**
     * @return classIndex
     */
    public int getClassIndex()          { return classIndex; }

    /**
     *
     * @return nameTypeIndex
     */
    public int getNameAndTypeIndex()    { return nameAndTypeIndex; }

    /**
     * @return calssentry
     */
    public String getClassName()        { return classEntry.getName(); }

    /**
     *
     * @return
     */
    public String getName()             { return nameAndTypeEntry.getName(); }

    /**
     *
     * @return
     */
    public String getType()             { return nameAndTypeEntry.getType(); }

    /**
     *
     * @return
     */
    public String getValues()
    {
        return String.format("class_index=0x%02x, name_and_type_index=0x%02x",
                classIndex, nameAndTypeIndex);
    }
}

/** Represents a CONSTANT_Fieldref entry (tag == 9). */
class ConstantFieldRef extends ConstantRef
{
    /**
     * @param dis
     * @throws IOException
     */
    public ConstantFieldRef(DataInputStream dis) throws IOException
    {
        super(dis);
    }

    /**
     * @return
     */
    public String getTagString() { return "Fieldref"; }
}

/** Represents a CONSTANT_Methodref entry (tag == 10). */
class ConstantMethodRef extends ConstantRef
{
    /**
     * @param dis
     * @throws IOException
     */
    public ConstantMethodRef(DataInputStream dis) throws IOException
    {
        super(dis);
    }

    /**
     * @return
     */
    public String getTagString() { return "Methodref"; }
}

/** Represents a CONSTANT_InterfaceMethodref entry (tag == 11). */
class ConstantInterfaceMethodRef extends ConstantRef
{
    /**
     * @param dis
     * @throws IOException
     */
    public ConstantInterfaceMethodRef(DataInputStream dis) throws IOException
    {
        super(dis);
    }

    /**
     * @return
     */
    public String getTagString() { return "InterfaceMethodref"; }
}

/**
 * Represents a CONSTANT_NameAndType entry (tag == 12). This holds references
 * to two Utf8 entries containing the "name" and "type" (or descriptor) of a
 * field or method (both just raw indexes until resolveReferences() is
 * called).
 */
class ConstantNameAndType extends CPEntry
{
    private int nameIndex;
    private int typeIndex;

    private ConstantUtf8 nameEntry = null;
    private ConstantUtf8 typeEntry = null;

    /**
     * @param dis
     * @throws IOException
     */
    public ConstantNameAndType(DataInputStream dis) throws IOException
    {
        this.nameIndex = dis.readUnsignedShort();
        this.typeIndex = dis.readUnsignedShort();
    }

    /**
     * @param cp
     * @throws InvalidConstantPoolIndex
     */
    public void resolveReferences(ConstantPool cp)
            throws InvalidConstantPoolIndex
    {
        this.nameEntry = (ConstantUtf8)cp.getEntry(nameIndex);
        this.typeEntry = (ConstantUtf8)cp.getEntry(typeIndex);
    }

    /**
     * @return naem index
     */
    public int getNameIndex()    { return nameIndex; }

    /**
     * @return type
     */
    public int getTypeIndex()    { return typeIndex; }

    /**
     * @return nameEnty
     */
    public String getName()      { return nameEntry.getBytes(); }

    /**
     * @return typenety
     */
    public String getType()      { return typeEntry.getBytes(); }

    /**
      * @return
     */
    public String getTagString() { return "NameAndType"; }

    /**
     *
     * @return
     */
    public String getValues()
    {
        return String.format("name_index=0x%02x, type_index=0x%02x",
                nameIndex, typeIndex);
    }
}


/**
 * Represents a CONSTANT_MethodHandle entry (tag == 15).
 */
class ConstantMethodHandle extends CPEntry
{
    private byte kind;
    private int index;
    private CPEntry entry = null;

    /**
     *
     * @param dis
     * @throws IOException
     */
    public ConstantMethodHandle(DataInputStream dis)
            throws IOException
    {
        this.kind = dis.readByte();
        this.index = dis.readUnsignedShort();
    }

    /**
     *
     * @param cp
     * @throws InvalidConstantPoolIndex
     */
    public void resolveReferences(ConstantPool cp)
            throws InvalidConstantPoolIndex
    {
        this.entry = cp.getEntry(index);
    }

    /**
     *
     * @return
     */
    public byte getKind()     { return kind; }

    /**
     *
     * @return
     */
    public int getIndex()     { return index; }

    /**
     *
     * @return
     */
    public CPEntry getEntry() { return entry; }

    /**
     *
     * @return
     */
    public String getTagString() { return "MethodHandle"; }

    /**
     *
     * @return
     */
    public String getValues()
    {
        return String.format("reference_kind=%d, reference_index=0x%02x",
                kind, index);
    }
}


/**
 * Represents a CONSTANT_MethodType entry (tag == 16).
 */
class ConstantMethodType extends CPEntry
{
    private int index;
    private ConstantUtf8 entry = null;

    /**
     *
     * @param dis
     * @throws IOException
     */
    public ConstantMethodType(DataInputStream dis) throws IOException
    {
        this.index = dis.readUnsignedShort();
    }

    /**
     *
     * @param cp
     * @throws InvalidConstantPoolIndex
     */
    public void resolveReferences(ConstantPool cp)
            throws InvalidConstantPoolIndex
    {
        this.entry = (ConstantUtf8)cp.getEntry(index);
    }

    /**
     *
     * @return
     */
    public int getIndex()   { return index; }

    /**
     *
     * @return
     */
    public String getType() { return entry.getBytes(); }

    /**
     *
     * @return
     */
    public String getTagString() { return "MethodType"; }

    /**
     *
     * @return
     */
    public String getValues()
    {
        return String.format("descriptor_index=0x%02x", index);
    }
}


/**
 * Represents a CONSTANT_InvokeDynamic entry (tag == 18). Such entries are used
 * in conjunction with the invokedynamic instruction to determine which method
 * should actually be invoked.
 */
class ConstantInvokeDynamic extends CPEntry
{
    private int bootstrapMethodIndex;
    private int nameAndTypeIndex;

    private ConstantNameAndType nameAndTypeEntry = null;

    /**
     *
     * @param dis
     * @throws IOException
     */
    public ConstantInvokeDynamic(DataInputStream dis) throws IOException
    {
        this.bootstrapMethodIndex = dis.readUnsignedShort();
        this.nameAndTypeIndex = dis.readUnsignedShort();
    }

    /**
     *
     * @param cp
     * @throws InvalidConstantPoolIndex
     */
    public void resolveReferences(ConstantPool cp)
            throws InvalidConstantPoolIndex
    {
        this.nameAndTypeEntry =
                (ConstantNameAndType)cp.getEntry(nameAndTypeIndex);
    }

    /**
     *
     * @return
     */
    public int getBootstrapMethodIndex() { return bootstrapMethodIndex; }

    /**
     *
     * @return
     */
    public int getNameAndTypeIndex()     { return nameAndTypeIndex; }

    /**
     *
     * @return
     */
    public String getName()
    { return nameAndTypeEntry.getName(); }

    /**
     *
     * @return
     */
    public String getType()
    { return nameAndTypeEntry.getType(); }

    /**
     *
     * @return
     */
    public String getTagString() { return "InvokeDynamic"; }

    /**
     *
     * @return s
     */
    public String getValues()
    {
        return String.format(
                "bootstrap_method_attr_index=0x%02x, " +
                        "name_and_type_index=0x%02x",
                bootstrapMethodIndex, nameAndTypeIndex);
    }
}


/**
 * Thrown when an unknown tag value is encountered (i.e. one that does not
 * indicate a known constant pool entry type.)
 */
class InvalidTagException extends ClassFileParserException
{
    public InvalidTagException(String msg) { super(msg); }
}
