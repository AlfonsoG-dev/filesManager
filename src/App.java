class App {
    public static void main(String[] args) {
        try {
            outter:for(int i=0; i<args.length; ++i) {
                Operaciones op = new Operaciones("./");
                switch(args[i]) {
                    case "-ls": 
                        op.ChangeDirectory(args[i+1]);
                        op.ListFiles();
                        break;
                    case "-cp":
                        if((i+2) < args.length && args[i+2].toLowerCase().equals("to") && (i+3) < args.length) {
                            op.CopyFilesfromSourceToTarget(args[i+1], args[i+3]);
                        }
                        break;
                    case "-mv":
                        if((i+1) < args.length && args[i+2].equals("to") && (i+3) < args.length) {
                            System.out.println("p ==");
                            op.MoveFilesFromSourceToTarget(args[i+1], args[i+3]);
                        }
                        break;
                    case "-md":
                        if((i+1) < args.length) {
                            op.CreateDirectories(args[i+1]);
                        }
                        break;
                    case "-df":
                        if((i+1) < args.length && (i+2) < args.length) {
                            op.DeleteDirectories(args[i+1], args[i+2]);
                        }
                        break;
                    case "--h":
                        System.out.println("utiliza -lc para listar los elementos de un directorio");
                        System.out.println("utiliza -cp para copiar los archivos del directorio source");
                        System.out.println("\t\tpara copiarlos en el directorio target");
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
