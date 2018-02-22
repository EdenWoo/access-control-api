package com.cfgglobal.test.config.json

import com.querydsl.core.types.Path
import com.querydsl.core.types.PathMetadata
import com.querydsl.core.types.PathType
import com.querydsl.core.types.Visitor
import java.lang.reflect.AnnotatedElement


class MockPath private constructor(internal var element: String) : Path<Any> {
    override fun <R : Any?, C : Any?> accept(v: Visitor<R, C>?, context: C?): R? {
        return null
    }

    override fun getMetadata(): PathMetadata {
        return PathMetadata(null, element, PathType.VARIABLE)
    }

    override fun getRoot(): Path<*>? {
        return null
    }

    override fun getAnnotatedElement(): AnnotatedElement? {
        return null
    }


    override fun getType(): Class<*>? {
        return null
    }

    companion object {

        fun create(name: String): MockPath {
            return MockPath(name)
        }
    }

}
