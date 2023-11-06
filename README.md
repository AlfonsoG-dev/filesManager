# FilesManager
>- a basic file system operator
>>- for basic operations like: delete, create, copy and move files or directories

## Features
- [x] delete directory and it content
- [x] make a directory and nested directories
- [x] move the directory to another
- [x] copy the directory to another

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

>- `-cp`: copy a source directory into target directory
>>- `java -jar app.jar -cp source to target`
>>- only copy directories and their content into another directory
>>- cant copy files or the contend of source directory with: `test/*`

>- `-df`: delete a directory and the content inside
>>- you need to give consent: `-df test_file yes`
>>>- or: `-df test_file no`

-------

## TODO'S
- [ ] delete, create using the separator: ","
- [ ] copy, move only the files and not the entire directory

-------

## Disclaimer
>- this proyect is for educational purposes
>- it is not intended to create a fully functional program
>- securitty issues are not taken into account
