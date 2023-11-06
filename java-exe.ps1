$clases = " ./src/*.java ./src/Utils/*.java ./src/Mundo/*.java"
$compile = "javac -d ./bin/" + "$clases"
$javaCommand = "java -cp ./bin/ ./src/App.java"
$createJarFile = "jar -cfm test.jar Manifesto.txt -C ./bin/ ."
$runCommand = "$compile" + " && " + "$javaCommand" + " && " + "$createJarFile"

Invoke-Expression $runCommand
