package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/directories")
public class FTPDirectoryDeleteController {

    @DeleteMapping("/delete/{directoryName}")
    public ResponseEntity<String> deleteDirectory(@PathVariable String directoryName) {
        String baseDirectory = "D:/ArchivosApp/";  // Cambia esto según tu configuración
        String directoryPath = baseDirectory + directoryName;

        File directoryToDelete = new File(directoryPath);
        if (directoryToDelete.exists()) {
            if (directoryToDelete.delete()) {
                return ResponseEntity.ok("Directorio eliminado exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el directorio");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El directorio no existe");
        }
    }
}
