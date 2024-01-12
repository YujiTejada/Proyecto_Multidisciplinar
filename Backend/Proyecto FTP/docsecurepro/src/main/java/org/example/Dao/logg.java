package org.example.Dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class logg {
    private int id_log;
    private String fecha_hora;
    private int id_usuario;
    private int tipo_operacion;
    private int id_archivos;
}