package com.cxplan.projection.mediate.protocol;

import com.cxplan.projection.mediate.message.MessageUtil;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * Created on 2017/4/21.
 *
 * @author kenny
 */
public class DoubleFieldTypeHandler implements IFieldTypeHandler<Double> {
    @Override
    public void encode(Double value, IoBuffer outBuffer) {
        double val = value;
        outBuffer.put(getType());
        outBuffer.putDouble(val);
    }

    @Override
    public Double decode(IoBuffer inBuffer) {
        if (inBuffer.remaining() < 8) {
            return null;
        } else {
            return inBuffer.getDouble();
        }
    }

    @Override
    public byte getType() {
        return MessageUtil.FIELD_TYPE_DOUBLE;
    }
}
