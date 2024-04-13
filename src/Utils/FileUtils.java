package Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
public class FileUtils {
    private TextUtils textUtils;

    public FileUtils() {
        textUtils = new TextUtils();
    }
    /**
     * get the local name
     * @param localPath: local path
     * @return the name of the given path
     */
    public String getLocalName(String localPath) {
        String 
            n = "",
            p = "";
        try {
            File local = new File(localPath);
            if(local.exists()) {
                p = local.getCanonicalPath(); 
                n = new File(p).getName();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return n;
    }
    /**
     * ayuda a generar la ruta de los elementos del directorio
     * @param miFiles: elementos del directorio
     * @return string con la ruta de los elementos del directorio
     */
    public ArrayList<File> getDirectoryNames(DirectoryStream<Path> myFiles) {
        ArrayList<File> dirs = new ArrayList<>();
        Thread t = new Thread(new Runnable() {
            public void run() {
                for(Path p: myFiles) {
                    File f = p.toFile();
                    if(f.isDirectory()) {
                        dirs.add(f);
                        if(f.listFiles() != null) {
                            try {
                                dirs.addAll(
                                        getDirectoryNames(
                                            Files.newDirectoryStream(f.toPath())
                                        )
                                );
                            } catch(IOException err) {
                                err.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        return dirs;
    }
    /**
     * ayuda a generar la ruta de los archivos dentro de cualquier directorio
     * @param miFiles: los archivos dentro de un directorio
     * @return la ruta de los archivos dentro de cualquier directorio
     */
    public ArrayList<File> getDirectoryFiles(DirectoryStream<Path> myFiles) {
        ArrayList<File> files = new ArrayList<>();
        Thread t = new Thread(new Runnable() {
            public void run() {
                for(Path p: myFiles) {
                    File f = p.toFile();
                    if(f.exists() && f.isFile()) {
                        files.add(f);
                    } else if(f.isDirectory()) {
                        try {
                            files.addAll(
                                    getDirectoryFiles(
                                        Files.newDirectoryStream(f.toPath())
                                    )
                            );
                        } catch(IOException err) {
                            err.printStackTrace();
                        }

                    }
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        return files;
    }
    /**
     * listar la ruta de todos los archivos del directorio 
     * @param filePath: ruta del directorio a buscar
     * @return un String con la ruta de todos los archivos
     */
    public ArrayList<File> listFilesFromPath(String filePath) {
        ArrayList<File> files = new ArrayList<>();
        try {
            File f = new File(filePath);
            if(f.exists() && f.isFile()) {
                files.add(f);
            } else {
                files.addAll(
                    getDirectoryFiles(
                        Files.newDirectoryStream(f.toPath())
                    )
                );
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
     * helper method to print the file path.
     * @param f: file to print its path
     */
    public void printFilePath(File f) {
        System.out.println(
                String.format(
                    "| %s |",
                    Colors.GREEN_UNDERLINE + f.getPath() + Colors.RESET
                )
        );
    }
    /**
     * compare the directory names.
     * @param f: file to compare its name
     * @param cliOption: cli options to compare the directory names
     * @param cliContext: name to compare with the file name
     *
     */
    public boolean areSimilarDirs(File f, String cliOption, String cliContext) throws IOException {
        boolean similar = false;
        String c = cliContext.toLowerCase();
        switch(cliOption) {
            case "-td":
                if(f.isDirectory() && f.listFiles() != null) {
                    getDirectoryNames(Files.newDirectoryStream(f.toPath()))
                        .parallelStream()
                        .filter(e -> e.getName().toLowerCase().contains(c))
                        .forEach(e -> {
                            printFilePath(e);
                        });
                } else if(f.getName().toLowerCase().contains(c)) {
                    printFilePath(f);
                }
            break;
        }
        return similar;
    }
    /**
     * compare 2 files or folders with CLIOption
     * @param cliOption: Cli options -e or -n
     * @param first: first file
     * @param second: second file
     * @return true if the file have similarities, false otherwise
     */
    public boolean areSimilar(File first, String cliOption, String second) {
        boolean similar = false;
        String s = "";
        if(second.contains(".")) {
            s = second.split("\\.")[1].toLowerCase();
        } else {
            s = second.toLowerCase();
        }
        switch(cliOption) {
            case "-e":
                if(first.isFile() && first.getName().contains(".")) {
                    String f = first.getName().split("\\.")[1].toLowerCase();
                    if(f.equals(s)) {
                        similar = true;
                    }
                }
                break;
            case "-tf":
            case "-n":
                String f = first.getName().toLowerCase();
                if(f.contains(s)) {
                    similar = true;
                }
                break;
        }
        return similar;
    }
    public boolean areSimilarLines(String cliOption, String first, String cliContext) {
        boolean isSimilar = false;
        switch(cliOption) {
            // ignore case
            case "-i":
                if(first.compareToIgnoreCase(cliContext) == 0) {
                    isSimilar = true;
                }
                break;
            // smart case
            case "-s":
                if(first.toLowerCase().contains(cliContext.toLowerCase())) {
                    isSimilar = true;
                }
                break;
        }
        return isSimilar;
    }
    /**
     * si en la ruta target no existe el directorio se crea
     * <br> pre: </br> si la ruta tiene más de 1 directorio como padre entonces se crea con mkdirs
     * @param targetFilePath: ruta del directorio target
     * @param parentFileNames: nombres de los directorios a crear
     */
    public void createParentFile(String targetFilePath, String parentFileNames) {
        String[] pn = parentFileNames.split("\n");
        for(String p: pn) {
            String fileName = p.replace(targetFilePath, "");
            File miFile = new File(p);
            int c = textUtils.countNestedLevels(fileName);
            if(!miFile.exists() && c > 1) {
                miFile.mkdirs();
            } else if(!miFile.exists() && c <= 1) {
                miFile.mkdir();
            }
            System.out.println(
                    Colors.YELLOW_UNDERLINE + "CREATED: " + Colors.RESET
                    + miFile.getPath()
            );
        }
    }
    private void addZipFileConcurrent(File source, String base, ZipOutputStream zop) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                FileInputStream fileInput = null;
                try {
                    fileInput = new FileInputStream(source);
                    ZipEntry zEntry = new ZipEntry(base);
                    zop.putNextEntry(zEntry);

                    byte[] buffer = new byte[1024];
                    int lenght;
                    while((lenght = fileInput.read(buffer)) > 0) {
                        zop.write(buffer, 0, lenght);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(fileInput != null) {
                        try {
                            fileInput.close();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        fileInput = null;
                    }
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch(InterruptedException err) {
            err.printStackTrace();
        }
    }
    /**
     * recursive add file to the zip 
     * @param source: source of the files
     * @param base: name of the file
     * @param zop: {@link ZipOutputStream}
     */
    private void addFilesToZip(File source, String base, ZipOutputStream zop, String includeFiles) {
        if(source.isDirectory() && source.listFiles() != null) {
            File[] sourceFiles = source.listFiles();
            if(includeFiles == null) {
                for(File sf: sourceFiles) {
                    addFilesToZip(
                            sf,
                            base + File.separator + sf.getName(),
                            zop,
                            includeFiles
                    );
                }
            }
            if(includeFiles != null && includeFiles.contains(",")) {
                String[] includes = includeFiles.split(",");
                for(File sf: sourceFiles) {
                    for(String ic: includes) {
                        if(sf.getPath().contains(ic.trim())) {
                            addFilesToZip(
                                    sf,
                                    base + File.separator + sf.getName(),
                                    zop,
                                    includeFiles
                            );
                        }
                    }
                }
            } else if(includeFiles != null && !includeFiles.contains(",")) {
                for(File sf: sourceFiles) {
                    if(sf.getPath().contains(includeFiles)) {
                        addFilesToZip(
                                sf,
                                base + File.separator + sf.getName(),
                                zop,
                                includeFiles
                        );
                    }
                }

            }
        } else {
            addZipFileConcurrent(
                    source,
                    base,
                    zop
            );
        }
    }
    /**
     * create the zip destination with the source file
     * @param source: source file
     * @param destination: destination file
     */
    public void createZipFile(File source, File destination, String includeFiles) {
        FileOutputStream fileOutput = null;
        ZipOutputStream zipOutput = null;
        try {
            fileOutput = new FileOutputStream(destination);
            zipOutput = new ZipOutputStream(fileOutput);
            addFilesToZip(
                    source,
                    source.getName(),
                    zipOutput,
                    includeFiles
            );
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(zipOutput != null) {
                try {
                    zipOutput.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                zipOutput = null;
            }
            if(fileOutput != null) {
                try {
                    fileOutput.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                fileOutput = null;
            }
        }
    }
    /**
     * de-compress files from a zip file 
     * @param zipIn: {@link java.io.InputStream} to read the {@link ZipEntry}
     * @param path: filePath of the zip file
     */
    public void extractZipFiles(ZipInputStream zipIn, String path) {
        FileOutputStream myFileOutputStream = null;
        try {
            myFileOutputStream = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int readBytes;
            while((readBytes = zipIn.read(buffer)) != -1) {
                myFileOutputStream.write(buffer, 0, readBytes);
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(myFileOutputStream != null) {
                try {
                    myFileOutputStream.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                myFileOutputStream = null;
            }
        }
    }
    /**
     * create the file destination for the unzipped files
     */
    public void createUnZipFile(String zipFilePath, String directoryPath) {
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            File miFile = null;

            outter: while(entry != null) {
                String filePath = directoryPath + entry.getName();
                String entryParent = new File(filePath).getParent();
                miFile = new File(entryParent);
                if(!miFile.exists()) {
                    System.out.println(
                            String.format(
                                Colors.YELLOW_UNDERLINE +
                                "THE FOLDER STRUCTURE: | %s | ARE NEDDED." + 
                                Colors.RESET,
                                miFile.getPath()
                            )
                    );
                    break outter;
                } else if(!entry.isDirectory()) {
                    extractZipFiles(zipIn, filePath);
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            System.out.println(
                    Colors.YELLOW_UNDERLINE + "de-compress operation finished" + Colors.RESET
            );

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * list the zip entries given an option
     * <br> pre: </br> the zip archive has entries
     * @param 
     */
    public void zipEntries(String zipFilePath) {
        ZipInputStream zipIn = null;
        try {
            zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry zEntry = zipIn.getNextEntry();
            if(zEntry == null) {
                System.out.println(
                        Colors.YELLOW_UNDERLINE + "EMPTY ZIP FILE" + Colors.RESET
                );
            } else {
                int count = 1;
                while(zEntry != null) {
                    System.out.println(
                            String.format(
                                "%s: %s",
                                count,
                                Colors.GREEN_UNDERLINE + zEntry.getName() + Colors.RESET
                            )
                    );
                    ++count;
                    zipIn.closeEntry();
                    zEntry = zipIn.getNextEntry();
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(zipIn != null) {
                try {
                    zipIn.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                zipIn = null;
            }
        }
    }
}
