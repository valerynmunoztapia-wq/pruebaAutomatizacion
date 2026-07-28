package StepDefinition;

import Constant.Constant;
import Control.DriverContext;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * Hooks para configuración pre/post de escenarios en Cucumber
 * Maneja:
 * - Inicialización de contexto antes de cada scenario
 * - Captura de evidencia fotográfica
 * - Limpieza de recursos después de cada scenario
 */
public class Hooks {
    private Scenario scenario;
    private static final String tomarCapturaPantalla;
    private static final int MAX_SCREENSHOT_ATTEMPTS = 3;

    static {
        tomarCapturaPantalla = System.getProperty("evidence", "fullEvidence");
    }

    /**
     * Se ejecuta antes de cada escenario
     * Inicializa el contexto y variables globales
     */
    @Before
    public void setUp(Scenario scenario) {
        try {
            this.scenario = scenario;
            Constant.scenarioStep = scenario;
            Constant.build_name = "Nombre de Proyecto";
            System.out.println("\n" + "=".repeat(80));
            System.out.println("🧪 Iniciando scenario: " + scenario.getName());
            System.out.println("=".repeat(80));
        } catch (Exception e) {
            System.err.println("✗ Error en @Before: " + e.getMessage());
        }
    }

    /**
     * Se ejecuta después de cada escenario
     * Cierra navegador y limpia recursos
     */
    @After
    public void tearDown() {
        try {
            String estado = scenario.isFailed() ? "❌ FALLIDO" : "✅ EXITOSO";
            System.out.println("\n" + "=".repeat(80));
            System.out.println("Scenario " + estado + ": " + scenario.getName());
            System.out.println("=".repeat(80) + "\n");
        } catch (Exception e) {
            System.err.println("ℹ Error capturando estado del scenario: " + e.getMessage());
        } finally {
            // Cierra el navegador siempre, incluso si hay excepciones
            try {
                DriverContext.quitDriver();
            } catch (Exception e) {
                System.err.println("✗ Error cerrando driver en @After: " + e.getMessage());
            }
        }
    }

    /**
     * Se ejecuta después de cada step
     * Captura pantallas de fallos o evidencia completa según configuración
     */
    @AfterStep
    public void capturaEvidencia() {
        try {
            if (scenario.isFailed()) {
                generarEvidencia("[FAIL] Step ScreenShot");
            } else if (tomarCapturaPantalla.equalsIgnoreCase("fullEvidence")) {
                generarEvidencia("[SUCCESS] Step ScreenShot");
            }
        } catch (Exception e) {
            System.err.println("ℹ Error capturando evidencia en step: " + e.getMessage());
        }
    }

    /**
     * Genera y adjunta screenshot al reporte de Cucumber
     * Reintentos en caso de fallos temporales
     * @param imageRefName Nombre de referencia para la imagen
     */
    private void generarEvidencia(String imageRefName) {
        if (DriverContext.getDriver() == null || scenario == null) {
            System.out.println("ℹ No se puede capturar evidencia - Driver o Scenario no disponible");
            return;
        }

        int intentos = 0;
        boolean exitoso = false;

        while (intentos < MAX_SCREENSHOT_ATTEMPTS && !exitoso) {
            try {
                byte[] screenShot = ((TakesScreenshot) DriverContext.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
                
                if (screenShot != null && screenShot.length > 0) {
                    scenario.attach(screenShot, "image/png", imageRefName);
                    System.out.println("✓ Screenshot capturado: " + imageRefName);
                    exitoso = true;
                } else {
                    System.err.println("✗ Screenshot vacío en intento " + (intentos + 1));
                }
            } catch (Exception e) {
                intentos++;
                System.err.println("✗ Intento " + intentos + " de captura fallido: " + e.getMessage());
                
                if (intentos < MAX_SCREENSHOT_ATTEMPTS) {
                    try {
                        Thread.sleep(500); // Pequeña pausa antes de reintentar
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        if (!exitoso) {
            System.err.println("✗ No se pudo capturar screenshot después de " + MAX_SCREENSHOT_ATTEMPTS + " intentos");
        }
    }
}