package Control;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Centraliza acciones comunes sobre elementos del DOM
 * Simplifica y reutiliza operaciones frecuentes en Page Objects
 */
public class ElementActions {

    /**
     * Envía texto a un campo con limpieza previa
     */
    public static void sendText(WebDriver driver, By locator, String text) {
        WebElement element = WaitUtils.waitForElementClickable(driver, locator);
        element.clear();
        element.sendKeys(text);
        System.out.println("✓ Texto enviado a " + locator + ": " + text);
    }

    /**
     * Envía texto a un elemento WebElement ya localizado
     */
    public static void sendText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
        System.out.println("✓ Texto enviado a elemento: " + text);
    }

    /**
     * Realiza clic en un elemento por localizador
     */
    public static void click(WebDriver driver, By locator) {
        WebElement element = WaitUtils.waitForElementClickable(driver, locator);
        element.click();
        System.out.println("✓ Clic realizado en: " + locator);
    }

    /**
     * Realiza clic en un WebElement
     */
    public static void click(WebElement element) {
        WaitUtils.waitForWebElementClickable(DriverContext.getDriver(), element);
        element.click();
        System.out.println("✓ Clic realizado en elemento");
    }

    /**
     * Clic con Actions (útil para elementos que requieren hover)
     */
    public static void clickWithActions(WebDriver driver, WebElement element) {
        WaitUtils.waitForWebElementClickable(driver, element);
        Actions actions = new Actions(driver);
        actions.click(element).perform();
        System.out.println("✓ Clic con Actions realizado");
    }

    /**
     * Limpia y envía texto con presión de Enter (para búsquedas)
     */
    public static void searchText(WebDriver driver, By locator, String text) {
        WebElement element = WaitUtils.waitForElementClickable(driver, locator);
        element.clear();
        element.sendKeys(text);
        element.submit();
        System.out.println("✓ Búsqueda realizada: " + text);
    }

    /**
     * Obtiene texto de un elemento con espera explícita
     */
    public static String getText(WebDriver driver, By locator) {
        WebElement element = WaitUtils.waitForElementVisibility(driver, locator);
        String text = element.getText();
        System.out.println("✓ Texto obtenido: " + text);
        return text;
    }

    /**
     * Obtiene atributo de un elemento
     */
    public static String getAttribute(WebDriver driver, By locator, String attribute) {
        WebElement element = WaitUtils.waitForElementPresence(driver, locator);
        String value = element.getAttribute(attribute);
        System.out.println("✓ Atributo '" + attribute + "' obtenido: " + value);
        return value;
    }

    /**
     * Verifica si un elemento es visible
     */
    public static boolean isElementVisible(WebDriver driver, By locator) {
        try {
            WebElement element = WaitUtils.waitForElementVisibility(driver, locator, 3);
            return element.isDisplayed();
        } catch (Exception e) {
            System.out.println("ℹ Elemento no visible: " + locator);
            return false;
        }
    }

    /**
     * Verifica si un elemento existe en el DOM
     */
    public static boolean isElementPresent(WebDriver driver, By locator) {
        try {
            WaitUtils.waitForElementPresence(driver, locator, 3);
            return true;
        } catch (Exception e) {
            System.out.println("ℹ Elemento no presente: " + locator);
            return false;
        }
    }

    /**
     * Realiza scroll hacia un elemento
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
        System.out.println("✓ Scroll realizado al elemento");
    }

    /**
     * Espera y valida que un elemento contenga texto específico
     */
    public static boolean waitForTextInElement(WebDriver driver, By locator, String expectedText, int timeout) {
        try {
            WebElement element = WaitUtils.waitForElementVisibility(driver, locator, timeout);
            String actualText = element.getText();
            boolean matches = actualText.contains(expectedText);
            if (matches) {
                System.out.println("✓ Texto encontrado en elemento: " + expectedText);
            } else {
                System.out.println("✗ Texto no coincide. Esperado: " + expectedText + ", Actual: " + actualText);
            }
            return matches;
        } catch (Exception e) {
            System.err.println("✗ Error esperando texto en elemento: " + e.getMessage());
            return false;
        }
    }
}
