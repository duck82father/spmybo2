package kr.or.ysedu.mybo2.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.ysedu.mybo2.answer.Answer;
import kr.or.ysedu.mybo2.question.Question;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	Comment findByComment(String comment);
	Comment findByCommentAndQuestion(String comment, Question question);
	Comment findByCommentAndAnswer(String comment, Answer question);
}
