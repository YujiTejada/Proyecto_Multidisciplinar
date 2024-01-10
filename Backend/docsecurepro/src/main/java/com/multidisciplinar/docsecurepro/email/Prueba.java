package com.multidisciplinar.docsecurepro.email;

import com.multidisciplinar.docsecurepro.email.sender.Sender;

public class Prueba {

    public static void main(String[] args) {
        var sender = new Sender();
        sender.send("aterrasamoya@gmail.com", "Prueba correo", "Probando probando");
    }

}
