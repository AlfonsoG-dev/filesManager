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
            int otherAssignation = options[j].indexOf("--y");
            if(assignation == 0 || otherAssignation == 0) {
                res = j;
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
        if((GetAssignIndex()-1) > 0) {
            res = options[GetAssignIndex()-1];
        }
        return res;
    }
    /**
     * verifica si se asigna otro archivo
     * @return el archivo
     */
    public String verifySecondFile() {
        String res = "";
        if((GetAssignIndex()+1) < options.length) {
            res = options[GetAssignIndex()+1];
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
     * read file lines
     */
    public void ReadFileLinesOperation() {
        if(verifyFirstFile().isEmpty() == false) {
            fileOperations.ReadFileLines(verifyFirstFile());
        }
    }
    /**
     * realiza la opreacion de mover los directorios
     */
    public void MoveDirectoryOperation() throws Exception {
        if(!verifyFirstFile().isEmpty() && !verifyFirstFile().contains(",") &&
                !verifySecondFile().contains(",") &&!verifySecondFile().isEmpty() && VerifyAssign() == true) { 
            fileOperations.MoveFromSourceToTarget(verifyFirstFile(), verifySecondFile());
        }
        /** 
        * move 1 source to more than 1 target
        * not require assignation
        */
        if(!verifyFirstFile().contains(",") && verifySecondFile().contains(",")) {
            throw new Exception("move to more than 1 target is not possible");
        }
        /**
         * move more than 1 source to 1 target
         */
        if(verifyFirstFile().contains(",") == true && options[options.length-2].contains(",") == false &&
                VerifyAssign() == true) {
            for(int j=i+1; j<options.length-2; ++j) {
                String  sFile = options[j].replace(",", "");
                fileOperations.MoveFromSourceToTarget(sFile, options[options.length-1]);
            }
        }
    }
    /**
     * move the source files to target
     */
    public void MoveSourceFilesToTarget() {
        if(!verifyFirstFile().isEmpty() && !verifyFirstFile().contains(",") && !verifySecondFile().isEmpty() &&
                !verifySecondFile().contains(",") && options.length < 4) {
            fileOperations.MoveFromSourceToTarget(verifyFirstFile(), verifySecondFile());
        }
        /**
         * move all files of 1 or more sources in 1 target
         */
        if(!options[i+1].contains(",") && VerifyAssign() == true) {
            for(int j=i+1; j<GetAssignIndex(); ++j) {
                fileOperations.MoveFromSourceToTarget(options[j], options[options.length-1]);
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
    /*
     * creates a file wihtin the CLI options
     */
    public void CreateFileOperation() {
        if(verifyFirstFile().isEmpty() == false && verifyFirstFile().contains(",") == false) {
            fileOperations.CreateFiles(verifyFirstFile());
        } else {
            for(int j = i+1; j<options.length; ++j) {
                String fileName = options[j].replace(",", "");
                fileOperations.CreateFiles(fileName);
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
        } else if(verifyFirstFile().contains(",") == true && VerifyAssign() == true) {
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
            fileOperations.CopyFromSourceToTarget(verifyFirstFile(), verifySecondFile());
        }
        /** 
        * copy 1 source to more than 1 target
        * not require assignation
        */
        if(!verifyFirstFile().contains(",") && verifySecondFile().contains(",")) {
            for(int j=i+3; j<options.length; ++j) {
                String sFile = options[j].replace(",", "");
                fileOperations.CopyFromSourceToTarget(verifyFirstFile(), sFile);
            }
        }
        /**
         * copy more than 1 source to 1 target
         */
        if(verifyFirstFile().contains(",") && options[options.length-2].contains(",") == false) {
            for(int j=i+1; j<options.length-2; ++j) {
                String  sFile = options[j].replace(",", "");
                fileOperations.CopyFromSourceToTarget(sFile, options[options.length-1]);
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
                    fileOperations.CopyFromSourceToTarget(fFile, sFile);
                }
            }
        }
    }
    /**
     * copy files from source to target
     */
    public void CopyFilesSourceToTarget() {
        if(!verifyFirstFile().isEmpty() && !verifyFirstFile().contains(",") && !verifySecondFile().isEmpty() &&
                !verifySecondFile().contains(",") && options.length < 4) {
            fileOperations.CopyFromSourceToTarget(verifyFirstFile(), verifySecondFile());
        }
        /**
         * copy all files of 1 or more sources in 1 target
         */
        if(!options[i+1].contains(",") && VerifyAssign() == true) {
            for(int j=i+1; j<GetAssignIndex(); ++j) {
                fileOperations.CopyFromSourceToTarget(options[j], options[options.length-1]);
            }
        }
        /**
         * copy all files of source in more than 1 target
         */
        if(!options[i+1].contains(",") && options[options.length-2].contains(",") && VerifyAssign() == true) {
            for(int f=i+1; f<GetAssignIndex(); ++f) {
                for(int t=GetAssignIndex()+1; t<options.length; ++t) {
                    String tFile = options[t].replace(",", "");
                    fileOperations.CopyFromSourceToTarget(options[f], tFile);
                }
            }
        }
    }
}
