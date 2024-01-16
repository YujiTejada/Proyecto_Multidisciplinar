package com.multidisciplinar.docsecurepro.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoOperacionEnum {

    SUBIDA_ARCHIVO(1, "Subida de archivo"),
    DESCARGA_ARCHIVO(2, "Descarga de archivo"),
    RENOMBRE_ARCHIVO(3, "Renombre de archivo"),
    ELIMINACION_ARCHIVO(7, "Eliminacion de archivo"),
    CREACION_CARPETA(4, "Creacion de carpeta"),
    ELIMINACION_CARPETA(5, "Eliminacion de carpeta"),
    RENOMBRE_CARPETA(6, "Renombre de carpeta");

    private int idTipoOperacion;
    private String tipoOperacion;

}
