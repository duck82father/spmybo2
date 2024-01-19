package kr.or.ysedu.mybo2.question;

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
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kr.or.ysedu.mybo2.DataNotFoundException;
import kr.or.ysedu.mybo2.answer.Answer;
import kr.or.ysedu.mybo2.user.SiteUser;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

	private final QuestionRepository questionRepository;
	
	private Specification<Question> search(String kw){
		return new Specification<>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);		// 중복 제거
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
				return cb.or(cb.like(q.get("subject"), "%" + kw + "%"),		// 제목
						cb.like(q.get("content"), "%" + kw + "%"),			// 내용
						cb.like(u1.get("username"), "%" + kw + "%"),		// 질문 작성자
						cb.like(a.get("content"), "%" + kw + "%"),			// 답변 내용
						cb.like(u2.get("username"), "%" + kw + "%")			// 답변 작성자
						);
			}
		};
	}
	
	
	public List<Question> getList(){
		return this.questionRepository.findAll();
	}
	
	public Question getQuestion(Integer id) {
		Optional<Question> question = this.questionRepository.findById(id);
		if(question.isPresent()) {
			return question.get();
		} else {
			throw new DataNotFoundException("question not found");
		}
	}
	
	public Page<Question> getList(int page, String kw){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));
		Specification<Question> spec = search(kw);
		return this.questionRepository.findAll(spec, pageable);
	}
	
	public void create(String subject, String content, SiteUser user) {
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreateDate(LocalDateTime.now());
		question.setAuthor(user);
		this.questionRepository.save(question);
	}
	
	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		this.questionRepository.save(question);
	}
	
	public void delete(Question question) {
		this.questionRepository.delete(question);
	}
	
	public void vote(Question question, SiteUser siteUser) {
		question.getVoter().add(siteUser);
		this.questionRepository.save(question);
	}
	
	public void addViewCount(Question question) {
		Integer questionViewCount = question.getViewCount();
		if (questionViewCount==null) {
			question.setViewCount(1);
		} else {
			question.setViewCount(questionViewCount+1);
		}
		this.questionRepository.save(question);
	}
	
}
