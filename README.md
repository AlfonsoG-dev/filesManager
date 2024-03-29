# Files Manager
>- a basic file system operator
>>- for basic operations like: delete, create, copy and move files or folders

# Dependencies
- [java_jdk_17.0.8](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

# References 
- [java 17 I/O docs](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/package-summary.html)

## Features
- [x] delete files or folders
- [x] make folders or nested directories
- [x] move folders or files
- [x] rename folders or files
- [x] copy folders or files

-------

## Usage

>- you can use the `.jar` file to execute the project
```shell
java -jar app.jar operation
```
### list

>- `-ls`: list all files of the given folder
>>- `java -jar app.jar -ls .\src\`

### List zip entries

>- `le`: list the entries of a compressed file
>>- `java -jar app.jar -le file.zip`

### read lines
>- `-rl`: read file lines of the given file
>>- `java -jar app.jar -rl README.md`

### Search

>- `-ff`: to search in the given path by:
>- only use to search in folders, it doesn't work with files
>>- extension: `-e ".java"`
>>>- `java -jar app.jar -ff .\bin\ -e ".class"`
>>- name: `-n "textutils"`
>>>- `java -jar app.jar -ff .\bin\ -n "textutils"`

>- `-fl`: search for a word in the given file path by:
>>- `-i`: ignore case
>>- `-s`: smart ignore case
>>>- `java -jar app.jar -fl .\src\Main.jar -s "args"`

### make

>- `-md`: create a folder
>>- `java -jar app.jar -md folder_name`

>- to create folders with: ","
>>- `java -jar app.jar -md folder1, directory2, ...n`

>- `-ni`: create a file
>>- `java -jar app.jar -ni main.py`

>>- supports creation using: ","
>>- `java -jar app.jar -ni main.py, main.java`

### start or open

>- `-st`: start or open
>>- `java -jar app.jar -st .\bin\`

### move

>- `-mv`: move the content of source to target
>>- `java -jar app.jar -mv .\source to .\target`

>- to move directories with : ","
>>- `java -jar app.jar -mv source1, source2 to target`


>- `-mf`: move files
>>- `java -jar app.jar -mf source/* to target`

>>-supports move from more than 1 source to 1 target
>>- `-mf surce1/* source2/* to target`

### rename

>- `-rn`: rename the folder
>>- `java -jar app.jar -rn oldName to newName`
>>>- it's the same as move but this creates the new name folder in run time and deletes the old directory

### Copy

>- `-cp`: copy a source folder into target directory
>>- `java -jar app.jar -cp source to target`

>- `-cf`: to copy files with: `*`
>>- `java -jar app.jar -cf source/* to target`

>>- supports copy from 1 source to more than 1 target
>>- `-cf source/* to target1, target2`

>>- supports copy from more than 1 source to 1 or more targets
>>- `-cf source1/* source2/* to target1, target2`

>- to copy folders with: ","
>>- `java -jar app.jar -cp source1, source2 to target`

>>- supports copy from 1 source to more than 1 target
>>- `-cp source to target1, target2`

>>- also supports copy form more than 1 source to more than 1 target
>>- `-cp source1, source2 to target1, target2`

#### Sub-Command

>- `--r`: used when you need to replace existing resources of the target.
>>- `java -jar -cp .\src\App.java to .\example\ --r`
>- `--c`: used when you need to copy and the source files or directories don't exists in the target.
>>- `java -jar -cp .\src\App.java to .\example\ --c`
>>>- `--c`: also is the default option.

### Delete


>- `-dd`: delete a folder and the content inside
>>- you need to give consent: `-dd test_file --y`
>>- to delete with: ","
>>- `-dd test1, test2 --y`

>- `-df`: delete files
>>- you need to give consent: `-df test_file.c --y`
>>- to delete a file with: ","
>>- `-df test1.c, test2.c --y`


### Compression

>- `-cm`: compress the element of the given path
>>- `java -jar app.jar -cm .\bin\`

>- if you want to control the elements to include in the zip use: `-i`
>>- `java -jar app.jar -cm .\localPath -i "src, bin, .jar`

### De-compression

>- `-dc`: de-compress the elements of the compressed file
>>- `java -jar app.jar -dc .\compressedFile.zip`

-------

## Disclaimer
>- this project is for educational purposes
>- it is not intended to create a fully functional program
>- security issues are not taken into account
