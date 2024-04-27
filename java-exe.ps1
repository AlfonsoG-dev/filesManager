$compile = "javac -Xlint:all -d .\bin\ .\src\*.java -sourcepath .\src\"
$createJar = "jar -cfm filesManager.jar Manifesto.txt -C .\bin\ ."
$javaCommand = "java -jar filesManager.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand
