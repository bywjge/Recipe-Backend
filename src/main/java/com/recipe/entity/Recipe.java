package com.recipe.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recipe {

    @JsonIgnore //返回前端时的Json里去掉不想要的属性
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
//    @NonNull
    private String name;

    @Column
    private String category;

    @Column
    private LocalDateTime date;

    @Column
//    @NonNull
    private String description;

    @Column
    @ElementCollection
    @CollectionTable
    private List<String> ingredients;

    @Column
    @ElementCollection
    @CollectionTable
    private List<String> directions;

}
