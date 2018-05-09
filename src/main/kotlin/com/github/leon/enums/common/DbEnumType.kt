package com.github.leon.enums.common

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.usertype.DynamicParameterizedType
import org.hibernate.usertype.UserType
import java.io.Serializable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types
import java.util.*


class DbEnumType : UserType, DynamicParameterizedType {

    private var enumClass: Class<*>? = null

    override fun setParameterValues(parameters: Properties) {
        val reader = parameters[DynamicParameterizedType.PARAMETER_TYPE] as DynamicParameterizedType.ParameterType

        enumClass = reader.returnedClass.asSubclass<Enum<*>>(Enum::class.java)

    }

    override fun sqlTypes(): IntArray {
        return SQL_TYPES
    }

    override fun returnedClass(): Class<*>? {
        return enumClass
    }

    //是否相等，不相等会触发JPA update操作
    @Throws(HibernateException::class)
    override fun equals(x: Any?, y: Any?): Boolean {
        if (x == null && y == null) {
            return true
        }
        return if (x == null && y != null || x != null && y == null) {
            false
        } else x == y
    }

    @Throws(HibernateException::class)
    override fun hashCode(x: Any?): Int {
        return x?.hashCode() ?: 0
    }


    /**
     * 返回枚举
     */
    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeGet(rs: ResultSet, names: Array<String>, session: SharedSessionContractImplementor, owner: Any): Any? {
        val value = rs.getString(names[0]) ?: return null
        for (`object` in enumClass!!.enumConstants) {
            if (Integer.parseInt(value) == (`object` as IBaseDbEnum).value) {
                return `object`
            }
        }
        throw RuntimeException(String.format("Unknown name value [%s] for enum class [%s]", value, enumClass!!.name))
    }

    /**
     * 保存枚举值
     */
    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeSet(st: PreparedStatement, value: Any?, index: Int, session: SharedSessionContractImplementor) {
        when (value) {
            null -> st.setNull(index, SQL_TYPES[0])
            is Int -> st.setInt(index, (value as Int?)!!)
            else -> st.setInt(index, (value as IBaseDbEnum).value!!)
        }
    }

    @Throws(HibernateException::class)
    override fun deepCopy(value: Any?): Any? {
        return value
    }

    override fun isMutable(): Boolean {
        return false
    }

    @Throws(HibernateException::class)
    override fun disassemble(value: Any): Serializable {
        return value as Serializable
    }

    @Throws(HibernateException::class)
    override fun assemble(cached: Serializable, owner: Any): Any {
        return cached
    }

    @Throws(HibernateException::class)
    override fun replace(original: Any, target: Any, owner: Any): Any {
        return original
    }

    companion object {
        private val SQL_TYPES = intArrayOf(Types.INTEGER)
    }
}