package Runner;

import io.cucumber.core.cli.Main;

/**
 * Esta clase es ahora un programa Java ejecutable que lanza las pruebas de Cucumber.
 * Para ejecutar, haz clic derecho en esta clase y selecciona "Run 'LeaveListRunner.main()'".
 * Esto evita por completo los problemas del ejecutor de pruebas de Gradle.
 */
public class LeaveListRunner {

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("INICIANDO EJECUCIÓN DE CUCUMBER DIRECTAMENTE");
        System.out.println("====================================================");

        byte exitStatus = Main.run(
                new String[]{
                        "--plugin", "pretty",
                        "--plugin", "html:target/leave-list-report.html",
                        "--glue", "StepDefinition",
                        "src/test/resources/features/LeaveList.feature"
                },
                Thread.currentThread().getContextClassLoader()
        );

        System.out.println("====================================================");
        System.out.println("EJECUCIÓN DE CUCUMBER FINALIZADA CON CÓDIGO: " + exitStatus);
        System.out.println("====================================================");

        // Forzar la salida del sistema para asegurar que el proceso termine.
        System.exit(exitStatus);
    }
}