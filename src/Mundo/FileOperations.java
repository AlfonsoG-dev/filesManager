package Mundo;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import Utils.BusquedaUtils;
import Utils.Formulario;
import Utils.TextUtils;
public class FileOperations {
    /**
     * declaración de la instancia {@link BusquedaUtils}
     */
    private BusquedaUtils busquedaUtils;
    /**
     */
    private Formulario formulario;
    /**
     */
    private TextUtils textUtils;
    /**
     */
    private String localFilePath;
    /**
     * constructor de la clase
     * <br> post: </br> inicializa la intancia de {@link BusquedaUtils}
     */
    public FileOperations(String nLocalFilePath) {
        busquedaUtils = new BusquedaUtils();
        localFilePath = nLocalFilePath;
        formulario = new Formulario();
        textUtils = new TextUtils();
    }
    /**
     * busca los archivos y puede navegar entre directorios
     * @param nPath: dirección nueva a ver los archivos
     */
    public void ChangeDirectory(String nPath) {
        File localFile = new File(localFilePath);
        try {
            if(nPath.equals("./") == false || nPath.equals("..")) {
                String cPath = textUtils.GetCleanPath(nPath);
                localFilePath = localFile.getCanonicalPath().concat(cPath);
            } else {
                throw new Exception("not implemented yet");
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
     * lista los archivos del directorio
     */
    public void ListFiles() {
        try {
            File miLocalFilePath = new File(localFilePath);
            File[] files = miLocalFilePath.listFiles();
            if(files.length == 0) {
                System.out.println("\n\t CARPETA SIN ARCHIVOS \n");
            }
            for(File f: files) {
                System.out.println(f.getCanonicalPath());
            }
        } catch(Exception e) {
            System.err.println(e);
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
            if(directoryNames.startsWith(".")) {
                cDirectory = textUtils.GetCleanPath(directoryNames);
            }
            int count = textUtils.CountFilesInDirectory(cDirectory);
            if(count <= 1) {
                String nDirectory = localFile.getCanonicalPath() + "\\"+ cDirectory;
                File miFile = new File(nDirectory);
                if(miFile.exists() == false) {
                    if(miFile.mkdir() == true) {
                        System.out.println("se creo: " + cDirectory);
                    }
                }
            } else if(count > 1) {
                busquedaUtils.CreateParentFile(localFile.getCanonicalPath(), cDirectory);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    /**
     * elimina un directorio deseado o varios directorios
     * <br> pre: </br> si el directorio tiene archivos primero eliminar los archivos  luego el directorio
     * @param directoryPath: direccion del directorio(s) a eliminar
     */
    public void DeleteDirectories(String filePath, String permiso) {
        try {
            String cFile = textUtils.GetCleanPath(filePath);
            File miFile = new File(cFile);
            if(miFile.exists() && miFile.isFile() &&
                    formulario.ComprobarAccion(permiso) == true) {
                if(miFile.delete() == true) {
                    System.out.println("se elimino el directorio: " + cFile);
                }
            } else if(miFile.exists() && miFile.isDirectory() && miFile.listFiles().length > 0 && 
                    formulario.ComprobarAccion(permiso) == true) {
                boolean b = false;
                File[] files = miFile.listFiles();
                for(File f: files) {
                    b = f.delete();
                    if(b == true) {
                        System.out.println("se elimino el elemento: " + f);
                    }
                }
                if(b == true && miFile.delete() == true) {
                    System.out.println("se elimino el directorio: " + miFile);
                }
            } else if(miFile.exists() && miFile.isDirectory() && miFile.listFiles().length == 0 &&
                    formulario.ComprobarAccion(permiso) == true) {
                if(miFile.delete() == true) {
                    System.out.println("se elimino el directorio: " + miFile);
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
    public void MoveFilesFromSourceToTarget(String sourceFilePath, String targetFilePath) {
        try {
            if(sourceFilePath.equals(targetFilePath)) {
                throw new Exception("no se puede mover un archivo del mismo nombre");
            }
            File sourceFile = new File(sourceFilePath);
            File targetFile = new File(targetFilePath);
            if(sourceFile.exists() && targetFile.exists()) {
                Path sourcePath = sourceFile.toPath();
                Path targetPath = targetFile.toPath();
                Path movePath = Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                if(movePath.toFile().getName().isEmpty() == false) {
                    System.out.println("archivos se movieron de: " + sourceFilePath + " to: " + targetFilePath);
                }
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
    public void CopyFilesfromSourceDirectoryToTargetDirectory(String sourceFilePath, String targetFilePath) {
        try {
            String[] fileNames = busquedaUtils.listFilesFromPath(sourceFilePath).split("\n");

            for(String fn: fileNames) {
                File sourceFile = new File(fn);
                String cTargetNames = textUtils.CreateTargetFromParentPath(sourceFile.getCanonicalPath()) + ";";
                String[] names = cTargetNames.split(";");
                for(String n: names) {
                    File targetFile = new File(targetFilePath + "\\" + n);
                    busquedaUtils.CreateParentFile(targetFilePath, targetFile.getParent());
                    Path sourcePath = sourceFile.toPath();
                    Path targetPath = targetFile.toPath();
                    System.out.println( Files.copy(sourcePath, targetPath, StandardCopyOption.COPY_ATTRIBUTES));
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
}
