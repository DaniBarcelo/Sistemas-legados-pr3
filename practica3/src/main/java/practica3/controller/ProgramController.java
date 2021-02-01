package practica3.controller;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import practica3.domain.Game;
import practica3.service.Program;

import java.io.IOException;
import java.util.List;

@RestController
public class ProgramController {
    @Autowired
    private Program program;

    @GetMapping("/program/viewInfo")
    public ResponseEntity<String> getInfo() {
        try {

            String result = program.viewInfo();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("error en view info", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/program/listData/{name}")
    public ResponseEntity<Game> getGame(@PathVariable("name") String name) throws InterruptedException, IOException, TesseractException {

            Game g = program.listData(name);
            return new ResponseEntity<>(g, HttpStatus.OK);
    }

    @GetMapping("/program/listGamesInLocation/{tape}")
    public ResponseEntity<List<Game>> getlistGamesInLocation(@PathVariable("tape") String tape) throws InterruptedException, IOException, TesseractException {
        List<Game> games = program.listGamesInLocation(tape);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }
}
