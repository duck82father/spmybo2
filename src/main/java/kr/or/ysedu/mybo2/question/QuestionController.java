package kr.or.ysedu.mybo2.question;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import kr.or.ysedu.mybo2.Category.Category;
import kr.or.ysedu.mybo2.Category.CategoryService;
import kr.or.ysedu.mybo2.answer.Answer;
import kr.or.ysedu.mybo2.answer.AnswerForm;
import kr.or.ysedu.mybo2.answer.AnswerService;
import kr.or.ysedu.mybo2.user.SiteUser;
import kr.or.ysedu.mybo2.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {
	
	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserService userService;
	private final CategoryService categoryService;
	
	@GetMapping("/list/{categoryName}")
	public String listCategory(Model model,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw,
			@PathVariable("categoryName") String categoryName) {
		Category category = this.categoryService.getCategory(categoryName);
//		Integer categoryId = category.getId();
		Page<Question> paging = this.questionService.getList(page, kw, category);
		log.info(">> "+paging.toString());
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		model.addAttribute("category", category);
		return "question_list";
	}
	
	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id,
			AnswerForm answerForm,
			@RequestParam(value="page", defaultValue = "1") int page,
			@RequestParam(value="setSortType", defaultValue = "createDate") String setSortType,
			Principal principal) {
		Question question = this.questionService.getQuestion(id);
		if(principal!=null){
			SiteUser viewer = this.userService.getUser(principal.getName());
			this.questionService.addViewCount(question, viewer);
		}
		model.addAttribute(question);
		model.addAttribute("setSortType", setSortType);
		Category category = this.categoryService.getCategory(question.getCategory().getCategory());
		Page<Answer> paging = this.answerService.getListBySortRule(question, page, setSortType);
		model.addAttribute("paging", paging);
		model.addAttribute("category", category);
		return "question_detail";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm) {
		return "question_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String questionCreate(@Valid QuestionForm questionForm,
			BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}
		String categoryName = questionForm.getCategory();
		Category category = this.categoryService.getCategory(categoryName);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser, category);
		return String.format("redirect:/question/list/%s", categoryName);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm,
			@PathVariable("id") Integer id, Principal principal) { 
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
		}
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		return "question_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String questionModify(@Valid QuestionForm questionForm,
			BindingResult bindingResult, Principal principal,
			@PathVariable("id") Integer id) {
		if (bindingResult.hasErrors()) {
			return "quetsion_form";
		}
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
		}
		this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%s", id);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
		}
		String categoryName = question.getCategory().getCategory();
		this.questionService.delete(question);
		return String.format("redirect:/question/list/%s", categoryName);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String questionVote(Principal principal, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.vote(question, siteUser);
		return String.format("redirect:/question/detail/%s", id);
	}
	
	
}
