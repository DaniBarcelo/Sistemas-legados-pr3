/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package practica3;

import java.io.IOException;
import java.util.List;

import net.sourceforge.tess4j.TesseractException;

import java.awt.AWTException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import practica3.service.Program;
import practica3.domain.Game;

@SpringBootApplication
public class App {
    public static void main(String[] args) throws AWTException, InterruptedException, TesseractException {
        /*
        try {
            Program program = new Program();
            program.viewInfo();
            Game g = program.listData("Reversi");
            List<Game> games = program.listGamesInLocation("B");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        SpringApplicationBuilder builder = new SpringApplicationBuilder(App.class);
        builder.headless(false).run(args);
    }
}
