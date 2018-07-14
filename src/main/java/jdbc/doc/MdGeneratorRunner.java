package jdbc.doc;

import jdbc.doc.generator.MdGenerator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.IOException;

public class MdGeneratorRunner {

    public static void main(String[] args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MdGenerator mdGenerator = context.getBean(MdGenerator.class);
        Runnable task = () -> {
            try {
                mdGenerator.generateMdFile();
            } catch (IOException ignored) {
            }
        };
        runXTimes(12, task);
    }

    public static void runXTimes(int count, Runnable task) {
        for (int i = 0; i < count; i++) {
            long startTime = System.nanoTime();
            task.run();
            long endTime = System.nanoTime();
            long result = endTime - startTime;
            System.out.println("Attempt #" + i + ": " + result);
        }

    }

}
