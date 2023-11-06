package Utils;

public record Formulario() {
    
    /**
     */
    public boolean ComprobarAccion(String permiso) {
        boolean comprobante = false;
        switch(permiso) {
            case "yes":
                comprobante = true;
                break;
            case "no":
                comprobante = false;
                break;
            default:
                System.out.println("\tEsta seguro de eliminar la carpeta ?\n");
                break;
        }
        return comprobante;
    }
}
