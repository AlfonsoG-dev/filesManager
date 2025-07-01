$srcClases = "src\*.java src\Application\Operations\*.java src\Application\Utils\*.java src\Application\Utils\Verifications\*.java "
$libFiles = ""
$compile = "javac --release 23 -Werror -Xlint:all -d .\bin\ $srcClases"
$createJar = "jar -cfm filesManager.jar Manifesto.txt -C .\bin\ ."
$javaCommand = "java -jar filesManager.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand 
