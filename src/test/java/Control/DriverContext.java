package Control;

import Constant.Navegador;
import org.openqa.selenium.WebDriver;

/**
 * Contexto centralizado para la gestión del WebDriver
 * Proporciona métodos singleton para obtener y configurar el driver
 */
public class DriverContext {
    private static final DriverManager driverManager = new DriverManager();
    private static boolean isInitialized = false;

    /**
     * Inicializa el driver con el navegador y URL especificados
     * Previene inicializaciones duplicadas en el mismo scenario
     * @param nav Navegador a utilizar
     * @param url URL a abrir
     */
    public static void setUp(Navegador nav, String url) {
        if (isInitialized && driverManager.getDriver() != null) {
            System.out.println("ℹ Driver ya inicializado. Navegando a: " + url);
            driverManager.getDriver().navigate().to(url);
        } else {
            System.out.println("🔧 Inicializando driver -> Navegador: " + nav);
            driverManager.resolverDriver(nav, url);
            isInitialized = true;
        }
    }

    /**
     * Obtiene el WebDriver actual
     * @return WebDriver inicializado o null
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverManager.getDriver();
        if (driver == null) {
            System.err.println("✗ Advertencia: WebDriver no está inicializado");
        }
        return driver;
    }

    /**
     * Cierra el driver actual y reinicia el contexto
     */
    public static void quitDriver() {
        try {
            WebDriver driver = driverManager.getDriver();
            if (driver != null) {
                System.out.println("🔲 Cerrando navegador...");
                driver.quit();
                isInitialized = false;
                System.out.println("✓ Navegador cerrado");
            }
        } catch (Exception e) {
            System.err.println("✗ Error al cerrar driver: " + e.getMessage());
            isInitialized = false;
        }
    }

    /**
     * Valida si el driver está inicializado
     * @return true si driver existe y está funcional
     */
    public static boolean isDriverReady() {
        try {
            WebDriver driver = driverManager.getDriver();
            if (driver != null) {
                driver.getCurrentUrl(); // Test simple para validar conexión
                return true;
            }
        } catch (Exception e) {
            System.err.println("ℹ Driver no está listo: " + e.getMessage());
        }
        return false;
    }

    /**
     * Reinicia el contexto sin cerrar el driver
     * Útil para cambiar entre scenarios
     */
    public static void reset() {
        isInitialized = false;
        System.out.println("🔄 Contexto reiniciado");
    }
}
