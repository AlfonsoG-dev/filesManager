javac -d ./bin/ ./src/*.java ./src/Mundo/*.java ./src/Utils/*.java ./src/Utils/Verifications/*.java

jar -cfm files.jar Manifesto.txt -C ./bin/ .
