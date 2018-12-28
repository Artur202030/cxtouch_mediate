package com.cxplan.projection.mediate.message;

import android.util.Log;

import com.cxplan.projection.mediate.protocol.BooleanFieldTypeHandler;
import com.cxplan.projection.mediate.protocol.ByteArrayFieldTypeHandler;
import com.cxplan.projection.mediate.protocol.ByteFieldTypeHandler;
import com.cxplan.projection.mediate.protocol.DoubleFieldTypeHandler;
import com.cxplan.projection.mediate.protocol.FloatFieldTypeHandler;
import com.cxplan.projection.mediate.protocol.IFieldTypeHandler;
import com.cxplan.projection.mediate.protocol.IntegerFieldTypeHandler;
import com.cxplan.projection.mediate.protocol.LongFieldTypeHandler;
import com.cxplan.projection.mediate.protocol.ShortFieldTypeHandler;
import com.cxplan.projection.mediate.protocol.StringFieldTypeHandler;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created on 2018/4/17.
 *
 * @author kenny
 */
public class MessageUtil {
    private static final String TAG = "MessageUtil";
    public static final Charset CHARSET_UTF8 = Charset.forName("utf-8");

    public static final byte START_CODE = (byte)0;
    public static final byte FIELD_TYPE_STRING = (byte)10;
    public static final byte FIELD_TYPE_INT = (byte)1;
    public static final byte FIELD_TYPE_BYTE = (byte)2;
    public static final byte FIELD_TYPE_SHORT = (byte)3;
    public static final byte FIELD_TYPE_DOUBLE = (byte)4;
    public static final byte FIELD_TYPE_FLOAT = (byte)5;
    public static final byte FIELD_TYPE_LONG = (byte)6;
    public static final byte FIELD_TYPE_BOOLEAN = (byte)7;
    public static final byte FIELD_TYPE_ARRAY_BYTE = (byte)8;
    /**
     * The commands received by controller
     */
    public static final String CMD_CONTROLLER_IMAGE = "c_image";//The actions related with image channel
    /**
     * The commands sent to device
     */
    public static final String CMD_DEVICE_CREATE_SESSION = "d_create";//create session with device for remote session (controller).
    public static final String CMD_DEVICE_INIT_SESSION = "d_init";//initialize session with device for local session.
    public static final String CMD_DEVICE_SET_NAME = "d_sn";//set name for device.
    public static final String CMD_DEVICE_MONKEY = "d_m";//execute monkey operation.
    public static final String CMD_DEVICE_IMAGE = "d_image";//Tell device something about image channel.
    public static final String CMD_DEVICE_SERVICE = "d_service";//Service management.
    public static final String CMD_DEVICE_IME = "d_ime";//the status of ime.

    public static final String CMD_PING = "ping";
    public static final String CMD_PING_HEART = "heart";//The response command to 'ping'.

    /**
     * The commands sent to IME
     */
    public static final String CMD_DEVICE_INPUTER = "d_inputer";//

    public static final String ERROR_TYPE_NAME = "errorType";

    private static Map<Class, IFieldTypeHandler> typeHandlerMap;
    static {

        typeHandlerMap = new HashMap<Class, IFieldTypeHandler>();
        typeHandlerMap.put(StringFieldTypeHandler.class, new StringFieldTypeHandler());
        typeHandlerMap.put(IntegerFieldTypeHandler.class, new IntegerFieldTypeHandler());
        typeHandlerMap.put(LongFieldTypeHandler.class, new LongFieldTypeHandler());
        typeHandlerMap.put(ShortFieldTypeHandler.class, new ShortFieldTypeHandler());
        typeHandlerMap.put(DoubleFieldTypeHandler.class, new DoubleFieldTypeHandler());
        typeHandlerMap.put(FloatFieldTypeHandler.class, new FloatFieldTypeHandler());
        typeHandlerMap.put(ByteFieldTypeHandler.class, new ByteFieldTypeHandler());
        typeHandlerMap.put(BooleanFieldTypeHandler.class, new BooleanFieldTypeHandler());
        typeHandlerMap.put(ByteArrayFieldTypeHandler.class, new ByteArrayFieldTypeHandler());
    }

