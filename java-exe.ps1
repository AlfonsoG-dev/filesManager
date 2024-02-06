$compile = "javac -Xlint:all -d .\bin\ .\src\*.java .\src\Mundo\*.java .\src\Utils\*.java "
$createJar = "jar -cfm filesManager.jar Manifesto.txt -C .\bin\ ."
$javaCommand = "java -jar filesManager.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand