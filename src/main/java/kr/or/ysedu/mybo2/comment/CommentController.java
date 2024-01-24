package kr.or.ysedu.mybo2.comment;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import kr.or.ysedu.mybo2.answer.Answer;
import kr.or.ysedu.mybo2.answer.AnswerService;
import kr.or.ysedu.mybo2.question.Question;
import kr.or.ysedu.mybo2.question.QuestionService;
import kr.or.ysedu.mybo2.user.SiteUser;
import kr.or.ysedu.mybo2.user.UserService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {

	private final CommentService commentService;
	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserService userService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/q/create/{questionId}")
	public String commentQuestionCreate(@PathVariable("questionId") Integer questionId,
			CommentForm commentForm,
			BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "question_detail";
		}
		Question question = this.questionService.getQuestion(questionId);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.commentService.create(question, commentForm.getComment(), siteUser);
		return String.format("redirect:/question/detail/%s", questionId);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/a/create/{answerId}")
	public String commentAnswerCreate(@PathVariable("answerId") Integer answerId,
			CommentForm commentForm,
			BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "question_detail";
		}
		Answer answer = this.answerService.getAnswer(answerId);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.commentService.create(answer, commentForm.getComment(), siteUser);
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/delete/{id}")
	public String commentQuestionDelete(@PathVariable("id") Integer id,
			Principal principal) {
		Comment comment = this.commentService.getComment(id);
		boolean check = this.commentService.checkQuestionOrAnswer(comment);
		Integer questionId;
		if (check){
			questionId = comment.getQuestion().getId();
		} else {
			questionId = comment.getAnswer().getQuestion().getId();
		};
		if (!comment.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
		}
		this.commentService.delete(comment);
		return String.format("redirect:/question/detail/%s", questionId);
	}
	
}
