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
    public boolean verifyAssign() {
        boolean res = false;
        for(int j=0; j<options.length; ++j) {
            int assignation = options[j].indexOf("to");
            int permision = options[j].indexOf("--y");
            if(assignation != -1 || permision != -1) {
                res = true;
            }
        }
        return res;
    }
    public boolean verifyIsReplaceable() {
        boolean isReplaceable = false;
        for(int j=0; j<options.length; ++j) {
            int 
                replaceable = options[j].indexOf("--r"),
                copyable    = options[j].indexOf("--c");
            if(replaceable != -1) {
                isReplaceable = true;
            } else if(copyable != -1) {
                isReplaceable = false;
            }
        }
        return isReplaceable;
    }
    /**
     * da el indice de la asignacion
     */
    public int getAssignIndex() {
        int  res = -1;
        for(int j=0; j<options.length; ++j) {
            int assignation = options[j].indexOf("to");
            int otherAssignation = options[j].indexOf("--y");
            if(assignation != -1 || otherAssignation != -1) {
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
    public int getIndexOfOption(String mOption) {
        int res = -1;
        for(int j=0; j<options.length; ++j) {
            int option = options[j].indexOf(mOption);
            if(option != -1) {
                res = j;
            }
        }
        return res;
    }
    /**
     * verifica si se asigna el archivo
     * @return el archivo
     */
    public String verifyFirstFile(int i) {
        String res = "";
        boolean 
            conditionA = (i+1) < options.length,
            conditionB = !options[i+1].isEmpty();
        if(conditionA && conditionB) {
            res = options[i+1];
        }
        return res;
    }
    /**
     * verifica si se asigna otro archivo
     * @return el archivo
     */
    public String verifySecondFile() {
        String res = "";
        if((getAssignIndex()+1) < options.length) {
            res = options[getAssignIndex()+1];
        }
        return res;
    }
}
