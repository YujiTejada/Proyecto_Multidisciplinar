package org.example.Dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class eventos {
    private int id_evento;
    private String nombre_evento;
    private int archivo;
    private int id_usuario;
}
