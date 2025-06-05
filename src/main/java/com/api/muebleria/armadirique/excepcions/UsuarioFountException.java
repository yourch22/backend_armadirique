package com.api.muebleria.armadirique.excepcions;

public class UsuarioFountException extends Exception {

    public UsuarioFountException(){
        super("EL Usuario con ese username ya existe en la base de datos, vuelva a intentar!!");
    }
    public UsuarioFountException(String msg){
        super(msg);
    }
}
