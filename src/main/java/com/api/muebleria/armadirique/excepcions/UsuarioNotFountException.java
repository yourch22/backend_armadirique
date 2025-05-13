package com.api.muebleria.armadirique.excepcions;

public class UsuarioNotFountException extends Exception {

    public UsuarioNotFountException(){
        super("EL Usuario con ese username no existe en la base de datos, vuelva a intentar!!");
    }
    public UsuarioNotFountException(String msg){
        super(msg);
    }
}
