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
                op = new OperationUtils("./", args, i);
                switch(args[i]) {
                    case "-ls": 
                        op.ChangeDirectoryOperation();
                        break;
                    case "-rl":
                        op.ReadFileLinesOperation();
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
                    case "-df":
                        op.DeleteDirectoryOperation();
                        break;
                    case "--h":
                        System.out.println("use -ls to list dir elements");
                        System.out.println("\tuse -rl to read file lines");
                        System.out.println("use -cp copy to source dir to target");
                        System.out.println("\tuse -cf to copy source file to target");
                        System.out.println("use -mv to move source dir to target");
                        System.out.println("\tuse -mf to move source file to target");
                        System.out.println("use -rn to rename file or dir");
                        System.out.println("use -md to creates a dir");
                        System.out.println("\tuse -ni to create file");
                        System.out.println("\tuse -cm to compress the file(s) or folder(s)");
                        System.out.println("use -df to delete a dir");
                        break;
                    default: 
                        System.out.println("utiliza --h para mas informacion");
                        break outter;
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
}
