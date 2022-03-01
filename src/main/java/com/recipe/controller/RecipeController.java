package com.recipe.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.recipe.entity.Recipe;
import com.recipe.service.RecipeService;

import java.util.Map;

@RestController
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @PostMapping("/api/recipe/new")
    //Map<String, Long>
    public ResponseEntity<Object> newRecipe(@RequestBody @NotNull Recipe recipe){

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
        //找不到返回404
        if (recipeService.findRecipeById(id) == null) {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        //不合法返回Bad Request 400
        if (!recipeService.isLegalRecipe(recipe)) {return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}
        //合法返回204
        recipe.setId(id);
        return new ResponseEntity<>(recipeService.save(recipe), HttpStatus.NO_CONTENT);

    }


    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Object> delRecipe(@PathVariable Long id) {

        if(recipeService.findRecipeById(id) != null){
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
