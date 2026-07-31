package ObjectPage;

import Control.BaseController;
import Control.ElementActions;
import Control.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object para Facebook Marketplace
 * Encapsula todos los elementos y acciones de la página principal
 */
public class MarketplaceFPage extends BaseController {

    private final WebDriver driver;

    // ========== LOCALIZADORES ==========
    private By txtEmail = By.id("email");
    private By txtPassword = By.id("pass");
    private By btnLogin = By.name("login");
    private By inputBusqueda = By.xpath("//input[@type='search']");
    private By resultadosBusqueda = By.xpath("//div[contains(@class, 'resultado')]");
    private By titlePagina = By.xpath("//title");

    /**
     * Constructor que inicializa el Page Object
     */
    public MarketplaceFPage(WebDriver driver) {
        super();
        this.driver = driver;
    }

    // ========== VALIDACIONES ==========

    /**
     * Valida que la página principal se haya cargado correctamente
     * Verifica título y presencia de elementos clave
     */
    public boolean validarPaginaPrincipal() {
        try {
            String title = driver.getTitle();
            System.out.println("Título de página: " + title);
            
            boolean titleValido = title != null && title.toLowerCase().contains("marketplace");
            boolean elementosPresentes = ElementActions.isElementPresent(driver, inputBusqueda);
            
            if (titleValido && elementosPresentes) {
                System.out.println("✓ Página principal cargada correctamente");
                return true;
            } else {
                System.err.println("✗ Validación de página principal fallida. Title: " + titleValido + ", Elementos: " + elementosPresentes);
                return false;
            }
        } catch (Exception e) {
            System.err.println("✗ Error validando página principal: " + e.getMessage());
            return false;
        }
    }

    // ========== LOGIN ==========

    /**
     * Ingresa credenciales de usuario con esperas explícitas
     * @param email Correo del usuario
     * @param password Contraseña del usuario
     */
    public void ingresarCredenciales(String email, String password) {
        try {
            System.out.println("→ Ingresando credenciales...");
            ElementActions.sendText(driver, txtEmail, email);
            ElementActions.sendText(driver, txtPassword, password);
            ElementActions.click(driver, btnLogin);
            System.out.println("✓ Credenciales ingresadas exitosamente");
        } catch (Exception e) {
            System.err.println("✗ Error ingresando credenciales: " + e.getMessage());
            throw new RuntimeException("Falló el login con credenciales: " + email, e);
        }
    }

    /**
     * Valida que se ingresó correctamente a la cuenta (URL contiene "marketplace")
     */
    public boolean validarLoginExitoso() {
        try {
            WaitUtils.sleep(2); // Pequeña espera para que se redirija
            String urlActual = driver.getCurrentUrl();
            boolean loginExitoso = urlActual != null && urlActual.toLowerCase().contains("marketplace");
            
            if (loginExitoso) {
                System.out.println("✓ Login exitoso. URL actual: " + urlActual);
            } else {
                System.err.println("✗ Login fallido. URL actual: " + urlActual);
            }
            return loginExitoso;
        } catch (Exception e) {
            System.err.println("✗ Error validando login: " + e.getMessage());
            return false;
        }
    }

    // ========== BÚSQUEDA ==========

    /**
     * Busca un producto en la barra de búsqueda
     * @param producto Nombre del producto a buscar
     */
    public void buscarProducto(String producto) {
        try {
            System.out.println("→ Buscando producto: " + producto);
            ElementActions.searchText(driver, inputBusqueda, producto);
            WaitUtils.sleep(2); // Espera a que carguen resultados
            System.out.println("✓ Búsqueda de producto realizada");
        } catch (Exception e) {
            System.err.println("✗ Error al buscar producto: " + e.getMessage());
            throw new RuntimeException("Falló la búsqueda de producto: " + producto, e);
        }
    }

    /**
     * Valida que existan resultados de búsqueda
     * Verifica presencia de elementos y contenido del producto en la página
     * @param producto Nombre del producto a validar
     */
    public boolean validarResultadosBusqueda(String producto) {
        try {
            System.out.println("→ Validando resultados de búsqueda para: " + producto);
            
            String pageSource = driver.getPageSource();
            if (pageSource == null || pageSource.isEmpty()) {
                System.err.println("✗ Página vacía");
                return false;
            }

            // Validación 1: Verificar presencia de elementos de resultado
            boolean elementosPresentes = ElementActions.isElementPresent(driver, resultadosBusqueda);
            
            // Validación 2: Verificar que el contenido incluya el producto o indicadores de resultados
            boolean contenidoValido = pageSource.toLowerCase().contains(producto.toLowerCase())
                    || pageSource.contains("Resultados")
                    || pageSource.contains("resultado");
            
            boolean resultadoValido = elementosPresentes || contenidoValido;
            
            if (resultadoValido) {
                System.out.println("✓ Resultados de búsqueda válidos para: " + producto);
            } else {
                System.err.println("✗ Sin resultados válidos para: " + producto);
            }
            
            return resultadoValido;
        } catch (Exception e) {
            System.err.println("✗ Error validando resultados de búsqueda: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la cantidad de resultados encontrados (si es posible)
     */
    public int obtenerCantidadResultados() {
        try {
            return driver.findElements(resultadosBusqueda).size();
        } catch (Exception e) {
            System.err.println("✗ Error obteniendo cantidad de resultados: " + e.getMessage());
            return 0;
        }
    }
}

