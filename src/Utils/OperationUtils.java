package Utils;

import Mundo.FileOperations;
/**
 * clase para ayudar a crear la operaciones de forma ordenada
 */
public final class OperationUtils {
    /**
     * operaciones con los archivos
     */
    private FileOperations fileOperations;
    /**
     * opciones CLI
     */
    private String[] options;
    /**
     * contador de opciones
     */
    private int i;
    /**
     * constructor
     */
    public OperationUtils(String localFilePath, String[] nOptions, int nI) {
        fileOperations = new FileOperations(localFilePath);
        options = nOptions;
        i = nI;
    }
    /**
     * verifica si se asigna el archivo
     * @return el archivo
     */
    public String verifyFirstFile() {
        String res = "";
        if((i+1) < options.length) {
            res = options[i+1];
        }
        return res;
    }
    /**
     * verifica si existe asignación
     * @return true si existe, false de lo contrario
     */
    public boolean VerifyAssign() {
        boolean res = false;
        if((i+2) < options.length && options[i+2].equals("to") ||
                options[i+2].equals("--y")) {
            res = true;
        }
        return res;
    }
    /**
     * verifica si se asigna otro archivo
     * @return el archivo
     */
    public String verifySecondFile() {
        String res = "";
        if((i+3) < options.length) {
            res = options[i+3];
        }
        return res;
    }
    /**
     * realiza la operacion de listar los directorios
     */
    public void ChangeDirectoryOperation() {
        if(verifyFirstFile().isEmpty() == false) {
            fileOperations.ChangeDirectory(verifyFirstFile());
            fileOperations.ListFiles();
        }
    }
    /**
     * realiza la opreacion de mover los directorios
     */
    public void MoveDirectoryOperation() {
        if(verifyFirstFile().isEmpty() == false &&
                verifySecondFile().isEmpty() == false && VerifyAssign() == true) {
            fileOperations.MoveFilesFromSourceToTarget(verifyFirstFile(), verifySecondFile());
        }
    }
    /**
     * realiza la operacion de renombrar un archivo
     * <br> pre: </br> en realidad es mover el archivo pero en este caso crea el target
     */
    public void RenameDirectory() {
        if(verifyFirstFile().isEmpty() == false &&
                verifySecondFile().isEmpty() == false && VerifyAssign() == true) {
            fileOperations.RenameDirectory(verifyFirstFile(), verifySecondFile());
        }
    }
    /**
     * realiza la operacion de crear un directorio
     */
    public void CreateDirectoryOperation() {
        if(verifyFirstFile().isEmpty() == false && verifyFirstFile().contains(",") == false) {
            fileOperations.CreateDirectories(verifyFirstFile());
        } else {
            System.out.println("not implemented yet");
        }
    }
    /**
     * realiza la opreacion de eliminar el directorio
     */
    public void DeleteDirectoryOperation() {
        if(verifyFirstFile().isEmpty() == false &&
                VerifyAssign() == true && verifyFirstFile().contains(",") == false) {
            fileOperations.DeleteDirectories(verifyFirstFile(), "--y");
        } else {
            System.out.println("not implemented yet");
        }
    }
    /**
     * realiza la operacion de copiar source en target
     */
    public void CopySourceDirectoryToTargetOperation() {
        if(verifyFirstFile().isEmpty() == false &&
                verifySecondFile().isEmpty() == false && VerifyAssign() == true) {
            fileOperations.CopyFilesfromSourceDirectoryToTargetDirectory(verifyFirstFile(), verifySecondFile());
        }
    }
}
