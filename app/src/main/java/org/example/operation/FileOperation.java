package org.example.operation;

import org.example.utils.*;

import java.util.stream.Stream;
import java.util.List;
import java.io.Console;
import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;

// FIXME: use optional to avoid null or empty values.  
public class FileOperation {

    private FileUtils fileUtils;
    private TextUtils textUtils;
    private static final Console consol = System.console();
    private static final String CONSOLE_FORMAT = "[%s] %s%n";
    private static final String[] LOG_LEVEL = {
        "Info",
        "Error"
    };


    public FileOperation(FileUtils fileUtils, TextUtils textUtils) {
        this.fileUtils = fileUtils;
        this.textUtils = textUtils;
    }
    public FileOperation() {
        fileUtils = new FileUtils();
        textUtils = new TextUtils();
    }
    public void createDirectory(String pathURI) {
        if(!fileUtils.createDirectory(pathURI)) {
            consol.printf(CONSOLE_FORMAT, LOG_LEVEL[1],"Couldn't create directory");
        }
    }
    public void createFile(String fileURI) {
        if(!fileUtils.createFile(fileURI)) {
            consol.printf(CONSOLE_FORMAT, LOG_LEVEL[1],"Couldn't create file");
        }
    }

    /**
     * Deleted a directory.
     * <p> If the directory contains files provide the prefix --r
     * @param pathURI - the directory to delete.
     * @param permission - the prefix to also delete the directory content.
     * <p> permission sets a default value of 1, if you pass --r it changes to 0, symbolizing the recursively action.
     */
    public void deleteDirectory(String pathURI, String permission) {
        boolean recursively = false;
        if(!permission.isBlank() && permission.equals("--r")) recursively = true; 
        if(!fileUtils.deleteDirectory(pathURI,recursively)) {
            consol.printf(CONSOLE_FORMAT, LOG_LEVEL[1],"Can't delete this directory");
        }
        if(!recursively) {
            consol.printf(CONSOLE_FORMAT, LOG_LEVEL[0],"If the directory to delete contain files you must provide --r");
        }
    }
    /**
     * Deletes a file.
     * @param fileURI - the file to delete.
     */
    public void deleteFile(String fileURI) {
        if(!fileUtils.deleteFile(fileURI)) {
            consol.printf(CONSOLE_FORMAT, LOG_LEVEL[1],"Can't delete this file");
        }
    }
    /**
     * List a file content if its a directory.
     * <p> The content can be the immediate content  or recursively.
     * @param pathURI - the path to show its content.
     * @param permission - the prefix to change immediate "" or recursively "--r".
     */
    public void listContent(String pathURI, String permission) {
        int level = 1;
        if(!permission.isBlank() && permission.equals("--r")) level = 0;
        List<Path> paths = fileUtils.listDirContent(pathURI, level);
        if(paths.isEmpty()) {
            consol.printf(CONSOLE_FORMAT, LOG_LEVEL[0]," EMPTY");
            return;
        }
        for(Path p: paths) {
            consol.printf(CONSOLE_FORMAT, LOG_LEVEL[0], p);
        }
    }
    /**
     * Copy a file into a destination directory.
     */
    public void copyFile(String sourceURI, String targetURI) {
        fileUtils.copyFileToTarget(Paths.get(sourceURI), targetURI);
    }
    /**
     * copy a file into a multiple destination directories.
     */
    public void copyFileToTargets(String sourceURI, List<String> targets) {
        if(targets.isEmpty()) return;
        for(String t: targets) {
            copyFile(sourceURI, t);
        }
    }
    /**
     * copy multiple files into a destination directory.
     */
    public void copyFilesToTarget(List<String> sources, String targetURI) {
        if(sources.isEmpty()) return;
        for(String s: sources) {
            copyFile(s, targetURI);
        }
    }
    /**
     * copy multiple files into multiple destination directories.
     */
    public void copyFilesToTargets(List<String> sources, List<String> targets) {
        if(sources.isEmpty()) return;
        for(String s: sources) {
            copyFileToTargets(s, targets);
        }
    }
    /**
     * @param permission - if you copy immediate or recursively.
     */
    public void copyDir(String sourceURI, String targetURI, String permission) {
        int level = 1;
        if(!permission.isBlank() && permission.equals("--r")) level = 0;
        fileUtils.copyDirToTarget(Paths.get(sourceURI), targetURI, level);
    }
    /**
     * copy a multiple directories into a target.
     */
    public void copyDirsToTarget(List<String> sources, String targetURI, String permission) {
        if(sources.isEmpty()) return; 
        for(String s: sources) {
            copyDir(s, targetURI, permission);
        }
    }
    /**
     * copy a directory into multiple targets.
     */
    public void copyDirToTargets(String sourceURI, List<String> targets, String permission) {
        if(targets.isEmpty()) return; 
        for(String t: targets) {
            copyDir(sourceURI, t, permission);
        }
    }
    /**
     * copy multiple directories into multiple targets.
     */
    public void copyDirsToTargets(List<String> sources, List<String> targets, String permission) {
        if(sources.isEmpty()) return; 
        for(String s: sources) {
            copyDirToTargets(s, targets, permission);
        }
    }
    /**
     * Move a file to target
     * @param sourceURI
     * @param targetURI
     */
    public void moveFile(String sourceURI, String targetURI) {
        fileUtils.moveFileToTarget(Paths.get(sourceURI), targetURI);
    }
    /**
     * Move multiple files to one destination.
     * @param sources - the files to move.
     * @param targetURI - the destination path.
     */
    public void moveFilesToTarget(List<String> sources, String targetURI) {
        if(sources.isEmpty()) return;
        for(String s: sources) {
            moveFile(s, targetURI);
        }
    }
    /**
     * Move multiple files to multiple targets.
     * <p> Its not possible to move a single file to multiple targets.
     * <p> This method will move the first source file to the first target destination, and will continue like that.
     * @param sources - the list of files to move.
     * @param targets - the list of destination directories.
     */
    public void moveFilesToTargets(List<String> sources, List<String> targets) {
        if(sources.isEmpty()) return;
        if(sources.size() != targets.size()) return;

        for(int i = 0; i < sources.size(); ++i) {
            moveFile(sources.get(i), targets.get(i));
        }
    }
    /**
     * Move a directory to a target destination.
     * @param sourceURI - the source of the directory to move.
     * @param targetURI - the target path.
     * @param permission - if to move immediate order or recursively.
     */
    public void moveDir(String sourceURI, String targetURI, String permission) {
        int level = 1;
        if(!permission.isBlank() && permission.equals("--r")) level = 0;
        fileUtils.moveDirToTarget(Paths.get(sourceURI), targetURI, level);
    }
    /**
     * Move multiple sources to a single target.
     * @param sources - the list of sources to move. 
     * @param targetURI - the target destination path.
     * @param permission - to move immediate or recursively.
     */
    public void moveDirsToTarget(List<String> sources, String targetURI, String permission) {
        if(sources.isEmpty()) return;
        for(String s: sources) {
            moveDir(targetURI, s, permission);
        }
    }
    /**
     * Move multiple source files to multiple target destinations.
     * <p> You can't move a single file to multiple destinations, but in this method you move the first file to the first target, and in that order until the operation is completed.
     * @param sources - the list of files to move.
     * @param targets - the list of targets to receive the files.
     * @param permission - if to move immediate or recursively.
     */
    public void moveDirsToTargets(List<String> sources, List<String> targets, String permission) {
        if(sources.size() != targets.size()) return;
        for(int i=0; i<sources.size(); ++i) {
            moveDir(sources.get(i), targets.get(i), permission);
        }
    }
    public void readCompressedFile(String fileURI) {
        if(!fileURI.contains(".zip") || !fileURI.contains(".rar")) {
            consol.printf(CONSOLE_FORMAT, LOG_LEVEL[1], "No zip file was provided");
            return;
        }
        fileUtils.readZipFile(fileURI);
    }
    public void compressPath(String sourceURI, String targetURI, String permission) {
        int level = 1;
        if(!permission.isBlank() && permission.equals("--r")) level = 0;
        fileUtils.compreessPath(Paths.get(sourceURI), Paths.get(targetURI), level);
    }
    public void deCompressFile(String fileURI, String targetURI) {
        if(fileURI.isBlank() || targetURI.isBlank()) return;
        fileUtils.deCompressFile(fileURI, Paths.get(targetURI));
    }
    public void printFileLines(String fileURI) {
        String[] lines = TextUtils.getFileLines(fileURI).split("\n");
        if(lines.length == 0) return;
        for(int i=0; i<lines.length; ++i) {
            String l = lines[i];
            int c = i;
            consol.printf(CONSOLE_FORMAT, LOG_LEVEL[0], ++c + ":" + l);
        }
    }
    public void printFileLines(String fileURI, int start, int stop) {
        try (Stream<String> lazyLines = TextUtils.getLazilyFileLines(fileURI)) {
            List<String> lines = lazyLines.toList();
            stop = stop >= lines.size() || stop <= 0 ? lines.size() : stop;
            start = start > 0 && start < stop ? start-1 : 0;
            for(int i=start; i<stop; ++i) {
                int c = i;
                consol.printf(CONSOLE_FORMAT, LOG_LEVEL[0], ++c + ":" + lines.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * search in the file lines for a particular word.
     * <p> the path that you provide must be of a file type.
     * @param fileURI - the file to read lines and search for the word.
     * @param word - the word to search in a file.
     */
    public void searchWordInFile(String fileURI, String word) {
        File f = new File(fileURI);
        if(!f.isFile()) return;
        try(Stream<String> fileLines = TextUtils.getLazilyFileLines(fileURI)) {
            List<String> lines = fileLines.toList();
            for(int i=0; i<lines.size(); ++i) {
                String l = lines.get(i);
                if(textUtils.lineContainsWord(l, word)) {
                    int lineNumber = i;
                    consol.printf(CONSOLE_FORMAT, LOG_LEVEL[0], String.format("%s:%d\t\t%s", fileURI, ++lineNumber, l));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * search in the directory files for a particular word.
     * <p> you provide a nested level to reach, 0 means you search recursively.
     * @param fileURI - the file to read lines and search for the word.
     * @param word - the word to search in a file.
     * @param level - the nested level to reach.
     */
    public void searchWordInDirectory(String pathURI, String word, int level) {
        File f = new File(pathURI);
        if(!f.isDirectory()) return;
        List<Path> paths = fileUtils.listDirContent(pathURI, level);
        if(paths.isEmpty()) return;
        for(Path p: paths) {
            if(p.toFile().isFile()) {
                searchWordInFile(p.toString(), word);
            }
            consol.printf("%s%n", " ");
        }
    }

}
