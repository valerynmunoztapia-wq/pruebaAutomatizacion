package StepDefinition;
import Constant.Navegador;
import ObjectPage.MarketplacePage;
import Control.DriverContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class MarketplaceDefinition {
    private MarketplacePage marketplacePage;

    @Given("abro el navegador en la url {string}")
    public void abroElNavegadorEnLaUrl(String url) {
        DriverContext.setUp(Navegador.Chrome, url);
        marketplacePage = new MarketplacePage();
        marketplacePage.ingresarPagina(url);
    }

    @When("hago clic en el buscador")
    public void hagoClicEnElBuscador() {
        try {
            if (marketplacePage == null) {
                marketplacePage = new MarketplacePage();
            }
            marketplacePage.hacerClicBuscador();
        } catch (Exception e) {
            Assert.fail("Error al hacer clic en el buscador: " + e.getMessage());
        }
    }

    @And("escribo {string}")
    public void escribo(String producto) {
        marketplacePage.escribirProducto(producto);
    }

    @And("presiono Enter")
    public void presionoEnter() {
        marketplacePage.presionarEnter();
    }

    @Then("valido que existan productos")
    public void validoQueExistanProductos() {
        Assert.assertTrue(marketplacePage.existenProductos());
    }

    @When("selecciono el primer producto")
    public void seleccionoElPrimerProducto() {
        marketplacePage.seleccionarPrimerProducto();
    }

    @When("agrego el producto al carro")
    public void agregoElProductoAlCarro() {
        marketplacePage.agregarAlCarrito();
    }

    @When("ingreso al Mini-carrito")
    public void ingresoAlMiniCarrito() {
        marketplacePage.irAlMiniCarrito();
    }

    //cantidaddeproductos
    @And("ingreso al carrito")
    public void ingresoAlCarrito() {
        marketplacePage.irAlCarrito();
    }

    @Then("^valido que el producto \"([^\"]*)\" est.* en el carrito$")
    public void validoProductoEnCarrito(String nombreProducto) {
        Assert.assertTrue(marketplacePage.validarProductoEnCarritoPorNombre(nombreProducto));
    }

    @And("aumento la cantidad del producto")
    public void aumentoLaCantidadDelProducto() {
        marketplacePage.aumentarCantidad();
    }

    @Then("valido que la cantidad sea 2")
    public void validoQueLaCantidadSea2() {
        Assert.assertTrue(marketplacePage.validarCantidad(2));
    }

    @Then("valido que el subtotal se actualice")
    public void validoQueElSubtotalSeActualice() {
        Assert.assertTrue(marketplacePage.validarSubtotal());
    }

    //vaciarcarro
    @When("elimino el producto")
    public void eliminoElProducto() {
        marketplacePage.eliminarProducto();
    }

    @Then("^valido que el carrito quede vac[ií]o$")
    public void validoQueElCarritoQuedeVacio() {
        Assert.assertTrue(marketplacePage.validarCarritoVacio());
    }

    @Then("valido que no existan productos")
    public void validoQueNoExistanProductos() {
        Assert.assertTrue(marketplacePage.noExistenProductos());
    }


    //refrech
    @When("^refresco la p[aá]gina$")
    public void refrescoLaPagina() {
        marketplacePage.refrescarPagina();
    }

    @Then("valido que el producto permanezca en el carrito")
    public void validoQueElProductoPermanezcaEnElCarrito() {
        Assert.assertTrue(marketplacePage.validarProductoPermanece());
    }

    //validar correo
    @When("escribo el correo {string} en {string}")
    public void escriboElCorreo(String correo, String contexto) {
        if (marketplacePage == null) {
            marketplacePage = new MarketplacePage();
        }
        if (contexto.equalsIgnoreCase("login")) {
            marketplacePage.escribirCorreo(correo); // login
        } else if (contexto.equalsIgnoreCase("checkout")) {
            marketplacePage.escribirCorreoCheckout(correo); // checkout
        }
    }

    @When("escribo la contraseña {string}")
    public void escriboLaContrasena(String password) {
        marketplacePage.escribirPassword(password);
    }

    @When("presiono el botón Entrar")
    public void presionoElBotonEntrar() {
        marketplacePage.hacerClickEntrar();
    }

    //invalid
    @Then("valido que aparece el mensaje de error")
    public void validoQueApareceElMensajeDeError() {
        Assert.assertTrue(
                marketplacePage.validarMensajeError());
    }

    //Checkout
    @When("presiono completar la compra")
    public void presionoCompletarLaCompra() {
        marketplacePage.completarCompra();

    }

    @When("presiono proceder al pago")
    public void presionoProcederAlPago() {
        marketplacePage.irAlCarrito();
        marketplacePage.completarCompra();
    }

    @When("presiono continuar")
    public void presionoContinuar() {
        if (marketplacePage == null) {
            marketplacePage = new MarketplacePage();
        }
        marketplacePage.presionarContinuarCheckout();
    }

    @When("ingreso al login")
    public void ingresoAlLogin() {
        if (marketplacePage == null) {
            marketplacePage = new MarketplacePage();
        }
        marketplacePage.ingresarAlLogin();
    }

    @Then("valido que el usuario inició sesión")
    public void validoQueElUsuarioInicioSesion() {
        Assert.assertTrue(marketplacePage.validarLoginExitoso());
    }

    @Then("valido que el usuario permanezca sin sesión")
    public void validoQueElUsuarioPermanezcaSinSesion() {
        Assert.assertTrue(marketplacePage.validarMensajeError());
    }

    @Then("valido que ingreso al checkout")
    public void validoQueIngresoAlCheckout() {
        Assert.assertTrue(marketplacePage.validarIngresoCheckout());
    }

    @When("presiono continuar sin completar los datos")
    public void presionoContinuarSinCompletarLosDatos() {
        marketplacePage.presionarContinuarCheckout();
    }

    @Then("valido mensaje de campos obligatorios")
    public void validoMensajeDeCamposObligatorios() {
        Assert.assertTrue(marketplacePage.validarMensajeCampoObligatorio());
    }

    //validacampocorreo
    @Then("valido mensaje de correo invalido")
    public void validoMensajeDeCorreoInvalido() {
        Assert.assertTrue(marketplacePage.validarCorreoInvalido());
    }

    @When("selecciono la categoría {string}")
    public void seleccionoLaCategoria(String categoria) {
        if (marketplacePage == null) {
            marketplacePage = new MarketplacePage();
        }
        marketplacePage.cerrarPopup();
        marketplacePage.seleccionarCategoria(categoria);
    }

    @Then("valido que estoy en la categoría")
    public void validoQueEstoyEnLaCategoria() {
        Assert.assertTrue(marketplacePage.validarCategoria());
    }
}
