import Mundo.Operaciones;
class filesManager {
    public static void main(String[] args) {
        try {
            Operaciones misOperaciones = new Operaciones();
            misOperaciones.organizeOptions(args);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
