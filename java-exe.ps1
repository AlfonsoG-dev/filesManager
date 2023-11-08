$clases = " ./src/*.java ./src/Utils/*.java ./src/Mundo/*.java"
$compile = "javac -d ./bin/" + "$clases"
$javaCommand = "java -jar test.jar"
$createJarFile = "jar -cfm test.jar Manifesto.txt -C ./bin/ ."
$runCommand = "$compile" + " && " + "$createJarFile" + " && " + "$javaCommand"

Invoke-Expression $runCommand
