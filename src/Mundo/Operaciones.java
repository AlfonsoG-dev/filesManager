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
                    case "-df":
                        op.DeleteDirectoryOperation();
                        break;
                    case "--h":
                        System.out.println("utiliza -ls para listar los elementos de un directorio");
                        System.out.println("utiliza -cp para copiar los archivos del directorio source");
                        System.out.println("\t\tpara copiarlos en el directorio target");
                        System.out.println("utiliza -mv para mover los archivos de source a target");
                        System.out.println("utiliza -rn para renombrar a source como target");
                        System.out.println("utiliza -md para crear directorios");
                        System.out.println("utiliza -df para eliminar directorios y archivos");
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
