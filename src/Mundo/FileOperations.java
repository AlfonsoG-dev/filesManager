package Mundo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import Utils.FileUtils;
import Utils.TextUtils;
public class FileOperations {
    /**
     * declaración de la instancia {@link FileUtils}
     */
    private FileUtils fileUtils;
    /**
     * declare the instance of {@link TextUtils}
     */
    private TextUtils textUtils;
    /**
     * declara the local path
     */
    private String localFilePath;
    /**
     * constructor de la clase
     * <br> post: </br> inicializa la intancia de {@link FileUtils}
     */
    public FileOperations(String nLocalFilePath) {
        fileUtils = new FileUtils();
        localFilePath = nLocalFilePath;
        textUtils = new TextUtils();
    }
    /**
     * busca los archivos y puede navegar entre directorios
     * @param nPath: dirección nueva a ver los archivos
     */
    public void ChangeDirectory(String nPath) {
        File localFile = new File(localFilePath);
        try {
            String cPath = textUtils.GetCleanPath(nPath);
            localFilePath = localFile.getPath().concat("\\" + cPath);
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
     * lista los archivos del directorio
     */
    public void ListFiles() {
        try {
            File miFile = new File(localFilePath);
            if(miFile.isDirectory() && miFile.listFiles() !=  null) {
                for(File f: miFile.listFiles()) {
                    System.out.println(f.getPath());
                }
            } else {
                System.out.println("\n\t CARPETA SIN ARCHIVOS \n");
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
     * start or open a file
     * @param filePath: path of the file to open
     */
    public void StartOrOpenFile(String filePath) {
        try {
            File miFile = new File(filePath);
            if(miFile.exists()) {
                Runtime.getRuntime().exec("pwsh -NoProfile -Command start " + miFile.getPath());
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
     * read the file lines like the cat command
     */
    public void ReadFileLines(String fileName) {
        BufferedReader mibuBufferedReader = null;
        try {
            String cPath = textUtils.GetCleanPath(fileName);
            File miFile = new File(cPath);
            if(miFile.isFile()) {
                mibuBufferedReader = new BufferedReader(new FileReader(miFile));
                while(mibuBufferedReader.ready()) {
                    System.out.println(mibuBufferedReader.readLine());
                }
            } else if(new File(cPath).isDirectory()) {
                ListFiles();
            }
        } catch(Exception e) {
            System.err.println(e);
        } finally {
            if(mibuBufferedReader != null) {
                try {
                    mibuBufferedReader.close();
                } catch(Exception e) {
                    System.err.println(e);
                }
                mibuBufferedReader = null;
            }
        }
    }
    /**
     * search for a file or folder according to Cli option
     * @param filePath: path to search for the cli context
     * @param cliOption: option to make the search
     * @param cliContext: context to search in the given path
     */
    public void SearchFileOrFolder(String filePath, String cliOption, String cliContext) {
        String methodResult = "";
        try {
            File miFile = new File(filePath);
            if(cliOption.equals("-tf") || cliOption.equals("-td")) {
                switch(cliOption) {
                    case "-td":
                        if(miFile.isDirectory() && miFile.listFiles() != null) {
                            String[] dirNames = fileUtils.getDirectoryNames(
                                    Files.newDirectoryStream(miFile.toPath())).split("\n");
                            for(String dn: dirNames) {
                                if(new File(dn).getName().toLowerCase().contains(cliContext.toLowerCase())) {
                                    methodResult += dn + "\n";
                                }
                            }
                        } else if(miFile.listFiles() == null && miFile.getName().toLowerCase().contains(cliContext.toLowerCase())) {
                            methodResult += miFile.getPath() + "\n";
                        }
                        break;
                    case "-tf":
                        SearchFileOrFolder(miFile.getPath(), "-n", cliContext);
                        break;
                }
            } else if(miFile.exists() && miFile.isDirectory()) {
                String[] fileNames = fileUtils.listFilesFromPath(filePath).split("\n");
                if(fileNames.length > 0) {
                    for(String fn: fileNames) {
                        if(fileUtils.areSimilar(cliOption, fn, cliContext)) {
                            methodResult += fn  + "\n";
                        }
                    }
                }
            } else {
                throw new Exception("cannot operate with files only with folders");
            }
        } catch(Exception e) {
            System.err.println(e);
        }
        if(methodResult != "") {
            String[] files = methodResult.split("\n");
            for(String f: files) {
                System.out.println(String.format("| %s |", f));
            }
        }
    }
    /**
     * crea un directorio en el directorio local
     * @param directoryNames: nombre del directorio a crear
     * @throws Execute no soporta crear varios directorios separados por ","
     */
    public void CreateDirectories(String directoryNames) {
        try {
            String cDirectory = "";
            File localFile = new File(localFilePath);
            cDirectory = textUtils.GetCleanPath(directoryNames);
            int count = textUtils.CountFilesInDirectory(cDirectory);
            if(count <= 1) {
                String nDirectory = localFile.getPath() + "\\"+ cDirectory;
                File miFile = new File(nDirectory);
                if(!miFile.exists()) {
                    if(miFile.mkdir()) {
                        System.out.println("se creo: " + nDirectory);
                    }
                }
            } else if(count > 1) {
                fileUtils.CreateParentFile(localFile.getPath(), cDirectory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * create a file with the given path
     * @param fileName: path or name of the file
     */
    public void CreateFiles(String fileName) {
        try {
            File localFile = new File(localFilePath);
            String cfileName = textUtils.GetCleanPath(fileName);
            String nFile = localFile.getPath() + "\\" + cfileName;
            File miFile = new File(nFile);
            if(!miFile.exists()) {
                if(miFile.createNewFile()){
                    System.out.println(String.format("file: %s has been created", miFile.getName()));
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
     * compress the files of the given path into a zip file
     * @param givenPath: path of the files to compress
     * @param includeFiles: files or names or patters to exlude in the compression
     */
    public void CompressFilesInPath(String givenPath, String includeFiles) {
        try {
            File miFile = new File(givenPath);
            if(!miFile.exists()) {
                throw new IOException("file not found");
            }
            String localName = fileUtils.getLocalName(localFilePath);
            fileUtils.CreateZipFile(miFile, new File(localName + ".zip"), includeFiles);
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
     * de-compre all the files inside the compressed archive
     * -l list the files inside the compressed archive
     */
    public void DeCompressFilesInPath(String givenPath, String listOption) {
        try {
            File miFile = new File(givenPath);
            if(!miFile.exists()) { throw new Exception("file not found"); }
            if(listOption != null) { 
                throw new Exception("not implemented yet");
            } else {
                fileUtils.CreateUnZipFile(givenPath, localFilePath);
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
     * elimina un directorio deseado o varios directorios
     * <br> pre: </br> si el directorio tiene archivos primero eliminar los archivos  luego el directorio
     * @param directoryPath: direccion del directorio(s) a eliminar
     */
    public void DeleteDirectories(String filePath) {
        try {
            File miFile = new File(filePath);
            if(miFile.exists()) {
                if(miFile.isDirectory() && miFile.listFiles() != null) {
                    for(File mf: miFile.listFiles()) {
                        DeleteDirectories(mf.getPath());
                    }
                } 
                if(miFile.isDirectory() && miFile.delete()) {
                    System.out.println(
                            String.format(
                                "File: %s \thas been deleted.",
                                miFile.getPath()
                            )
                    );
                }
                if(miFile.isFile() && miFile.delete()) {
                    System.out.println(
                            String.format(
                                "File: %s \thas been deleted.",
                                miFile.getPath()
                            )
                    );
                } else {
                    DeleteDirectories(miFile.getPath());
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * delete files from folder given by path
     * <br> pre: </br> if the path is directory delete files leave directory, if its file delete the file
     * <br> post: </br> the files are deted not the folder
     * @param deletePath: path of the files or folders to delete
     */
    public void DeleteFilesFromPath(String deletePath) {
        try {
            File miFile = new File(deletePath);
            if(miFile.exists() && miFile.isFile()) {
                String deleteMessage = miFile.delete() ?
                    String.format("File: %s\thas been deleted", miFile.getPath()) : "";
                 System.out.println(deleteMessage);
            } else if(miFile.isDirectory() && miFile.listFiles() != null) {
                for(File f: miFile.listFiles()) {
                    DeleteFilesFromPath(f.getPath());
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
     * mueve los archivos de source to target
     * <br> post: </br> los archivos de source seran eliminados
     * @param sourceFilePath: direccion source de archivos
     * @param targetFilePath: direccion target para los archivos
     */
    public void MoveFromSourceToTarget(String sourceFilePath, String targetFilePath) {
        try {
            if(sourceFilePath.equals(targetFilePath)) {
                throw new Exception("no se puede mover un archivo del mismo nombre");
            }
            File sourceFile = new File(sourceFilePath);
            File targetFile = new File(targetFilePath);
            if(sourceFile.exists() && targetFile.exists() && !sourceFile.getPath().contains("git")) {
                Path sourcePath = sourceFile.toPath();
                Path targetPath = targetFile.toPath();
                Path movePath = Files.move(
                        sourcePath,
                        targetPath.resolve(sourcePath.getFileName()),
                        StandardCopyOption.REPLACE_EXISTING
                );
                if(!movePath.toFile().getName().isEmpty()) {
                    System.out.println(
                            "archivos se movieron de: " +
                            sourceFilePath +
                            " to: " +
                            targetFilePath
                    );
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
     * renombra un directorio con otro nombre
     * <br> post: </br> los archivos del source se quedan
     * @param oldName: nombre a cambiar
     * @param newName: nuevo nombre
     */
    public void RenameDirectory(String oldName, String newName) {
        try {
            if(oldName.equals(newName)) {
                throw new Exception("no se puede realizar la operacion en archivos del mismo nombre");
            }
            File sourceFile = new File(oldName);
            File targetFile = new File(newName);
            if(!targetFile.exists()) {
                targetFile.mkdir();
            }
            if(sourceFile.exists() && targetFile.exists()) {
                Path sourcePath = sourceFile.toPath();
                Path targetPath = targetFile.toPath();
                Files.move(
                        sourcePath,
                        targetPath,
                        StandardCopyOption.REPLACE_EXISTING
                );
                sourceFile.deleteOnExit();
                System.out.println("el archivo: " + oldName + " se renombra por: " + newName);
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
     * copia el directorio source en el target
     * <br> pre: </br> se tienen en cuenta que si en el target no existe el directorio a copiar se crea
     * @param sourceFilePath: ruta del directorio source
     * @param targetFilePath: ruta del directorio target
     */
    public void CopyFromSourceToTarget(String sourceFilePath, String targetFilePath) {
        try {
            if(new File(sourceFilePath).isFile()) {
                String sourceFileName = new File(sourceFilePath).getName();
                String sourceParent = new File(sourceFilePath).getParent();
                String sourceParentName = new File(sourceParent).getName();
                File targetFile = new File(targetFilePath + "\\" + sourceParentName + "\\" + sourceFileName);
                fileUtils.CreateParentFile(
                        targetFile.getPath(),
                        targetFile.getParent()
                );
                System.out.println(
                        Files.copy(
                            new File(sourceFilePath).toPath(),
                            targetFile.toPath(),
                            StandardCopyOption.COPY_ATTRIBUTES
                        )
                );
            } else if(new File(sourceFilePath).isDirectory()) {
                String[] dirSourceFiles = fileUtils.listFilesFromPath(sourceFilePath).split("\n");
                String sourceParent = new File(sourceFilePath).getParent();
                for(String sourceFiles: dirSourceFiles) {
                    String sourceWithoutParent = sourceFiles.replace(sourceParent, "");
                    File targetFile = new File(targetFilePath + "\\" + sourceWithoutParent);
                    fileUtils.CreateParentFile(targetFile.getPath(), targetFile.getParent());
                    System.out.println(
                            Files.copy(
                                new File(sourceFiles).toPath(),
                                targetFile.toPath(),
                                StandardCopyOption.COPY_ATTRIBUTES
                            )
                    );
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
}
