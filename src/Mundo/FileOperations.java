package Mundo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.util.ArrayList;

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
    public void changeDirectory(String nPath) {
        File f = new File(localFilePath);
        String p = "";
        try {
            p = textUtils.getCleanPath(nPath);
            localFilePath = f.getPath().concat("\\" + p);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * lista los archivos del directorio
     */
    public void listFiles() {
        File lf = new File(localFilePath);
        try {
            DirectoryStream<Path> files = Files.newDirectoryStream(lf.toPath());
            files
                .forEach(e -> {
                    System.out.println(
                            textUtils.getCleanPath(
                                e.toFile().getPath()
                            )
                    );
                });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * lista los archivos de un zip
     */
    public void listZipEntries(String zipFilePath) {
        try {
            fileUtils.zipEntries(zipFilePath);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * start or open a file
     * @param filePath: path of the file to open
     */
    public void startOrOpenFile(String filePath) {
        File f = new File(filePath);
        try {
            if(f.exists()) {
                Runtime.getRuntime().exec(
                        "pwsh -NoProfile -Command start " +
                        f.getPath()
                );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * read the file lines like the cat command
     */
    public String readFileLines(String fileName) {
        BufferedReader reader = null;
        String p = textUtils.getCleanPath(fileName);
        StringBuffer b = new StringBuffer();
        File f = new File(p);
        try {
            if(f.isDirectory()) {
                throw new Exception("CANNOT READ LINES USING DIRECTORIES");
            }
            reader = new BufferedReader(new FileReader(f));
            while(reader.ready()) {
                b.append(reader.readLine() + "\n");
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                reader = null;
            }
        }
        return b.toString();
    }
    /**
     * search for a file or folder according to Cli option
     * @param filePath: path to search for the cli context
     * @param cliOption: option to make the search
     * @param cliContext: context to search in the given path
     */
    public void searchFileOrFolder(String filePath, String cliOption, String cliContext) {
        System.out.println("SEARCHING ...");
        try {
            File f = new File(filePath);
            switch(cliOption) {
                case "-td":
                    fileUtils.areSimilarDirs(
                            f,
                            cliOption,
                            cliContext
                    );
                    break;
                default:
                    ArrayList<File> fileNames = fileUtils.listFilesFromPath(filePath);
                    fileNames
                        .parallelStream()
                        .filter(e -> fileUtils.areSimilar(e, cliOption, cliContext))
                        .forEach(e -> {
                            fileUtils.printFilePath(e);
                        });
                    break;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * helper method to show the result of search line of file. 
     * @param lines: the file lines
     * @param filePath: the filePath to read
     * @param cliOption: the option to use in the search
     * @param cliContext: the search sentence
     */
    private synchronized void searchLine(String[] lines, String filePath, String option, String context) {
        System.out.println("\n \tSEARCHING ...\n");
        for(int i=0; i<lines.length; ++i) {
            String line = lines[i];
            if(context.isEmpty()) {
                System.out.println(
                        String.format(
                            "%s:%d:%s",
                            filePath,
                            i+1,
                            line
                        )
                );
            } else if(fileUtils.areSimilarLines(option, line, context)) {
                System.out.println(
                        String.format(
                            "%s:%d:%s",
                            filePath,
                            i+1,
                            line
                        )
                );
            }
        }
    }
    /**
     * search for a pattern or line inside files according to the CLI options
     * @param filePath: path to search for the cli context
     * @param cliOption: option to make the search
     * @param cliContext: context to search in the given path
     */
    public void searchFileLine(String filePath, String cliOption, String cliContext) {
        File f = new File(filePath);
        if(f.isFile()) {
            String[] lines = readFileLines(filePath).split("\n");
            searchLine(
                    lines,
                    filePath,
                    cliOption,
                    cliContext
            );
        } else {
            ArrayList<File> files = fileUtils.listFilesFromPath(f.getPath());
            files
                .parallelStream()
                .map(e -> e.getPath())
                .forEach(e -> {
                    searchFileLine(e, cliOption, cliContext);
                });
        }
    }
    /**
     * crea un directorio en el directorio local
     * @param directoryNames: nombre del directorio a crear
     * @throws Execute no soporta crear varios directorios separados por ","
     */
    public void createDirectories(String directoryNames) {
        try {
            File lf = new File(localFilePath);
            String d = textUtils.getCleanPath(directoryNames);
            int c = textUtils.countNestedLevels(d);
            if(c <= 1) {
                String nd = lf.getPath() + "\\"+ d;
                File f = new File(nd);
                if(!f.exists()) {
                    if(f.mkdir()) {
                        System.out.println("CREATED: " + f.getPath());
                    }
                }
            } else if(c > 1) {
                fileUtils.createParentFile(lf.getPath(), d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * create a file with the given path
     * @param fileName: path or name of the file
     */
    public void createFiles(String fileName) {
        try {
            String 
                cf    = textUtils.getCleanPath(fileName),
                nFile = localFilePath + "\\" + cf;
            File f = new File(nFile);
            if(!f.exists() && f.createNewFile()) {
                System.out.println(
                        String.format(
                            "FILE: %S HAS BEEN CREATED",
                            f.getName()
                        )
                );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * compress the files of the given path into a zip file
     * @param givenPath: path of the files to compress
     * @param includeFiles: files or names or patters to exlude in the compression
     */
    public void compressFilesInPath(String givenPath, String includeFiles) {
        try {
            File f = new File(givenPath);
            if(!f.exists()) {
                throw new IOException("FILE NOT FOUND");
            }
            String ln = fileUtils.getLocalName(localFilePath);
            fileUtils.createZipFile(
                    f,
                    new File(ln + ".zip"),
                    includeFiles
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * de-compre all the files inside the compressed archive
     * -l list the files inside the compressed archive
     */
    public void deCompressFilesInPath(String givenPath, String listOption) {
        try {
            File f = new File(givenPath);
            if(!f.exists()) { throw new Exception("file not found"); }
            if(listOption != null) { 
                throw new Exception("not implemented yet");
            } else {
                fileUtils.createUnZipFile(givenPath, localFilePath);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * elimina un directorio deseado o varios directorios
     * <br> pre: </br> si el directorio tiene archivos primero eliminar los archivos  luego el directorio
     * @param directoryPath: direccion del directorio(s) a eliminar
     */
    public void deleteDirectories(String filePath) {
        try {
            File f = new File(filePath);
            if(f.exists()) {
                if(f.isDirectory() && f.listFiles() != null) {
                    for(File mf: f.listFiles()) {
                        deleteDirectories(mf.getPath());
                    }
                } 
                if(f.isDirectory() && f.delete()) {
                    System.out.println(
                            String.format(
                                "File: %s \thas been deleted.",
                                f.getPath()
                            )
                    );
                }
                if(f.isFile() && f.delete()) {
                    System.out.println(
                            String.format(
                                "File: %s \thas been deleted.",
                                f.getPath()
                            )
                    );
                } else {
                    deleteDirectories(f.getPath());
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
    public void deleteFilesFromPath(String deletePath) {
        try {
            File f = new File(deletePath);
            if(f.exists() && f.isFile()) {
                 System.out.println(
                         f.delete() ?
                         String.format("File: %s\thas been deleted", f.getPath()) : ""
                 );
            } else if(f.isDirectory() && f.listFiles() != null) {
                for(File mf: f.listFiles()) {
                    deleteFilesFromPath(mf.getPath());
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * mueve los archivos de source to target
     * <br> post: </br> los archivos de source seran eliminados
     * @param sourceFilePath: direccion source de archivos
     * @param targetFilePath: direccion target para los archivos
     */
    public void moveFromSourceToTarget(String sourceFilePath, String targetFilePath) {
        try {
            if(sourceFilePath.equals(targetFilePath)) {
                throw new Exception("CANNOT MOVE SOURCE FILE TO TARGET FILE");
            }
            File sf = new File(sourceFilePath);
            File tf = new File(targetFilePath);
            if(sf.exists() && tf.exists() && !sf.getPath().contains("git")) {
                Path sp = sf.toPath();
                Path tp = tf.toPath();
                Path mp = Files.move(
                        sp,
                        tp.resolve(sp.getFileName()),
                        StandardCopyOption.REPLACE_EXISTING
                );
                if(!mp.toFile().getName().isEmpty()) {
                    System.out.println(
                            "MOVED: " +
                            sourceFilePath +
                            " TO: " +
                            targetFilePath
                    );
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * renombra un directorio con otro nombre
     * <br> post: </br> los archivos del source se quedan
     * @param oldName: nombre a cambiar
     * @param newName: nuevo nombre
     */
    public void renameDirectory(String oldName, String newName) {
        try {
            if(oldName.equals(newName)) {
                throw new Exception("CANNOT RENAME");
            }
            File sf = new File(oldName);
            File tf = new File(newName);
            if(!tf.exists()) {
                tf.mkdir();
            }
            if(sf.exists() && tf.exists()) {
                Path sp = sf.toPath();
                Path tp = tf.toPath();
                Files.move(
                        sp,
                        tp,
                        StandardCopyOption.REPLACE_EXISTING
                );
                if(sf.delete()) {
                    System.out.println(
                            "File: " +
                            oldName + " now is: " +
                            newName
                    );
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * copia el directorio source en el target
     * <br> pre: </br> se tienen en cuenta que si en el target no existe el directorio a copiar se crea
     * @param sourceFilePath: ruta del directorio source
     * @param targetFilePath: ruta del directorio target
     */
    public void copyFromSourceToTarget(String sourceFilePath, String targetFilePath) {
        try {
            File sf = new File(sourceFilePath);
            if(sf.isFile()) {
                String sfn = sf.getName();
                File tf = new File(
                        targetFilePath + "\\" +
                        sfn
                );
                System.out.println(
                        Files.copy(
                            new File(sf.getPath()).toPath(),
                            tf.toPath(),
                            StandardCopyOption.COPY_ATTRIBUTES
                        )
                );
            } else if(sf.isDirectory()) {
                ArrayList<File> files = fileUtils.listFilesFromPath(sourceFilePath);
                files
                    .parallelStream()
                    .map(e -> e.getPath())
                    .forEach(e -> {
                        try {
                            String
                                source              = new File(sf.getCanonicalPath()).getParent(),
                                sourceWithoutParent = e.replace(source, "");
                            File target = new File(targetFilePath + "\\" + sourceWithoutParent);
                            fileUtils.createParentFile(target.getPath(), target.getParent());
                            System.out.println(
                                    Files.copy(
                                        new File(e).toPath(),
                                        target.toPath(),
                                        StandardCopyOption.COPY_ATTRIBUTES
                                    )
                            );
                        } catch(Exception err) {
                            err.printStackTrace();
                        }
                    });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
