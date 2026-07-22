package Control;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Constant.Constant;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.fail;

public class BaseController {

    public BaseController() {
        WebDriver driver = DriverContext.getDriver();
        if (driver != null) {
            PageFactory.initElements(new AjaxElementLocatorFactory(driver, Constant.TIME_RESPONSE), this);
        } else {
            System.out.println("WebDriver no está inicializado! Page Objects no serán inicializados.");
        }
    }

    public boolean visualizarElemento(WebElement elementoWeb, int tiempoEspera) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverContext.getDriver(), Duration.ofSeconds(tiempoEspera));
            wait.until(ExpectedConditions.visibilityOf(elementoWeb));
            System.out.println("Es visible el elemento web " + elementoWeb.getText());
            return true;
        } catch (Exception e) {
            System.out.println("No es visible el elemento web. Causa: " + e.getMessage());
            return false;
        }
    }

    public void hacerClickEnBotonPorTexto(String textoBoton) {
        try {
            // Se crea un XPath dinámico para encontrar el botón por su texto.
            String xpath = "//button[contains(text(),'" + textoBoton + "')]";
            WebElement boton = DriverContext.getDriver().findElement(By.xpath(xpath));

            if (visualizarElemento(boton, 10)) {
                boton.click();
            } else {
                fail("No se pudo encontrar o hacer clic en el botón con el texto: '" + textoBoton + "'");
            }
        } catch (Exception e) {
            fail("Error al hacer clic en el botón con texto '" + textoBoton + "'. Causa: " + e.getMessage());
        }
    }
}