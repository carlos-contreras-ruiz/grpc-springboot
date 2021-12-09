# Documentacion de Spring GRPC
https://github.com/grpc/grpc-java

## Importante 
declarar target/generated-sources/protobuf/java
como una source en inteliji para que la reconozco
#### Project Structure → Modules → Click the generated-sources folder and make it a sources folder.
Instalar el plugion de google funciona mejor que el de inteliji

### Udemy
https://omnitracs.udemy.com/course/grpc-the-complete-guide-for-java-developers/

### Generate the proto files for another languages

target/protoc-plugins/protoc-3.17.2.exe --js_out=./ *.proto

este comando debe ejecuratse desde donde se encuentran los proto files