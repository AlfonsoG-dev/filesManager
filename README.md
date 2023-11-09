# FilesManager
>- a basic file system operator
>>- for basic operations like: delete, create, copy and move files or directories

## Features
- [x] delete file or directory and it's content
- [x] make a directory and nested directories
- [x] move the content of the source directory to the target directory
- [x] rename the directory with the given name
- [x] copy a file or directory to target

-------

# Usage

>- you can use the `.jar` file to execute the proyect
```shell
java -jar app.jar operation
```

>- `-ls`: list all files of the given directory
>>- `java -jar app.jar -ls .\src\`

>- `-md`: create a directory
>>- `java -jar app.jar -md directory_name`
>>- cant create directories with: ","
>>- can create nested directories: "test/testTwo"

>- `-mv`: move the content of source to target
>>- `java -jar app.jar -mv .\source to .\target`

>- `-rn`: rename the directory
>>- `java -jar app.jar -rn oldName to newName`
>>>- it's the same as move but this creates the newName directory in runtime
>>>- move need the directory to be created before execute

>- `-cp`: copy a source directory into target directory
>>- `java -jar app.jar -cp source to target`
>>- only copy directories and their content into another directory
>>- cant copy files or the contend of source directory with: `test/*`

>- `-df`: delete a directory and the content inside
>>- you need to give consent: `-df test_file --y`

-------

## TODO'S
- [ ] delete, create using the separator: ","

-------

## Disclaimer
>- this proyect is for educational purposes
>- it is not intended to create a fully functional program
>- securitty issues are not taken into account
