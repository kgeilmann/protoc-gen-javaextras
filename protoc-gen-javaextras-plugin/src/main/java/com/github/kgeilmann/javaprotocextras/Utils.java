package com.github.kgeilmann.javaprotocextras;

import com.google.protobuf.DescriptorProtos;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {

    public static String setterName(String protoFieldName) {
        return Arrays.stream(protoFieldName.split("_"))
                     .map(StringUtils::capitalize)
                     .collect(Collectors.joining("", "set", ""));
    }

    public static String capitalizedJavaName(String protoFieldName) {
        return Arrays.stream(protoFieldName.split("_")).map(StringUtils::capitalize).collect(Collectors.joining());
    }

    public static String javaPackage(DescriptorProtos.FileDescriptorProto fileDesc) {
        var options = fileDesc.getOptions();
        if (options.hasJavaPackage()) {
            return fileDesc.getOptions().getJavaPackage();
        } else {
            return fileDesc.getPackage();
        }
    }

    public static String fileName(DescriptorProtos.FileDescriptorProto fileDesc,
            DescriptorProtos.DescriptorProto messageTypeDesc) {
        var options = fileDesc.getOptions();
        String directory = javaPackage(fileDesc).replace(".", "/");

        String fileName;
        if (options.getJavaMultipleFiles()) {
            fileName = messageTypeDesc.getName() + ".java";
        } else {
            fileName = fileDesc.getName().replace(".proto", ".java");
        }

        return directory + "/" + fileName;
    }

}


