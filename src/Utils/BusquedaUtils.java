package Utils;
import java.io.File;
public class BusquedaUtils {
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
}
