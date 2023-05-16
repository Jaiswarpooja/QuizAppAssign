package com.quiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quiz.model.Quiz;

import jakarta.transaction.Transactional;

@Repository
public interface QuizRepo extends JpaRepository<Quiz ,Long> {
	
	@Query(value = "SELECT * FROM quiz q WHERE q.status = 'active'", nativeQuery = true)
	List<Quiz> findAllActiveQuiz();

//	UPDATE quiz q SET q.status = 'active' WHERE q.start_date_time <= CURRENT_TIMESTAMP AND q.status = 'inactive';
//	UPDATE quiz q SET q.status = 'finished' WHERE q.end_date_time <= CURRENT_TIMESTAMP AND q.status = 'active';

	@Transactional
    @Modifying
    @Query(value = "UPDATE quiz q\r\n"
    		+ "SET q.status = CASE \r\n"
    		+ "    WHEN q.start_date_time <= CURRENT_TIMESTAMP AND q.status = 'inactive' THEN 'active'\r\n"
    		+ "    WHEN q.end_date_time <= CURRENT_TIMESTAMP AND q.status = 'active' THEN 'finished'\r\n"
    		+ "    ELSE q.status\r\n"
    		+ "    END\r\n"
    		+ "WHERE (q.start_date_time <= CURRENT_TIMESTAMP AND q.status = 'inactive')\r\n"
    		+ "   OR (q.end_date_time <= CURRENT_TIMESTAMP AND q.status = 'active');", nativeQuery = true)
    void updateStatus();
}
