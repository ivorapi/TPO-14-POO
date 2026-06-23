package controller;

import modelo.Rubro;

import java.util.ArrayList;

public class RubroController {
    private ArrayList<Rubro> rubros;

    public RubroController() {
        this.rubros = new ArrayList<>();
    }

    public void agregarRubro(Rubro rubro) {
        rubros.add(rubro);
    }

    public Rubro buscarRubroPorNombre(String nombre) {
        for (Rubro rubro : rubros) {
            if (rubro.getNombre().equalsIgnoreCase(nombre)) {
                return rubro;
            }
        }

        return null;
    }

    public ArrayList<Rubro> getRubros() {
        return rubros;
    }
}