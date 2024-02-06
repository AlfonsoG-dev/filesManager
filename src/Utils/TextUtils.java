package Utils;

import java.io.File;
import java.nio.file.Path;

public class TextUtils {
    /**
     * create a clean string with the file path
     * @param filePath: file path to clean
     * @return the clean file path
     */
    public String getCleanPath(String filePath) {
        String build = "";
        if(filePath.startsWith(".\\")) {
            Path mio = new File(filePath).toPath().normalize();
            build = mio.toString();
        } else {
            build = filePath;
        }
        return build;
    }
    /**
     * create a clean path for the target using the parent file path
     * @param parentFile: file path to the parent
     * @return the target file path from parent
     */
    public String createTargetFromParentPath(String parentFile, String dirs) {
        String parentName = new File(parentFile).getParent();
        String targetNames = dirs.replace(parentName, "");
        return targetNames;
    }
    /**
     * count the number of file within a directory
     * @param directory: path of the directory
     * @return the number of files
     */
    public int countNestedLevels(String directory) {
        Path countNestLevel = new File(directory).toPath();
        return countNestLevel.getNameCount();
    }
}
