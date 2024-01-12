package org.example.Dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class archivos {
    private int id_archivo;
    private String nombre_archivo;
    private String ruta;
    private int carpeta_no;
    private int id_usuario;
    private int id_carpeta;

}
