package kr.or.ysedu.mybo2;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.or.ysedu.mybo2.Category.Category;
import kr.or.ysedu.mybo2.Category.CategoryRepository;
import kr.or.ysedu.mybo2.Category.CategoryService;
import kr.or.ysedu.mybo2.question.Question;
import kr.or.ysedu.mybo2.question.QuestionRepository;
import kr.or.ysedu.mybo2.question.QuestionService;
import lombok.RequiredArgsConstructor;

@SpringBootTest
class Mybo2ApplicationTests {

	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Test
	void testJpa() {
		for (int i=0; i<100; i++) {
			String subject = String.format("%03d 번째 게시물 입니다.", i+1);
			String content = "내용 없음";
			Category category = this.categoryService.getCategory("qna");
			this.questionService.create(subject, content, null, category);
		}
	}
}
