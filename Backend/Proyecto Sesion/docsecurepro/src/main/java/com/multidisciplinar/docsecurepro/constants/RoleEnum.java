package com.multidisciplinar.docsecurepro.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    SUPER_ADMIN(1, "superAdmin"),
    DIRECTIVO(2, "directivo"),
    REPRESENTANTE(3, "representante");

    private int roleCode;
    private String roleName;

}
