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
     * verifica si existen opciones separadas por ","
     * @return un string con las opciones
     */
    public boolean VerifySecuence() {
        boolean res = false;
        for(int j=1; j<options.length; ++i) {
            if(options[j].equals(",")) {
                res = true;
            }
        }
        return res;
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
        if(VerifySecuence() == false && verifyFirstFile().isEmpty() == false) {
            fileOperations.ChangeDirectory(verifyFirstFile());
            fileOperations.ListFiles();
        }
    }
    /**
     * realiza la opreacion de mover los directorios
     */
    public void MoveDirectoryOperation() {
        if(VerifySecuence() == false && verifyFirstFile().isEmpty() == false &&
                verifySecondFile().isEmpty() == false && VerifyAssign() == true) {
            fileOperations.MoveFilesFromSourceToTarget(verifyFirstFile(), verifySecondFile());
        }
    }
    /**
     * realiza la operacion de crear un directorio
     */
    public void CreateDirectoryOperation() {
        if(VerifySecuence() == false && verifyFirstFile().isEmpty() == false) {
            fileOperations.CreateDirectories(verifyFirstFile());
        }
    }
    /**
     * realiza la opreacion de eliminar el directorio
     */
    public void DeleteDirectoryOperation() {
        if(VerifySecuence() == false && verifyFirstFile().isEmpty() == false &&
                VerifyAssign() == true) {
            fileOperations.DeleteDirectories(verifyFirstFile(), "--y");
        }
    }
    /**
     * realiza la operacion de copiar source en target
     */
    public void CopySourceDirectoryToTargetOperation() {
        if(VerifySecuence() == false && verifyFirstFile().isEmpty() == false &&
                verifySecondFile().isEmpty() == false && VerifyAssign() == true) {
            fileOperations.CopyFilesfromSourceDirectoryToTargetDirectory(verifyFirstFile(), verifySecondFile());
        }
    }
}
