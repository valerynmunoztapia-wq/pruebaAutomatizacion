package ObjectPage;

import Control.BaseController;
import Control.DriverContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.junit.jupiter.api.Assertions.fail;

public class LoginPage extends BaseController {

    @SuppressWarnings("unused")
    @FindBy(id = "username")
    private WebElement nombreUsuario;

    @SuppressWarnings("unused")
    @FindBy(css = "input[name='password']")
    private WebElement passwordUsuario;

    public void escribirUsersname(String nUsuario) {
        try {
            if (visualizarElemento(this.nombreUsuario, 10)) {
                this.nombreUsuario.clear();
                this.nombreUsuario.sendKeys(nUsuario);
            } else {
                fail("No se encuentra el campo Nombre de Usuario en la página");
            }
        } catch (Exception e) {
            fail("Error al escribir en el campo de usuario: " + e.getMessage());
        }
    }

    public void escribirPassword(String pass) {
        try {
            this.passwordUsuario.clear();
            this.passwordUsuario.sendKeys(pass);
        } catch (Exception e) {
            fail("Error al escribir en el campo de password: " + e.getMessage());
        }
    }

    public void validarMensaje(String mensaje) {
        try {
            String xpath = "//*[contains(text(),\"" + mensaje + "\")]";
            WebElement mensajeElemento = DriverContext.getDriver().findElement(By.xpath(xpath));
            if (visualizarElemento(mensajeElemento, 10)) {
                System.out.println("Mensaje esperado '" + mensaje + "' está visible.");
            } else {
                fail("No se pudo encontrar el mensaje: '" + mensaje + "'");
            }
        } catch (Exception e) {
            fail("Error al validar el mensaje '" + mensaje + "'. Causa: " + e.getMessage());
        }
    }
    public void presionarBotonLogin() {
        try {
            WebElement botonLogin = DriverContext.getDriver().findElement(
                    By.xpath("//button[contains(., 'Entrar')]")
            );
            if (visualizarElemento(botonLogin, 10)) {
                botonLogin.click();
            } else {
                fail("No se encontró el botón de Ingresar en la página.");
            }
        } catch (Exception e) {
                fail("Error al presionar el botón de Ingresar: " + e.getMessage());
            }

        }
    }