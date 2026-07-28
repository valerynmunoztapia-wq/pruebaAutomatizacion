package ObjectPage;

import Control.BaseController;
import Control.ElementActions;
import Control.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * EJEMPLO: Cómo crear un nuevo Page Object después de la refactorización
 * 
 * Sigue estos pasos para mantener consistencia en toda la suite:
 * 1. Hereda de BaseController
 * 2. Usa ElementActions para todas las acciones
 * 3. Usa WaitUtils para todas las esperas
 * 4. Incluye JavaDoc descriptivo
 * 5. Agrupa por funcionalidad
 */
public class EjemploPageObject extends BaseController {

    private final WebDriver driver;

    // ========== LOCALIZADORES ==========
    // Organiza por sección de la página
    private By btnBuscar = By.id("search-button");
    private By inputBusqueda = By.xpath("//input[@placeholder='Buscar']");
    private By resultadosList = By.xpath("//div[@class='resultado-item']");
    
    private By btnFiltros = By.id("filters-toggle");
    private By modalFiltros = By.id("filters-modal");
    private By btnAplicarFiltros = By.xpath("//button[text()='Aplicar']");

    /**
     * Constructor que inicializa el Page Object
     * @param driver WebDriver de Selenium
     */
    public EjemploPageObject(WebDriver driver) {
        super();
        this.driver = driver;
    }

    // ========== ACCIONES DE BÚSQUEDA ==========

    /**
     * Realiza una búsqueda simple
     * @param termino Término a buscar
     */
    public void buscar(String termino) {
        try {
            System.out.println("→ Buscando: " + termino);
            ElementActions.searchText(driver, inputBusqueda, termino);
            WaitUtils.sleep(2); // Espera a que carguen resultados
            System.out.println("✓ Búsqueda realizada");
        } catch (Exception e) {
            System.err.println("✗ Error en búsqueda: " + e.getMessage());
            throw new RuntimeException("Falló la búsqueda de: " + termino, e);
        }
    }

