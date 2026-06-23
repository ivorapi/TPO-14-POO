package modelo;

import modelo.enums.RolUsuario;

public class Usuario {
    private String nombre;
    private String email;
    private String password;
    private RolUsuario rolUsuario;

    public Usuario(String nombre, String email, String password, RolUsuario rolUsuario) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rolUsuario = rolUsuario;
    }

    public boolean isSupervisor() {
        return rolUsuario == RolUsuario.SUPERVISOR;
    }

    public String getNombre() {
        return nombre;
    }

    public RolUsuario getRolUsuario() {
        return rolUsuario;
    }
}