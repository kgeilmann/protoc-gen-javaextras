package com.github.kgeilmann.javaprotocextras.setters;

import com.github.kgeilmann.javaprotocextras.ExtrasGenerator;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.compiler.PluginProtos;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static com.github.kgeilmann.javaprotocextras.Utils.capitalizedJavaName;
import static com.github.kgeilmann.javaprotocextras.Utils.fileName;

public class UnwrappedSetterGenerator extends ExtrasGenerator {

    private static final Map<String, Class<?>> unwrappedTypes = Map.of(//@formatter:off
            "com.google.protobuf.DoubleValue", double.class,
            "com.google.protobuf.FloatValue", float.class,
            "com.google.protobuf.Int64Value", long.class,
            "com.google.protobuf.Int32Value", int.class,
            "com.google.protobuf.BoolValue", boolean.class,
            "com.google.protobuf.StringValue", String.class
    );//@formatter:on

    protected Stream<PluginProtos.CodeGeneratorResponse.File> handleMessageType(
            DescriptorProtos.FileDescriptorProto fileDesc, final DescriptorProtos.DescriptorProto messageTypeDesc) {
        var fileName = fileName(fileDesc, messageTypeDesc);
        var insertionPoint = "builder_scope:" + fileDesc.getPackage() + "." + messageTypeDesc.getName();

        return messageTypeDesc.getFieldList()
                              .stream()
                              .map(fieldDesc -> this.handleField(fileName, insertionPoint, fieldDesc))
                              .filter(Objects::nonNull);
    }

    private PluginProtos.CodeGeneratorResponse.File handleField(String fileName, String insertionPoint,
            DescriptorProtos.FieldDescriptorProto fieldDescriptorProto) {
        var setter = generate(fieldDescriptorProto);
        if (StringUtils.isBlank(setter)) {
            return null;
        }
        return makeFile(fileName, setter, insertionPoint);

    }

    String generate(final DescriptorProtos.FieldDescriptorProto fieldDescriptorProto) {
        var javaFieldName = capitalizedJavaName(fieldDescriptorProto.getName());
        var javaType = protoTypeMap.toJavaTypeName(fieldDescriptorProto.getTypeName());
        var unwrappedType = unwrappedTypes.get(javaType);
        if (unwrappedType == null) {
            return null;
        }

        Map<?, ?> context = ImmutableMap.builder()
                                        .put("fieldName", javaFieldName)
                                        .put("unwrappedJavaType", unwrappedType.getSimpleName())
                                        .put("javaType", javaType)
                                        .build();
        return applyTemplate("templates/setters/Set_Unwrapped.mustache", context);
    }

}
