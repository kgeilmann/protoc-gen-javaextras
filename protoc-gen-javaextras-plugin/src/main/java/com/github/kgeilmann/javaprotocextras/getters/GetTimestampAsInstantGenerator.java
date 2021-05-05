package com.github.kgeilmann.javaprotocextras.getters;

import com.github.kgeilmann.javaprotocextras.ExtrasGenerator;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.compiler.PluginProtos;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static com.github.kgeilmann.javaprotocextras.Utils.capitalizedJavaName;
import static com.github.kgeilmann.javaprotocextras.Utils.fileName;

public class GetTimestampAsInstantGenerator extends ExtrasGenerator {
    protected Stream<PluginProtos.CodeGeneratorResponse.File> handleMessageType(
            DescriptorProtos.FileDescriptorProto fileDesc, final DescriptorProtos.DescriptorProto messageTypeDesc) {
        var fileName = fileName(fileDesc, messageTypeDesc);
        var insertionPoint = "class_scope:" + fileDesc.getPackage() + "." + messageTypeDesc.getName();

        return messageTypeDesc.getFieldList()
                              .stream()
                              .map(fieldDesc -> this.handleField(fileName, insertionPoint, fieldDesc))
                              .filter(Objects::nonNull);
    }

    private PluginProtos.CodeGeneratorResponse.File handleField(String fileName, String insertionPoint,
            DescriptorProtos.FieldDescriptorProto fieldDesc) {
        return Optional.of(fieldDesc)
                       .filter(f -> f.getTypeName().equals(".google.protobuf.Timestamp"))
                       .map(this::generate)
                       .filter(StringUtils::isNotBlank)
                       .map(getter -> makeFile(fileName, getter, insertionPoint))
                       .orElse(null);
    }

    private String generate(final DescriptorProtos.FieldDescriptorProto fieldDesc) {
        var javaFieldName = capitalizedJavaName(fieldDesc.getName());
        var data = Map.of("fieldName", javaFieldName);
        return applyTemplate("templates/getters/GetTimestampAsInstant.mustache", data);
    }
}
