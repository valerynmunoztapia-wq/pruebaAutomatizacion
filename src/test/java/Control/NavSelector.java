package Control;

import Constant.Navegador;

import static Constant.Navegador.*;

public class NavSelector {
    static Navegador nav;

    public static Navegador seleccionNavegador(String navegador) {
        switch (navegador.trim()) {
            case "Chrome":
                nav = Chrome;
                return nav;
            case "Edge":
                nav = Edge;
                return nav;
            case "Firefox":
                nav = Firefox;
                return nav;
        }
        return Chrome;
    }
}
