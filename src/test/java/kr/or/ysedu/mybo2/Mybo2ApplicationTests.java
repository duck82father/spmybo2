package kr.or.ysedu.mybo2;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.or.ysedu.mybo2.question.Question;
import kr.or.ysedu.mybo2.question.QuestionRepository;
import kr.or.ysedu.mybo2.question.QuestionService;

@SpringBootTest
class Mybo2ApplicationTests {

	@Autowired
	private QuestionService questionService;
	
	@Test
	void testJpa() {
		for (int i=0; i<300; i++) {
			String subject = String.format("%03d 번째 게시물 입니다.", i);
			String content = "내용 없음";
			this.questionService.create(subject, content);
		}
	}
}
