$srcClases = ".\src\*.java .\src\Utils\*.java .\src\Mundo\*.java .\src\Utils\Verifications\*.java "
$libFiles = ""
$compile = "javac -Werror -Xlint:all -d .\bin\ $srcClases"
$createJar = "jar -cfm filesManager.jar Manifesto.txt -C .\bin\ ."
$javaCommand = "java -jar filesManager.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand 
