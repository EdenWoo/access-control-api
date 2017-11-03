package com.cfgglobal.test.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Accessors
@DynamicUpdate
@DynamicInsert
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermission extends BaseEntity implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_id")
    Permission permission;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission_rule",
            joinColumns = @JoinColumn(name = "role_permission_id"),
            inverseJoinColumns = @JoinColumn(name = "rule_id"))

    List<Rule> rules;

}
