package com.recipe.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.recipe.entity.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long>{

    Recipe findRecipeById(Long id);

    void deleteRecipeById(Long id);

    // 类别是精准搜索， 但大小写不敏感
    List<Recipe> findAllByCategoryIgnoreCaseOrderByDateDesc(String category);
//    List<Recipe> findAllByCategoryIgnoreCaseContaining(String category);

    // 名称是模糊搜索，大小写不敏感 Containing
    List<Recipe> findAllByNameIgnoreCaseContainingOrderByDateDesc(String name);

}
