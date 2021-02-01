package practica3.service;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import com.sun.jna.platform.WindowUtils;

import org.springframework.stereotype.Service;
import practica3.domain.Game;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.Collections;


@Service
public class Program {

    private Robot robot;
    private static ITesseract ocr;
    private static final Logger logger = Logger.getLogger(Program.class.getName());
    

    private static final long DELAY_KEY = 50;
    private static final long DELAY_SCREENSHOT = 100;

    public Program() throws IOException, AWTException, InterruptedException {
        robot = new Robot();
    }


    private void startDatabase() throws IOException, InterruptedException {
        System.out.println("\\..\\..\\..\\..\\..\\..\\..\\Database-MSDOS");
        String path=System.getProperty("user.dir"); //returns the working directory
        String relativePath="\\..\\Database-MSDOS";
        path+=relativePath;
        Runtime.getRuntime().exec("cmd /c DOSBox-0.74\\DOSBox.exe Database\\gwbasic.bat -noconsole", null, new File(path));
    }

    private void pressKey(Integer key) throws InterruptedException {
        robot.keyPress(key);
        Thread.sleep(DELAY_KEY);

    }

    private void releaseKey(Integer key) throws InterruptedException {
        robot.keyRelease(key);
        Thread.sleep(DELAY_KEY);
    }


