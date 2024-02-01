package Utils;

import Mundo.FileOperations;
import Utils.Verifications.OptionVerification;
/**
 * clase para ayudar a crear la operaciones de forma ordenada
 */
public final class OperationUtils {
    /**
     * operaciones con los archivos
     */
    private FileOperations fileOperations;
    /**
     * CLI option verification
     */
    private OptionVerification optionVerification;
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
        optionVerification = new OptionVerification(nOptions);
        options = nOptions;
        i = nI;
    }
    /**
     * realiza la operacion de listar los directorios
     */
    public void ChangeDirectoryOperation() {
        if(!optionVerification.verifyFirstFile(i).isEmpty()) {
            fileOperations.ChangeDirectory(optionVerification.verifyFirstFile(i));
            fileOperations.ListFiles();
        }
    }
    /**
     * start or open the file or folder
     */
    public void StartOrOpenOperation() {
        String file = "";
        if((i+1) < options.length) {
            file = options[i+1];
            fileOperations.StartOrOpenFile(file);
        }
    }
    /**
     * read file lines
     */
    public void ReadFileLinesOperation() {
        if(!optionVerification.verifyFirstFile(i).isEmpty()) {
            fileOperations.ReadFileLines(optionVerification.verifyFirstFile(i));
        }
    }
    /**
     * search for a file or folder with certain options
     * -n search for name
     * -e search for extension 
     * -t search for type:
     *  -td -> type directory
     *  -tf -> type field
     */
    public void SearchFileOrFolderOperation() {
        String filePath = (i+1) < options.length ? options[i+1] : "";
        String CLI_option = (i+2) < options.length ? options[i+2] : "";
        if(filePath != "" && CLI_option != "") {
            String cliContext = (i+3) < options.length ? options[i+3] : "";
            fileOperations.SearchFileOrFolder(filePath, CLI_option, cliContext);
        }
    }
    /**
     * realiza la opreacion de mover los directorios
     */
    public void MoveDirectoryOperation() throws Exception {
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifyFirstFile(i).contains(",") &&
                !optionVerification.verifySecondFile().contains(",") && !optionVerification.verifySecondFile().isEmpty() && optionVerification.VerifyAssign()) { 
            fileOperations.MoveFromSourceToTarget(
                    optionVerification.verifyFirstFile(i),
                    optionVerification.verifySecondFile()
            );
        }
        /** 
        * move 1 source to more than 1 target
        * not require assignation
        */
        if(!optionVerification.verifyFirstFile(i).contains(",") && optionVerification.verifySecondFile().contains(",")) {
            throw new Exception("move to more than 1 target is not possible");
        }
        /**
         * move more than 1 source to 1 target
         */
        if(optionVerification.verifyFirstFile(i).contains(",") && !options[options.length-2].contains(",") &&
                optionVerification.VerifyAssign()) {
            for(int j=i+1; j<options.length-2; ++j) {
                String  sFile = options[j].replace(",", "");
                fileOperations.MoveFromSourceToTarget(
                        sFile,
                        options[options.length-1]
                );
            }
        }
    }
    /**
     * move the source files to target
     */
    public void MoveSourceFilesToTarget() {
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifyFirstFile(i).contains(",") &&
                !optionVerification.verifySecondFile().isEmpty() &&
                !optionVerification.verifySecondFile().contains(",") && options.length < 4) {
            fileOperations.MoveFromSourceToTarget(
                    optionVerification.verifyFirstFile(i),
                    optionVerification.verifySecondFile()
            );
        }
        /**
         * move all files of 1 or more sources in 1 target
         */
        if(!options[i+1].contains(",") && optionVerification.VerifyAssign()) {
            for(int j=i+1; j<optionVerification.GetAssignIndex(); ++j) {
                fileOperations.MoveFromSourceToTarget(options[j], options[options.length-1]);
            }
        }
    }
    /**
     * realiza la operacion de renombrar un archivo
     * <br> pre: </br> en realidad es mover el archivo pero en este caso crea el target
     */
    public void RenameDirectory() {
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifySecondFile().isEmpty()
                && optionVerification.VerifyAssign()) {
            fileOperations.RenameDirectory(
                    optionVerification.verifyFirstFile(i),
                    optionVerification.verifySecondFile()
            );
        }
    }
    /**
     * realiza la operacion de crear un directorio
     */
    public void CreateDirectoryOperation() {
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifyFirstFile(i).contains(",")) {
            fileOperations.CreateDirectories(optionVerification.verifyFirstFile(i));
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
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifyFirstFile(i).contains(",")) {
            fileOperations.CreateFiles(optionVerification.verifyFirstFile(i));
        } else {
            for(int j = i+1; j<options.length; ++j) {
                String fileName = options[j].replace(",", "");
                fileOperations.CreateFiles(fileName);
            }
        }
    }
    /**
     * create a compression CLI option for the operation
     */
    public void CreateCompressionOperation() throws Exception {
        // -i include only this files not implemented
        int includeIndex = optionVerification.GetIndexOfOption("-i");
        String compressPath = "", includeOption = "";
        if(!options[i+1].isEmpty() && includeIndex != -1 && (i+3) < options.length) {
            compressPath = options[i+1];
            includeOption = options[i+3];
            fileOperations.CompressFilesInPath(compressPath, includeOption);
        }
        if(includeIndex == -1) {
            compressPath = options[i+1];
            fileOperations.CompressFilesInPath(compressPath, null);
        }
    }
    /**
     * create a de-compression CLI option for the operation
     */
    public void CreateDeCompressOperation() throws Exception {
        // -l list the files inside the compressed archive
        int includeIndex = optionVerification.GetIndexOfOption("-l");
        String deCompressPath = "", includeOption = "";
        if(!options[i+1].isEmpty() && includeIndex != -1 && (i+3) < options.length) {
            deCompressPath = options[i+1];
            includeOption = options[i+3];
            fileOperations.DeCompressFilesInPath(deCompressPath, includeOption);
        }
        if(includeIndex == -1) {
            deCompressPath = options[i+1];
            fileOperations.DeCompressFilesInPath(deCompressPath, null);
        }
    }
    /**
     * realiza la opreacion de eliminar el directorio
     */
    public void DeleteDirectoryOperation() {
        if(!optionVerification.VerifyAssign()) {
            System.out.println("use --y to delete");
        }
        if(!options[i+1].isEmpty() && optionVerification.VerifyAssign() &&
                !options[i+1].contains(",") && options.length < 4) {
            fileOperations.DeleteDirectories(options[i+1]);
        }
        if(options[i+1].contains(",") && optionVerification.VerifyAssign()) {
            for(int j = i+1; j<options.length; ++j) {
                String fFile = options[j].replace(",", "");
                fileOperations.DeleteDirectories(fFile);
            }
        } 
    }
    /**
     * delete files from path but not the folder that contains the files
     */
    public void DeleteFilesOperation() {
        if(!optionVerification.VerifyAssign()) {
            System.out.println("use --y to delete");
        }
        if(!optionVerification.verifyFirstFile(i).isEmpty() && optionVerification.VerifyAssign() && 
                !options[i+1].contains(",") && options.length < 4) {
            fileOperations.DeleteFilesFromPath(options[i+1]);
        }
        /**
         * copy all files of 1 or more sources in 1 target
         */
        if(!options[i+1].contains(",") && optionVerification.VerifyAssign()) {
            for(int j=i+1; j<optionVerification.GetAssignIndex(); ++j) {
                fileOperations.DeleteFilesFromPath(options[j]);
            }
        }
        if(options[i+1].contains(",") && optionVerification.VerifyAssign()) {
            for(int j=i+1; j<options.length; ++j) {
                String fFile = options[j].replace(",", "");
                fileOperations.DeleteFilesFromPath(fFile);
            }
        }
    }
    /**
     * realiza la operacion de copiar source en target
     */
    public void CopySourceDirectoryToTargetOperation() {
        //require assignation "source to target"
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifyFirstFile(i).contains(",") &&
                !optionVerification.verifySecondFile().contains(",") &&!optionVerification.verifySecondFile().isEmpty() && optionVerification.VerifyAssign()) {
            fileOperations.CopyFromSourceToTarget(optionVerification.verifyFirstFile(i), optionVerification.verifySecondFile());
        }
        /** 
        * copy 1 source to more than 1 target
        * not require assignation
        */
        if(!optionVerification.verifyFirstFile(i).contains(",") && optionVerification.verifySecondFile().contains(",")) {
            for(int j=i+3; j<options.length; ++j) {
                String sFile = options[j].replace(",", "");
                fileOperations.CopyFromSourceToTarget(optionVerification.verifyFirstFile(i), sFile);
            }
        }
        /**
         * copy more than 1 source to 1 target
         */
        if(optionVerification.verifyFirstFile(i).contains(",") && !options[options.length-2].contains(",")) {
            for(int j=i+1; j<options.length-2; ++j) {
                String  sFile = options[j].replace(",", "");
                fileOperations.CopyFromSourceToTarget(sFile, options[options.length-1]);
            }
        }
        /**
         * copy from more than 1 source to more than 1 target
         * assignation is in the middle
         */
        if(optionVerification.verifyFirstFile(i).contains(",") && options[options.length-2].contains(",")) {
            for(int f=i+1; f<optionVerification.GetAssignIndex(); ++f) {
                for(int s=optionVerification.GetAssignIndex()+1; s<options.length; ++s) {
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
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifyFirstFile(i).contains(",") && !optionVerification.verifySecondFile().isEmpty() &&
                !optionVerification.verifySecondFile().contains(",") && options.length < 4) {
            fileOperations.CopyFromSourceToTarget(optionVerification.verifyFirstFile(i), optionVerification.verifySecondFile());
        }
        /**
         * copy all files of 1 or more sources in 1 target
         */
        if(!options[i+1].contains(",") && optionVerification.VerifyAssign()) {
            for(int j=i+1; j<optionVerification.GetAssignIndex(); ++j) {
                fileOperations.CopyFromSourceToTarget(options[j], options[options.length-1]);
            }
        }
        /**
         * copy all files of source in more than 1 target
         */
        if(!options[i+1].contains(",") && options[options.length-2].contains(",") && optionVerification.VerifyAssign()) {
            for(int f=i+1; f<optionVerification.GetAssignIndex(); ++f) {
                for(int t=optionVerification.GetAssignIndex()+1; t<options.length; ++t) {
                    String tFile = options[t].replace(",", "");
                    fileOperations.CopyFromSourceToTarget(options[f], tFile);
                }
            }
        }
    }
}
