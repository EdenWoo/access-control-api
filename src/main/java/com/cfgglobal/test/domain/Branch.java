package com.cfgglobal.test.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Branch extends BaseEntity {


    String name;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, optional = true)
    @JoinColumn(name = "parent_id")
    Branch parent;
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "parent")
    List<Branch> children;
/*
    @ManyToMany(cascade = CascadeType.REFRESH)

     List<User> users;*/

}
