# protoc-gen-javaextras

Simple protoc plugin to add some convenience methods to the generated java code of protoc.

Look into the example project, to see how to use it. The generated code depends
on `com.google.protobuf:protobuf-java-util:3.15.8` library. 

## Ideas
Some ideas, that might get implemented some day.

* Support nested types
* Combine setOrClear with unwrapped setter
* Use custom options to select which generators to use
* Generate unit tests for the generated code 

