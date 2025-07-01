srcClases="src/*.java src/Application/Operations/*.java src/Application/Utils/*.java src/Application/Utils/Verifications/*.java "
libFiles=""
javac --release 23 -Werror -Xlint:all -d ./bin/ $srcClases
jar -cfm filesManager.jar Manifesto.txt -C ./bin/ .
java -jar filesManager.jar