package kr.or.ysedu.mybo2.answer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kr.or.ysedu.mybo2.DataNotFoundException;
import kr.or.ysedu.mybo2.question.Question;
import kr.or.ysedu.mybo2.user.SiteUser;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	
	private final AnswerRepository answerRepository;
	
	public Answer create(Question question, String content, SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setQuestion(question);
		answer.setCreateDate(LocalDateTime.now());
		answer.setAuthor(author);
		this.answerRepository.save(answer);
		return answer;
	}
	
	public Answer getAnswer(Integer id) {
		Optional<Answer> answer = this.answerRepository.findById(id);
		if (answer.isPresent()) {
			return answer.get();
		} else {
			throw new DataNotFoundException("answer not found");
		}
	}
	
	public void Modify(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());
		this.answerRepository.save(answer);
	}
	
	public void delete(Answer answer) {
		this.answerRepository.delete(answer);
	}
	
	public void vote(Answer answer, SiteUser siteUser) {
		answer.getVoter().add(siteUser);
		this.answerRepository.save(answer);
	}
	
	public void voteCount(Question question) {
		List<Answer> answer = this.answerRepository.findAllByQuestion(question);
		for (int i=0; i<answer.size();i++) {
			answer.get(i).setVoteCount(answer.get(i).voter.size());
		}		
		this.answerRepository.saveAll(answer);
	}
		
	public Page<Answer> getListBySortRule(Question question, int page, String setSortType) {
		List<Sort.Order> sorts = new ArrayList<>();
		this.voteCount(question);
		sorts.add(Sort.Order.desc(setSortType));
		Pageable pageable = PageRequest.of(page-1, 4, Sort.by(sorts));
		return this.answerRepository.findAllByQuestion(question, pageable);
	}	
		
}
