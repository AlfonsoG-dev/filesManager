# FilesManager
>- a basic file system operator
>>- for basic operations like: delete, create, copy and move files or directories

## Features
- [x] delete files or directories
- [x] make directories or nested directories
- [x] move directories or files
- [x] rename directories or files
- [x] copy directories or files

-------

## Usage

>- you can use the `.jar` file to execute the proyect
```shell
java -jar app.jar operation
```
### list

>- `-ls`: list all files of the given directory
>>- `java -jar app.jar -ls .\src\`

### read lines
>- `-rl`: read file lines of the given file
>>- `java -jar app.jar -rl README.md`

### make

>- `-md`: create a directory
>>- `java -jar app.jar -md directory_name`

>- to create directories with: ","
>>- `java -jar app.jar -md directory1, directory2, ...n`


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

>- `-rn`: rename the directory
>>- `java -jar app.jar -rn oldName to newName`
>>>- it's the same as move but this creates the newName directory in runtime and deletes the old directory

### Copy

>- `-cp`: copy a source directory into target directory
>>- `java -jar app.jar -cp source to target`

>- `-cf`: to copy files with: `*`
>>- `java -jar app.jar -cf source/* to target`

>>- supports copy from 1 source to more than 1 target
>>- `-cf source/* to target1, target2`

>>- supports copy from more than 1 source to 1 or more targets
>>- `-cf source1/* source2/* to target1, target2`

>- to copy directories with: ","
>>- `java -jar app.jar -cp source1, source2 to target`

>>- supports copy from 1 source to more than 1 target
>>- `-cp source to target1, target2`

>>- also supports copy form more than 1 source to more than 1 target
>>- `-cp source1, source2 to target1, target2`

### Delete

>- `-df`: delete a directory and the content inside
>>- you need to give consent: `-df test_file --y`
>>- to delete with: ","
>>- `-df test1, test2`

## TODO'S
- [ ] create file info option

-------

## Disclaimer
>- this proyect is for educational purposes
>- it is not intended to create a fully functional program
>- securitty issues are not taken into account
