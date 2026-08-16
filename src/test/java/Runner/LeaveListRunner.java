package Runner;

import io.cucumber.core.cli.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ejecuta las pruebas de Cucumber de Leave List.
 * En IntelliJ: clic derecho → Run 'LeaveListRunner.main()'
 * Con tags (ej. solo test-18): pasa argumentos "--tags" "@test-18"
 */
public class LeaveListRunner {

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("INICIANDO EJECUCIÓN DE CUCUMBER DIRECTAMENTE");
        System.out.println("====================================================");

        List<String> cucumberArgs = new ArrayList<>(Arrays.asList(
                "--plugin", "pretty",
                "--plugin", "html:target/leave-list-report.html",
                "--glue", "StepDefinition"
        ));

        // Permite filtrar: --tags @test-18
        if (args != null && args.length > 0) {
            cucumberArgs.addAll(Arrays.asList(args));
        } else {
            cucumberArgs.add("--tags");
            cucumberArgs.add("@Leave");
        }

        cucumberArgs.add("src/test/resources/features/LeaveList.feature");

        byte exitStatus = Main.run(
                cucumberArgs.toArray(new String[0]),
                Thread.currentThread().getContextClassLoader()
        );

        System.out.println("====================================================");
        System.out.println("EJECUCIÓN DE CUCUMBER FINALIZADA CON CÓDIGO: " + exitStatus);
        System.out.println("====================================================");

        System.exit(exitStatus);
    }
}
