package com.cfgglobal.test.domain;


import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;


@Accessors(chain = true)
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
@DynamicInsert
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseEntity {


    String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<User> users;


    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "role_id")
    List<RolePermission> rolePermissions;

}
