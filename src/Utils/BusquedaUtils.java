package Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
public class BusquedaUtils {
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
    public String getFileElements(File[] miFiles) throws Exception {
        String fileNames = "";
        try {
            for(File f: miFiles) {
                fileNames += f.getPath() + "\n";
            }
        } catch(Exception e) {
            System.err.println(e);
        }
        String cFiles = fileNames.substring(0, fileNames.length()-2);
        return cFiles;
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
                    fileNames += f.getCanonicalPath() + "\n";
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
                fileNames += miFile.getCanonicalPath() + "\n";
            } else {
                fileNames += getDirectoryFiles(miFile.listFiles());
            }
        } catch(Exception e) {
            System.err.println(e);
        }
        return fileNames;
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
                String nFileName = pn.replace(targetFilePath.replace("/", "\\"), "");
                File mio = new File(pn);
                int fileLenght = nFileName.split("\\\\").length;
                if(mio.exists() == false && fileLenght > 1) {
                    mio.mkdirs();
                } else if(mio.exists() == false && fileLenght <= 1) {
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
                        if(sf.getPath().contains(ic.trim()) == true) {
                            AddFilesToZip(sf, base + File.separator + sf.getName(), zop, includeFiles);
                        }
                    }
                }
            } else if(includeFiles != null && includeFiles.contains(",") == false) {
                for(File sf: sourceFiles) {
                    if(sf.getPath().contains(includeFiles) == true) {
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
}
