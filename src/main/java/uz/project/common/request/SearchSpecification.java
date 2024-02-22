package uz.project.common.request;

import com.google.common.base.CaseFormat;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import uz.project.common.DateUtil;
import uz.project.common.exception.SearchException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static uz.project.common.ReflectionUtils.*;

public class SearchSpecification<T> implements Specification<T> {
    private final List<SearchCriteria> params;

    public SearchSpecification(List<SearchCriteria> params) {
        this.params = params;
    }

    @Override
    public Predicate toPredicate(@Nullable Root<T> root, @Nullable CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();
        Assert.notNull(root, "Root element should be not null");
        if (!CollectionUtils.isEmpty(params)) {
            for (SearchCriteria param : params) {
                if (param.getKey().split("_").length > 1) {
                    param.setKey(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, param.getKey()));
                }
                if (checkExistField(root, param.getKey())) {
                    var predicateParam = getPredicate(param, criteriaBuilder, root, query);
                    predicate = criteriaBuilder.and(predicate, predicateParam);
                }
            }
        }
        return predicate;
    }

    private boolean checkExistField(Root<T> root, String key) {
        try {
            var rootJavaType = root.getJavaType();
            if (!isEntity(rootJavaType)) {
                throw new SearchException("Chosen class not annotated @Entity " + rootJavaType.getName());
            }
            var keys = key.split("\\.");
            if (!existDeclaredField(rootJavaType, keys[0])) {
                throw new SearchException("Entity" + rootJavaType.getName() + " not exist field" + keys[0]);
            }
            return true;
        } catch (Exception e) {
            throw new SearchException("Could not found field " + key);
        }
    }

    private Predicate getPredicate(SearchCriteria param, CriteriaBuilder builder, Root<T> root, CriteriaQuery<?> query) {
        boolean isJoinColumn = isJoinColumnField(root.getJavaType(), param.getKey().split("\\.")[0], query);
        if (isJoinColumn) {
            return join(param, builder, root, query);
        }
        return switch (param.getOperation()) {
            case ">" -> greaterThan(param, builder, root);
            case ">=" -> greaterThanOrEqual(param, builder, root);
            case "<" -> lessThan(param, builder, root);
            case "<=" -> lessThanOrEqual(param, builder, root);
            case "%_" -> endWith(param, builder, root);
            case "_%" -> startWith(param, builder, root);
            case "%_%" -> containWith(param, builder, root);
            case "!=" -> getPredicateOnNotEqual(param, builder, root);
            case "=" -> getPredicateOnEqual(param, builder, root);
            case "in" -> getPredicateIn(param, builder, root);
            default -> throw new SearchException("Could not support operation");
        };
    }

    @SuppressWarnings("unchecked")
    private Predicate getPredicateIn(SearchCriteria param, CriteriaBuilder builder, Root<T> root) {
        boolean isCollection = Collection.class.isAssignableFrom(param.getValue().getClass());
        var paramType = root.get(param.getKey()).getJavaType();
        if (!isCollection) {
            throw new SearchException("In not support for type should be array ");
        }
        return switch (param.getType()) {
            case NUMBER -> root.get(param.getKey()).in((Collection<Number>) param.getValue());
            case STRING -> {
                if (paramType.isEnum()) {
                    Collection<String> enums = (Collection<String>) param.getValue();
                    var enumList = enums.stream()
                            .map(en -> Enum.valueOf((Class<Enum>) paramType, en))
                            .toList();

                    yield root.get(param.getKey()).in(enumList);
                }
                yield root.get(param.getKey()).in((Collection<String>) param.getValue());
            }
            default -> throw new SearchException("In not support for type " + param.getType());
        };
    }

    @SuppressWarnings("all")
    private Predicate containWith(SearchCriteria param, CriteriaBuilder builder, Root<T> root) {
        var value = param.getValue() == null ? null : param.getValue().toString();
        switch (param.getType()) {
            case STRING -> {
                return builder.like(builder.lower(root.get(param.getKey())), "%" + value.toLowerCase() + "%");
            }
            case JSON -> {
                String[] keys = param.getKey().split("\\.");
                if (keys.length < 2) {
                    throw new SearchException("Json Type not valid");
                }
                return builder.like(builder.lower(builder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get(keys[0]), builder.literal(keys[1]))), "%" + value.toLowerCase() + "%");
            }
            default -> throw new SearchException("Contains  not support type " + param.getType());
        }
    }

    private Predicate greaterThan(SearchCriteria param, CriteriaBuilder builder, Root<T> root) {
        switch (param.getType()) {
            case LOCALDATETIME -> {
                LocalDateTime localDateTime = toLocalDateTime(param.getValue());
                return builder.greaterThan(root.get(param.getKey()).as(LocalDateTime.class), localDateTime);
            }
            case LOCALDATE -> {
                LocalDate localDate = toLocalDate(param.getValue());
                return builder.greaterThan(root.get(param.getKey()).as(LocalDate.class), localDate);
            }
            case DATE -> {
                var date = DateUtil.toDate(param.getValue().toString(), TypeSearch.FORMAT_DATE);
                return builder.greaterThan(root.get(param.getKey()).as(Date.class), date);
            }
            case NUMBER -> {
                Class<?> paramType = root.get(param.getKey()).getJavaType();
                if (Number.class.isAssignableFrom(paramType)) {
                    return builder.greaterThan(root.get(param.getKey()), param.getValue().toString());
                }
                return builder.conjunction();
            }
            default -> throw new SearchException("Greater than not support type " + param.getType());
        }
    }

    private Predicate join(SearchCriteria param, CriteriaBuilder builder, Root<T> root, CriteriaQuery<?> query) {
        var keys = param.getKey().split("\\.");
        var concat = param.getKey().replaceFirst(keys[0] + ".", "").split("\\.");
        return JoinSpecification.join(root.join(keys[0]), builder, keys = concat, param, query);
    }

    private Predicate lessThan(SearchCriteria param, CriteriaBuilder builder, Root<T> root) {
        switch (param.getType()) {
            case LOCALDATETIME -> {
                LocalDateTime localDateTime = toLocalDateTime(param.getValue());
                return builder.lessThan(root.get(param.getKey()).as(LocalDateTime.class), localDateTime);
            }
            case LOCALDATE -> {
                LocalDate localDate = toLocalDate(param.getValue());
                return builder.lessThan(root.get(param.getKey()).as(LocalDate.class), localDate);
            }
            case DATE -> {
                var date = DateUtil.toDate(param.getValue().toString(), TypeSearch.FORMAT_DATE);
                return builder.lessThan(root.get(param.getKey()).as(Date.class), date);
            }
            case NUMBER -> {
                Class<?> paramType = root.get(param.getKey()).getJavaType();
                if (Number.class.isAssignableFrom(paramType)) {
                    return builder.lessThan(root.get(param.getKey()), param.getValue().toString());
                }
                return builder.conjunction();
            }
            default -> throw new SearchException("Less than not support type " + param.getType());
        }
    }

    private Predicate greaterThanOrEqual(SearchCriteria param, CriteriaBuilder builder, Root<T> root) {
        return switch (param.getType()) {
            case LOCALDATETIME -> {
                LocalDateTime localDateTime = toLocalDateTime(param.getValue());
                yield builder.greaterThanOrEqualTo(root.get(param.getKey()).as(LocalDateTime.class), localDateTime);
            }
            case LOCALDATE -> {
                LocalDate localDate = toLocalDate(param.getValue());
                yield builder.greaterThanOrEqualTo(root.get(param.getKey()).as(LocalDate.class), localDate);
            }
            case DATE -> {
                var date = DateUtil.toDate(param.getValue().toString(), TypeSearch.FORMAT_DATE);
                yield builder.greaterThanOrEqualTo(root.get(param.getKey()).as(Date.class), date);
            }
            case NUMBER -> {
                Class<?> paramType = root.get(param.getKey()).getJavaType();
                if (Number.class.isAssignableFrom(paramType)) {
                    yield builder.greaterThanOrEqualTo(root.get(param.getKey()), param.getValue().toString());
                }
                yield builder.conjunction();

            }
            default -> throw new SearchException("Greater than not support type " + param.getType());
        };
    }

    private Predicate lessThanOrEqual(SearchCriteria param, CriteriaBuilder builder, Root<T> root) {
        return switch (param.getType()) {
            case LOCALDATETIME -> {
                LocalDateTime localDateTime = toLocalDateTime(param.getValue());
                yield builder.lessThanOrEqualTo(root.get(param.getKey()).as(LocalDateTime.class), localDateTime);
            }
            case LOCALDATE -> {
                LocalDate localDate = toLocalDate(param.getValue());
                yield builder.lessThanOrEqualTo(root.get(param.getKey()).as(LocalDate.class), localDate);
            }
            case DATE -> {
                var date = DateUtil.toDate(param.getValue().toString(), TypeSearch.FORMAT_DATE);
                yield builder.lessThanOrEqualTo(root.get(param.getKey()).as(Date.class), date);
            }
            case NUMBER -> {
                Class<?> paramType = root.get(param.getKey()).getJavaType();
                if (Number.class.isAssignableFrom(paramType)) {
                    yield builder.lessThanOrEqualTo(root.get(param.getKey()), param.getValue().toString());
                }
                yield builder.conjunction();
            }
            default -> throw new SearchException("Less than or equal not support type " + param.getType());
        };
    }

    private Predicate endWith(SearchCriteria param, CriteriaBuilder builder, Root<T> root) {
        switch (param.getType()) {
            case STRING -> {
                return builder.like(builder.lower(root.get(param.getKey())), "%" + param.getValue().toString().toLowerCase());
            }
            case JSON -> {
                var keys = param.getKey().split("\\.");
                if (!isJson(root.getJavaType(), keys[0])) {
                    throw new SearchException("Json column should be annotated org.hibernate.annotations @Type @TypeDef");
                }
                if (keys.length < 2) {
                    throw new SearchException("Json Type not valid");
                }
                return builder.like(builder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get(keys[0]), builder.literal(keys[1])), "%" + param.getValue().toString().toLowerCase());
            }
            default -> throw new SearchException("End with not support type " + param.getType());
        }
    }

    private Predicate startWith(SearchCriteria param, CriteriaBuilder builder, Root<T> root) {
        switch (param.getType()) {
            case STRING -> {
                return builder.like(builder.lower(root.get(param.getKey())), param.getValue().toString().toLowerCase() + "%");
            }
            case JSON -> {
                String[] keys = param.getKey().split("\\.");

                if (!isJson(root.getJavaType(), keys[0])) {
                    throw new SearchException("Json column should be annotated org.hibernate.annotations @Type @TypeDef");
                }
                if (keys.length < 2) {
                    throw new SearchException("Json Type not valid");
                }

                return builder.like(builder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get(keys[0]), builder.literal(keys[1])), param.getValue().toString().toLowerCase() + "%");
            }
            default -> throw new SearchException("Start with not support type " + param.getType());
        }
    }

    @SuppressWarnings("all")
    private Predicate getPredicateOnEqual(SearchCriteria param, CriteriaBuilder builder, Root<T> root) {
        var keys = param.getKey().split("\\.");
        var paramType = root.get(keys[0]).getJavaType();

        if (param.getValue() == null) {
            return builder.isNull(root.get(keys[0]));
        }
        return switch (param.getType()) {
            case NUMBER -> {
                if (paramType.isInstance(UUID.class)) {
                    var value = param.getValue() == null ? null : param.getValue().toString();
                    yield builder.equal(root.get(keys[0]), UUID.fromString(value));
                }
                ;
                if (isNumber(paramType)) {
                    yield builder.equal(root.get(param.getKey()), param.getValue().toString());
                }
                yield builder.conjunction();
            }
            case BOOL -> {
                if (isBoolean(paramType)) {
                    var bool = Boolean.valueOf(param.getValue().toString());
                    yield bool ? builder.isTrue(root.get(keys[0])) : builder.isFalse(root.get(keys[0]));
                }
                yield builder.conjunction();
            }
            case STRING -> {
                var value = param.getValue() == null ? null : param.getValue().toString();
                if (paramType.isInstance(UUID.class)) {
                    yield builder.equal(root.get(keys[0]), UUID.fromString(value));
                }
                if (paramType.isEnum()) {
                    yield builder.equal(root.get(param.getKey()), Enum.valueOf((Class<Enum>) paramType, value));
                }
                yield builder.equal(root.get(keys[0]), param.getValue());
            }
            case JSON -> {
                if (Collection.class.isAssignableFrom(paramType)) {
                    yield builder.like(builder.function(
                            "text",
                            String.class,
                            root.get(keys[0])), "%" + String.valueOf(param.getValue()) + "%");
                }
                var value = param.getValue() == null ? null : param.getValue().toString();
                if (!isJson(root.getJavaType(), keys[0])) {
                    throw new SearchException("Json column should be annotated org.hibernate.annotations @Type @TypeDef");
                }
                if (paramType.equals(HashMap.class)) {
                    yield builder.or(
                            builder.like(builder.lower(builder.function(
                                    "jsonb_extract_path_text",
                                    String.class,
                                    root.get(param.getKey()), builder.literal("uz"))), "%" + value.toLowerCase() + "%"),
                            builder.like(builder.lower(builder.function(
                                    "jsonb_extract_path_text",
                                    String.class,
                                    root.get(param.getKey()), builder.literal("oz"))), "%" + value.toLowerCase() + "%"),
                            builder.like(builder.lower(builder.function(
                                    "jsonb_extract_path_text",
                                    String.class,
                                    root.get(param.getKey()), builder.literal("ru"))), "%" + value.toLowerCase() + "%")
                    );
                }
                yield builder.equal(builder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get(keys[0]), builder.literal(keys[1])), value);
            }
            default -> throw new SearchException("NOT SUPPORTED FOR TYPE " + param.getType());
        };
    }

    @SuppressWarnings("all")
    private Predicate getPredicateOnNotEqual(SearchCriteria param, CriteriaBuilder builder, Root<T> root) {
        String[] keys = param.getKey().split("\\.");
        Class<?> paramType = root.get(keys[0]).getJavaType();
        var value = param.getValue() == null ? null : param.getValue().toString();
        if (value == null) {
            return builder.isNotNull(root.get(keys[0]));
        }
        switch (param.getType()) {
            case JSON -> {
                if (!isJson(root.getJavaType(), keys[0])) {
                    throw new SearchException("Json column should be annotated org.hibernate.annotations @Type @TypeDef");
                }
                if (keys.length < 2) {
                    throw new SearchException("Json Type not valid");
                }

                if (paramType.equals(HashMap.class)) {
                    return builder.and(
                            builder.notLike(builder.lower(builder.function(
                                    "jsonb_extract_path_text",
                                    String.class,
                                    root.get(param.getKey()), builder.literal("uz"))), "%" + value.toLowerCase() + "%"),
                            builder.notLike(builder.lower(builder.function(
                                    "jsonb_extract_path_text",
                                    String.class,
                                    root.get(param.getKey()), builder.literal("oz"))), "%" + value.toLowerCase() + "%"),
                            builder.notLike(builder.lower(builder.function(
                                    "jsonb_extract_path_text",
                                    String.class,
                                    root.get(param.getKey()), builder.literal("ru"))), "%" + value.toLowerCase() + "%")
                    );
                }
                return builder.notEqual(builder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        root.get(keys[0]), builder.literal(keys[1])), value);
            }
            case STRING -> {
                if (paramType.equals(UUID.class)) {
                    return builder.notEqual(root.get(param.getKey()), UUID.fromString(value));
                }
                if (paramType.isEnum()) {
                    return builder.notEqual(root.get(param.getKey()), Enum.valueOf((Class<Enum>) paramType, value));
                }
                return builder.notEqual(root.get(param.getKey()), value);
            }
            case NUMBER -> {
                if (paramType.equals(UUID.class)) {
                    return builder.notEqual(root.get(param.getKey()), UUID.fromString(value));
                }
                if (Number.class.isAssignableFrom(paramType)) {
                    return builder.notEqual(root.get(param.getKey()), param.getValue().toString());
                }
                return builder.conjunction();
            }
            default -> throw new SearchException("Not support type " + param.getType());
        }
    }

}
