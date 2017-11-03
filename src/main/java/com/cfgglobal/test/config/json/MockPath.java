package com.cfgglobal.test.config.json;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathType;
import com.querydsl.core.types.Visitor;

import javax.annotation.Nullable;
import java.lang.reflect.AnnotatedElement;


public class MockPath implements Path {
    private MockPath(String element) {
        this.element = element;
    }

    public static MockPath create(String name) {
        return new MockPath(name);
    }

    String element;

    @Override
    public PathMetadata getMetadata() {
        return new PathMetadata(null, element, PathType.VARIABLE);
    }

    @Override
    public Path<?> getRoot() {
        return null;
    }

    @Override
    public AnnotatedElement getAnnotatedElement() {
        return null;
    }

    @Nullable
    @Override
    public Object accept(Visitor v, @Nullable Object context) {
        return null;
    }

    @Override
    public Class getType() {
        return null;
    }

}
