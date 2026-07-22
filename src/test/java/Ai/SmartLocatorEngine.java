package Ai;
import org.openqa.selenium.*;
import java.util.List;



public class SmartLocatorEngine {
    private final WebDriver driver;

    private final OpenAIService aiService =
            new OpenAIService();

    public SmartLocatorEngine(WebDriver driver) {

        this.driver = driver;
    }

    public WebElement findElement(By locator) {

        try {

            return driver.findElement(locator);

        } catch (NoSuchElementException ex) {

            System.out.println(
                    "Locator falló: "
                            + locator
            );

            try {

                String dom =
                        driver.getPageSource();

                String nuevoXpath =
                        aiService.generarNuevoXpath(
                                locator.toString(),
                                reducirDom(dom)
                        );

                System.out.println(
                        "Nuevo xpath IA: "
                                + nuevoXpath
                );

                List<WebElement> elements =
                        driver.findElements(
                                By.xpath(nuevoXpath)
                        );

                if (elements.size() == 1) {

                    System.out.println(
                            "Locator recuperado"
                    );

                    return elements.get(0);
                }

                throw new RuntimeException(
                        "IA encontró múltiples elementos"
                );

            } catch (Exception iaEx) {

                throw new RuntimeException(
                        "No fue posible recuperar locator",
                        iaEx
                );
            }
        }
    }

    private String reducirDom(String dom) {

        int max = 12000;

        if (dom.length() > max) {

            return dom.substring(0, max);
        }

        return dom;
    }
}
