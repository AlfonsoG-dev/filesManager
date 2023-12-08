package Utils.Verifications;

public class OptionVerification {
    private String[] options;

    /**
     * main constructor 
     * @param nOptions: CLI options to verify
     */
    public OptionVerification(String[] nOptions) {
        options = nOptions;
    }
    // empty constructor
    public OptionVerification() { }
    /**
     * verifica si existe asignación
     * @return true si existe, false de lo contrario
     */
    public boolean VerifyAssign() {
        boolean res = false;
        for(int j=0; j<options.length; ++j) {
            int assignation = options[j].indexOf("to");
            int permision = options[j].indexOf("--y");
            if(assignation == 0 || permision == 0) {
                res = true;
            }
        }
        return res;
    }
    /**
     * da el indice de la asignacion
     */
    public int GetAssignIndex() {
        int  res =0;
        for(int j=0; j<options.length; ++j) {
            int assignation = options[j].indexOf("to");
            int otherAssignation = options[j].indexOf("--y");
            if(assignation == 0 || otherAssignation == 0) {
                res = j;
            }
        }
        return res;
    }
    /**
     * get the index of the given option
     * @param mOption: the given option
     * @return the index of the given option
     */
    public int GetIndexOfOption(String mOption) {
        int res = -1;
        for(int j=0; j<options.length; ++j) {
            int option = options[j].indexOf(mOption);
            if(option == 0 || option != -1) {
                res = j;
            }
        }
        return res;
    }
    /**
     * verifica si se asigna el archivo
     * @return el archivo
     */
    public String verifyFirstFile() {
        String res = "";
        if((GetAssignIndex()-1) > 0) {
            res = options[GetAssignIndex()-1];
        }
        return res;
    }
    /**
     * verifica si se asigna otro archivo
     * @return el archivo
     */
    public String verifySecondFile() {
        String res = "";
        if((GetAssignIndex()+1) < options.length) {
            res = options[GetAssignIndex()+1];
        }
        return res;
    }
}
