package com.cookbook.model.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class RecipePk implements Serializable {

    @Column
    private long id;

    @Column
    private int version;
}