    /**
     * Obtiene el número de resultados
     * @return Cantidad de resultados encontrados
     */
    public int obtenerCantidadResultados() {
        try {
            int cantidad = driver.findElements(resultadosList).size();
            System.out.println("ℹ Resultados encontrados: " + cantidad);
            return cantidad;
        } catch (Exception e) {
            System.err.println("✗ Error obteniendo cantidad de resultados: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Valida que existan resultados en la página
     * @return true si hay al menos un resultado
     */
    public boolean validarExistenResultados() {
        try {
            boolean elementosPresentes = ElementActions.isElementPresent(driver, resultadosList);
            int cantidad = obtenerCantidadResultados();
            
            boolean valido = elementosPresentes && cantidad > 0;
            if (valido) {
                System.out.println("✓ Resultados validados");
            } else {
                System.out.println("ℹ Sin resultados");
            }
            return valido;
        } catch (Exception e) {
            System.err.println("✗ Error validando resultados: " + e.getMessage());
            return false;
        }
    }

    // ========== ACCIONES DE FILTROS ==========

    /**
     * Abre el panel de filtros
     */
    public void abrirFiltros() {
        try {
            System.out.println("→ Abriendo filtros...");
            ElementActions.click(driver, btnFiltros);
            
            // Espera a que el modal sea visible
            ElementActions.isElementVisible(driver, modalFiltros);
            System.out.println("✓ Filtros abiertos");
        } catch (Exception e) {
            System.err.println("✗ Error abriendo filtros: " + e.getMessage());
            throw new RuntimeException("Falló abrir filtros", e);
        }
    }

    /**
     * Cierra el panel de filtros
     */
    public void cerrarFiltros() {
        try {
            System.out.println("→ Cerrando filtros...");
            // Asume que hay un botón X o se hace click fuera
            ElementActions.click(driver, By.xpath("//button[@aria-label='Cerrar']"));
            
            // Espera a que desaparezca el modal
            WaitUtils.waitForElementInvisibility(driver, modalFiltros, 5);
            System.out.println("✓ Filtros cerrados");
        } catch (Exception e) {
            System.err.println("✗ Error cerrando filtros: " + e.getMessage());
        }
    }

    /**
     * Aplica filtros seleccionados
     */
    public void aplicarFiltros() {
        try {
            System.out.println("→ Aplicando filtros...");
            ElementActions.click(driver, btnAplicarFiltros);
            
            // Espera a que se carguen los nuevos resultados
            WaitUtils.sleep(2);
            System.out.println("✓ Filtros aplicados");
        } catch (Exception e) {
            System.err.println("✗ Error aplicando filtros: " + e.getMessage());
            throw new RuntimeException("Falló aplicar filtros", e);
        }
    }

    /**
     * Selecciona un filtro específico
     * @param etiquetaFiltro Texto visible del filtro
     */
    public void seleccionarFiltro(String etiquetaFiltro) {
        try {
            System.out.println("→ Seleccionando filtro: " + etiquetaFiltro);
            
            By filtroLocator = By.xpath("//label[contains(text(), '" + etiquetaFiltro + "')]");
            ElementActions.click(driver, filtroLocator);
            
            System.out.println("✓ Filtro seleccionado: " + etiquetaFiltro);
        } catch (Exception e) {
            System.err.println("✗ Error seleccionando filtro: " + e.getMessage());
            throw new RuntimeException("Falló seleccionar filtro: " + etiquetaFiltro, e);
        }
    }

    // ========== VALIDACIONES ==========

    /**
     * Valida que la página se cargó correctamente
     * @return true si la página es accesible
     */
    public boolean validarPaginaCargada() {
        try {
            System.out.println("→ Validando carga de página...");
            
            // Validación 1: Elemento principal visible
            boolean elementoPrincipal = ElementActions.isElementVisible(driver, inputBusqueda);
            
            // Validación 2: Título correcto
            String title = driver.getTitle();
            boolean titleValido = title != null && !title.isEmpty();
            
            // Validación 3: URL correcta
            String url = driver.getCurrentUrl();
            boolean urlValida = url != null && url.contains("ejemplo");
            
            boolean todosValido = elementoPrincipal && titleValido && urlValida;
            
            if (todosValido) {
                System.out.println("✓ Página cargada correctamente");
            } else {
                System.err.println("✗ Validación de página falló. Principal: " + elementoPrincipal 
                                 + ", Title: " + titleValido + ", URL: " + urlValida);
            }
            
            return todosValido;
        } catch (Exception e) {
            System.err.println("✗ Error validando página: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida que un filtro esté seleccionado
     * @param etiquetaFiltro Nombre del filtro
     * @return true si el filtro está marcado
     */
    public boolean validarFiltroSeleccionado(String etiquetaFiltro) {
        try {
            By checkboxLocator = By.xpath("//input[@value='" + etiquetaFiltro + "']");
            boolean isSelected = driver.findElement(checkboxLocator).isSelected();
            
            if (isSelected) {
                System.out.println("✓ Filtro seleccionado: " + etiquetaFiltro);
            } else {
                System.out.println("ℹ Filtro no seleccionado: " + etiquetaFiltro);
            }
            
            return isSelected;
        } catch (Exception e) {
            System.err.println("✗ Error validando filtro: " + e.getMessage());
            return false;
        }
    }

    // ========== UTILIDADES ==========

    /**
     * Obtiene el texto de un resultado específico
     * @param indice Índice del resultado (0-based)
     * @return Texto del resultado
     */
    public String obtenerTextoResultado(int indice) {
        try {
            By resultadoEspecifico = By.xpath(
                String.format("(%s)[%d]", resultadosList.toString().replace("By.xpath: ", ""), indice + 1)
            );
            String texto = ElementActions.getText(driver, resultadoEspecifico);
            return texto;
        } catch (Exception e) {
            System.err.println("✗ Error obteniendo texto de resultado: " + e.getMessage());
            return "";
        }
    }

    /**
     * Hace scroll a un elemento específico
     * @param elementoLocator Localizador del elemento
     */
    public void scrollAlElemento(By elementoLocador) {
        try {
            System.out.println("→ Haciendo scroll...");
            driver.findElement(elementoLocador);
            ElementActions.scrollToElement(driver, driver.findElement(elementoLocador));
            System.out.println("✓ Scroll completado");
        } catch (Exception e) {
            System.err.println("✗ Error en scroll: " + e.getMessage());
        }
    }

    /**
     * Espera a que desaparezca un indicador de carga
     */
    public void esperarCargaCompleta() {
        try {
            System.out.println("→ Esperando carga completa...");
            By indicadorCarga = By.xpath("//div[@class='loading-spinner']");
            WaitUtils.waitForElementInvisibility(driver, indicadorCarga, 10);
            System.out.println("✓ Carga completada");
        } catch (Exception e) {
            System.err.println("ℹ Timeout esperando carga (continuando): " + e.getMessage());
        }
    }
}

/**
 * NOTAS IMPORTANTES:
 * 
 * 1. SIEMPRE HEREDA DE BaseController
 *    ✅ public class MiPage extends BaseController { }
 * 
 * 2. ORGANIZA MÉTODOS POR SECCIÓN
 *    - Acciones de búsqueda
 *    - Acciones de filtros
 *    - Validaciones
 *    - Utilidades
 * 
 * 3. USA ElementActions PARA TODAS LAS ACCIONES
 *    ✅ ElementActions.click(driver, locator);
 *    ❌ driver.findElement(locator).click();
 * 
 * 4. USA WaitUtils PARA ESPERAS
 *    ✅ WaitUtils.waitForElementVisibility(driver, locator);
 *    ❌ Thread.sleep(3000);
 * 
 * 5. INCLUYE LOGS EN CADA OPERACIÓN
 *    ✅ System.out.println("✓ Acción completada");
 *    ❌ Métodos silenciosos
 * 
 * 6. DOCUMENTA CON JavaDoc
 *    ✅ public void accion(String param) { // JavaDoc
 *    ❌ Sin comentarios
 * 
 * 7. MANEJA EXCEPCIONES SIEMPRE
 *    ✅ try { ... } catch (Exception e) { throw new RuntimeException(...); }
 *    ❌ Sin try-catch
 */
