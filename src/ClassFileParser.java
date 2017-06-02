/**
 * Created by VAWANIR on 5/14/2017.
 */
import java.io.*;
public class ClassFileParser
{
    private static int x;

    /**
     * @param file
     * @param callee
     * @throws IOException
     * @throws ClassFileParserException
     */
    public ClassFileParser(String file, String callee)
            throws IOException, ClassFileParserException {
        ClassFile cf = new ClassFile(file,callee);
        x= cf.uniqueset();
    }

    /**
     * @param args passes file name and the methodname
     */
    public static void main(String[] args)
    {
        if(args.length == 2)
        {
            File f = new File(args[0]);
            if (f.exists() && !f.isDirectory()){
                try
                {
                    System.out.println
                            ("**\tThis is the Caller Tree for "
                                    + args[1]+" " +
                                    "of this class "+args[0]+" **\n");
                    ClassFile cf = new ClassFile(args[0],args[1]);
                    System.out.println("\n");
                    System.out.println("******************" +
                            "***************************");
                    System.out.println
                            ("*\tNo of unique Methods & Constructors "
                            +(cf.uniqueset()+x));
                    System.out.println("********************" +
                            "*************************");
                    System.out.println("\n");
                    System.out.println("*********************" +
                            "************************");
                    System.out.println("*\t No of Classes Involved :"
                            +cf.getClassesCount());
                    System.out.println("******************" +
                            "***************************");
                }
                catch(IOException e)
                {
                    System.out.printf("Cannot read \"%s\": %s\n",
                            args[0], e.getMessage());
                }
                catch(ClassFileParserException e)
                {
                    System.out.printf("Class file format error in \"%s\": %s\n",
                            args[0], e.getMessage());
                }
            }else{
                System.out.println("This File Dosent Exist");
            }

        }
        else
        {
            System.out.println
                    ("Usage: java ClassFileParser <class-file> <Method Name>");
        }
    }

}
