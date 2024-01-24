package kr.or.ysedu.mybo2.comment;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.or.ysedu.mybo2.answer.Answer;
import kr.or.ysedu.mybo2.answer.AnswerRepository;
import kr.or.ysedu.mybo2.question.Question;
import kr.or.ysedu.mybo2.question.QuestionRepository;
import kr.or.ysedu.mybo2.user.SiteUser;
import kr.or.ysedu.mybo2.user.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	
	public void create(Question question, String comment, SiteUser siteUser) {
		Comment newComment = new Comment();
		newComment.setComment(comment);
		newComment.setCreateDate(LocalDateTime.now());
		newComment.setQuestion(question);
		newComment.setAuthor(siteUser);
		this.commentRepository.save(newComment);
	}
	
	public void create(Answer answer, String comment, SiteUser siteUser) {
		Comment newComment = new Comment();
		newComment.setComment(comment);
		newComment.setAnswer(answer);
		newComment.setAuthor(siteUser);
		newComment.setCreateDate(LocalDateTime.now());
		this.commentRepository.save(newComment);
	}

	public Comment getComment(Integer id) {
		return this.commentRepository.getById(id);
	}
	
	public void delete(Comment comment) {
		this.commentRepository.delete(comment);
	}
	
	public boolean checkQuestionOrAnswer(Comment comment) {
		Optional<Comment> targetComment = this.commentRepository.findById(comment.getId());
		if(targetComment.get().getQuestion()==null) {
			return false;
		}
		return true;
 }
	
}
