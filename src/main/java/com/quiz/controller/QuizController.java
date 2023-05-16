package com.quiz.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.model.Quiz;
import com.quiz.repository.QuizRepo;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class QuizController {

	@Autowired
	QuizRepo quizRepo;
	
	@GetMapping("/quizzes/all")
	public ResponseEntity<?> getQuiz(){
		List<Quiz> quizs = quizRepo.findAll();
		if(quizs.isEmpty()) {
			return new ResponseEntity<>("No Quiz Present",HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(quizs,HttpStatus.OK);
		}
	}
	
	@PostMapping("/quizzes")
	public ResponseEntity<?> postQuiz(@RequestBody Quiz quiz){
		Quiz quizs = new Quiz(quiz.getQuestion(), quiz.getOptions(), quiz.getRightAnswer(), quiz.getStartDateTime(), quiz.getEndDateTime(), "inactive");
		Quiz resquiz = quizRepo.save(quizs);
		return new ResponseEntity<>("Quiz Created with Quiz ID : "+ resquiz.getId(),HttpStatus.OK);
	}
		
	@GetMapping("/quizzes/active")
	public ResponseEntity<?> getActiveQuiz(){
		List<Quiz> listQuiz = quizRepo.findAllActiveQuiz();
		if(listQuiz.isEmpty()) {
			return new ResponseEntity<>("No Active Quiz",HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(listQuiz,HttpStatus.OK);
		}
		
	}
	
	@Scheduled(fixedRate = 60000) // Run every minute
    public void performScheduledTask() {
		quizRepo.updateStatus();
		System.out.println("Updating Status Every minute");
    }
	
	@GetMapping("/quizzes/{id}/result")
	public ResponseEntity<?> getquizResult(@PathVariable("id") Long id) {
		Optional<Quiz> quiz = quizRepo.findById(id);
		if(quiz.isPresent()) {
			return new ResponseEntity<>(quiz.get().getRightAnswer(),HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
}