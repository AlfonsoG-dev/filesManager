import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import Utils.BusquedaUtils;
public class Operaciones {
    /**
     * declaración de la instancia {@link BusquedaUtils}
     */
    private BusquedaUtils utils;
    /**
     */
    private String localFilePath;
    /**
     * constructor de la clase
     * <br> post: </br> inicializa la intancia de {@link BusquedaUtils}
     */
    public Operaciones(String nLocalFilePath) {
        utils = new BusquedaUtils();
        localFilePath = nLocalFilePath;
    }
    /**
     * busca los archivos y puede navegar entre directorios
     * @param nPath: dirección nueva a ver los archivos
     */
    public String ChangeDirectory(String nPath) {
        File localFile = new File(localFilePath);
        try {
            if(nPath.equals("./") == false || nPath.equals("..")) {
                String cPath = nPath.replace(".", "").replace("/", "\\").replace("\\\\", "\\");
                localFilePath = localFile.getCanonicalPath().concat(cPath);
            } else {
                throw new Exception("not implemented yet");
            }
        } catch(Exception e) {
            System.err.println(e);
        }
        return localFilePath;
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
     * copia todos los archivos desde source a target
     * <br> pre: </br> se tienen en cuenta que si en el target no existe el directorio a copiar se crea
     * @param sourceFilePath: ruta del directorio source
     * @param targetFilePath: ruta del directorio target
     */
    public void CopyFilesfromSourceToTarget(String sourceFilePath, String targetFilePath) {
        try {
            String[] fileNames = utils.listFilesFromPath(sourceFilePath).split("\n");
            for(String fn: fileNames) {
                File sourceFile = new File(fn);
                String[] parentName = sourceFile.getCanonicalPath().split("\\\\");
                String targetNames = "";
                for(int i=5; i<parentName.length; ++i) {
                    targetNames += parentName[i] + "\\";
                }
                String cTargetNames = targetNames + ";";
                String[] names = cTargetNames.split(";");
                for(String n: names) {
                    File targetFile = new File(targetFilePath + "\\" + n);
                    utils.CreateParentFile(targetFilePath, targetFile.getParent());
                    Path sourcePath = FileSystems.getDefault().getPath(sourceFile.getParent(), sourceFile.getName());
                    Path targetPath = FileSystems.getDefault().getPath(targetFile.getParent(), targetFile.getName());
                    Files.copy(sourcePath, targetPath, StandardCopyOption.COPY_ATTRIBUTES);
                    System.out.println("archivos copiados a: " + targetFile.getParent());
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
}
