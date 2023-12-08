# FilesManager
>- a basic file system operator
>>- for basic operations like: delete, create, copy and move files or folders

## Features
- [x] delete files or folders
- [x] make folders or nested directories
- [x] move folders or files
- [x] rename folders or files
- [x] copy folders or files

-------

## Usage

>- you can use the `.jar` file to execute the proyect
```shell
java -jar app.jar operation
```
### list

>- `-ls`: list all files of the given folder
>>- `java -jar app.jar -ls .\src\`

### read lines
>- `-rl`: read file lines of the given file
>>- `java -jar app.jar -rl README.md`

### make

>- `-md`: create a folder
>>- `java -jar app.jar -md folder_name`

>- to create folders with: ","
>>- `java -jar app.jar -md folder1, directory2, ...n`


### move

>- `-mv`: move the content of source to target
>>- `java -jar app.jar -mv .\source to .\target`

>- to move directries with : ","
>>- `java -jar app.jar -mv source1, source2 to target`


>- `-mf`: move files
>>- `java -jar app.jar -mf source/* to target`

>>-supports move from more than 1 source to 1 target
>>- `-mf surce1/* source2/* to target`

### rename

>- `-rn`: rename the folder
>>- `java -jar app.jar -rn oldName to newName`
>>>- it's the same as move but this creates the newName folder in runtime and deletes the old directory

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

### Delete

>- `-df`: delete a folder and the content inside
>>- you need to give consent: `-df test_file --y`
>>- to delete with: ","
>>- `-df test1, test2`

## TODO'S
- [ ] create file or folder compression operation.
- [ ] start or open file(s) or folder(s).
- [ ] find file(s) or folder(s) in the given or current path 
>- find by
>>- pattern
>>- extension
>>- name of file or folder
>>- exclude certain names or folders
>>- apply an operation to the finded file or folder

-------

## Disclaimer
>- this proyect is for educational purposes
>- it is not intended to create a fully functional program
>- securitty issues are not taken into account
