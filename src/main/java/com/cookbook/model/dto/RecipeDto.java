package com.cookbook.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecipeDto {

    private Long id;

    private Long parentId;

    private String name;

    @JsonInclude(NON_NULL)
    private String fullName;

    private String description;

    private String created;

    private Integer version;

    @JsonInclude(NON_EMPTY)
    private List<RecipeDto> childRecipes = new ArrayList<>();

    public boolean hasParent() {
        return parentId != null;
    }
}
