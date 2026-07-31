package StepDefinition;

import Constant.Navegador;
import Control.DriverContext;
import Control.WaitUtils;
import ObjectPage.MarketplaceFPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

/**
 * Step Definitions para escenarios de Facebook Marketplace
 * Implementa BDD con Cucumber para pruebas de:
 * - Carga de página principal
 * - Autenticación de usuario
 * - Búsqueda de productos
 */
public class MarketplaceFDefinition {
    
    private MarketplaceFPage marketplaceFPage;
    private WebDriver driver;
    private static final String BASE_URL = "https://www.facebook.com/marketplace/";

    // ========== TC-001: Carga de Página Principal ==========

    /**
     * Abre Facebook Marketplace en la URL especificada
     * @param url URL a abrir
     */
    @Given("abro facebook marketplace en la url {string}")
    public void abroFacebookMarketplaceEnLaUrl(String url) {
        try {
            System.out.println("\n🔹 TC-001: Abriendo Marketplace en URL: " + url);
            DriverContext.setUp(Navegador.Chrome, url);
            driver = DriverContext.getDriver();
            marketplaceFPage = new MarketplaceFPage(driver);
            
            // Espera a que la página cargue completamente
            WaitUtils.sleep(3);
            System.out.println("✓ Página abierta correctamente");
        } catch (Exception e) {
            System.err.println("✗ Error abriendo Marketplace: " + e.getMessage());
            throw new RuntimeException("Falló al abrir Marketplace", e);
        }
    }

    /**
     * Valida que la página principal se cargó correctamente
     */
    @Then("la página principal debería mostrarse correctamente")
    public void laPaginaPrincipalDeberiaMostrarseCorrectamente() {
        System.out.println("→ Validando que la página principal se muestre correctamente...");
        boolean paginaCargada = marketplaceFPage.validarPaginaPrincipal();
        Assert.assertTrue(
            "La página principal no se cargó correctamente",
            paginaCargada
        );
        System.out.println("✓ Página principal validada correctamente\n");
    }

    // ========== TC-002: Login Válido ==========

    /**
     * Ingresa credenciales de usuario
     * @param email Correo electrónico del usuario
     * @param password Contraseña del usuario
     */
    @And("inicio sesión con el correo {string} y la contraseña {string}")
    public void inicioSesionConElCorreoYLaContrasena(String email, String password) {
        try {
            System.out.println("\n🔹 TC-002: Iniciando sesión");
            System.out.println("→ Ingresando credenciales para: " + email);
            marketplaceFPage.ingresarCredenciales(email, password);
            
            // Espera a que se redirija después del login
            WaitUtils.sleep(2);
            System.out.println("✓ Credenciales ingresadas");
        } catch (Exception e) {
            System.err.println("✗ Error en login: " + e.getMessage());
            throw new RuntimeException("Falló el proceso de login", e);
        }
    }

    /**
     * Valida que el login fue exitoso
     */
    @Then("debería estar autenticado en marketplace")
    public void deberiEstarAutenticadoEnMarketplace() {
        System.out.println("→ Validando autenticación...");
        boolean loginExitoso = marketplaceFPage.validarLoginExitoso();
        Assert.assertTrue(
            "No se logró autenticar correctamente en Marketplace",
            loginExitoso
        );
        System.out.println("✓ Autenticación validada correctamente\n");
    }

    // ========== TC-003: Búsqueda de Productos ==========

    /**
     * Abre Marketplace sin sesión iniciada
     */
    @Given("que el usuario abre el Marketplace sin iniciar sesión")
    public void usuarioAbreMarketplaceFSinSesion() {
        try {
            System.out.println("\n🔹 TC-003: Abriendo Marketplace sin sesión");
            DriverContext.setUp(Navegador.Chrome, BASE_URL);
            driver = DriverContext.getDriver();
            marketplaceFPage = new MarketplaceFPage(driver);
            
            // Espera a que la página cargue
            WaitUtils.sleep(3);
            System.out.println("✓ Marketplace abierto sin sesión");
        } catch (Exception e) {
            System.err.println("✗ Error abriendo Marketplace: " + e.getMessage());
            throw new RuntimeException("Falló al abrir Marketplace sin sesión", e);
        }
    }

    /**
     * Ingresa el nombre de un producto en la barra de búsqueda
     * @param producto Nombre del producto a buscar
     */
    @When("ingresa el nombre de un producto {string} en la barra de búsqueda")
    public void ingresaNombreProducto(String producto) {
        try {
            System.out.println("→ Buscando producto: " + producto);
            marketplaceFPage.buscarProducto(producto);
            System.out.println("✓ Producto ingresado en búsqueda");
        } catch (Exception e) {
            System.err.println("✗ Error en búsqueda: " + e.getMessage());
            throw new RuntimeException("Falló la búsqueda de producto: " + producto, e);
        }
    }

    /**
     * Valida que los resultados de búsqueda se muestren
     * @param producto Nombre del producto buscado
     */
    @Then("debería mostrar los resultados de la búsqueda relacionados con {string}")
    public void deberiaMostrarResultadosBusquedaRelacionadosCon(String producto) {
        System.out.println("→ Validando resultados de búsqueda para: " + producto);
        
        boolean hayResultados = marketplaceFPage.validarResultadosBusqueda(producto);
        Assert.assertTrue(
            "No se encontraron resultados de búsqueda para: " + producto,
            hayResultados
        );
        
        // Información adicional
        int cantidadResultados = marketplaceFPage.obtenerCantidadResultados();
        System.out.println("✓ Búsqueda validada. Resultados encontrados: " + cantidadResultados + "\n");
    }
}