    public static IFieldTypeHandler getFieldTypeHandler(Object object) {

        Class<?> clazz;
        if (object instanceof String) {
            clazz = StringFieldTypeHandler.class;
        } else if (object instanceof Integer) {
            clazz = IntegerFieldTypeHandler.class;
        } else if (object instanceof Short) {
            clazz = ShortFieldTypeHandler.class;
        } else if (object instanceof Long) {
            clazz = LongFieldTypeHandler.class;
        } else if (object instanceof Double) {
            clazz = DoubleFieldTypeHandler.class;
        } else if (object instanceof Float) {
            clazz = FloatFieldTypeHandler.class;
        } else if (object instanceof Boolean) {
            clazz = BooleanFieldTypeHandler.class;
        } else if (object instanceof Byte) {
            clazz = ByteFieldTypeHandler.class;
        } else if (object instanceof byte[]) {
            clazz = ByteArrayFieldTypeHandler.class;
        } else  {
            throw new IllegalArgumentException("The type of object is illegal: " + object.getClass().getName());
        }

        return typeHandlerMap.get(clazz);
    }
    public static IFieldTypeHandler getFieldTypeHandler(byte type) {
        Class<?> clazz ;
        switch (type) {
            case FIELD_TYPE_STRING:
                clazz = StringFieldTypeHandler.class;
                break;
            case FIELD_TYPE_INT:
                clazz = IntegerFieldTypeHandler.class;
                break;
            case FIELD_TYPE_LONG:
                clazz = LongFieldTypeHandler.class;
                break;
            case FIELD_TYPE_SHORT:
                clazz = ShortFieldTypeHandler.class;
                break;
            case FIELD_TYPE_DOUBLE:
                clazz = DoubleFieldTypeHandler.class;
                break;
            case FIELD_TYPE_FLOAT:
                clazz = FloatFieldTypeHandler.class;
                break;
            case FIELD_TYPE_BYTE:
                clazz = ByteFieldTypeHandler.class;
                break;
            case FIELD_TYPE_BOOLEAN:
                clazz = BooleanFieldTypeHandler.class;
                break;
            case FIELD_TYPE_ARRAY_BYTE:
                clazz = ByteArrayFieldTypeHandler.class;
                break;
            default:
                throw new IllegalArgumentException("The type is illegal: " + type);

        }

        return typeHandlerMap.get(clazz);
    }
    /**
     * Retrieve type handler for specified object contained in a message.
     * @param object
     * @return
     */
    public static byte getValueType(Object object) {
        if (object instanceof String) {
            return FIELD_TYPE_STRING;
        } else if (object instanceof Integer) {
            return FIELD_TYPE_INT;
        } else if (object instanceof Short) {
            return FIELD_TYPE_SHORT;
        } else if (object instanceof Long) {
            return FIELD_TYPE_LONG;
        } else if (object instanceof Byte) {
            return FIELD_TYPE_BYTE;
        } else if (object instanceof byte[]) {
            return FIELD_TYPE_ARRAY_BYTE;
        } else  {
            throw new IllegalArgumentException("The type of object is illegal: " + object.getClass().getName());
        }
    }

    public static void sendMessage(IoSession session, Message message) throws MessageException {
        if (session == null || !session.isConnected()) {
            throw new MessageException("连接无效，无法发送");
        }

        IoBuffer buffer = message.getBinary();
        buffer.flip();
        int size = buffer.remaining();
        WriteFuture future = session.write(buffer);

        Log.i(TAG, "Send command: " + message.getCommand() + " successfully! size: " + size);
    }

