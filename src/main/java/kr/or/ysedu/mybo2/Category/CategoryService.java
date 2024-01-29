package kr.or.ysedu.mybo2.Category;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {
	
	private final CategoryRepository categoryRepository;
	
	public Category getCategory(String category) {
		return this.categoryRepository.findByCategory(category);
	}
	
}
