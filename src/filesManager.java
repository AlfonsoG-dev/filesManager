import Mundo.Operaciones;
class filesManager {
    public static void main(String[] args) {
        Thread miThread = new Thread("aplication");
        try {
            miThread.start();
            Operaciones misOperaciones = new Operaciones();
            misOperaciones.OrganizeOptions(args);
        } catch(Exception e) {
            miThread.interrupt();
            e.printStackTrace();
        }
    }
}
