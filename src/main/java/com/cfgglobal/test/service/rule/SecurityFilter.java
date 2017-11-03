package com.cfgglobal.test.service.rule;


import com.cfgglobal.test.base.Filter;
import com.cfgglobal.test.domain.User;
import io.vavr.collection.List;


public interface SecurityFilter {

    User currentUser();

    List<Filter> query(String method, String requestURI);

}
