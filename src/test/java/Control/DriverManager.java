package Control;

import Constant.Navegador;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverManager {
    private WebDriver driver;

    protected void resolverDriver(Navegador nav, String url){
        String os = System.getProperty("os.name").toLowerCase();
        String osVersion = System.getProperty("os.version").toLowerCase();
        System.out.println("\nSistema Operativo :" + os+", "+osVersion);
        System.out.println("\nNavegador :" + nav);
        switch (nav){
            case Chrome:
               System.out.println("Chrome seleccionado");
               WebDriverManager.chromedriver().setup();
               ChromeOptions configuracionChrome = new ChromeOptions();
                // === CONFIGURACIÓN AVANZADA PARA CORREGIR LOS COLORES VERDE/VIOLETA ===
                configuracionChrome.addArguments("--force-dark-mode=false");
                configuracionChrome.addArguments("--blink-settings=forceDarkModeEnabled=false");
                configuracionChrome.addArguments("--disable-features=DarkMode");

                // Desactiva la aceleración por hardware (Suele causar el bug de tinte verde/violeta)
                configuracionChrome.addArguments("--disable-gpu");
                configuracionChrome.addArguments("--disable-software-rasterizer");

                // Fuerza a Chrome a ignorar perfiles antiguos y arrancar totalmente limpio
                configuracionChrome.addArguments("--guest");
                configuracionChrome.addArguments("--incognito");
               if(os.contains("linux")){
                   System.out.println(System.getProperty("user.name"));
                   configuracionChrome.addArguments("--disable-dev-shm-usage");
                   configuracionChrome.addArguments("--no-sandbox");
                   configuracionChrome.addArguments("--disable-gpu");
                   configuracionChrome.addArguments("--headless");
                   configuracionChrome.addArguments("--ignore-ssl-errors=yes");
                   configuracionChrome.addArguments("--window-size=1920,1080");
               }
               configuracionChrome.addArguments("--remote-allow-origins=yes");
               this.driver = new ChromeDriver(configuracionChrome);
               this.driver.manage().deleteAllCookies();
               break;
            default:
               System.out.println("No es posible levantar el navegador "+nav);
               break;
        }
        driver.manage().window().maximize();
        driver.get(url);
    }

    protected WebDriver getDriver(){
        if(driver == null){
            return null;
        }else{
            return driver;
        }
    }
}
