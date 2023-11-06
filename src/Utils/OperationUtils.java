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
     * TODO: utilizar las opciones para realizar las opreaciones
     */
    public String VerifySecuence() {
        String res = "";
        for(String s: options) {
            if(s.contains(",")) {
                String[] comas = s.split(",");
                for(String c: comas) {
                    res += c + "\n";
                }
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
                options[i+2].equals("yes")) {
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
        if(VerifySecuence().isEmpty() && verifyFirstFile().isEmpty() == false) {
            fileOperations.ChangeDirectory(verifyFirstFile());
            fileOperations.ListFiles();
        }
    }
    /**
     * realiza la opreacion de mover los directorios
     */
    public void MoveDirectoryOperation() {
        if(VerifySecuence().isEmpty() && verifyFirstFile().isEmpty() == false &&
                verifySecondFile().isEmpty() == false && VerifyAssign() == true) {
            fileOperations.MoveFilesFromSourceToTarget(verifyFirstFile(), verifySecondFile());
        }
    }
    /**
     * realiza la operacion de crear un directorio
     */
    public void CreateDirectoryOperation() {
        if(VerifySecuence().isEmpty() && verifyFirstFile().isEmpty() == false) {
            fileOperations.CreateDirectories(verifyFirstFile());
        }
    }
    /**
     * realiza la opreacion de eliminar el directorio
     */
    public void DeleteDirectoryOperation() {
        if(VerifySecuence().isEmpty() && verifyFirstFile().isEmpty() == false &&
                VerifyAssign() == true) {
            fileOperations.DeleteDirectories(verifyFirstFile(), "yes");
        }
    }
    /**
     * realiza la operacion de copiar source en target
     */
    public void CopySourceDirectoryToTargetOperation() {

        if(VerifySecuence().isEmpty() && verifyFirstFile().isEmpty() == false &&
                verifySecondFile().isEmpty() == false && VerifyAssign() == true) {
            fileOperations.CopyFilesfromSourceDirectoryToTargetDirectory(verifyFirstFile(), verifySecondFile());
        }
    }
}
