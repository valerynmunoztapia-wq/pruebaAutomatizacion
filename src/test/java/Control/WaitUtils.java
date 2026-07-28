package Control;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Utilidad centralizada para manejo de esperas en Selenium
 * Proporciona métodos reutilizables con timeout configurable
 */
public class WaitUtils {
    
    private static final int DEFAULT_TIMEOUT = 10;
    private static final int SHORT_TIMEOUT = 5;
    private static final int LONG_TIMEOUT = 20;

    /**
     * Espera a que un elemento sea visible
     */
    public static WebElement waitForElementVisibility(WebDriver driver, By locator) {
        return waitForElementVisibility(driver, locator, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForElementVisibility(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            System.out.println("✓ Elemento visible: " + locator);
            return element;
        } catch (Exception e) {
            System.err.println("✗ Elemento no visible dentro de " + timeoutSeconds + "s: " + locator);
            throw new RuntimeException("Timeout esperando elemento: " + locator, e);
        }
    }

    /**
     * Espera a que un elemento sea clickeable
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator) {
        return waitForElementClickable(driver, locator, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForElementClickable(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            System.out.println("✓ Elemento clickeable: " + locator);
            return element;
        } catch (Exception e) {
            System.err.println("✗ Elemento no clickeable dentro de " + timeoutSeconds + "s: " + locator);
            throw new RuntimeException("Timeout esperando elemento clickeable: " + locator, e);
        }
    }

    /**
     * Espera a que un elemento ya encontrado sea clickeable
     */
    public static void waitForWebElementClickable(WebDriver driver, WebElement element) {
        waitForWebElementClickable(driver, element, DEFAULT_TIMEOUT);
    }

    public static void waitForWebElementClickable(WebDriver driver, WebElement element, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            wait.until(ExpectedConditions.elementToBeClickable(element));
            System.out.println("✓ WebElement clickeable");
        } catch (Exception e) {
            System.err.println("✗ WebElement no clickeable dentro de " + timeoutSeconds + "s");
            throw new RuntimeException("Timeout esperando WebElement clickeable", e);
        }
    }

    /**
     * Espera a que un elemento esté presente en el DOM
     */
    public static WebElement waitForElementPresence(WebDriver driver, By locator) {
        return waitForElementPresence(driver, locator, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForElementPresence(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            System.out.println("✓ Elemento presente en DOM: " + locator);
            return element;
        } catch (Exception e) {
            System.err.println("✗ Elemento no presente en DOM dentro de " + timeoutSeconds + "s: " + locator);
            throw new RuntimeException("Timeout esperando elemento en DOM: " + locator, e);
        }
    }

    /**
     * Espera a que un elemento sea invisible
     */
    public static boolean waitForElementInvisibility(WebDriver driver, By locator) {
        return waitForElementInvisibility(driver, locator, DEFAULT_TIMEOUT);
    }

    public static boolean waitForElementInvisibility(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            System.out.println("✓ Elemento invisible: " + locator);
            return invisible;
        } catch (Exception e) {
            System.err.println("✗ Elemento aún visible dentro de " + timeoutSeconds + "s: " + locator);
            return false;
        }
    }

    /**
     * Pausa simple en segundos
     */
    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrumpido: " + e.getMessage());
        }
    }
}
