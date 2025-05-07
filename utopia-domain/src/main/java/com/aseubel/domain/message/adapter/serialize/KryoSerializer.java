package com.aseubel.domain.message.adapter.serialize;

import com.aseubel.domain.message.model.MessageEntity;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @date 2025/4/28 下午11:06
 */
public class KryoSerializer {
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(MessageEntity.class);
        kryo.register(LocalDateTime.class);
        kryo.register(String.class);
        return kryo;
    });

    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Output output = new Output(stream);
        kryoThreadLocal.get().writeObject(output, obj);
        output.close();
        return stream.toByteArray();
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Input input = new Input(new ByteArrayInputStream(bytes));
        return kryoThreadLocal.get().readObject(input, clazz);
    }
}
