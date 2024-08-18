srcClases="./src/*.java ./src/Utils/*.java ./src/Mundo/*.java ./src/Utils/Verifications/*.java "
libFiles=""
javac -Werror -Xlint:all -d ./bin/ $srcClases
jar -cfm filesManager.jar Manifesto.txt -C ./bin/ .
java -jar filesManager.jar
