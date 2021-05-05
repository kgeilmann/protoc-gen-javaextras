package com.github.kgeilmann.javaprotocextras.setters;

import com.github.kgeilmann.javaprotocextras.ExtrasGenerator;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.compiler.PluginProtos;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static com.github.kgeilmann.javaprotocextras.Utils.capitalizedJavaName;
import static com.github.kgeilmann.javaprotocextras.Utils.fileName;

public class SetOrClearGenerator extends ExtrasGenerator {

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

    private String generate(final DescriptorProtos.FieldDescriptorProto fieldDescriptorProto) {
        var javaFieldName = capitalizedJavaName(fieldDescriptorProto.getName());
        var javaType = protoTypeMap.toJavaTypeName(fieldDescriptorProto.getTypeName());
        var data = Map.of("fieldName", javaFieldName, "javaType", javaType);
        return applyTemplate("templates/setters/SetOrClear.mustache", data);
    }
}
