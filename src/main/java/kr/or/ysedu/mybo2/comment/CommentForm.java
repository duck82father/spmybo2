package kr.or.ysedu.mybo2.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentForm {
	
	@NotEmpty(message = "코멘트 작성 시, 내용은 필수항목입니다.")
	@Size(max = 30)
	private String comment;
	
}
