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
        for(int j=0; j<options.length; ++j) {
            int assignation = options[j].indexOf("to");
            int permision = options[j].indexOf("--y");
            if(assignation == 0 || permision == 0) {
                res = true;
            }
        }
        return res;
    }
    /**
     * da el indice de la asignacion
     */
    private int GetAssignIndex() {
        int  res =0;
        for(int j=0; j<options.length; ++j) {
            int assignation = options[j].indexOf("to");
            if(assignation == 0) {
                res = j;
            }
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
    public void MoveDirectoryOperation() throws Exception {
        if(!verifyFirstFile().isEmpty() && !verifyFirstFile().contains(",") &&
                !verifySecondFile().contains(",") &&!verifySecondFile().isEmpty() && VerifyAssign() == true) { 
            fileOperations.MoveFilesFromSourceToTarget(verifyFirstFile(), verifySecondFile());
        }
        /** 
        * copy 1 source to more than 1 target
        * not require assignation
        */
        if(!verifyFirstFile().contains(",") && verifySecondFile().contains(",")) {
            throw new Exception("move to more than 1 target is not possible");
        }
        /**
         * copy more than 1 source to 1 target
         */
        if(verifyFirstFile().contains(",") && options[options.length-2].contains(",") == false) {
            for(int j=i+1; j<options.length-2; ++j) {
                String  sFile = options[j].replace(",", "");
                fileOperations.MoveFilesFromSourceToTarget(sFile, options[options.length-1]);
            }
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
            for(int j = i+1; j<options.length; ++j) {
                String fFile = options[j].replace(",", "");
                fileOperations.CreateDirectories(fFile);
            }
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
            for(int j = i+1; j<options.length; ++j) {
                String fFile = options[j].replace(",", "");
                fileOperations.DeleteDirectories(fFile, "--y");
            }
        }
    }
    /**
     * realiza la operacion de copiar source en target
     */
    public void CopySourceDirectoryToTargetOperation() {
        //require assignation "source to target"
        if(!verifyFirstFile().isEmpty() && verifyFirstFile().contains(",") == false &&
                verifySecondFile().contains(",") == false &&!verifySecondFile().isEmpty() && VerifyAssign()) {
            fileOperations.CopyFilesfromSourceDirectoryToTargetDirectory(verifyFirstFile(), verifySecondFile());
        }
        /** 
        * copy 1 source to more than 1 target
        * not require assignation
        */
        if(!verifyFirstFile().contains(",") && verifySecondFile().contains(",")) {
            for(int j=i+3; j<options.length; ++j) {
                String sFile = options[j].replace(",", "");
                fileOperations.CopyFilesfromSourceDirectoryToTargetDirectory(verifyFirstFile(), sFile);
            }
        }
        /**
         * copy more than 1 source to 1 target
         */
        if(verifyFirstFile().contains(",") && options[options.length-2].contains(",") == false) {
            for(int j=i+1; j<options.length-2; ++j) {
                String  sFile = options[j].replace(",", "");
                fileOperations.CopyFilesfromSourceDirectoryToTargetDirectory(sFile, options[options.length-1]);
            }
        }
        /**
         * copy from more than 1 source to more than 1 target
         * assignation is in the middle
         */
        if(verifyFirstFile().contains(",") && options[options.length-2].contains(",")) {
            for(int f=i+1; f<GetAssignIndex(); ++f) {
                for(int s=GetAssignIndex()+1; s<options.length; ++s) {
                    String fFile = options[f].replace(",", "");
                    String sFile = options[s].replace(",", "");
                    fileOperations.CopyFilesfromSourceDirectoryToTargetDirectory(fFile, sFile);
                }
            }
        }
    }
}
