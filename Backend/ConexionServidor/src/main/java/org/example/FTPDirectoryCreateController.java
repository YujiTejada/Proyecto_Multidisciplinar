package org.example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/directories")
public class FTPDirectoryCreateController {

    @PostMapping("/create")
    public ResponseEntity<String> createDirectory(@RequestBody String directoryName) {
        String baseDirectory = "D:/ArchivosApp/";
        String newDirectoryPath = baseDirectory + directoryName;

        File newDirectory = new File(newDirectoryPath);
        if (!newDirectory.exists()) {
            if (newDirectory.mkdirs()) {
                return ResponseEntity.ok("Directorio creado exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el directorio");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El directorio ya existe");
        }
    }
}
