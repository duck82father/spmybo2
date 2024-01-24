package kr.or.ysedu.mybo2.answer;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.ysedu.mybo2.question.Question;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	
	List<Answer> findAllByQuestion(Question question);
	Page<Answer> findAllByQuestion(Question question, Pageable pageable);
	
	
}
