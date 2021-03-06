package com.recipe.controller;

import com.recipe.UserDetailsImpl;
import com.recipe.entity.Recipe;
import com.recipe.service.RecipeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @PostMapping("/api/recipe/new")
    //Map<String, Long>
    public ResponseEntity<Object> newRecipe(@RequestBody @NotNull Recipe recipe){
        // 获取登录认证
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl details = (UserDetailsImpl) auth.getPrincipal();
        //获取当前用户Id并绑定到菜谱里
        recipe.setUserId(details.getUserId());
        System.out.println(recipe);
        //保证数据的合法性
        if (recipeService.isLegalRecipe(recipe)) {
            return new ResponseEntity<>(recipeService.save(recipe), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<Object> getRecipe(@PathVariable Long id) {
        if(recipeService.findRecipeById(id) != null){
            return new ResponseEntity<>(recipeService.findRecipeById(id), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<Object> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        // 找不到返回404
        if (recipeService.findRecipeById(id) == null) {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        //不合法返回Bad Request 400
        if (!recipeService.isLegalRecipe(recipe)) {return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}

        // 认证是否为原作者
        // 获取登录认证
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl details = (UserDetailsImpl) auth.getPrincipal();
        // 原来的菜谱
        Recipe originRecipe = (Recipe) recipeService.findRecipeById(id);
        // 若当前登录id不等于原菜谱作者id，则返回forbidden
        if (!Objects.equals(originRecipe.getUserId(), details.getUserId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        //合法返回204
        //设置回id
        recipe.setId(id);
        //设置回userId
        recipe.setUserId(details.getUserId());
        return new ResponseEntity<>(recipeService.save(recipe), HttpStatus.NO_CONTENT);

    }


    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Object> delRecipe(@PathVariable Long id) {

        if(recipeService.findRecipeById(id) != null){

            // 认证是否为原作者
            // 获取登录认证
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl details = (UserDetailsImpl) auth.getPrincipal();
            // 原来的菜谱
            Recipe originRecipe = (Recipe) recipeService.findRecipeById(id);
            // 若当前登录id不等于原菜谱作者id，则返回forbidden
            if (!Objects.equals(originRecipe.getUserId(), details.getUserId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            recipeService.deleteRecipeById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

//        System.out.println("!!!!!!");
//        System.out.println(recipeService.findRecipeById(id) == null);
//        if (recipeService.findRecipeById(id) == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        else {
//            recipeService.deleteRecipeById(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
    }


    @GetMapping("/api/recipe/search")
    public ResponseEntity<Object> searchRecipeByCategoryOrName(@RequestParam(required = false) String category,
                                                               @RequestParam(required = false) String name) {
        if (category != null && name == null) {
            return new ResponseEntity<>(recipeService.findAllByCategoryIgnoreCaseOrderByDateDesc(category),HttpStatus.OK);
        } else if (category == null && name != null){
            return new ResponseEntity<>(recipeService.findAllByNameIgnoreCaseContainingOrderByDateDesc(name),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
