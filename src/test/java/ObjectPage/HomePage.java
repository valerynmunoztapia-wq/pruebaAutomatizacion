package ObjectPage;

import Control.BaseController;
import Control.DriverContext;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import static org.junit.jupiter.api.Assertions.fail;

public class HomePage extends BaseController {

    @SuppressWarnings("unused")
    @FindBy(css = "input[type='search']")
    private WebElement campoBusqueda;

    @SuppressWarnings("unused")
    @FindBy(css = "button[class*='closeButton']")
    private WebElement btnCerrarModal;

    @SuppressWarnings("unused")
    @FindBy(id = "main-menu")
    private WebElement menuPrincipal;

    public void cerrarAnuncioSiEsVisible() {
        try {
            if (visualizarElemento(this.btnCerrarModal, 5)) {
                this.btnCerrarModal.click();
                return;
            }
        } catch (Exception e) {
            System.out.println("No se pudo hacer clic en el botón, intentando tecla ESCAPE...");
        }
        try {
            Actions action = new Actions(DriverContext.getDriver());
            action.sendKeys(Keys.ESCAPE).perform();
        } catch (Exception ex) {
            System.out.println("El anuncio no bloqueó la pantalla o no se pudo cerrar con ESCAPE, continuando...");
        }
    }

    public void ingresarTerminoBusqueda(String termino) {
        try {
            if (visualizarElemento(this.campoBusqueda, 10)) {
                this.campoBusqueda.clear();
                this.campoBusqueda.sendKeys(termino);
            } else {
                fail("No se encontró el campo de búsqueda en la página.");
            }
        } catch (Exception e) {
            fail("Error al escribir en el campo de búsqueda: " + e.getMessage());
        }
    }

    public void presionarBotonBuscar() {
        hacerClickEnBotonPorTexto("Buscar");
    }

    public void seleccionarCategoria(String categoria) {
        try {
            String xpath = "//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"
                         + categoria.toLowerCase() + "')]";
            
            WebElement enlaceCategoria = DriverContext.getDriver().findElement(By.xpath(xpath));

            if (visualizarElemento(enlaceCategoria, 10)) {
                enlaceCategoria.click();
            } else {
                fail("No se pudo hacer clic en la categoría: " + categoria);
            }
        } catch (Exception e) {
            fail("Error al seleccionar la categoría '" + categoria + "': " + e.getMessage());
        }
    }

    public void validarCampoBusquedaVisible() {
        try {
            if (!visualizarElemento(this.campoBusqueda, 10)) {
                fail("El campo de búsqueda no está visible.");
            }
        } catch (Exception e) {
            fail("Error al validar la visibilidad del campo de búsqueda: " + e.getMessage());
        }
    }

    public void validarMenuPrincipalVisible() {
        try {
            if (!visualizarElemento(this.menuPrincipal, 10)) {
                fail("El menú principal no está visible.");
            }
        } catch (Exception e) {
            fail("Error al validar la visibilidad del menú principal: " + e.getMessage());
        }
    }
}