    private String captureScreen() throws InterruptedException, TesseractException, IOException {
        //Maximize and find DOSBox Window
        pressKey(KeyEvent.VK_ALT);
        pressKey(KeyEvent.VK_ENTER);
        releaseKey(KeyEvent.VK_ENTER);
        releaseKey(KeyEvent.VK_ALT);
        Thread.sleep(2000);
        final Rectangle rect = new Rectangle(0, 0, 0, 0); //needs to be final or effectively final for lambda
        WindowUtils.getAllWindows(true).forEach(desktopWindow -> {
            if (desktopWindow.getTitle().contains("DOSBox")) {
                rect.setRect(desktopWindow.getLocAndSize());
            }
        });
        BufferedImage capture = robot.createScreenCapture(rect);
        File file = new File("captures/screen-capture.png");
        boolean status = ImageIO.write(capture, "png", file);
        System.out.println("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());
        //Undo Maximize
        pressKey(KeyEvent.VK_ALT);
        pressKey(KeyEvent.VK_ENTER);
        releaseKey(KeyEvent.VK_ENTER);
        releaseKey(KeyEvent.VK_ALT);
        Thread.sleep(2000);
        Tesseract1 ocr = new Tesseract1();

        String path=System.getProperty("user.dir"); //returns the working directory
        ocr.setDatapath(path);
        ocr.setLanguage("spa");
        String result = ocr.doOCR(capture);
        if (result != null) {
            //logger.info("Generated screen capture in file " + result);
            return result;
        } else {
            logger.warning("No capture file generated");
            return null;
        }
    }


    public List<Game> sortGames(List<Game> games){
        for (int i=1;i<games.size();i++){
            for(int j=i+1;j<games.size();j++){
                if(games.get(i).getRegister().compareTo(games.get(j).getRegister())>0){
                    Collections.swap(games,i,j);
                }
            }
        }
        return games;
    }


    public String viewInfo() throws InterruptedException, IOException, TesseractException {
        startDatabase();
        Thread.sleep(5000);
        pressKey(KeyEvent.VK_4);
        releaseKey(KeyEvent.VK_4);
        String screen = captureScreen();
        System.out.println(screen);
        pressKey(KeyEvent.VK_CONTROL);
        pressKey(KeyEvent.VK_F9);
        releaseKey(KeyEvent.VK_CONTROL);
        releaseKey(KeyEvent.VK_F9);

        String lines[] = screen.split("\n");
        String words [] = lines[1].split(" ");
        String result = "Contiene " + words[1] + "archivos.";
        return result;
    }

    public Game listData (String name) throws IOException, InterruptedException, TesseractException {
        startDatabase();
        Thread.sleep(5000);
        pressKey(KeyEvent.VK_7);
        releaseKey(KeyEvent.VK_7);
        pressKey(KeyEvent.VK_SHIFT);
        pressKey(KeyEvent.VK_N);
        releaseKey(KeyEvent.VK_N);
        releaseKey(KeyEvent.VK_SHIFT);
        pressKey(KeyEvent.VK_ENTER);
        releaseKey(KeyEvent.VK_ENTER);
        for (int i=0;i<name.length();i++){
            char c = name.charAt(i);
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(Character.toUpperCase(c));
            robot.keyRelease(Character.toUpperCase(c));
            robot.keyRelease(KeyEvent.VK_SHIFT);
            Thread.sleep(50);
        }
        pressKey(KeyEvent.VK_ENTER);
        releaseKey(KeyEvent.VK_ENTER);
        String screen = captureScreen();
        System.out.println(screen);
        String gameWords [] = name.split(" ");
        String pieces [] = screen.split("\n");
        String gameData[] = pieces[0].split(" ");
        String gameId=gameData[0];
        String gameName = "";
        int j = 2;
        while(j-2<gameWords.length){
            gameName = gameName + gameData[j] + " ";
            j++;
        }
        String gameLocation = gameData[gameData.length-1];
        String gameType = gameData[j];
        j++;
        while(gameData[j]!=gameLocation){
            gameType=gameType+" "+gameData[j];
            j++;
        }
        gameLocation=gameLocation.replaceFirst("E",":");
        System.out.println(gameId);
        System.out.println(gameName);
        System.out.println(gameType);
        System.out.println(gameLocation);
        Game g = new Game(gameId,gameName,gameType,gameLocation);
        pressKey(KeyEvent.VK_CONTROL);
        pressKey(KeyEvent.VK_F9);
        releaseKey(KeyEvent.VK_CONTROL);
        releaseKey(KeyEvent.VK_F9);
        return g;
    }


    public List<Game> listGamesInLocation(String location) throws IOException, InterruptedException, TesseractException {
        startDatabase();
        Thread.sleep(5000);
        pressKey(KeyEvent.VK_3);
        releaseKey(KeyEvent.VK_3);
        pressKey(KeyEvent.VK_3);
        releaseKey(KeyEvent.VK_3);
        pressKey(KeyEvent.VK_ENTER);
        releaseKey(KeyEvent.VK_ENTER);
        //Duerme el proceso 30 segundos para dar tiempo al programa a ordenar la base de datos
        Thread.sleep(30000);
        pressKey(KeyEvent.VK_ENTER);
        releaseKey(KeyEvent.VK_ENTER);
        pressKey(KeyEvent.VK_6);
        releaseKey(KeyEvent.VK_6);
        //Escribir la cinta deseada
        for (int i=0;i<location.length();i++){
            char c = location.charAt(i);
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(Character.toUpperCase(c));
            robot.keyRelease(Character.toUpperCase(c));
            robot.keyRelease(KeyEvent.VK_SHIFT);
            Thread.sleep(50);
        }
        pressKey(KeyEvent.VK_ENTER);
        releaseKey(KeyEvent.VK_ENTER);
        //Dormir 5 segundos para dar tiempo a buscar
        Thread.sleep(5000);
        String screen = captureScreen();
        List<Game> games = new ArrayList<>();
        //Game games [];                  //Inicializar los juegos a "vacío"?
        Game nullGame = new Game();
        games.add(nullGame);
        String lines[] = screen.split("\n");
        int i=0;
        int j=0;                    //Para permitir comparar con el anterior y contar cuantos juegos han sido añadidos.
        do{
            if(i==0){
                System.out.println("Estoy en i=0");
                lines = screen.split("\n");
            }
            if(i==18 || lines[i].contains("space")){
                System.out.println("Estoy en i=19 o mi linea contiene la palabra space");
                pressKey(KeyEvent.VK_SPACE);
                releaseKey(KeyEvent.VK_SPACE);
                Thread.sleep(200);
                screen = captureScreen();
                i=0;
            }
            else{
                j++;
                i++;
                System.out.println(lines[i]);
                String pieces[] = lines[i].split(" ");
                String register = pieces[pieces.length-1];
                String id = pieces[0];
                String tape = pieces[pieces.length-2];
                String type = pieces[pieces.length-3];
                int k=4;
                if (type.contains("DEPORTIVO")){
                    type = pieces[pieces.length-4] + " " + type;
                    k=k+1;
                }
                if(type.contains("MESA")){
                    type = pieces[pieces.length-5] + " " + pieces[pieces.length-4] + " " + type;
                    k=k+2;
                }
                String name = pieces[pieces.length-k];
                while(pieces.length-k>1){
                    k++;
                    name = pieces[pieces.length-k] + " " + name;
                }
                System.out.println(id);
                System.out.println(name);
                System.out.println(type);
                System.out.println(tape);
                System.out.println(register);
                System.out.println("--------------------");
                Game g = new Game(id,name,type,tape,register);
                games.add(g);
            }
        }while(games.get(j).getLocation().contains(location) || (games.get(j).getLocation()==games.get(j-1).getLocation() || games.get(j-1).getLocation()==null || games.get(j).getLocation().contains(games.get(j-1).getLocation()) || games.get(j-1).getLocation().contains(games.get(j).getLocation())));
        games.remove(j);
        System.out.println("VOY A ORDENAR LOS JUEGOS");
        sortGames(games);
        for(int w=1;w<games.size();w++){
            System.out.println(games.get(w).getRegister());
        }
        return games;
    }


}
