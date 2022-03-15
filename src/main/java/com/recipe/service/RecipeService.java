package com.recipe.service;

import com.recipe.entity.Recipe;
import com.recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepository;

    public Map<String, Long> save(Recipe recipe){
        recipe.setDate(LocalDateTime.now());
        recipeRepository.save(recipe);
        return Collections.singletonMap("id", recipe.getId());
    }

    public Object findRecipeById(Long id) {
        return recipeRepository.findRecipeById(id);
    }

    public Object findAllByCategoryIgnoreCaseOrderByDateDesc(String category) {
        return recipeRepository.findAllByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public Object findAllByNameIgnoreCaseContainingOrderByDateDesc(String name) {
        return recipeRepository.findAllByNameIgnoreCaseContainingOrderByDateDesc(name);
    }

    //保证事务统一性
    @Modifying
    @Transactional
    public void deleteRecipeById(Long id) {
        recipeRepository.deleteRecipeById(id);
    }






    //检查字符串数据的合法性(非空、非空白字符)
    public boolean isLegalString(String str) {
        return str.length() != 0 && str.trim().length() != 0;
    }

    //检查POST的Recipe的合法性
    public boolean isLegalRecipe(Recipe recipe) {
        //保证数据的合法性
        return recipe.getName() != null && recipe.getDescription() != null
                && recipe.getIngredients() != null && recipe.getDirections() != null
                && !recipe.getIngredients().isEmpty() && !recipe.getDirections().isEmpty()
                && this.isLegalString(recipe.getName()) && this.isLegalString(recipe.getDescription())
                && recipe.getCategory() != null && this.isLegalString(recipe.getCategory());
    }


}

//    public Object findRecipeById(Long id) {
//        //去除返回的Json中的Id再返回
//        Recipe tempRecipe = recipeRepository.findRecipeById(id);
//        //如果没有查到 那么返回null
//        if(tempRecipe == null){return null;}
//        //将去掉id后的 组成Map格式返回
//        LinkedHashMap<String, Object> recipe = new LinkedHashMap<>();
//        recipe.put("name", tempRecipe.getName());
//        recipe.put("category", tempRecipe.getCategory());
//        recipe.put("date", tempRecipe.getDate());
//        recipe.put("description", tempRecipe.getDescription());
//        recipe.put("ingredients", tempRecipe.getIngredients());
//        recipe.put("directions", tempRecipe.getDirections());
//        return recipe;
//    }   }