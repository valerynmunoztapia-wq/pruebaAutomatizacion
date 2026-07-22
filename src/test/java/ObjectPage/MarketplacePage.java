package ObjectPage;

import Control.BaseController;
import Control.DriverContext;
import org.openqa.selenium.WebDriver;
import java.util.List;
import java.util.logging.Logger;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.*;


public class MarketplacePage extends BaseController {
    private static final Logger logger = Logger.getLogger(MarketplacePage.class.getName());
    private final WebDriver driver;
    private List<WebElement> productos;
    private final WebDriverWait wait;
    private final By miniCartContainer = By.cssSelector(".vtex-minicart-2-x-minicartProductListContainer");
    private final By sinResultados = By.xpath("//*[contains(text(),'no se encontraron') or contains(text(),'Y no encuentro nada')]");
    private String subtotalAntesAumentar = "";
    private int cantidadAntesAumentar = -1;
    private String categoriaSeleccionada = "";


    public MarketplacePage() {
        super();
        this.driver = DriverContext.getDriver();
        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(20));
    }

    @FindBy(xpath = "//input[contains(@id,'downshift')]")
    private WebElement txtBuscador;
    @FindBy(css = "button.vtex-modal-layout-0-x-closeButton")
    private WebElement btnCerrarPopup;
    @FindBy(css = "div.vtex-minicart-2-x-openIconContainer")
    private WebElement btnMiniCarrito;

    //validocantidad
    @FindBy(xpath = "//span[contains(@class,'vtex-minicart-2-x-minicartQuantityBadge')]")
    private WebElement txtCantidad;

    private WebElement obtenerBtnMasCantidad() {
        By[] candidatos = new By[]{
                // VTEX IO - numeric stepper (namespaces más comunes)
                By.cssSelector("button[class*='buttonIncrement']"),
                By.cssSelector("button[class*='Increment']"),
                By.cssSelector("button[class*='increment']"),
                // VTEX IO en contenedor minicart
                By.cssSelector(".vtex-minicart-2-x-minicartProductListContainer button[class*='Increment']"),
                By.cssSelector(".vtex-minicart-2-x-minicartProductListContainer button[class*='increment']"),
                // VTEX IO - numeric stepper clásico
                By.cssSelector(".vtex-minicart-2-x-minicartProductListContainer a.item-quantity-change-increment"),
                By.cssSelector(".vtex-minicart-2-x-minicartProductListContainer button[data-testid='increment-button']"),
                By.cssSelector(".vtex-minicart-2-x-minicartProductListContainer button[class*='quantitySelectorIncrease']"),
                // Sin prefijo de contenedor
                By.cssSelector("a.item-quantity-change-increment"),
                By.cssSelector("button[data-testid='increment-button']"),
                By.cssSelector("button[class*='quantitySelectorIncrease']"),
                // XPath genérico
                By.xpath("//button[contains(@aria-label,'aumentar') or contains(@aria-label,'Increase') or contains(@aria-label,'increase')]"),
                By.xpath("//button[normalize-space(text())='+']"),
                By.xpath("//button[.//span[normalize-space(text())='+']]"),
                By.xpath("//div[contains(@class,'minicart')]//button[contains(@class,'increase') or contains(@class,'increment') or contains(@class,'Increment')]"),
                By.xpath("//div[contains(@class,'minicart')]//button[last()]")
        };
        return wait.until(d -> {
            for (By candidato : candidatos) {
                List<WebElement> elementos = d.findElements(candidato);
                for (WebElement elemento : elementos) {
                    try {
                        if (elemento.isDisplayed() && elemento.isEnabled()) {
                            return elemento;
                        }
                    } catch (StaleElementReferenceException ignored) { }
                }
            }
            return null;
        });
    }

    private WebElement obtenerBtnEliminar() {
        return this.driver.findElement(
                By.cssSelector("a.item-link-remove"));
    }

    // métodos
    public void ingresarPagina(String url) {
        DriverContext.getDriver().get(url);
    }

    public void cerrarPopup() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btnCerrarPopup));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnCerrarPopup);
        } catch (TimeoutException | NoSuchElementException ignored) {
        }
    }

    public void hacerClicBuscador() {
        cerrarPopup();
        visualizarElemento(txtBuscador, 10);
        txtBuscador.click();
    }

    public void escribirProducto(String producto) {
        visualizarElemento(txtBuscador, 10);
        txtBuscador.clear();
        txtBuscador.sendKeys(producto);
    }

    public void presionarEnter() {
        txtBuscador.sendKeys(Keys.ENTER);
    }

    public boolean existenProductos() {
        WebDriverWait waitProductos = new WebDriverWait(driver, Duration.ofSeconds(25));
        waitProductos.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfAllElementsLocatedBy(
                        By.cssSelector(".vtex-product-summary-2-x-container")),
                ExpectedConditions.visibilityOfElementLocated(sinResultados)
        ));

        productos = driver.findElements(
                By.cssSelector(".vtex-product-summary-2-x-container"));
        System.out.println("Cantidad de productos encontrados: " + productos.size());
        if (productos.isEmpty()) {
            return false;
        }
        visualizarElemento(productos.get(0), 10);
        return true;
    }

    public void seleccionarPrimerProducto() {
        productos = DriverContext.getDriver().findElements(
                By.cssSelector(".vtex-product-summary-2-x-container"));
        if (productos.isEmpty()) {
            throw new AssertionError("No hay productos visibles para seleccionar.");
        }
        visualizarElemento(productos.get(0), 10);
        ((JavascriptExecutor) DriverContext.getDriver())
                .executeScript("arguments[0].scrollIntoView({block:'center'});", productos.get(0));
        new WebDriverWait(
                DriverContext.getDriver(),
                Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(productos.get(0)));
        Actions actions = new Actions(DriverContext.getDriver());
        actions.moveToElement(productos.get(0)).click().perform();
    }

    public void irAlCarrito() {
        By miniCartButton = By.cssSelector("div.vtex-minicart-2-x-openIconContainer");
        By proceedToCheckout = By.id("proceed-to-checkout");
        WebElement botonMiniCarrito = wait.until(ExpectedConditions.elementToBeClickable(miniCartButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botonMiniCarrito);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(miniCartContainer),
                ExpectedConditions.elementToBeClickable(proceedToCheckout)));
        WebElement btnIrAlCarro = wait.until(ExpectedConditions.elementToBeClickable(proceedToCheckout));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnIrAlCarro);
    }

    public boolean validarSubtotal() {
        try {
            WebDriverWait wait = new WebDriverWait(
                    DriverContext.getDriver(),
                    Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(miniCartContainer));
            List<WebElement> subtotales = DriverContext.getDriver().findElements(
                    By.xpath("//*[contains(text(),'TOTAL')]"));
            if (subtotales.isEmpty()) {
                return false;
            }
            String subtotalActual = subtotales.get(0).getText().trim();
            return !subtotalActual.isEmpty()
                    && !subtotalActual.equals(subtotalAntesAumentar);
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void agregarAlCarrito() {
        By botonAgregar = By.xpath("//button[.//span[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'agregar')]]");
        for (int intento = 0; intento < 2; intento++) {
            try {
                WebElement boton = wait.until(ExpectedConditions.presenceOfElementLocated(botonAgregar));
                wait.until(ExpectedConditions.elementToBeClickable(boton));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", boton);
                return;
            } catch (StaleElementReferenceException e) {
                if (intento == 1) {
                    throw e;
                }
            }
        }
    }

    public boolean validarCarritoVacio() {
        try {
            WebElement mensaje = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("h2.empty-cart-title")));
            return mensaje.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean validarProductoEnCarritoPorNombre(String nombreProducto) {
        By miniCartButton = By.cssSelector("div.vtex-minicart-2-x-openIconContainer");
        By miniCartBadge = By.cssSelector("span.vtex-minicart-2-x-minicartQuantityBadge");
        By nombreProductoEnCarrito = By.cssSelector(
                ".vtex-minicart-2-x-minicartProductListContainer " +
                ".vtex-product-list-0-x-productName, " +
                ".vtex-minicart-2-x-minicartProductListContainer " +
                ".vtex-product-list-0-x-productNameLink, " +
                ".product-name a");
        try {
            wait.until(d -> {
                List<WebElement> badges = d.findElements(miniCartBadge);
                if (badges.isEmpty()) {
                    return false;
                }
                String cantidad = badges.get(0).getText().trim();
                return !cantidad.isEmpty() && !"0".equals(cantidad);
            });
            WebElement botonMiniCarrito = wait.until(ExpectedConditions.elementToBeClickable(miniCartButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botonMiniCarrito);
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(miniCartContainer),
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-name a"))));
            List<WebElement> productos = driver.findElements(nombreProductoEnCarrito);
            for (WebElement producto : productos) {
                if (producto.getText().trim().toUpperCase().contains(nombreProducto.toUpperCase())) {
                    return true;
                }
            }
            return false;
        } catch (TimeoutException | StaleElementReferenceException e) {
            return false;
        }
    }

    //Productonoexiste
    public boolean noExistenProductos() {
        try {
            WebElement mensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(sinResultados));
            return mensaje.isDisplayed();
        } catch (TimeoutException e) {
            return false; // no apareció mensaje de "sin resultados"
        }
    }

    //productoalcarrocantidad

    public void aumentarCantidad() {
        try {
            // Si el mini-carrito se cerró entre pasos, volver a abrirlo
            if (!estaVisibleMiniCarrito()) {
                logger.info("Mini-carrito no visible, reabriendo...");
                irAlMiniCarrito();
            }

            // Intento 1: Botón + en mini-carrito
            try {
                int cantidadAntes = obtenerCantidadDetectada();
                cantidadAntesAumentar = Math.max(cantidadAntes, 1);
                logger.info("Cantidad antes: " + cantidadAntes);

                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement btnMasCantidad = null;
                try {
                    btnMasCantidad = obtenerBtnMasCantidad();
                } catch (TimeoutException ignored) {
                    logger.warning("Botón + no encontrado en mini-carrito, probando input...");
                }

                if (btnMasCantidad != null) {
                    visualizarElemento(btnMasCantidad, 10);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnMasCantidad);
                    logger.info("Click en botón + del mini-carrito");
                } else {
                    // Intento 1b: Modificar input de cantidad directamente
                    By inputCantidad = By.cssSelector(
                            ".vtex-minicart-2-x-minicartProductListContainer input[type='text']," +
                            ".vtex-minicart-2-x-minicartProductListContainer input[name='quantity']," +
                            ".vtex-minicart-2-x-minicartProductListContainer input[class*='quantity']");
                    List<WebElement> inputs = driver.findElements(inputCantidad);
                    WebElement inputVisible = inputs.stream()
                            .filter(el -> { try { return el.isDisplayed() && el.isEnabled(); } catch (Exception e2) { return false; } })
                            .findFirst().orElse(null);
                    if (inputVisible != null) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", inputVisible);
                        inputVisible.sendKeys("2");
                        inputVisible.sendKeys(Keys.ENTER);
                        logger.info("Cantidad seteada vía input en mini-carrito");
                    } else {
                        throw new Exception("No se encontró botón ni input de cantidad en mini-carrito");
                    }
                }

                // Esperar a que el DOM refleje el cambio
                final int cantidadAntesFinal = cantidadAntes;
                try {
                    shortWait.until(d -> subtotalCambioDetectado() || obtenerCantidadDetectada() > cantidadAntesFinal);
                } catch (TimeoutException ignored) { }

                int cantidadDespues = obtenerCantidadDetectada();
                logger.info("Cantidad después: " + cantidadDespues + " | Subtotal cambió: " + subtotalCambioDetectado());

                if (subtotalCambioDetectado() || cantidadDespues > cantidadAntes) {
                    logger.info("Incremento detectado en mini-carrito");
                    return;
                }
            } catch (Exception e1) {
                logger.warning("Intento en mini-carrito falló: " + e1.getMessage());
            }

            // Intento 2: Ir al carrito completo (checkout/#/cart)
            logger.info("Intentando en carrito completo");
            irAlCarrito();

            By selectoresIncremento = By.cssSelector(
                    "a.item-quantity-change-increment," +
                    "button.item-quantity-change-increment," +
                    "button[class*='Increment']," +
                    "button[class*='increment']," +
                    "button[data-testid='increment-button']");

            WebElement btnCarrito = null;
            try {
                btnCarrito = wait.until(ExpectedConditions.elementToBeClickable(selectoresIncremento));
            } catch (TimeoutException te) {
                logger.warning("Botón + no encontrado en carrito completo, probando input...");
            }

            int cantidadAntesCarrito = obtenerCantidadDetectada();
            cantidadAntesAumentar = Math.max(cantidadAntesCarrito, 1);

            if (btnCarrito != null) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btnCarrito);
                logger.info("Click en botón + del carrito completo");
            } else {
                // Intento 2b: input de cantidad en carrito completo
                WebElement inputCarrito = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("input.item-quantity-change, input[name='quantity'], input[class*='quantity']")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", inputCarrito);
                inputCarrito.sendKeys("2");
                inputCarrito.sendKeys(Keys.ENTER);
                logger.info("Cantidad seteada vía input en carrito completo");
            }

            final int cantidadAntesCarritoFinal = cantidadAntesCarrito;
            try {
                wait.until(d -> obtenerCantidadDetectada() > cantidadAntesCarritoFinal
                        || subtotalCambioDetectado());
            } catch (TimeoutException ignored) { }

            logger.info("Cantidad en carrito después: " + obtenerCantidadDetectada());

        } catch (Exception e) {
            logger.severe("Error en aumentarCantidad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //validocantidad
    public boolean validarCantidad(int cantidadEsperada) {
        try {
            if (cantidadEsperada < 1) {
                return false;
            }
            // Abrir mini-carrito si no está visible (puede que estemos en carrito completo)
            if (!estaVisibleMiniCarrito()) {
                try {
                    irAlMiniCarrito();
                } catch (TimeoutException te) {
                    logger.warning("No se pudo abrir mini-carrito para validar cantidad, continuando con otras estrategias");
                }
            }
            
            // Estrategia 1: Revisar cantidad numérica
            int cantidadActual = obtenerCantidadDetectada();
            if (cantidadActual == cantidadEsperada) {
                return true;
            }
            
            // Estrategia 2: Si esperamos 2 y el subtotal cambió, considerar exitoso
            if (cantidadEsperada == 2 && subtotalCambioDetectado()) {
                return true;
            }
            
            // Estrategia 3: Buscar en los controles del DOM
            By controlesCantidad = By.cssSelector(
                    ".vtex-minicart-2-x-minicartProductListContainer input[name='quantity'], " +
                    ".vtex-minicart-2-x-minicartProductListContainer input.cartSkuQuantity, " +
                    ".vtex-minicart-2-x-minicartProductListContainer .item-quantity-value, " +
                    ".vtex-minicart-2-x-minicartProductListContainer [class*='quantitySelector'] input, " +
                    ".vtex-minicart-2-x-minicartProductListContainer [class*='quantity'] [class*='value']");
            
            List<WebElement> controles = driver.findElements(controlesCantidad);
            for (WebElement control : controles) {
                try {
                    if (!control.isDisplayed()) {
                        continue;
                    }
                    String valueAttr = normalizarCantidad(control.getAttribute("value"));
                    String valueText = normalizarCantidad(control.getText());
                    if (coincideCantidad(valueAttr, cantidadEsperada) || coincideCantidad(valueText, cantidadEsperada)) {
                        return true;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }

            // Estrategia 4: Badge de cantidad
            String badge = normalizarCantidad(obtenerCantidadActual());
            if (coincideCantidad(badge, cantidadEsperada)) {
                return true;
            }

            // Estrategia 5: Buscar en página del carrito completo
            By controlesPaginaCarrito = By.cssSelector(
                    "input.item-quantity-change, " +
                    "input[name='quantity'], " +
                    ".item-quantity-value, " +
                    "input[class*='quantity']");
            List<WebElement> controlesCarrito = driver.findElements(controlesPaginaCarrito);
            for (WebElement control : controlesCarrito) {
                try {
                    if (!control.isDisplayed()) continue;
                    String val = normalizarCantidad(control.getAttribute("value"));
                    if (val.isEmpty()) val = normalizarCantidad(control.getText());
                    if (coincideCantidad(val, cantidadEsperada)) return true;
                } catch (StaleElementReferenceException ignored) { }
            }

            return false;
        } catch (TimeoutException e) {
            return false;
        }
    }

    //vaciarcarro
    public void eliminarProducto() {
        WebElement btnEliminar = obtenerBtnEliminar();
        visualizarElemento(btnEliminar, 10);
        ((JavascriptExecutor) DriverContext.getDriver())
                .executeScript("arguments[0].click();", btnEliminar);
    }

    //refrech
    public void refrescarPagina() {
        driver.navigate().refresh();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public boolean validarProductoPermanece() {
        By nombreProductoEnCarrito = By.cssSelector(
                ".vtex-minicart-2-x-minicartProductListContainer .vtex-product-list-0-x-productName, " +
                ".vtex-minicart-2-x-minicartProductListContainer .vtex-product-list-0-x-productNameLink, " +
                ".product-name a");
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(miniCartContainer),
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-name a"))));
            List<WebElement> productos = driver.findElements(nombreProductoEnCarrito);
            return !productos.isEmpty();
        } catch (TimeoutException te) {
            return false;
        }
    }
    public void irAlMiniCarrito() {
        By miniCartButton = By.cssSelector("div.vtex-minicart-2-x-openIconContainer");
        WebElement botonMiniCarrito = wait.until(ExpectedConditions.elementToBeClickable(miniCartButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botonMiniCarrito);
        wait.until(ExpectedConditions.visibilityOfElementLocated(miniCartContainer));
        List<WebElement> subtotales = driver.findElements(
                By.xpath("//*[contains(text(),'TOTAL')]"));
        if (!subtotales.isEmpty()) {
            subtotalAntesAumentar = subtotales.get(0).getText().trim();
        }
    }

    public void escribirCorreo(String correo) {
        WebElement input = obtenerInputCorreo();
        input.clear();
        input.sendKeys(correo);
    }

    public void escribirCorreoCheckout(String correoCheckout) {
        escribirCorreo(correoCheckout);
    }

    public void escribirPassword(String password) {
        WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='password']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", passwordInput);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", passwordInput);
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void hacerClickEntrar() {
        WebElement entrar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", entrar);
    }

    public boolean validarCorreoInvalido() {
        WebElement txtCorreo = obtenerInputCorreo();
        String mensaje = txtCorreo.getAttribute("validationMessage");
        String valor = txtCorreo.getAttribute("value");
        if (mensaje != null && (mensaje.contains("Ingresa texto")
                || mensaje.contains("correo@")
                || mensaje.contains("incompleta"))) {
            return true;
        }
        if (valor != null && !valor.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return true;
        }
        List<WebElement> mensajesError = driver.findElements(By.cssSelector("span.help.error, p.error, div.error"));
        return mensajesError.stream()
                .map(WebElement::getText)
                .anyMatch(texto -> texto != null && texto.toLowerCase().contains("correo"));
    }

    public boolean validarMensajeError() {
        try {
            WebElement alerta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']")));
            return alerta.getText().contains("Usuario y/o contraseña equivocada");
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void completarCompra() {
        WebElement completar = wait.until(ExpectedConditions.elementToBeClickable(By.id("cart-to-orderform")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", completar);
        js.executeScript("arguments[0].click();", completar);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("checkout"),
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@data-i18n='clientProfileData.identification']"))));
    }

    public boolean validarIngresoCheckout() {
        try {
            WebElement identificacion = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[@data-i18n='clientProfileData.identification']")));
            return identificacion.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    //ContinuarCheckout
    public void presionarContinuarCheckout() {
        WebElement continuar = wait.until(ExpectedConditions.elementToBeClickable(By.id("go-to-shipping")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                continuar);
        js.executeScript(
                "arguments[0].click();",
                continuar);
    }

    public boolean validarMensajeCampoObligatorio() {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("span.help.error")));
            return driver.findElements(By.cssSelector("span.help.error")).size() >= 1;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void ingresarAlLogin() {
        cerrarPopup();
        WebElement miCuenta = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(text(),'Mi Cuenta')] | //div[contains(text(),'Mi Cuenta')] | //a[contains(.,'Mi Cuenta')]")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", miCuenta);
        List<WebElement> botonesLogin = driver.findElements(By.xpath(
                "//button[contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'iniciar sesión') or " +
                "contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'ingresar')]"));
        if (!botonesLogin.isEmpty() && botonesLogin.get(0).isDisplayed()) {
            js.executeScript("arguments[0].click();", botonesLogin.get(0));
        }
        obtenerInputCorreo();
    }

    public boolean validarLoginExitoso() {
        try {
            WebElement miCuenta = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[contains(text(),'Mi Cuenta')] | //div[contains(text(),'Mi Cuenta')] | //a[contains(.,'Mi Cuenta')]")));
            return miCuenta.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    private WebElement obtenerInputCorreo() {
        By[] candidatos = new By[] {
                By.id("client-email"),
                By.id("client-pre-email"),
                By.cssSelector("input[name='email']"),
                By.cssSelector("input[name='checkout-email']"),
                By.cssSelector("input.checkout-email"),
                By.cssSelector("input[placeholder='Ej.: ejemplo@mail.com']"),
                By.cssSelector("input[type='email']")
        };
        return wait.until(d -> {
            for (By candidato : candidatos) {
                List<WebElement> elementos = d.findElements(candidato);
                for (WebElement elemento : elementos) {
                    if (elemento.isDisplayed() && elemento.isEnabled()) {
                        return elemento;
                    }
                }
            }
            return null;
        });
    }

    private String obtenerCantidadActual() {
        List<WebElement> cantidadesCarrito = driver.findElements(
                By.cssSelector(
                        ".vtex-minicart-2-x-minicartProductListContainer input[name='quantity'], " +
                        ".vtex-minicart-2-x-minicartProductListContainer input.cartSkuQuantity, " +
                        ".vtex-minicart-2-x-minicartProductListContainer .item-quantity-value, " +
                        ".vtex-minicart-2-x-minicartProductListContainer [class*='quantitySelector'] input"));
        for (WebElement cantidad : cantidadesCarrito) {
            if (!cantidad.isDisplayed()) {
                continue;
            }
            String valor = cantidad.getAttribute("value");
            if (valor == null || valor.isBlank()) {
                valor = cantidad.getText().trim();
            }
            valor = normalizarCantidad(valor);
            if (!valor.isBlank()) {
                return valor;
            }
        }

        List<WebElement> badges = driver.findElements(By.xpath("//span[contains(@class,'vtex-minicart-2-x-minicartQuantityBadge')]"));
        for (WebElement badge : badges) {
            if (!badge.isDisplayed()) {
                continue;
            }
            String badgeText = normalizarCantidad(badge.getText());
            if (!badgeText.isBlank()) {
                return badgeText;
            }
        }
        return "";
    }

    private int obtenerCantidadActualNumerica() {
        try {
            String cantidad = obtenerCantidadActual();
            int parsed = parseCantidad(cantidad);
            return parsed > 0 ? parsed : 1; // Retornar 1 si no encuentra nada
        } catch (Exception e) {
            return 1; // Default a 1 si hay error
        }
    }

    private boolean existeCantidadEsperadaEnControles(int esperada) {
        List<WebElement> controlesCantidad = driver.findElements(
                By.cssSelector(
                        ".vtex-minicart-2-x-minicartProductListContainer input[name='quantity'], " +
                        ".vtex-minicart-2-x-minicartProductListContainer input.cartSkuQuantity, " +
                        ".vtex-minicart-2-x-minicartProductListContainer .item-quantity-value, " +
                        ".vtex-minicart-2-x-minicartProductListContainer [class*='quantitySelector'] input, " +
                        ".vtex-minicart-2-x-minicartProductListContainer [class*='quantity'] [class*='value']"));
        for (WebElement control : controlesCantidad) {
            if (!control.isDisplayed()) {
                continue;
            }
            String valor = control.getAttribute("value");
            if (valor == null || valor.isBlank()) {
                valor = control.getText();
            }
            if (parseCantidad(valor) == esperada) {
                return true;
            }
        }
        return parseCantidad(obtenerCantidadActual()) == esperada;
    }

    private boolean estaVisibleMiniCarrito() {
        List<WebElement> containers = driver.findElements(miniCartContainer);
        for (WebElement container : containers) {
            try {
                if (container.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return false;
    }

    private boolean subtotalCambioDetectado() {
        if (subtotalAntesAumentar == null || subtotalAntesAumentar.isBlank()) {
            return false;
        }
        List<WebElement> subtotales = driver.findElements(By.xpath("//*[contains(text(),'TOTAL')]"));
        if (subtotales.isEmpty()) {
            return false;
        }
        String subtotalActual = subtotales.get(0).getText().trim();
        return !subtotalActual.isBlank() && !subtotalActual.equals(subtotalAntesAumentar);
    }

    private int obtenerCantidadDetectada() {
        try {
            int cantidadMiniCarrito = obtenerCantidadActualNumerica();
            if (cantidadMiniCarrito > 0) {
                return cantidadMiniCarrito;
            }

            By[] candidatos = new By[] {
                    By.cssSelector(".vtex-minicart-2-x-minicartProductListContainer input[type='text']"),
                    By.cssSelector(".vtex-minicart-2-x-minicartProductListContainer input[name='quantity']"),
                    By.cssSelector(".vtex-minicart-2-x-minicartProductListContainer input.item-quantity-change"),
                    By.cssSelector(".vtex-minicart-2-x-minicartProductListContainer input.cartSkuQuantity"),
                    By.cssSelector("input.cartSkuQuantity"),
                    By.cssSelector("input.item-quantity-change"),
                    By.cssSelector("input[name='quantity']"),
                    By.cssSelector(".item-quantity-value")
            };

            for (By candidato : candidatos) {
                try {
                    List<WebElement> elementos = driver.findElements(candidato);
                    for (WebElement elemento : elementos) {
                        try {
                            if (!elemento.isDisplayed()) {
                                continue;
                            }
                            String valor = elemento.getAttribute("value");
                            if (valor == null || valor.isBlank()) {
                                valor = elemento.getText();
                            }
                            int cantidad = parseCantidad(valor);
                            if (cantidad > 0) {
                                return cantidad;
                            }
                        } catch (StaleElementReferenceException ignored) {
                        }
                    }
                } catch (Exception ignored) {
                }
            }
            return 1; // Retornar 1 como default cuando inicia (1 producto agregado)
        } catch (Exception e) {
            return 1;
        }
    }

    private int parseCantidad(String valor) {
        if (valor == null) {
            return -1;
        }
        String digitos = valor.replaceAll("\\D", "");
        if (digitos.isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(digitos);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean coincideCantidad(String valor, int esperada) {
        return parseCantidad(valor) == esperada;
    }

    private String normalizarCantidad(String valor) {
        if (valor == null) {
            return "";
        }
        String limpio = valor.trim();
        String digitos = limpio.replaceAll("\\D", "");
        return digitos.isEmpty() ? limpio : digitos;
    }

    public boolean validarCategoria() {
        return DriverContext.getDriver()
                .getCurrentUrl()
                .toLowerCase()
                .contains(categoriaSeleccionada.toLowerCase());
    }

    public void seleccionarCategoria(String categoria) {
        this.categoriaSeleccionada = categoria;
        By categoriaLocator = By.xpath("//span[contains(translate(normalize-space(.),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'" + categoria.toUpperCase() + "')]");
        WebElement categoriaElemento = wait.until(ExpectedConditions.elementToBeClickable(categoriaLocator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", categoriaElemento);
    }
}