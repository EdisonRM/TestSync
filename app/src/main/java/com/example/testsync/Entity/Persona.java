package com.example.testsync.Entity;

import java.util.Date;

public class Persona {
    private int idPersona;
    private String cNombre;
    private String cPrimerApellido;
    private String cSegundoApellido;
    private Date dFechaNacimiento;
    private int idSexo;


    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }


}
