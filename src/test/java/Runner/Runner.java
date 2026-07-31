package Runner;

import io.cucumber.core.cli.Main;
import org.junit.Assert;
import org.junit.Test;

public class Runner {

    @Test
    public void Runner() {
        byte exitStatus = Main.run(
                new String[]{
                        "--plugin", "pretty",
                        "--plugin", "html:target/cucumber-report.html",
                        "--plugin", "junit:build/test-results/test/cucumber.xml",
                        "--glue", "StepDefinition",
                        "--tags", "@Busqueda",
                        "src/test/resources/features/MarketplaceF.feature"
                },
                Thread.currentThread().getContextClassLoader()
        );

        Assert.assertEquals("Falló la ejecución de Cucumber para MarketplaceF", 0, exitStatus);
    }
}
