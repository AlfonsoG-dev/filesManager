package Utils;

import java.io.File;

public class TextUtils {
    /**
     * create a clean string with the file path
     * @param filePath: file path to clean
     * @return the clean file path
     */
    public String GetCleanPath(String filePath) {
        String build = "";
        if(filePath.startsWith("\\.")) {
            build = filePath.replace(".", "").replace("/", "\\").replace("\\\\", "\\");
        } else {
            build = filePath.replace("/", "\\").replace("\\\\", "\\");
        }
        return build;
    }
    
    /**
     * create a clean path for the target using the parent file path
     * @param parentFile: file path to the parent
     * @return the target file path from parent
     */
    public String CreateTargetFromParentPath(String parentFile, String dirs) {
        String parentName = new File(parentFile).getParent();
        String targetNames = dirs.replace(parentName, "");
        return targetNames;
    }
    /**
     * count the number of file within a directory
     * @param directory: path of the directory
     * @return the number of files
     */
    public int CountFilesInDirectory(String directory) {
        int count = 0;
        String[] valores = directory.split("\\\\");
        for(String v: valores) {
            if(v.isEmpty() == false) {
                ++count;
            }
        }
        return count;
    }
}
