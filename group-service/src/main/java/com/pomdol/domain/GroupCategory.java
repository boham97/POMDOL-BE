package com.pomdol.domain;

import com.pomdol.dto.CategoryResDto;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
@Getter
@Entity
public class GroupCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_category_id")
    private Integer id;
    @Column(length = 30, name = "group_category_name")
    private String name;
    public CategoryResDto toDto(){
        return new CategoryResDto(
                this.id,
                this.name
        );
    }
}
