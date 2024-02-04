package Mundo;

import Utils.OperationUtils;
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
    public void OrganizeOptions(String[] args) {
        try {
            outter:for(int i=0; i<args.length; ++i) {
                op = new OperationUtils(".\\", args, i);
                switch(args[i]) {
                    case "-ls": 
                        op.ChangeDirectoryOperation();
                        break;
                    case "-st":
                        op.StartOrOpenOperation();
                        break;
                    case "-rl":
                        op.ReadFileLinesOperation();
                        break;
                    case "-ff":
                        op.SearchFileOrFolderOperation();
                        break;
                    case "-cp":
                        op.CopySourceDirectoryToTargetOperation();
                        break;
                    case "-cf":
                        op.CopyFilesSourceToTarget();
                        break;
                    case "-mv":
                        op.MoveDirectoryOperation();
                        break;
                    case "-mf":
                        op.MoveSourceFilesToTarget();
                        break;
                    case "-rn":
                        op.RenameDirectory();
                        break;
                    case "-md":
                        op.CreateDirectoryOperation();
                        break;
                    case "-ni":
                        op.CreateFileOperation();
                        break;
                    case "-cm":
                        op.CreateCompressionOperation();
                        break;
                    case "-dc":
                        op.CreateDeCompressOperation();
                        break;
                    case "-dd":
                        op.DeleteDirectoryOperation();
                        break;
                    case "-df":
                        op.DeleteFilesOperation();
                        break;
                    case "--h":
                        System.out.println("use -ls to list dir elements");
                        System.out.println("use -st to start or open an element");
                        System.out.println("use -rl to read file lines");
                        System.out.println("use -ff to search in the given path for:");
                        System.out.println("\tuse -e search by file extension");
                        System.out.println("\tuse -n search by file name");
                        System.out.println("\tuse -tf search by file");
                        System.out.println("\tuse -td search by directory name");
                        System.out.println("use -cp copy to source dir to target");
                        System.out.println("\tuse -cf to copy source file to target");
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
