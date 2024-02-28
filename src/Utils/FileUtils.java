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
        String n = "", p = "";
        try {
            File local = new File(localPath);
            if(local.exists()) {
                p = local.getCanonicalPath(); 
                n = new File(p).getName();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return n;
    }
    /**
     * ayuda a generar la ruta de los elementos del directorio
     * @param miFiles: elementos del directorio
     * @return string con la ruta de los elementos del directorio
     */
    public ArrayList<String> getDirectoryNames(DirectoryStream<Path> myFiles) {
        ArrayList<String> ld = new ArrayList<>();
        myFiles
            .forEach(e -> {
                File f = e.toFile();
                if(f.isDirectory()) {
                    ld.add(f.getPath());
                    if(f.listFiles() != null) {
                        try {
                            ld.addAll(
                                    getDirectoryNames(
                                        Files.newDirectoryStream(f.toPath())
                                    )
                            );
                        } catch(Exception err) {
                            err.printStackTrace();
                        }
                    }
                }
            });
        return ld;
    }
    /**
     * ayuda a generar la ruta de los archivos dentro de cualquier directorio
     * @param miFiles: los archivos dentro de un directorio
     * @return la ruta de los archivos dentro de cualquier directorio
     */
    public ArrayList<String> getDirectoryFiles(DirectoryStream<Path> myFiles) {
        ArrayList<String> lf = new ArrayList<>();
        myFiles
            .forEach(e -> {
                File f = e.toFile();
                if(f.exists() && f.isFile()) {
                    lf.add(f.getPath());
                } else if(f.isDirectory()) {
                    try {
                        lf.addAll(
                                getDirectoryFiles(
                                    Files.newDirectoryStream(f.toPath())
                                )
                        );
                    } catch(Exception err) {
                        err.printStackTrace();
                    }
                }
            });
        return lf;
    }
    /**
     * litar la ruta de todos los archivos del directorio 
     * @param filePath: ruta del directorio a buscar
     * @return un String con la ruta de todos los archivos
     */
    public ArrayList<String> listFilesFromPath(String filePath) {
        ArrayList<String> lf = new ArrayList<>();
        try {
            File f = new File(filePath);
            if(f.exists() && f.isFile()) {
                lf.add(f.getPath());
            } else {
                lf.addAll(
                    getDirectoryFiles(
                        Files.newDirectoryStream(f.toPath())
                    )
                );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return lf;
    }

    public void printFilePath(File f) {
        System.out.println(
                String.format(
                    "| %s |",
                    f.getPath()
                )
        );
    }
    public boolean areSimilarDirs(File f, String cliOption, String cliContext) throws IOException {
        boolean similar = false;
        String contextLowerCase = cliContext.toLowerCase();
        switch(cliOption) {
            case "-td":
                if(f.isDirectory() && f.listFiles() != null) {
                    ArrayList<String> dirNames = getDirectoryNames(
                            Files.newDirectoryStream(f.toPath())
                    );
                    dirNames
                        .parallelStream()
                        .map(e -> new File(e))
                        .filter(e -> e.getName().toLowerCase().contains(contextLowerCase))
                        .forEach(e -> {
                            printFilePath(e);
                        });
                } else if(f.getName().toLowerCase().contains(contextLowerCase)) {
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
    public boolean areSimilar(String cliOption, File first, String second) {
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
        try {
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
                System.out.println("CREATED: " + miFile.getPath());
            }
        } catch(Exception e) {
            e.printStackTrace();
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
            } catch (Exception e) {
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
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(zipOutput != null) {
                try {
                    zipOutput.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                zipOutput = null;
            }
            if(fileOutput != null) {
                try {
                    fileOutput.close();
                } catch(Exception e) {
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
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(myFileOutputStream != null) {
                try {
                    myFileOutputStream.close();
                } catch(Exception e) {
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
                                "THE FOLDER STRUCTURE: | %s | ARE NEDDED.",
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
            System.out.println("de-compress operation finished");

        } catch(Exception e) {
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
                System.out.println("EMPTY ZIP FILE");
            } else {
                int count = 1;
                while(zEntry != null) {
                    System.out.println(
                            String.format(
                                "%s: %s",
                                count,
                                zEntry.getName()
                                )
                            );
                    ++count;
                    zipIn.closeEntry();
                    zEntry = zipIn.getNextEntry();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(zipIn != null) {
                try {
                    zipIn.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                zipIn = null;
            }
        }
    }
}
