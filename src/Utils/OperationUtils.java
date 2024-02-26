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
    public void changeDirectoryOperation() {
        if(!optionVerification.verifyFirstFile(i).isEmpty()) {
            fileOperations.changeDirectory(optionVerification.verifyFirstFile(i));
            fileOperations.listFiles();
        }
    }
    /**
     * list entries of zip file
     */
    public void listZipEntriesOperation() {
        if(!optionVerification.verifyFirstFile(i).isEmpty()) {
            fileOperations.listZipEntries(
                    optionVerification.verifyFirstFile(i)
            );
        }
    }
    /**
     * start or open the file or folder
     */
    public void startOrOpenOperation() {
        String file = "";
        if((i+1) < options.length) {
            file = options[i+1];
            fileOperations.startOrOpenFile(file);
        }
    }
    /**
     * read file lines
     */
    public void readFileLinesOperation() {
        if(!optionVerification.verifyFirstFile(i).isEmpty()) {
            fileOperations.readFileLines(optionVerification.verifyFirstFile(i));
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
    public void searchFileOrFolderOperation() {
        String filePath = (i+1) < options.length ? options[i+1] : "";
        String CLI_option = (i+2) < options.length ? options[i+2] : "";
        if(filePath != "" && CLI_option != "") {
            String cliContext = (i+3) < options.length ? options[i+3] : "";
            fileOperations.searchFileOrFolder(filePath, CLI_option, cliContext);
        }
    }
    /**
     */
    public void searchFileLineOperation() {
    }
    /**
     * realiza la opreacion de mover los directorios
     */
    public void moveDirectoryOperation() throws Exception {
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifyFirstFile(i).contains(",") &&
                !optionVerification.verifySecondFile().contains(",") && !optionVerification.verifySecondFile().isEmpty() && optionVerification.verifyAssign()) { 
            fileOperations.moveFromSourceToTarget(
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
                optionVerification.verifyAssign()) {
            for(int j=i+1; j<options.length-2; ++j) {
                String  sFile = options[j].replace(",", "");
                fileOperations.moveFromSourceToTarget(
                        sFile,
                        options[options.length-1]
                );
            }
        }
    }
    /**
     * move the source files to target
     */
    public void moveSourceFilesToTarget() {
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifyFirstFile(i).contains(",") &&
                !optionVerification.verifySecondFile().isEmpty() &&
                !optionVerification.verifySecondFile().contains(",") && options.length < 4) {
            fileOperations.moveFromSourceToTarget(
                    optionVerification.verifyFirstFile(i),
                    optionVerification.verifySecondFile()
            );
        }
        /**
         * move all files of 1 or more sources in 1 target
         */
        if(!options[i+1].contains(",") && optionVerification.verifyAssign()) {
            for(int j=i+1; j<optionVerification.getAssignIndex(); ++j) {
                fileOperations.moveFromSourceToTarget(options[j], options[options.length-1]);
            }
        }
    }
    /**
     * realiza la operacion de renombrar un archivo
     * <br> pre: </br> en realidad es mover el archivo pero en este caso crea el target
     */
    public void renameDirectory() {
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifySecondFile().isEmpty()
                && optionVerification.verifyAssign()) {
            fileOperations.renameDirectory(
                    optionVerification.verifyFirstFile(i),
                    optionVerification.verifySecondFile()
            );
        }
    }
    /**
     * realiza la operacion de crear un directorio
     */
    public void createDirectoryOperation() {
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifyFirstFile(i).contains(",")) {
            fileOperations.createDirectories(optionVerification.verifyFirstFile(i));
        } else {
            for(int j = i+1; j<options.length; ++j) {
                String fFile = options[j].replace(",", "");
                fileOperations.createDirectories(fFile);
            }
        }
    }
    /*
     * creates a file wihtin the CLI options
     */
    public void createFileOperation() {
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifyFirstFile(i).contains(",")) {
            fileOperations.createFiles(optionVerification.verifyFirstFile(i));
        } else {
            for(int j = i+1; j<options.length; ++j) {
                String fileName = options[j].replace(",", "");
                fileOperations.createFiles(fileName);
            }
        }
    }
    /**
     * create a compression CLI option for the operation
     */
    public void createCompressionOperation() throws Exception {
        // -i include only this files not implemented
        int includeIndex = optionVerification.getIndexOfOption("-i");
        String compressPath = "", includeOption = "";
        if(!options[i+1].isEmpty() && includeIndex != -1 && (i+3) < options.length) {
            compressPath = options[i+1];
            includeOption = options[i+3];
            fileOperations.compressFilesInPath(compressPath, includeOption);
        }
        if(includeIndex == -1) {
            compressPath = options[i+1];
            fileOperations.compressFilesInPath(compressPath, null);
        }
    }
    /**
     * create a de-compression CLI option for the operation
     */
    public void createDeCompressOperation() throws Exception {
        // -l list the files inside the compressed archive
        int includeIndex = optionVerification.getIndexOfOption("-l");
        String deCompressPath = "", includeOption = "";
        if(!options[i+1].isEmpty() && includeIndex != -1 && (i+3) < options.length) {
            deCompressPath = options[i+1];
            includeOption = options[i+3];
            fileOperations.deCompressFilesInPath(deCompressPath, includeOption);
        }
        if(includeIndex == -1) {
            deCompressPath = options[i+1];
            fileOperations.deCompressFilesInPath(deCompressPath, null);
        }
    }
    /**
     * realiza la opreacion de eliminar el directorio
     */
    public void deleteDirectoryOperation() {
        if(!optionVerification.verifyAssign()) {
            System.out.println("use --y to delete");
        }
        if(!options[i+1].isEmpty() && optionVerification.verifyAssign() &&
                !options[i+1].contains(",") && options.length < 4) {
            fileOperations.deleteDirectories(options[i+1]);
        }
        if(options[i+1].contains(",") && optionVerification.verifyAssign()) {
            for(int j = i+1; j<options.length; ++j) {
                String fFile = options[j].replace(",", "");
                fileOperations.deleteDirectories(fFile);
            }
        } 
    }
    /**
     * delete files from path but not the folder that contains the files
     */
    public void deleteFilesOperation() {
        if(!optionVerification.verifyAssign()) {
            System.out.println("use --y to delete");
        }
        if(!optionVerification.verifyFirstFile(i).isEmpty() && optionVerification.verifyAssign() && 
                !options[i+1].contains(",") && options.length < 4) {
            fileOperations.deleteFilesFromPath(options[i+1]);
        }
        /**
         * copy all files of 1 or more sources in 1 target
         */
        if(!options[i+1].contains(",") && optionVerification.verifyAssign()) {
            for(int j=i+1; j<optionVerification.getAssignIndex(); ++j) {
                fileOperations.deleteFilesFromPath(options[j]);
            }
        }
        if(options[i+1].contains(",") && optionVerification.verifyAssign()) {
            for(int j=i+1; j<options.length; ++j) {
                String fFile = options[j].replace(",", "");
                fileOperations.deleteFilesFromPath(fFile);
            }
        }
    }
    /**
     * realiza la operacion de copiar source en target
     */
    public void copySourceDirectoryToTargetOperation() {
        //require assignation "source to target"
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifyFirstFile(i).contains(",") &&
                !optionVerification.verifySecondFile().contains(",") &&!optionVerification.verifySecondFile().isEmpty() && optionVerification.verifyAssign()) {
            fileOperations.copyFromSourceToTarget(optionVerification.verifyFirstFile(i), optionVerification.verifySecondFile());
        }
        /** 
        * copy 1 source to more than 1 target
        * not require assignation
        */
        if(!optionVerification.verifyFirstFile(i).contains(",") && optionVerification.verifySecondFile().contains(",")) {
            for(int j=i+3; j<options.length; ++j) {
                String sFile = options[j].replace(",", "");
                fileOperations.copyFromSourceToTarget(optionVerification.verifyFirstFile(i), sFile);
            }
        }
        /**
         * copy more than 1 source to 1 target
         */
        if(optionVerification.verifyFirstFile(i).contains(",") && !options[options.length-2].contains(",")) {
            for(int j=i+1; j<options.length-2; ++j) {
                String  sFile = options[j].replace(",", "");
                fileOperations.copyFromSourceToTarget(sFile, options[options.length-1]);
            }
        }
        /**
         * copy from more than 1 source to more than 1 target
         * assignation is in the middle
         */
        if(optionVerification.verifyFirstFile(i).contains(",") && options[options.length-2].contains(",")) {
            for(int f=i+1; f<optionVerification.getAssignIndex(); ++f) {
                for(int s=optionVerification.getAssignIndex()+1; s<options.length; ++s) {
                    String fFile = options[f].replace(",", "");
                    String sFile = options[s].replace(",", "");
                    fileOperations.copyFromSourceToTarget(fFile, sFile);
                }
            }
        }
    }
    /**
     * copy files from source to target
     */
    public void copyFilesSourceToTarget() {
        if(!optionVerification.verifyFirstFile(i).isEmpty() && !optionVerification.verifyFirstFile(i).contains(",") && !optionVerification.verifySecondFile().isEmpty() &&
                !optionVerification.verifySecondFile().contains(",") && options.length < 4) {
            fileOperations.copyFromSourceToTarget(optionVerification.verifyFirstFile(i), optionVerification.verifySecondFile());
        }
        /**
         * copy all files of 1 or more sources in 1 target
         */
        if(!options[i+1].contains(",") && optionVerification.verifyAssign()) {
            for(int j=i+1; j<optionVerification.getAssignIndex(); ++j) {
                fileOperations.copyFromSourceToTarget(options[j], options[options.length-1]);
            }
        }
        /**
         * copy all files of source in more than 1 target
         */
        if(!options[i+1].contains(",") && options[options.length-2].contains(",") && optionVerification.verifyAssign()) {
            for(int f=i+1; f<optionVerification.getAssignIndex(); ++f) {
                for(int t=optionVerification.getAssignIndex()+1; t<options.length; ++t) {
                    String tFile = options[t].replace(",", "");
                    fileOperations.copyFromSourceToTarget(options[f], tFile);
                }
            }
        }
    }
}
