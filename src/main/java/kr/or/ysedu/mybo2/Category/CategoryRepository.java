package kr.or.ysedu.mybo2.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Category findByCategory(String category);
}
