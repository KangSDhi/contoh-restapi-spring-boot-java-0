package com.example.hellorestapi0.controller;

import com.example.hellorestapi0.model.ResponseTutorial;
import com.example.hellorestapi0.model.Tutorial;
import com.example.hellorestapi0.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TutorialController {

    @Autowired
    TutorialRepository tutorialRepository;

    @GetMapping("/tutorials")
    public ResponseEntity<ResponseTutorial<List<Tutorial>>> getAllTutorials(@RequestParam(required = false) String title){
        try{
            List<Tutorial> tutorials = new ArrayList<Tutorial>();

            if (title == null)
                tutorialRepository.findAll().forEach(tutorials::add);
            else
                tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

            if (tutorials.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            ResponseTutorial<List<Tutorial>> responseTutorial = new ResponseTutorial<>(200, "Yay Sukses", tutorials);

            return new ResponseEntity<>(responseTutorial, HttpStatus.valueOf(200));
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<ResponseTutorial<Tutorial>> getTutorialById(@PathVariable("id") long id){
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

        if (tutorialData.isPresent()){
            ResponseTutorial<Tutorial> responseTutorial = new ResponseTutorial<>(200, "Ok Deh", tutorialData.get());
            return new ResponseEntity<>(responseTutorial, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tutorials")
    public ResponseEntity<ResponseTutorial<Tutorial>> createTutorial(@RequestBody Tutorial tutorial){
        try {
            Tutorial _tutorial = tutorialRepository.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
            ResponseTutorial<Tutorial> responseTutorial = new ResponseTutorial<>(201, "ok create", _tutorial);
            return new ResponseEntity<>(responseTutorial, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<ResponseTutorial<Tutorial>> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial){
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

        if (tutorialData.isPresent()){
            Tutorial _tutorial = tutorialData.get();
            _tutorial.setTitle(tutorial.getTitle());
            _tutorial.setDescription(tutorial.getDescription());
            _tutorial.setPublished(tutorial.isPublished());
            ResponseTutorial<Tutorial> responseTutorial = new ResponseTutorial<>(201, "ok update", tutorialRepository.save(_tutorial));

            return new ResponseEntity<>(responseTutorial, HttpStatus.CREATED);

        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id){
        try {
            tutorialRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tutorials")
    public ResponseEntity<HttpStatus> deleteAllTutorials(){
        try{
            tutorialRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<ResponseTutorial<List<Tutorial>>> findByPublished(){
        try{
            List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

            if (tutorials.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            ResponseTutorial<List<Tutorial>> responseTutorial = new ResponseTutorial<>(200, "ok published", tutorials);
            return new ResponseEntity<>(responseTutorial, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
