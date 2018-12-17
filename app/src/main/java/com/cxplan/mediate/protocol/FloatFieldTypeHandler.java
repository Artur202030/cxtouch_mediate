package com.cxplan.mediate.protocol;

import com.cxplan.mediate.message.MessageUtil;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * Created on 2017/4/21.
 *
 * @author kenny
 */
public class FloatFieldTypeHandler implements IFieldTypeHandler<Float> {
    @Override
    public void encode(Float value, IoBuffer outBuffer) {
        float val = value;
        outBuffer.put(getType());
        outBuffer.putFloat(val);
    }

    @Override
    public Float decode(IoBuffer inBuffer) {
        if (inBuffer.remaining() < 4) {
            return null;
        } else {
            return inBuffer.getFloat();
        }
    }

    @Override
    public byte getType() {
        return MessageUtil.FIELD_TYPE_FLOAT;
    }
}
