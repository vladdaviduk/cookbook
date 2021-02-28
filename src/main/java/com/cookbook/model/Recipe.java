package com.cookbook.model;

import com.cookbook.model.pk.RecipePk;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "recipe")
public class Recipe {

    @EmbeddedId
    private RecipePk pk;

    private Long parentId;

    private String name;

    private String description;

    private Date created;

    @Transient
    @EqualsAndHashCode.Exclude
    private String fullName;

    @Transient
    @EqualsAndHashCode.Exclude
    private List<Recipe> childRecipes = new ArrayList<>();

    public boolean hasParent() {
        return parentId != null;
    }

    public boolean hasChildren() {
        return !childRecipes.isEmpty();
    }
}
