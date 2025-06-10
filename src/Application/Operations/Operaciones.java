package Application.Operations;

import Application.Utils.OperationUtils;

import java.io.File;
/**
 * clase que se encarga de organizar las opciones según la operacion
 */
public class Operaciones {
    /**
     * las operaciones a realizar
     */
    private OperationUtils op;
    /**
     * constructor
     */
    public Operaciones() {
        op = null;
    }
    /**
     * organiza las operaciones según la opcion designada en CLI
     */
    public void organizeOptions(String[] args) {
        try {
            outter:for(int i=0; i<args.length; ++i) {
                op = new OperationUtils("." + File.separator, args, i);
                switch(args[i]) {
                    case "-ls": 
                        op.changeDirectoryOperation();
                        break;
                    case "-le":
                        op.listZipEntriesOperation();
                        break;
                    case "-st":
                        op.startOrOpenOperation();
                        break;
                    case "-rl":
                        op.readFileLinesOperation();
                        break;
                    case "-ff":
                        op.searchFileOrFolderOperation();
                        break;
                    case "-fl":
                        op.searchFileLineOperation();
                        break;
                    case "-cp":
                        op.copySourceDirectoryToTargetOperation();
                        break;
                    case "-cf":
                        op.copyFilesSourceToTarget();
                        break;
                    case "-mv":
                        op.moveDirectoryOperation();
                        break;
                    case "-mf":
                        op.moveSourceFilesToTarget();
                        break;
                    case "-rn":
                        op.renameDirectory();
                        break;
                    case "-md":
                        op.createDirectoryOperation();
                        break;
                    case "-ni":
                        op.createFileOperation();
                        break;
                    case "-cm":
                        op.createCompressionOperation();
                        break;
                    case "-dc":
                        op.createDeCompressOperation();
                        break;
                    case "-dd":
                        op.deleteDirectoryOperation();
                        break;
                    case "-df":
                        op.deleteFilesOperation();
                        break;
                    case "--h":
                        System.out.println("use -ls to list dir elements");
                        System.out.println("use -le to list entries of zip file");
                        System.out.println("use -st to start or open an element");
                        System.out.println("use -rl to read file lines");
                        System.out.println("use -ff to search in the given path for:");
                        System.out.println("\tuse -e search by file extension");
                        System.out.println("\tuse -n search by file name");
                        System.out.println("\tuse -tf search by file");
                        System.out.println("\tuse -td search by directory name");
                        System.out.println("use -fl to search in the given path for:");
                        System.out.println("\tuse -s search file line using smartIgnoreCase");
                        System.out.println("\tuse -i search file line using ignore case");
                        System.out.println("use -cp copy to source dir to target");
                        System.out.println("\tuse -cf to copy source file to target");
                        System.out.println("\t\tuse --r to REPLACE or --c to COPY");
                        System.out.println("use -mv to move source dir to target");
                        System.out.println("\tuse -mf to move source file to target");
                        System.out.println("use -rn to rename file or dir");
                        System.out.println("use -md to creates a dir");
                        System.out.println("\tuse -ni to create file");
                        System.out.println("use -cm to compress the folder(s)");
                        System.out.println("\tuse -i to list the folder(s) or file(s) to compress");
                        System.out.println("use -dc to compress the folder(s)");
                        System.out.println("\tuse -l to list the file(s) inside the compressed archive");
                        System.out.println("use -dd to delete a folder(s)");
                        System.out.println("\tuse -df to delete a file(s) of folder(s)");
                        break;
                    default: 
                        System.out.println("utiliza --h para mas informacion");
                        break outter;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
