package com.github.kgeilmann.javaextras.example;


import com.google.protobuf.BoolValue;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.FloatValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import com.google.protobuf.UInt32Value;
import com.google.protobuf.UInt64Value;
import com.google.protobuf.util.Timestamps;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ExampleMessageTest {

    @Test
    void equal_messages_generated_by_wrapped_and_unwrapped_setters() {
        var wrapped = ExampleMessage.newBuilder()
                                    .setBoolValueField(BoolValue.of(true))
                                    .setDoubleValueField(DoubleValue.of(2.5d))
                                    .setFloatValueField(FloatValue.of(12.4f))
                                    .setInt32ValueField(Int32Value.of(5))
                                    .setInt64ValueField(Int64Value.of(6))
                                    .setUint32ValueField(UInt32Value.of(7))
                                    .setUint64ValueField(UInt64Value.of(8))
                                    .setStringValueField(StringValue.of("hello"))
                                    .build();

        var unwrapped = ExampleMessage.newBuilder()
                                      .setBoolValueField(true)
                                      .setDoubleValueField(2.5d)
                                      .setFloatValueField(12.4f)
                                      .setInt32ValueField(5)
                                      .setInt64ValueField(6)
                                      .setUint32ValueField(7)
                                      .setUint64ValueField(8)
                                      .setStringValueField("hello")
                                      .build();

        assertEquals(wrapped, unwrapped);
    }

    @Test
    void setOrClear_clearsValue_when_called_with_null() {
        var msg = ExampleMessage.newBuilder().setInt64ValueField(52).setOrClearInt64ValueField(null).build();
        assertFalse(msg.hasInt64ValueField());
    }

    @Test
    void setOrClear_setsValue_when_called_with_nonnull_value() {
        var msg = ExampleMessage.newBuilder().setOrClearInt64ValueField(Int64Value.of(58)).build();
        assertEquals(Int64Value.of(58), msg.getInt64ValueField());
    }

    @Test
    void getTimestampAsInstant_returns_correct_value_if_value_is_set() throws ParseException {
        var timestamp = "2021-03-04T12:13:14Z";
        var msg = ExampleMessage.newBuilder().setTimestampField(Timestamps.parse("2021-03-04T12:13:14Z")).build();

        Instant expected = Instant.parse(timestamp);
        assertEquals(expected, msg.getTimestampFieldAsInstant());
    }

    @Test
    void getTimestampAsInstant_returns_correct_value_if_value_is_not_set() {
        var msg = ExampleMessage.newBuilder().clearTimestampField().build();

        assertEquals(Instant.ofEpochMilli(0), msg.getTimestampFieldAsInstant());
    }
}
