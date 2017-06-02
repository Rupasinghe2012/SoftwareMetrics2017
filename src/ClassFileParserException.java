/**
 * Created by VAWANIR on 5/14/2017.
 */

/**
 * This method is used to catch the file exceptions
 */
public abstract class ClassFileParserException extends Exception
{
    /**
     *
     * @param msg
     */
    public ClassFileParserException(String msg)
    {
        super(msg);
    }

    /**
     *
     * @param msg
     * @param cause
     */
    public ClassFileParserException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
