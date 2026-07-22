package StepDefinition;

import Constant.Constant;
import Control.DriverContext;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {
    private Scenario scenario;
    private static final String tomarCapturaPantalla;

    static{
        tomarCapturaPantalla = System.getProperty("evidence","fullEvidence");
    }

    @Before
    public void setUp(Scenario scenario){
        this.scenario = scenario;
        Constant.scenarioStep = scenario;
        Constant.build_name = "Nombre de Proyecto";
    }

    @After
    public void tearDown(){
        DriverContext.quitDriver();
    }

    public void generarEvidencia(String imageRefName){
        if (DriverContext.getDriver() != null && this.scenario != null) {
            try {
                byte[] screenShot = ((TakesScreenshot) DriverContext.getDriver()).getScreenshotAs(OutputType.BYTES);
                this.scenario.attach(screenShot, "image/png", imageRefName);
            } catch (RuntimeException ignored) {
            }
        }
    }

    @SuppressWarnings("unused")
    @AfterStep
    public void capturaEvidencia(){
        if(this.scenario.isFailed()){
            generarEvidencia("[FAIL] Step ScreenShots");
        }else if(Hooks.tomarCapturaPantalla.equalsIgnoreCase("fullEvidence")){
            generarEvidencia("[SUCCESS] Step ScreenShots");
        }
    }
}