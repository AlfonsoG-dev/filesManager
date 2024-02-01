package Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
public class FileUtils {
    /**
     * get the local name
     * @param localPath: local path
     * @return the name of the given path
     */
    public String getLocalName(String localPath) {
        String name = "";
        try {
            File local = new File(localPath);
            if(local.exists()) {
                String path = local.getCanonicalPath(); 
                name = new File(path).getName();
            }
        } catch(Exception e) {
            System.err.println(e);
        }
        return name;
    }
    /**
     * ayuda a generar la ruta de los elementos del directorio
     * @param miFiles: elementos del directorio
     * @return string con la ruta de los elementos del directorio
     */
    public String getDirectoryNames(File[] miFiles) {
        String dirNames = "";
        try {
            for(File f: miFiles) {
                if(f.isDirectory()) {
                    dirNames += f.getPath() + "\n";
                    if(f.listFiles() != null) {
                        getDirectoryFiles(f.listFiles());
                    }
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
        String cDirNames = dirNames.substring(0, dirNames.length()-2);
        return cDirNames;
    }
    /**
     * ayuda a generar la ruta de los archivos dentro de cualquier directorio
     * @param miFiles: los archivos dentro de un directorio
     * @return la ruta de los archivos dentro de cualquier directorio
     */
    public String getDirectoryFiles(File[] miFiles) {
        String fileNames = "";
        try {
            for(File f: miFiles) {
                if(f.exists() && f.isFile()) {
                    fileNames += f.getPath() + "\n";
                } else if(f.isDirectory()) {
                    fileNames += this.getDirectoryFiles(f.listFiles());
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
        return fileNames;
    }
    /**
     * litar la ruta de todos los archivos del directorio 
     * @param filePath: ruta del directorio a buscar
     * @return un String con la ruta de todos los archivos
     */
    public String listFilesFromPath(String filePath) {
        String fileNames = "";
        try {
            File miFile = new File(filePath);
            if(miFile.exists() && miFile.isFile()) {
                fileNames += miFile.getPath() + "\n";
            } else {
                fileNames += getDirectoryFiles(miFile.listFiles());
            }
        } catch(Exception e) {
            System.err.println(e);
        }
        return fileNames;
    }
    /**
     * compare 2 files or folders with CLIOption
     * @param cliOption: Cli options -e or -n
     * @param first: first file
     * @param second: second file
     * @return true if the file have similarities, false otherwise
     */
    public boolean areSimilar(String cliOption, String first, String second) {
        boolean iguales = false;
        File firstFile = new File(first);
        String secondFile = second.contains(".") ? second.split("\\.")[1] : second;
        switch(cliOption) {
            case "-e":
                if(firstFile.isFile()) {
                    String eSecondFile = secondFile;
                    if(firstFile.getName().split("\\.")[1].toLowerCase().contains(eSecondFile.toLowerCase())) {
                        iguales = true;
                    }
                } else {
                    iguales = false;
                    System.err.println("folders dont contain extension");
                }
                break;
            case "-n":
                if(firstFile.getName().toLowerCase().contains(secondFile.toLowerCase())) {
                    iguales = true;
                }
                break;
        }
        return iguales;
    }
    /**
     * si en la ruta target no existe el directorio se crea
     * <br> pre: </br> si la ruta tiene más de 1 directorio como padre entonces se crea con mkdirs
     * @param targetFilePath: ruta del directorio target
     * @param parentFileNames: nombres de los directorios a crear
     */
    public void CreateParentFile(String targetFilePath, String parentFileNames) {
        try {
            String[] parentNames = parentFileNames.split("\n");
            for(String pn: parentNames) {
                String nFileName = pn.replace(targetFilePath, "");
                File mio = new File(pn);
                int fileLenght = nFileName.split("\\\\").length;
                if(!mio.exists() && fileLenght > 1) {
                    mio.mkdirs();
                } else if(!mio.exists() && fileLenght <= 1) {
                    mio.mkdir();
                }
                System.out.println("directorio creado: " + mio.getName());
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }
    /**
     * recursive add file to the zip 
     * @param source: source of the files
     * @param base: name of the file
     * @param zop: {@link ZipOutputStream}
     */
    private void AddFilesToZip(File source, String base, ZipOutputStream zop, String includeFiles) {
        if(source.isDirectory() && source.listFiles() != null) {
            File[] sourceFiles = source.listFiles();
            if(includeFiles == null) {
                for(File sf: sourceFiles) {
                    AddFilesToZip(sf, base + File.separator + sf.getName(), zop, includeFiles);
                }
            }
            if(includeFiles != null && includeFiles.contains(",")) {
                String[] includes = includeFiles.split(",");
                for(File sf: sourceFiles) {
                    for(String ic: includes) {
                        if(sf.getPath().contains(ic.trim())) {
                            AddFilesToZip(sf, base + File.separator + sf.getName(), zop, includeFiles);
                        }
                    }
                }
            } else if(includeFiles != null && !includeFiles.contains(",")) {
                for(File sf: sourceFiles) {
                    if(sf.getPath().contains(includeFiles)) {
                        AddFilesToZip(sf, base + File.separator + sf.getName(), zop, includeFiles);
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
                System.err.println(e);
            } finally {
                if(fileInput != null) {
                    try {
                        fileInput.close();
                    } catch(Exception e) {
                        System.err.println(e);
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
    public void CreateZipFile(File source, File destination, String includeFiles) {
        FileOutputStream fileOutput = null;
        ZipOutputStream zipOutput = null;
        try {
            fileOutput = new FileOutputStream(destination);
            zipOutput = new ZipOutputStream(fileOutput);
            AddFilesToZip(source, source.getName(), zipOutput, includeFiles);
        } catch(Exception e) {
            System.err.println(e);
        } finally {
            if(zipOutput != null) {
                try {
                    zipOutput.close();
                } catch(Exception e) {
                    System.err.println(e);
                }
                zipOutput = null;
            }
            if(fileOutput != null) {
                try {
                    fileOutput.close();
                } catch(Exception e) {
                    System.err.println(e);
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
    public void ExtractZipFiles(ZipInputStream zipIn, String path) {
        FileOutputStream myFileOutputStream = null;
        try {
            myFileOutputStream = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int readBytes;
            while((readBytes = zipIn.read(buffer)) != -1) {
                myFileOutputStream.write(buffer, 0, readBytes);
            }
        } catch(Exception e) {
            System.err.println(e);
        } finally {
            if(myFileOutputStream != null) {
                try {
                    myFileOutputStream.close();
                } catch(Exception e) {
                    System.err.println(e);
                }
                myFileOutputStream = null;
            }
        }
    }
    /**
     * create the file destination for the unzipped files
     */
    public void CreateUnZipFile(String zipFilePath, String directoryPath) {
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            File miFile = null;

            outter: while(entry != null) {
                String filePath = directoryPath + entry.getName();
                String entryParent = new File(filePath).getParent();
                miFile = new File(entryParent);
                if(!miFile.exists()) {
                    System.out.println(String.format("THE FOLDER STRUCTURE: | %s | ARE NEDDED.", miFile.getPath()));
                    break outter;
                } else if(!entry.isDirectory()) {
                    ExtractZipFiles(zipIn, filePath);
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            System.out.println("de-compress operation finished");

        } catch(Exception e) {
            System.err.println(e);
        }
    }

}
