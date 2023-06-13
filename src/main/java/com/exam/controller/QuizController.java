package com.exam.controller;

import com.exam.model.exam.Category;
import com.exam.model.exam.Quiz;
import com.exam.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    //add quiz
    @PostMapping("/")
    public ResponseEntity<Quiz> add(@RequestBody Quiz quiz){
        return ResponseEntity.ok(this.quizService.addQuiz(quiz));
    }

    //update quiz
    @PutMapping("/")
    public ResponseEntity<Quiz> update(@RequestBody Quiz quiz){
        return ResponseEntity.ok(this.quizService.updateQuiz(quiz));
    }

    //Get quizzes
    @GetMapping("/")
    public ResponseEntity<?> quizzes(){
        return ResponseEntity.ok(this.quizService.getQuizzes());
    }

    //get single quiz
    @GetMapping("/{qid}")
    public ResponseEntity<Quiz> quiz(@PathVariable("qid") Long qid){
        return ResponseEntity.ok(this.quizService.getQuiz(qid));
    }

    //delte quiz
    @DeleteMapping("/{qid}")
    public void delete(@PathVariable("qid") Long qid){
        this.quizService.deleteQuiz(qid);
    }

    //get quizzes of category
    @GetMapping("/category/{cId}")
    public ResponseEntity<List<Quiz>> getQuizzesOfCategory(@PathVariable("cId") Long cId){

        Category category = new Category();
        category.setcId((cId));

        List<Quiz> quizzesOfCategory = this.quizService.getQuizzesOfCategory(category);
        return ResponseEntity.ok(quizzesOfCategory);
    }

    //get active quizzes
    @GetMapping("/active")
    public List<Quiz> getActiveQuizzes(){
        return this.quizService.getActiveQuizzes();
    }

    //get active quizzes of category
    @GetMapping("/category/active/{cId}")
    public List<Quiz> getActiveQuizzesByCategory(@PathVariable("cId") Long cId){
        Category category = new Category();
        category.setcId(cId);
        return this.quizService.getActiveQuizzesOfCategory(category);
    }
}
