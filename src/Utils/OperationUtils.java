package Utils;

public class OperationUtils {
    /**
     * create a clean string with the file path
     * @param filePath: file path to clean
     * @return the clean file path
     */
    public String GetCleanPath(String filePath) {
        String build = filePath.replace(".", "").replace("/", "\\").replace("\\\\", "\\");
        return build;
    }
    
    /**
     * create a clean path for the target using the parent file path
     * @param parentFile: file path to the parent
     * @return the target file path from parent
     */
    public String CreateTargetFromParentPath(String parentFile) {
        String[] parentName = parentFile.split("\\\\");
        String targetNames = "";
        for(int i=6; i<parentName.length; ++i) {
            targetNames += parentName[i] + "\\";
        }
        return targetNames;
    }
}
