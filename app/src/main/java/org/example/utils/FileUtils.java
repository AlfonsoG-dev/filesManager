package org.example.utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.FileVisitOption;
import java.nio.file.StandardCopyOption;


public class FileUtils {
    private static Console console = System.console();
    private static final String CONSOLE_FORMAT = "[%s] %s%n";
    private static final String[] LOG_LEVEL = {
        "Info",
        "Error",
        "Warning"
    };
    /**
     * transform {@Path} to {@String}
     */
    private Function<Path, String> getString = Object::toString;
    /**
     * transform {@Path} to {@File}
     */
    private Function<Path, File> path2File = Path::toFile;

    /**
     * create a directories if they're not created.
     * @param pathURI - the path to create the directory.
     * @return true if its created, false otherwise.
     */
    public boolean createDirectory(String pathURI) {
        File f = new File(pathURI);
        if(f.isFile()) return false;
        try {
            Path p = Files.createDirectories(f.toPath());
            if(p != null) {
                System.console().printf(CONSOLE_FORMAT, LOG_LEVEL[0], "Creating directory => " + getString.apply(p));
                return true;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * creates a file it its not already created, if the file its in a nested structure created the directories first.
     * @param pathURI - the file to create.
     * @return true if the file was created, false otherwise.
     */
    public boolean createFile(String pathURI) {
        Path p = Paths.get(pathURI);
        try {
            // create nested structure for directories
            if(p.getNameCount() > 2) {
                Path parent = p.getParent();
                if(parent != null) createDirectory(getString.apply(parent));
            }
            if(path2File.apply(p).createNewFile()) {
                console.printf(CONSOLE_FORMAT, LOG_LEVEL[0], String.format("Creating file => %s", getString.apply(p)));
                return true;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Deletes a directory if its empty, otherwise you must provide --r to delete it.
     * @param pathURI - the directory to delete.
     * @param recursively - to delete a directory that isn't empty.
     */
    public boolean deleteDirectory(String pathURI, boolean recursively) {
        File f = new File(pathURI);
        if(!f.isDirectory() || !f.exists()) return false;
        try {
            if(recursively && f.listFiles() != null) {
                // get files to delete.
                List<Path> paths = listDirContent(pathURI, 0)
                    .stream()
                    .sorted(Comparator.reverseOrder())
                    .toList();
                for(Path p: paths) {
                    if(Files.isRegularFile(p) && Files.deleteIfExists(p)) {
                        console.printf(CONSOLE_FORMAT, LOG_LEVEL[0], "Deleting file => " + p);
                    } else if(Files.deleteIfExists(p)) {
                        console.printf(CONSOLE_FORMAT, LOG_LEVEL[0], "Deleting directory => " + p);
                    }
                }
            } else {
                if(Files.deleteIfExists(f.toPath())) {
                    console.printf(CONSOLE_FORMAT, LOG_LEVEL[0], "Deleting directory => " + f.toString());
                    return true;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * Deletes a file given the path.
     * @param fileURI - the file to delete.
     */
    public boolean deleteFile(String fileURI) {
        File f = new File(fileURI);
        if(!f.isFile()) return false;
        try {
            if(Files.deleteIfExists(f.toPath())) {
                console.printf(CONSOLE_FORMAT, LOG_LEVEL[0], "[Info] Deleting file => " + f.toString());
                return true;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        if(f.delete()) {
            console.printf(CONSOLE_FORMAT, LOG_LEVEL[0],  "Deleting file => " + f.toString());
            return true;
        }
        return false;
    }
    /**
     * List directory content by level, if nested level is 0, it will search recursively.
     * @param pathURI - the path to list its content.
     * @param level - the nested level to reach.
     * @return the list with the path content without filtering directories or files.
     */
    public List<Path> listDirContent(String pathURI, int level) {
        if (level <= 0) level = Integer.MAX_VALUE;
        try(Stream<Path> p = Files.walk(Paths.get(pathURI), level, FileVisitOption.FOLLOW_LINKS)) {
            return p.toList();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    /**
     * Copy a file to a destination target.
     * @param sourcePath - the file to copy.
     * @param targetURI - the path where to copy the source file.
     */
    public void copyFileToTarget(Path sourcePath, String targetURI) {
        try {
            Path destination = Paths.get(targetURI).resolve(sourcePath.getFileName());
            Path result = Files.copy(sourcePath, destination, StandardCopyOption.COPY_ATTRIBUTES);
            if(result != null) {
                console.printf(CONSOLE_FORMAT, LOG_LEVEL[0],
                        String.format("Copy %s %n\tinto \t=>[%s]",
                            getString.apply(sourcePath), getString.apply(result))
                );
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Copy a directory into a target path.
     * @param sourcepath - the directory path to copy.
     * @param targetURI - the destination where to copy the source directory.
     * @param level - the nested level to reach.
     */
    public void copyDirToTarget(Path sourcePath, String targetURI, int level) {
        if(!path2File.apply(sourcePath).isDirectory() || !path2File.apply(sourcePath).exists()) return;
        try {
            Path targetPath = Paths.get(targetURI);
            // first list and create the directory structure.
            List<Path> paths = listDirContent(getString.apply(sourcePath), level);
            for(Path p: paths) {
                Path relative = sourcePath.relativize(p);
                Path destination = targetPath.resolve(relative);
                if(Files.isDirectory(p)) {
                    createDirectory(getString.apply(destination));
                } else {
                    Path r = Files.copy(p, destination, StandardCopyOption.COPY_ATTRIBUTES);
                    console.printf(CONSOLE_FORMAT, LOG_LEVEL[0], String.format("Copy %s %n\tinto \t=>[%s]", p, r));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * Move a file from one place to another.
     * <p> Make sure the destination already exists.
     * <p> If the file already exists, it will be replaced.
     * @param sourcePath - the file to move.
     * @param targetURI - the destination path.
     */
    public void moveFileToTarget(Path sourcePath, String targetURI) {
        if(!path2File.apply(sourcePath).exists()) return;
        try {
            Path destination = Paths.get(targetURI).resolve(sourcePath.getFileName());
            Path result = Files.move(sourcePath, destination, StandardCopyOption.REPLACE_EXISTING);
            if(result != null) {
                console.printf(CONSOLE_FORMAT, LOG_LEVEL[0],String.format("Move %s %n\tinto \t=>[%s]", sourcePath, result));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void moveDirToTarget(Path sourcePath, String targetURI, int level) {
        if(!path2File.apply(sourcePath).exists()) return;
        try {
            Path targetPath = Paths.get(targetURI);
            // first list and create the directory structure.
            List<Path> paths = listDirContent(getString.apply(sourcePath), level);
            for(Path p: paths) {
                Path relative = sourcePath.relativize(p);
                Path destination = targetPath.resolve(relative);
                if(Files.isDirectory(p)) {
                    createDirectory(getString.apply(destination));
                } else {
                    Path r = Files.move(p, destination, StandardCopyOption.REPLACE_EXISTING);
                    console.printf(CONSOLE_FORMAT, LOG_LEVEL[0],String.format("Move %s %n\tinto \t=>[%s]", p, r));
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Reading a compressed file entries.
     * @param fileURI - the file to read its entries.
     */
    public void readZipFile(String fileURI) {
        File f = new File(fileURI);
        if(!f.isFile() && !f.exists()) return;
        try(ZipFile z = new ZipFile(f)) {
            int i=1;
            for(Enumeration<?> e = z.entries(); e.hasMoreElements();) {
                console.printf(CONSOLE_FORMAT, LOG_LEVEL[0],String.format("%d:%s", i, e.nextElement()));
                ++i;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Compress a directory into a zip file.
     * @param sourcePath - the path to compress.
     * @param targetPath - the compressed file path.
     * @param level - the nested level to reach.
     */
    public void compreessPath(Path sourcePath, Path targetPath, int level) {
        try(ZipOutputStream output = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(targetPath)))) {
            List<Path> paths = listDirContent(getString.apply(sourcePath), level)
                .stream()
                .filter(Files::isRegularFile)
                .toList();
            if(paths.isEmpty()) {
                console.printf(CONSOLE_FORMAT, LOG_LEVEL[1],"Empty file provided");
                return;
            }
            for(Path p: paths) {
                Path relative = sourcePath.relativize(p);
                // replace "\\" with "/" by zip standards.
                ZipEntry entry = new ZipEntry(getString.apply(relative).replace("\\", "/"));
                console.printf(CONSOLE_FORMAT, LOG_LEVEL[0],
                        String.format(
                            "Adding %s to the compressed file %s",
                            getString.apply(relative),
                            getString.apply(targetPath)
                        )
                );
                output.putNextEntry(entry);
                // copy files into zip
                Files.copy(p, output);
                output.closeEntry();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * decompress a compressed file into a target path.
     * @param fileURI - the file to decompress.
     * @param targetPath - the path where to store the decompressed files
     */
    public void deCompressFile(String fileURI, Path targetPath) {
        File f = new File(fileURI);
        if(!f.isFile() && !f.exists()) return;
        try(ZipFile z = new ZipFile(f)) {
            console.printf(CONSOLE_FORMAT, LOG_LEVEL[0],"Decompressing file...");
            Enumeration<? extends ZipEntry> zipEntries = z.entries();
            while(zipEntries.hasMoreElements()) {
                ZipEntry entry = zipEntries.nextElement();
                Path destination = targetPath.resolve(entry.getName()).normalize();
                // prevent zip attacks.
                if(!destination.startsWith(targetPath)) throw new IOException("Bad zip entry " + entry);

                // create directory
                Path parent = destination.getParent();
                if(parent != null) {
                    Path cd = Files.createDirectories(parent);
                    console.printf(CONSOLE_FORMAT, LOG_LEVEL[0]," Creating directory %n\t=> " + cd);
                }

                // extract file
                try(InputStream is = z.getInputStream(entry); OutputStream os = Files.newOutputStream(destination)) {
                    console.printf(CONSOLE_FORMAT, LOG_LEVEL[0]," Transferring files %n\tTo => " + destination);
                    is.transferTo(os);
                }

            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
