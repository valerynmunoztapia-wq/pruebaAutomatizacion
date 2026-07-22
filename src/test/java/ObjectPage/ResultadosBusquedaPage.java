package ObjectPage;

import Control.BaseController;
import Control.DriverContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResultadosBusquedaPage extends BaseController {

    @SuppressWarnings("unused")
    @FindBy(xpath = "//h1[contains(., 'Resultados de búsqueda para')]")
    private WebElement tituloResultados;

    @SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
    @FindBy(css = "li.product-item")
    private List<WebElement> listaProductos;

    @SuppressWarnings("unused")
    @FindBy(xpath = "//div[contains(text(), 'Tu búsqueda no arrojó ningún resultado')]")
    private WebElement mensajeSinResultados;

    public void validarPaginaResultados() {
        try {
            if (!visualizarElemento(this.tituloResultados, 15)) {
                fail("No se cargó la página de resultados de búsqueda.");
            }
        } catch (Exception e) {
            fail("Error al validar la página de resultados de búsqueda: " + e.getMessage());
        }
    }

    public void validarExistenciaDeProductos() {
        try {
            if (listaProductos.isEmpty()) {
                fail("No se encontraron productos en la página de resultados.");
            }
        } catch (Exception e) {
            fail("Error al validar la existencia de productos: " + e.getMessage());
        }
    }

    public void validarAusenciaMensajeSinResultados() {
        try {
            if (visualizarElemento(this.mensajeSinResultados, 2)) {
                 fail("Se encontró el mensaje de 'búsqueda sin resultados' inesperadamente.");
            }
        } catch (Exception e) {
            // La excepción es esperada si el elemento no se encuentra.
        }
    }

    public void validarMensajeSinResultadosVisible() {
        try {
            if (!visualizarElemento(this.mensajeSinResultados, 10)) {
                fail("No se encontró el mensaje de 'búsqueda sin resultados'.");
            }
        } catch (Exception e) {
            fail("Error al validar la visibilidad del mensaje de 'sin resultados': " + e.getMessage());
        }
    }

    public void validarNoHayProductos() {
        try {
            assertTrue(listaProductos.isEmpty(), "Se encontraron productos cuando no se esperaban.");
        } catch (Exception e) {
            fail("Error al validar que no hay productos en la lista: " + e.getMessage());
        }
    }

    public void validarUrlContiene(String texto) {
        try {
            String urlActual = DriverContext.getDriver().getCurrentUrl();
            assertTrue(urlActual.contains(texto), "La URL no contiene el término de búsqueda esperado. URL: " + urlActual);
        } catch (Exception e) {
            fail("Error al validar la URL: " + e.getMessage());
        }
    }
}