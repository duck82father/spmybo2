package kr.or.ysedu.mybo2.comment;

import java.time.LocalDateTime;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import kr.or.ysedu.mybo2.answer.Answer;
import kr.or.ysedu.mybo2.question.Question;
import kr.or.ysedu.mybo2.user.SiteUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 50)
	private String comment;
	
	private LocalDateTime createDate;
	
	@ManyToOne
	private SiteUser author;
	
	@ManyToOne
	private Question question; 
	
	@ManyToOne
	private Answer answer;
	
}
