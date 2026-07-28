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
                        "--glue", "StepDefinition",
                        "--tags", "@MarketplaceF",
                        "src/test/resources/features/Marketplace.feature"
                },
                Thread.currentThread().getContextClassLoader()
        );

        Assert.assertEquals("Falló la ejecución de Cucumber para MarketplaceF", 0, exitStatus);
    }
}
