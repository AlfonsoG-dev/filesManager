class App {
    public static void main(String[] args) {
        try {
            for(int i=0; i<args.length; ++i) {
                Operaciones op = new Operaciones("./");
                switch(args[i]) {
                    case "-lc": 
                        op.ChangeDirectory(args[i+1]);
                        op.ListFiles();
                        break;
                    case "-cp":
                        if((i+2) < args.length && args[i+2].toLowerCase().equals("to") && (i+3) < args.length) {
                            op.CopyFilesfromSourceToTarget(args[i+1], args[i+3]);
                        }
                        break;
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
}
