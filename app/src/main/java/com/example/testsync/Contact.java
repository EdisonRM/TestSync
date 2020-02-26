package com.example.testsync;

public class Contact {
    private String cNombre;
    private int nSicronizado;

    public Contact(String cNombre, int nSicronizado)
    {
        this.cNombre = cNombre;
        this.nSicronizado = nSicronizado;
    }

    public  String getcNombre()
    {
        return this.cNombre;
    }
    public  void setcNombre(String cNombre)
    {
        this.cNombre = cNombre;
    }
    public int getnSicronizado()
    {
        return this.nSicronizado;
    }
    public void setnSicronizado(int nSicronizado)
    {
        this.nSicronizado = nSicronizado;
    }
}
