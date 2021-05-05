package com.github.kgeilmann.javaprotocextras;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorRequest;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse;
import com.salesforce.jprotoc.Generator;
import com.salesforce.jprotoc.GeneratorException;
import com.salesforce.jprotoc.ProtoTypeMap;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ExtrasGenerator extends Generator {

    protected ProtoTypeMap protoTypeMap;

    @Override
    public List<CodeGeneratorResponse.File> generateFiles(CodeGeneratorRequest request) throws GeneratorException {
        this.protoTypeMap = ProtoTypeMap.of(request.getProtoFileList());

        return request.getProtoFileList()
                      .stream()
                      .filter(file -> request.getFileToGenerateList().contains(file.getName()))
                      .flatMap(this::handleProtoFile)
                      .collect(Collectors.toList());
    }

    private Stream<CodeGeneratorResponse.File> handleProtoFile(final DescriptorProtos.FileDescriptorProto fileDesc) {
        return fileDesc.getMessageTypeList().stream().flatMap(msg -> this.handleMessageType(fileDesc, msg));
    }

    protected abstract Stream<CodeGeneratorResponse.File> handleMessageType(DescriptorProtos.FileDescriptorProto fileDesc,
            final DescriptorProtos.DescriptorProto messageTypeDesc);

    protected CodeGeneratorResponse.File makeFile(String fileName, String content, String insertionPoint) {
        return makeFile(fileName, content).toBuilder().setInsertionPoint(insertionPoint).build();
    }
}
