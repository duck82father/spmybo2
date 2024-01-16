package kr.or.ysedu.mybo2.answer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import kr.or.ysedu.mybo2.question.Question;
import kr.or.ysedu.mybo2.user.SiteUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	
	private final AnswerRepository answerRepository;
	
	public void create(Question question, String content, SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setQuestion(question);
		answer.setCreateDate(LocalDateTime.now());
		answer.setAuthor(author);
		this.answerRepository.save(answer);
	}
	
}
