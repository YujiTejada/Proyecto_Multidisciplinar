package org.example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/files")
public class FTPFileDeleteController {

    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        String baseDirectory = "D:/ArchivosApp/";
        String filePath = baseDirectory + filename;

        File fileToDelete = new File(filePath);
        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                return ResponseEntity.ok("Archivo eliminado exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el archivo");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El archivo no existe");
        }
    }
}
