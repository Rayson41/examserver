package com.exam.controller;

import com.exam.model.exam.Question;
import com.exam.model.exam.Quiz;
import com.exam.service.QuestionService;
import com.exam.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuizService quizService;

    //add question
    @PostMapping("/")
    public ResponseEntity<Question> add(@RequestBody Question question){
        return ResponseEntity.ok(this.questionService.addQuestion(question));
    }

    //update question
    @PutMapping("/")
    public ResponseEntity<Question> update(@RequestBody Question question) {
        return ResponseEntity.ok(this.questionService.updateQuestion(question));
    }

    //Get questions
    @GetMapping("/")
    public ResponseEntity<?> questions(){
        return ResponseEntity.ok(this.questionService.getQuestions());
    }

    //get single question
    @GetMapping("/{qid}")
    public ResponseEntity<Question> question(@PathVariable("qid") Long qid){
        return ResponseEntity.ok(this.questionService.getQuestion(qid));
    }

    //get questions of any quiz
    @GetMapping("/quiz/{qid}")
    public ResponseEntity<?> getQuestionsOfQuiz(@PathVariable("qid") Long qid){

//        Quiz quiz = new Quiz();
//        quiz.setqId(qid);
//
//        Set<Question> questionOfQuiz = this.questionService.getQuestionsOfQuiz(quiz);
//
//        return ResponseEntity.ok(questionOfQuiz);

        Quiz quiz = this.quizService.getQuiz(qid);
        Set<Question> questions = quiz.getQuestions();

        List<Question> list = new ArrayList(questions);
        if(list.size()>Integer.parseInt(quiz.getNumberOfQuestions())){
            list = list.subList(0,Integer.parseInt(quiz.getNumberOfQuestions()+1));
        }

        list.forEach((q)->{
            q.setAnswer("");
        });

        Collections.shuffle(list);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/quiz/all/{qid}")
    public ResponseEntity<?> getQuestionsOfQuizAdmin(@PathVariable("qid") Long qid){

        Quiz quiz = new Quiz();
        quiz.setqId(qid);

        Set<Question> questionOfQuiz = this.questionService.getQuestionsOfQuiz(quiz);

        return ResponseEntity.ok(questionOfQuiz);

    }

    //delete question
    @DeleteMapping("/{qid}")
    public void delete(@PathVariable("qid") Long qid){
        this.questionService.deleteQuestions(qid);
    }


    //evaluate quiz
    @PostMapping("/eval-quiz")
    public ResponseEntity<?> evalQuiz(@RequestBody List<Question> questions){

        System.out.println(questions);

        double marksGot=0;
        int correctAnswer=0;
        int attempted=0;

        for(Question q: questions){

//            single question
            Question question = this.questionService.get(q.getQuessId());
            if(question.getAnswer().equals(q.getGivenAnswer())){
                //correct
                correctAnswer+=1;

                double marksSingle= Double.parseDouble(questions.get(0).getQuiz().getMaxMarks())/questions.size();

                marksGot+=marksSingle;
            }
            if(q.getGivenAnswer()!=null){
                attempted+=1;
            }

        };

        Map<String, Object> map = Map.of("marksGot",marksGot,"correctAnswer",correctAnswer,"attempted",attempted);

        return ResponseEntity.ok(map);
    }
}
