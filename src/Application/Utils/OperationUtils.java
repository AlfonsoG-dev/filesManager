package Application.Utils;

import Application.Operations.FileOperations;
import Application.Utils.Verifications.OptionVerification;
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
            fileOperations.changeDirectory(
                optionVerification.verifyFirstFile(i)
            );
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
            String m  = fileOperations.readFileLines(optionVerification.verifyFirstFile(i));
            System.out.println(m);
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
            fileOperations.searchFileOrFolder(
                filePath,
                CLI_option,
                cliContext
            );
        }
    }
    /**
     */
    public void searchFileLineOperation() {
        String
            filePath = optionVerification.verifyFirstFile(i),
            cliOption = (i+2) < options.length ? options[i+2] : "",
            cliContext = (i+3) < options.length ? options[i+3] : "";
        fileOperations.searchFileLine(
            filePath,
            cliOption,
            cliContext
        );
    }
    /**
     * realiza la opreacion de mover los directorios
     */
    public void moveDirectoryOperation() throws Exception {
        boolean 
            conditionA = optionVerification.verifyFirstFile(i).isEmpty(),
            conditionB = optionVerification.verifyFirstFile(i).contains(","),
            conditionC = optionVerification.verifySecondFile().isEmpty(),
            conditionD = optionVerification.verifySecondFile().contains(","),
            conditionE = optionVerification.verifyAssign();
        if(!conditionA && !conditionB && !conditionC && !conditionD && conditionE) { 
            fileOperations.moveFromSourceToTarget(
                optionVerification.verifyFirstFile(i),
                optionVerification.verifySecondFile()
            );
        }
        /** 
        * move 1 source to more than 1 target
        * not require assignation
        */
        if(!conditionB && conditionD) {
            throw new Exception("move to more than 1 target is not possible");
        }
        /**
         * move more than 1 source to 1 target
         */
        if(conditionB && !conditionD && conditionE) {
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
        boolean 
            conditionA = optionVerification.verifyFirstFile(i).isEmpty(),
            conditionB = optionVerification.verifyFirstFile(i).contains(","),
            conditionC = optionVerification.verifySecondFile().isEmpty(),
            conditionD = optionVerification.verifySecondFile().contains(",");
        if(!conditionA && !conditionB && !conditionC && !conditionD && options.length < 4) {
            fileOperations.moveFromSourceToTarget(
                optionVerification.verifyFirstFile(i),
                optionVerification.verifySecondFile()
            );
        }
        /**
         * move all files of 1 or more sources in 1 target
         */
        if(!conditionB && optionVerification.verifyAssign()) {
            for(int j=i+1; j<optionVerification.getAssignIndex(); ++j) {
                fileOperations.moveFromSourceToTarget(
                    options[j],
                    options[options.length-1]
                );
            }
        }
    }
    /**
     * realiza la operacion de renombrar un archivo
     * <br> pre: </br> en realidad es mover el archivo pero en este caso crea el target
     */
    public void renameDirectory() {
        boolean 
            conditionA = optionVerification.verifyFirstFile(i).isEmpty(),
            conditionB = options[options.length-1].equals("to");
        if(conditionA == true || conditionB == true) {
            System.err.println("please give the file path and the new Name for that file");
        }
        if(optionVerification.verifyAssign() == false) {
            System.err.println("give a new name using: to after file path");
        } else if(conditionA == false && conditionB == false) {
            fileOperations.renameDirectory(
                optionVerification.verifyFirstFile(i),
                options[options.length-1]
            );
        }
    }
    /**
     * realiza la operacion de crear un directorio
     */
    public void createDirectoryOperation() {
        boolean 
            conditionA = optionVerification.verifyFirstFile(i).isEmpty(),
            conditionB = optionVerification.verifyFirstFile(i).contains(",");
        if(!conditionA && !conditionB) {
            fileOperations.createDirectories(
                optionVerification.verifyFirstFile(i)
            );
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
        boolean
            conditionA = optionVerification.verifyFirstFile(i).isEmpty(),
            conditionB = optionVerification.verifyFirstFile(i).contains(",");
        if(!conditionA && !conditionB) {
            fileOperations.createFiles(optionVerification.verifyFirstFile(i));
        } else {
            for(int j = i+1; j<options.length; ++j) {
                String fileName = options[j].replace(",", "");
                fileOperations.createFiles(fileName);
            }
        }
    }
    protected String nameIndex() {
        int index = optionVerification.getIndexOfOption("-n");
        if(index != -1) {
            return options[index+1];
        } else {
            return null;
        }
    }
    /**
     * create a compression CLI option for the operation
     */
    public void createCompressionOperation() throws Exception {
        // -i include only this files not implemented
        int includeIndex = optionVerification.getIndexOfOption("-i");
        String 
            compressPath = "",
            includeOption = "",
            name = nameIndex();
        boolean 
            conditionA = optionVerification.verifyFirstFile(i).isEmpty(),
            conditionB = includeIndex != -1;
        if(!conditionA && conditionB && (i+3) < options.length) {
            compressPath = optionVerification.verifyFirstFile(i);
            includeOption = options[i+3];
            fileOperations.compressFilesInPath(
                compressPath,
                includeOption,
                name
            );
        }
        if(!conditionB) {
            compressPath = options[i+1];
            fileOperations.compressFilesInPath(
                compressPath,
                null,
                name
            );
        }
    }
    /**
     * create a de-compression CLI option for the operation
     */
    public void createDeCompressOperation() throws Exception {
        // -l list the files inside the compressed archive
        int includeIndex = optionVerification.getIndexOfOption("-l");
        String deCompressPath = "", includeOption = "";
        boolean 
            conditionA = optionVerification.verifyFirstFile(i).isEmpty(),
            conditionB = includeIndex != -1;
        if(!conditionA && conditionB && (i+3) < options.length) {
            deCompressPath = optionVerification.verifyFirstFile(i);
            includeOption = options[i+3];
            fileOperations.deCompressFilesInPath(deCompressPath, includeOption);
        }
        if(!conditionB) {
            deCompressPath = options[i+1];
            fileOperations.deCompressFilesInPath(deCompressPath, null);
        }
    }
    /**
     * realiza la opreacion de eliminar el directorio
     */
    public void deleteDirectoryOperation() {
        boolean 
            conditionA = optionVerification.verifyFirstFile(i).isEmpty(),
            conditionB = optionVerification.verifyFirstFile(i).contains(","),
            conditionC = optionVerification.verifyAssign();
        if(!conditionC) {
            System.out.println("use --y to delete");
        }
        if(!conditionA && !conditionB && options.length < 4 && conditionC) {
            fileOperations.deleteDirectories(optionVerification.verifyFirstFile(i));
        }
        if(conditionB && conditionC) {
            for(int j = i+1; j<options.length; ++j) {
                String fFile = options[j].replace(",", "");
                if(!fFile.contains("--")) {
                    fileOperations.deleteDirectories(fFile);
                }
            }
        } 
    }
    /**
     * delete files from path but not the folder that contains the files
     */
    public void deleteFilesOperation() {
        boolean 
            conditionA = optionVerification.verifyFirstFile(i).isEmpty(),
            conditionB = optionVerification.verifyFirstFile(i).contains(","),
            conditionC = optionVerification.verifyAssign();
        if(!conditionC) {
            System.out.println("use --y to delete");
        }
        if(!conditionA && conditionC && !conditionB && options.length < 4) {
            fileOperations.deleteFilesFromPath(options[i+1]);
        }
        /**
         * copy all files of 1 or more sources in 1 target
         */
        if(!conditionB && conditionC) {
            for(int j=i+1; j<optionVerification.getAssignIndex(); ++j) {
                fileOperations.deleteFilesFromPath(options[j]);
            }
        }
        if(conditionB && conditionC) {
            for(int j=i+1; j<options.length; ++j) {
                String fFile = options[j].replace(",", "");
                fileOperations.deleteFilesFromPath(fFile);
            }
        }
    }
    protected void copyFileHelper(String target, String source, boolean isReplaceable) {
        fileOperations.copyFromSourceToTarget(
            target,
            source,
            isReplaceable
        );
    }
    /**
     * realiza la operacion de copiar source en target
     */
    public void copySourceDirectoryToTargetOperation() {
        boolean 
            conditionA = optionVerification.verifyFirstFile(i).isEmpty(),
            conditionB = optionVerification.verifyFirstFile(i).contains(","),
            conditionC = optionVerification.verifySecondFile().isEmpty(),
            conditionD = optionVerification.verifySecondFile().contains(","),
            conditionE = optionVerification.verifyAssign(),
            conditionH = optionVerification.verifyIsReplaceable();
        //require assignation "source to target"
        if(!conditionA && !conditionB && !conditionC && !conditionD && conditionE) {
            copyFileHelper(
                optionVerification.verifyFirstFile(i),
                optionVerification.verifySecondFile(),
                conditionH
            );
        }
        /** 
        * copy 1 source to more than 1 target
        * not require assignation
        */
        if(!conditionB && conditionD) {
            for(int j=i+3; j<options.length; ++j) {
                String sFile = options[j].replace(",", "");
                copyFileHelper(
                    optionVerification.verifyFirstFile(i),
                    sFile,
                    conditionH
                );
            }
        }
        /**
         * copy more than 1 source to 1 target
         */
        if(conditionB && !options[options.length-2].contains(",")) {
            for(int j=i+1; j<options.length-2; ++j) {
                String  sFile = options[j].replace(",", "");
                copyFileHelper(
                    sFile,
                    options[options.length-1],
                    conditionH
                );
            }
        }
        /**
         * copy from more than 1 source to more than 1 target
         * assignation is in the middle
         */
        if(conditionB && options[options.length-2].contains(",")) {
            for(int f=i+1; f<optionVerification.getAssignIndex(); ++f) {
                for(int s=optionVerification.getAssignIndex()+1; s<options.length; ++s) {
                    String 
                        fFile = options[f].replace(",", ""),
                        sFile = options[s].replace(",", "");
                    copyFileHelper(fFile, sFile, conditionH);
                }
            }
        }
    }
    /**
     * copy files from source to target
     */
    public void copyFilesSourceToTarget() {
        boolean 
            conditionA = optionVerification.verifyFirstFile(i).isEmpty(),
            conditionB = optionVerification.verifyFirstFile(i).contains(","),
            conditionC = optionVerification.verifySecondFile().isEmpty(),
            conditionD = optionVerification.verifySecondFile().contains(","),
            conditionE = optionVerification.verifyAssign(),
            conditionH = optionVerification.verifyIsReplaceable();
        if(!conditionA && !conditionB && !conditionC && !conditionD && options.length < 4) {
            copyFileHelper(
                optionVerification.verifyFirstFile(i),
                optionVerification.verifySecondFile(),
                conditionH
            );
        }
        /**
         * copy all files of 1 or more sources in 1 target
         */
        if(!conditionB && conditionE) {
            for(int j=i+1; j<optionVerification.getAssignIndex(); ++j) {
                copyFileHelper(
                    options[j],
                    options[options.length-1],
                    conditionH
                );
            }
        }
        /**
         * copy all files of source in more than 1 target
         */
        if(!conditionB && options[options.length-2].contains(",") && conditionE) {
            for(int f=i+1; f<optionVerification.getAssignIndex(); ++f) {
                for(int t=optionVerification.getAssignIndex()+1; t<options.length; ++t) {
                    String tFile = options[t].replace(",", "");
                    copyFileHelper(options[f], tFile, conditionH);
                }
            }
        }
    }
}
