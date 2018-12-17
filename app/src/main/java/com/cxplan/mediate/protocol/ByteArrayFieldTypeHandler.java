/**
 * The code is written by ytx, and is confidential.
 * Anybody must not broadcast these files without authorization.
 */
package com.cxplan.mediate.protocol;

import com.cxplan.mediate.message.MessageUtil;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * Created on 2017/4/21.
 * byte array handler
 *
 * @author kenny
 */
public class ByteArrayFieldTypeHandler implements IFieldTypeHandler<byte[]> {
    @Override
    public void encode(byte[] value, IoBuffer outBuffer) {
        byte[] val = value;
        outBuffer.put(getType());
        outBuffer.putInt(val.length);
        outBuffer.put(val);
    }

    @Override
    public byte[] decode(IoBuffer inBuffer) {
        if (inBuffer.remaining() < 4) {
            return null;
        }
        inBuffer.mark();
        int length = inBuffer.getInt();
        if (inBuffer.remaining() < length) {
            inBuffer.reset();
            return null;
        }
        byte[] bytes = new byte[length];
        inBuffer.get(bytes);

        return bytes;
    }

    @Override
    public byte getType() {
        return MessageUtil.FIELD_TYPE_ARRAY_BYTE;
    }
}