    public static String readStringByInt(IoBuffer buffer, CharsetDecoder decoder) throws MessageException  {
        return readString(buffer, LengthType.INT, decoder);
    }
    public static String readStringByShort(IoBuffer buffer, CharsetDecoder decoder) throws MessageException {
        return readString(buffer, LengthType.SHORT, decoder);
    }
    public static String readStringByByte(IoBuffer buffer, CharsetDecoder decoder) throws MessageException {
        return readString(buffer, LengthType.BYTE, decoder);
    }
    public static String readString(IoBuffer buffer, LengthType lenType, CharsetDecoder decoder) throws MessageException {
        int length;
        if (lenType == LengthType.INT) {
            length = buffer.getInt();
        } else if(lenType == LengthType.SHORT) {
            length = buffer.getShort();
        } else if (lenType == LengthType.BYTE) {
            length = buffer.get();
        } else {
            length = buffer.getInt();
        }

        if (length < 0) {
            return null;
        } if (length == 0) {
            return "";
        } else {
            try {
                return buffer.getString(length, decoder);
            } catch (CharacterCodingException e) {
                throw new MessageException("Reading string failed:" + e.getMessage(), e);
            }
        }
    }
    public static int writeStringByInt(String text, IoBuffer buffer) {
        return writeString(text, buffer, LengthType.INT, CHARSET_UTF8);
    }
    public static int writeStringByShort(String text, IoBuffer buffer) {
        return writeString(text, buffer, LengthType.SHORT, CHARSET_UTF8);
    }
    public static int writeStringByByte(String text, IoBuffer buffer) {
        return writeString(text, buffer, LengthType.BYTE, CHARSET_UTF8);
    }
    public static int writeStringByInt(String text, IoBuffer buffer, Charset charset) {
        return writeString(text, buffer, LengthType.INT, charset);
    }
    public static int writeStringByShort(String text, IoBuffer buffer, Charset charset) {
        return writeString(text, buffer, LengthType.SHORT, charset);
    }
    public static int writeStringByByte(String text, IoBuffer buffer, Charset charset) {
        return writeString(text, buffer, LengthType.BYTE, charset);
    }
    public static int writeString(String text, IoBuffer buffer, LengthType lenType, Charset charset) {
        byte[] data = text == null ? null : text.getBytes(charset);
        int length = text == null ? -1 : data.length;
        int byteCount = length == -1 ? 0 : length;
        if (lenType == LengthType.INT) {
            buffer.putInt(length);
            byteCount += 4;
        } else if (lenType == LengthType.SHORT) {
            buffer.putShort((short)length);
            byteCount += 2;
        } else if (lenType == LengthType.BYTE) {
            buffer.put((byte)length);
            byteCount += 1;
        } else {
            buffer.putInt(length);
            byteCount += 4;
        }

        if (length  <= 0) {
            return byteCount;
        }

        buffer.put(data);

        return byteCount;
    }

    public enum LengthType {
        INT,
        SHORT,
        BYTE
    }

    private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
    /**
     * Pseudo-random number generator object for use with randomString().
     * The Random class is not considered to be cryptographically secure, so
     * only use these random Strings for low to medium security applications.
     */
    private static Random randGen = new Random();
    /**
     * A prefix helps to make sure that ID's are unique across mutliple instances.
     */
    private static String prefix = randomString(5) + "-";

    /**
     * Keeps track of the current increment, which is appended to the prefix to
     * forum a unique ID.
     */
    private static long idIndex = 0;
    /**
     * Returns the next unique id. Each id made up of a short alphanumeric
     * prefix along with a unique numeric value.
     *
     * @return the next id.
     */
    public static synchronized String nextID() {
        return prefix + Long.toString(idIndex++);
    }

    /**
     * Returns a random String of numbers and letters (lower and upper case)
     * of the specified length. The method uses the Random class that is
     * built-in to Java which is suitable for low to medium grade security uses.
     * This means that the output is only pseudo random, i.e., each number is
     * mathematically generated so is not truly random.<p>
     *
     * The specified length must be at least one. If not, the method will return
     * null.
     *
     * @param length the desired length of the random String to return.
     * @return a random String of numbers and letters of the specified length.
     */
    public static String randomString(int length) {
        if (length < 1) {
            return null;
        }
        // Create a char buffer to put random letters and numbers in.
        char [] randBuffer = new char[length];
        for (int i=0; i<randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }
}
