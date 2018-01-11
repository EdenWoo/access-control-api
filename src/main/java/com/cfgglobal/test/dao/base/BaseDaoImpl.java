package com.cfgglobal.test.dao.base;

import com.cfgglobal.test.base.Condition;
import com.cfgglobal.test.base.Filter;
import com.cfgglobal.test.config.app.ApplicationProperties;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joor.Reflect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class BaseDaoImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseDao<T, ID> {

    private final Class<T> domainClass;

    private EntityManager entityManager;

    public BaseDaoImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.domainClass = domainClass;
        this.entityManager = em;

    }


    private Option<Enum> str2Enum(String str) {
        return List.ofAll(ApplicationProperties.enums)
                .map(e -> (Class<? extends Enum>) (Reflect.on(ApplicationProperties.enumPackage + "." + e).get()))
                .map(e -> Try.of(() -> Reflect.on(e).call("valueOf", str).get()))
                .filter(Try::isSuccess)
                .map(t -> (Enum) t.get()).toOption();
    }


    @Override
    public Page<T> findByFilter(List<Filter> filters, Pageable pageable) {
        log.debug(filters.toString());
        Specification<T> spec = (root, query, cb) -> {
            List<Predicate> predicates = getPredicates(filters, root, cb);
            query.where(predicates.toJavaArray(Predicate.class));
            return query.getRestriction();
        };
        return findAll(spec, pageable);
    }

    @Override
    public List<T> findByFilter(List<Filter> filters) {
        log.debug(filters.toString());
        Specification<T> spec = (root, query, cb) -> {
            List<Predicate> predicates = getPredicates(filters, root, cb);
            query.where(predicates.toJavaArray(Predicate.class));
            return query.getRestriction();
        };
        return List.ofAll(findAll(spec));
    }

    @Override
    public List<T> findByFilter(Filter filter) {
        return findByFilter(List.of(filter));
    }


    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable, String entityGraphName) {
        TypedQuery<T> query = getQuery(spec, pageable.getSort());
        EntityGraph<?> entityGraph = entityManager.getEntityGraph(entityGraphName);
        query.setHint(entityGraph.getName(), entityGraph);
        return readPage(query, domainClass, pageable, spec);
    }

    @Override
    public Page<T> findByRequestParameters(Map<String, String[]> info, Pageable pageable) {
        return findByFilter(Filter.createFilters(info), pageable);
    }

    @Override
    public List<T> findByRequestParameters(Map<String, String[]> info) {
        return findByFilter(Filter.createFilters(info));
    }


    private List<Predicate> getPredicates(List<Filter> filters, Root<T> root, CriteriaBuilder cb) {
        return filters
                .filter(it -> it != Filter.EMPTY)
                .map(filter -> {
                    List<Predicate> predicates = filter.conditions()
                            .filter(condition -> {
                                String val = condition.getValue().toString();
                                if (condition.getOperator().equals(Filter.OPERATOR_BETWEEN)) {
                                    Object o1 = Array.get(condition.getValue(), 0);
                                    Object o2 = Array.get(condition.getValue(), 1);
                                    return StringUtils.isNoneBlank(o1.toString()) && StringUtils.isNoneBlank(o2.toString());
                                } else if (StringUtils.isBlank(val)) {
                                    return false;
                                }
                                return true;
                            })
                            .map(condition -> {
                                Path searchPath;
                                String fieldName = condition.getFieldName();
                                List<String> fields = List.of(fieldName.split("\\."));
                                if (fields.size() > 1) {
                                    Join join = root.join(fields.get(0));
                                    for (int i = 1; i < fields.size() - 1; i++) {
                                        join = join.join(fields.get(i));
                                    }
                                    searchPath = join.get(fields.get(fields.size() - 1));
                                } else {
                                    searchPath = root.get(fieldName);
                                }
                                Predicate predicate = getPredicate(cb, condition, searchPath);
                                if (predicate == null) {
                                    return null;
                                }
                                if (condition.getRelation().equalsIgnoreCase(Filter.RELATION_AND)) {
                                    return cb.and(predicate);
                                } else {
                                    return cb.or(predicate);
                                }
                            });
                    return predicates.isEmpty() ? null : predicates
                            .reduce((l, r) -> {
                                if (r.getOperator().equals(Predicate.BooleanOperator.AND)) {
                                    return cb.and(l, r);
                                } else {
                                    return cb.or(l, r);
                                }
                            });
                }).filter(Objects::nonNull);
    }

    private Predicate getPredicate(CriteriaBuilder cb, Condition condition, Path searchPath) {
        Predicate predicate = null;
        Object value = condition.getValue();
        String s = value.toString();
        if (isEnum(s) && !condition.getOperator().toUpperCase().equals("NULL") && !condition.getOperator().toUpperCase().equals("NOTNULL")) {
            condition.setOperator(Filter.OPERATOR_EQ);
        }
        switch (condition.getOperator().toUpperCase()) {
            case Filter.OPERATOR_NULL:
                predicate = cb.isNull(searchPath);
                break;
            case Filter.OPERATOR_LIKE:
                predicate = cb.like(searchPath, "%" + s + "%");
                break;
            case Filter.OPERATOR_LESS_EQ:
                predicate = cb.lessThan(searchPath, s);
                break;
            case Filter.OPERATOR_BETWEEN:
                Object o1 = Array.get(value, 0);
                Object o2 = Array.get(value, 1);

                String from = o1.toString();
                String to = o2.toString();

                if (condition.getFieldName().equals("verificationDate") || condition.getFieldName().equals("incorporatedDate")) { //
                    Path<String> t = searchPath;
                    predicate = cb.between(t, from, to);

                } else if (NumberUtils.isCreatable(from)) {
                    Path<Integer> t = searchPath;
                    predicate = cb.between(t, NumberUtils.toInt(from), NumberUtils.toInt(to));
                } else {
                    if ("ZonedDateTime".equals(ApplicationProperties.dateType)) {
                        Path<ZonedDateTime> t = searchPath;
                        if (from.length() == "1970-01-01".length()) {
                            from += " 00:00:00";
                        }
                        if (to.length() == "1970-01-01".length()) {
                            to += " 23:59:59";
                        }
                        ZoneId displayTimeZone = ZoneId.of(ApplicationProperties.displayTimeZone);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        ZonedDateTime fromDateTime = LocalDateTime.parse(from, formatter).atZone(ZoneId.systemDefault());
                        ZonedDateTime toDateTime = LocalDateTime.parse(to, formatter).atZone(ZoneId.systemDefault());
                        if (ApplicationProperties.dbTimeZone.equals("UTC")) {
                            fromDateTime = ZonedDateTime.of(LocalDateTime.parse(from, formatter), displayTimeZone).withZoneSameInstant(ZoneOffset.UTC);
                            toDateTime = ZonedDateTime.of(LocalDateTime.parse(to, formatter), displayTimeZone).withZoneSameInstant(ZoneOffset.UTC);
                        }
                        predicate = cb.between(t, fromDateTime, toDateTime);

                    } else if ("LocalDateTime".equals(ApplicationProperties.dateType)) {
                        Path<LocalDateTime> t = searchPath;
                        from += " 00:00:00";
                        to += " 23:59:59";
                        ZoneId displayTimeZone = ZoneId.of(ApplicationProperties.displayTimeZone);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        ZonedDateTime fromDateTime = ZonedDateTime.of(LocalDateTime.parse(from, formatter), displayTimeZone).withZoneSameInstant(ZoneOffset.UTC);
                        ZonedDateTime toDateTime = ZonedDateTime.of(LocalDateTime.parse(to, formatter), displayTimeZone).withZoneSameInstant(ZoneOffset.UTC);
                        predicate = cb.between(t, fromDateTime.toLocalDateTime(), toDateTime.toLocalDateTime());
                    }

                }
                break;
            case Filter.OPERATOR_IN:
                Object[] list;
                if (value instanceof String) {
                    list = new Object[]{value};
                } else {
                    list = (Object[]) value;
                }

                if (isEnum(list[0].toString())) {
                    predicate = searchPath.in(List.of(list).map(en -> str2Enum(en.toString()).get()).toJavaList());
                } else {
                    predicate = searchPath.in(Arrays.asList(list));
                }
                break;
            default:
                if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
                    predicate = cb.equal(searchPath, Boolean.valueOf(s));
                } else if (isEnum(s)) {
                    predicate = cb.equal(searchPath, str2Enum(s).get());
                } else if (NumberUtils.isCreatable(s)) {
                    predicate = cb.equal(searchPath, NumberUtils.toLong(s));
                } else if (s.contains("-")) {
                    predicate = cb.equal(searchPath, LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } else {
                    predicate = cb.equal(searchPath, s);
                }

        }
        return predicate;
    }

    private boolean isEnum(String cs) {
        return str2Enum(cs).isDefined();
    }

    @Override
    public boolean support(String modelType) {
        return domainClass.getName().equals(modelType);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.of(findOne(id));
    }

}