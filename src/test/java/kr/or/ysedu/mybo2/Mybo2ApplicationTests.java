package kr.or.ysedu.mybo2;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.or.ysedu.mybo2.question.Question;
import kr.or.ysedu.mybo2.question.QuestionRepository;

@SpringBootTest
class Mybo2ApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;
	
	@Test
	void contextLoads() {
		Question q1 = new Question();
		q1.setSubject("첫번째 게시물");
		q1.setContent("내용없음");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);		
	}

}
