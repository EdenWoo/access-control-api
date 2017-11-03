package com.cfgglobal.test.dao;

import com.cfgglobal.test.dao.base.BaseDao;
import com.cfgglobal.test.domain.Rule;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleDao extends BaseDao<Rule, Long> {

    Option<Rule> findByName(String name);

    List<Rule> findByType(String basic);
}