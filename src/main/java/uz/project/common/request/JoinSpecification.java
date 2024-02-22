package uz.project.common.request;

import lombok.experimental.UtilityClass;
import uz.project.common.DateUtil;
import uz.project.common.exception.SearchException;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static uz.project.common.ReflectionUtils.*;

@UtilityClass
public class JoinSpecification {

    public static Predicate join(
            Join<Object, Object> joinPath,
            CriteriaBuilder builder,
            String[] keys,
            SearchCriteria searchCriteria,
            CriteriaQuery<?> query
    ) {
        boolean existJoinField = keys.length > 2 && isJoinColumnField(joinPath.getJavaType(), keys[0], query);
        if (existJoinField) {
            return join(joinPath.join(keys[0], JoinType.LEFT), builder, removeFirsElementArr(keys), searchCriteria, query);
        }
        if (!checkExistField(joinPath, keys[0])) return builder.conjunction();

        return switch (searchCriteria.getOperation()) {
            case ">" -> greaterThan(searchCriteria, builder, joinPath, keys, searchCriteria.getValue());
            case ">=" -> greaterThanOrEqual(searchCriteria, builder, joinPath, keys, searchCriteria.getValue());
            case "<" -> lessThan(searchCriteria, builder, joinPath, keys, searchCriteria.getValue());
            case "<=" -> lessThanOrEqual(searchCriteria, builder, joinPath, keys, searchCriteria.getValue());
            case "%_" -> endWith(searchCriteria, builder, joinPath, keys, searchCriteria.getValue());
            case "_%" -> startWith(searchCriteria, builder, joinPath, keys, searchCriteria.getValue());
            case "%_%" -> containWith(searchCriteria, builder, joinPath, keys, searchCriteria.getValue());
            case "!=" -> getPredicateOnNotEqual(searchCriteria, builder, joinPath, keys, searchCriteria.getValue());
            case "=" -> getPredicateOnEqual(searchCriteria, builder, joinPath, keys, searchCriteria.getValue());
            case "in" -> getPredicateIn(searchCriteria, builder, joinPath, keys, searchCriteria.getValue());
            default -> throw new SearchException("Not support type " + searchCriteria.getOperation());
        };
    }

    private static String[] removeFirsElementArr(String[] keys) {
        return String.join(".", keys)
                .replaceFirst(keys[0] + ".", "")
                .split("\\.");
    }


    private boolean checkExistField(Join<Object, Object> root, String key) {
        var rootJavaType = root.getJavaType();
        if (!isEntity(rootJavaType)) {
            throw new SearchException("Chosen class not annotated @Entity " + rootJavaType.getName());
        }
        var keys = key.split("\\.");
        if (!existDeclaredField(rootJavaType, keys[0])) {
            throw new SearchException("Entity " + rootJavaType.getName() + " not exist field" + keys[0]);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private static Predicate getPredicateIn(
            SearchCriteria param,
            CriteriaBuilder builder,
            Join<Object, Object> joinPath,
            String[] key,
            Object value
    ) {

        boolean isCollection = Collection.class.isAssignableFrom(value.getClass());
        if (!isCollection) {
            throw new SearchException("In not support for type should be array ");
        }
        return switch (param.getType()) {
            case NUMBER -> builder.in(joinPath.get(key[0])).in((Collection<Number>) value);
            case STRING -> builder.in(joinPath.get(key[0])).in((Collection<String>) value);
            default -> throw new SearchException("In not support for type " + param.getType());
        };
    }

    private Predicate greaterThan(
            SearchCriteria param,
            CriteriaBuilder builder,
            Join<Object, Object> joinPath,
            String[] key,
            Object value
    ) {
        if (isJson(joinPath.getJavaType(), key[0])) {
            throw new SearchException("Json type not support for less than");
        }
        switch (param.getType()) {
            case LOCALDATETIME -> {
                LocalDateTime localDateTime = toLocalDateTime(param.getValue());
                return builder.greaterThan(joinPath.get(key[0]).as(LocalDateTime.class), localDateTime);
            }
            case LOCALDATE -> {
                LocalDate localDate = toLocalDate(param.getValue());
                return builder.greaterThan(joinPath.get(key[0]).as(LocalDate.class), localDate);
            }
            case DATE -> {
                var date = DateUtil.toDate(value.toString(), TypeSearch.FORMAT_DATE);
                return builder.greaterThan(joinPath.get(key[0]).as(Date.class), date);
            }
            case NUMBER -> {
                Class<?> paramType = joinPath.get(key[0]).getJavaType();
                if (isNumber(paramType)) {
                    return builder.greaterThan(joinPath.get(key[0]), param.getValue().toString());
                }
                return builder.conjunction();
            }
            default -> throw new SearchException("Greater than not support type " + param.getType());
        }
    }

    private Predicate greaterThanOrEqual(
            SearchCriteria param,
            CriteriaBuilder builder,
            Join<Object, Object> joinPath,
            String[] key,
            Object value
    ) {
        if (isJson(joinPath.getJavaType(), key[0])) {
            throw new SearchException("Json type not support for less than");
        }
        switch (param.getType()) {
            case LOCALDATETIME -> {
                LocalDateTime localDateTime = toLocalDateTime(param.getValue());
                return builder.greaterThanOrEqualTo(joinPath.get(key[0]).as(LocalDateTime.class), localDateTime);
            }
            case LOCALDATE -> {
                LocalDate localDate = toLocalDate(param.getValue());
                return builder.greaterThanOrEqualTo(joinPath.get(key[0]).as(LocalDate.class), localDate);
            }
            case DATE -> {
                var date = DateUtil.toDate(value.toString(), TypeSearch.FORMAT_DATE);
                return builder.greaterThanOrEqualTo(joinPath.get(key[0]).as(Date.class), date);
            }
            case NUMBER -> {
                var paramType = joinPath.get(key[0]).getJavaType();
                if (isNumber(paramType)) {
                    return builder.greaterThanOrEqualTo(joinPath.get(key[0]), param.getValue().toString());
                }
                return builder.conjunction();
            }
            default -> throw new SearchException("Greater or equal than not support type " + param.getType());
        }
    }


    private Predicate lessThan(
            SearchCriteria param,
            CriteriaBuilder builder,
            Join<Object, Object> joinPath,
            String[] key,
            Object value
    ) {
        if (isJson(joinPath.getJavaType(), key[0])) {
            throw new SearchException("Json type not support for less than");
        }
        switch (param.getType()) {
            case LOCALDATETIME -> {
                LocalDateTime localDateTime = toLocalDateTime(param.getValue());
                return builder.lessThan(joinPath.get(key[0]).as(LocalDateTime.class), localDateTime);
            }
            case LOCALDATE -> {
                LocalDate localDate = toLocalDate(param.getValue());
                return builder.lessThan(joinPath.get(key[0]).as(LocalDate.class), localDate);
            }
            case DATE -> {
                var date = DateUtil.toDate(value.toString(), TypeSearch.FORMAT_DATE);
                return builder.lessThan(joinPath.get(key[0]).as(Date.class), date);
            }
            case NUMBER -> {
                Class<?> paramType = joinPath.get(key[0]).getJavaType();
                if (isNumber(paramType)) {
                    return builder.lessThan(joinPath.get(key[0]), param.getValue().toString());
                }
                return builder.conjunction();
            }
            default -> throw new SearchException("Less than not support type " + param.getType());
        }
    }

    private Predicate lessThanOrEqual(
            SearchCriteria param,
            CriteriaBuilder builder,
            Join<Object, Object> joinPath,
            String[] key,
            Object value
    ) {
        if (isJson(joinPath.getJavaType(), key[0])) {
            throw new SearchException("Json type not support for less than");
        }
        switch (param.getType()) {
            case LOCALDATETIME -> {
                LocalDateTime localDateTime = toLocalDateTime(param.getValue());
                return builder.lessThanOrEqualTo(joinPath.get(key[0]).as(LocalDateTime.class), localDateTime);
            }
            case LOCALDATE -> {
                LocalDate localDate = toLocalDate(param.getValue());
                return builder.lessThanOrEqualTo(joinPath.get(key[0]).as(LocalDate.class), localDate);
            }
            case DATE -> {
                var date = DateUtil.toDate(value.toString(), TypeSearch.FORMAT_DATE);
                return builder.lessThanOrEqualTo(joinPath.get(key[0]).as(Date.class), date);
            }
            case NUMBER -> {
                Class<?> paramType = joinPath.get(key[0]).getJavaType();
                if (Number.class.isAssignableFrom(paramType)) {
                    return builder.lessThanOrEqualTo(joinPath.get(key[0]), param.getValue().toString());
                }
                return builder.conjunction();
            }
            default -> throw new SearchException("Less than not support type " + param.getType());
        }
    }

    private Predicate endWith(
            SearchCriteria param,
            CriteriaBuilder builder,
            Join<Object, Object> joinPath,
            String[] key,
            Object value
    ) {
        switch (param.getType()) {
            case STRING -> {
                return builder.like(builder.lower(joinPath.get(key[0])), "%" + value.toString().toLowerCase());
            }
            case JSON -> {
                if (!isJson(joinPath.getJavaType(), key[0])) {
                    throw new SearchException("Json column should be annotated org.hibernate.annotations @Type @TypeDef");
                }
                if (key.length < 2) {
                    throw new SearchException("Type not valid");
                }
                return builder.like(builder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        joinPath.get(key[0]), builder.literal(key[1])), "%" + value.toString().toLowerCase());
            }
            default -> throw new SearchException("End with not support type " + param.getType());
        }
    }

    private Predicate startWith(
            SearchCriteria param,
            CriteriaBuilder builder,
            Join<Object, Object> joinPath,
            String[] key,
            Object value
    ) {
        switch (param.getType()) {
            case STRING -> {
                return builder.like(builder.lower(joinPath.get(key[0])), value.toString().toLowerCase() + "%");
            }
            case JSON -> {
                if (key.length < 2) {
                    throw new SearchException("Type not valid");
                }
                return builder.like(builder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        joinPath.get(key[0]), builder.literal(key[1])), value.toString().toLowerCase() + "%");
            }
            default -> throw new SearchException("End with not support type " + param.getType());
        }
    }

    private Predicate containWith(
            SearchCriteria param,
            CriteriaBuilder builder,
            Join<Object, Object> joinPath,
            String[] key,
            Object val
    ) {
        String value = val == null ? "" : (String) param.getValue();
        switch (param.getType()) {
            case STRING -> {
                return builder.like(builder.lower(joinPath.get(key[0])), "%" + value.toLowerCase() + "%");
            }
            case JSON -> {
                if (!isJson(joinPath.getJavaType(), key[0])) {
                    throw new SearchException("Json column should be annotated org.hibernate.annotations @Type @TypeDef");
                }
                if (key.length < 2) {
                    throw new SearchException("Json Type not valid");
                }
                return builder.like(builder.lower(builder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        joinPath.get(key[0]), builder.literal(key[1]))), "%" + value.toLowerCase() + "%");
            }
            default -> throw new SearchException("Contains  not support type " + param.getType());
        }
    }

    @SuppressWarnings("all")
    private Predicate getPredicateOnNotEqual(
            SearchCriteria param,
            CriteriaBuilder builder,
            Join<Object, Object> joinPath,
            String[] key,
            Object val
    ) {
        String[] keys = param.getKey().split("\\.");
        Class<?> paramType = joinPath.get(keys[0]).getJavaType();
        String value = param.getValue() == null ? null : (String) param.getValue();

        if (value == null) {
            return builder.isNotNull(joinPath.get(keys[0]));
        }
        switch (param.getType()) {
            case JSON -> {
                if (!isJson(joinPath.getJavaType(), keys[0])) {
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
                                    joinPath.get(keys[0]), builder.literal("uz"))), "%" + value.toLowerCase() + "%"),
                            builder.notLike(builder.lower(builder.function(
                                    "jsonb_extract_path_text",
                                    String.class,
                                    joinPath.get(keys[0]), builder.literal("oz"))), "%" + value.toLowerCase() + "%"),
                            builder.notLike(builder.lower(builder.function(
                                    "jsonb_extract_path_text",
                                    String.class,
                                    joinPath.get(keys[0]), builder.literal("ru"))), "%" + value.toLowerCase() + "%")
                    );
                }
                return builder.notEqual(builder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        joinPath.get(keys[0]), builder.literal(keys[1])), value);
            }
            case STRING -> {
                if (paramType.equals(UUID.class)) {
                    return builder.notEqual(joinPath.get(keys[0]), UUID.fromString(value));
                }
                if (paramType.isEnum()) {
                    return builder.notEqual(joinPath.get(keys[0]), Enum.valueOf((Class<Enum>) paramType, value));
                }
                return builder.notEqual(joinPath.get(keys[0]), value);
            }
            case NUMBER -> {
                if (paramType.equals(UUID.class)) {
                    return builder.notEqual(joinPath.get(keys[0]), UUID.fromString(value));
                }
                if (isNumber(paramType)) {
                    return builder.notEqual(joinPath.get(keys[0]), param.getValue().toString());
                }
                return builder.conjunction();
            }
            default -> throw new SearchException("Not support type " + param.getType());
        }
    }


    @SuppressWarnings("all")
    private Predicate getPredicateOnEqual(
            SearchCriteria param,
            CriteriaBuilder builder,
            Join<Object, Object> joinPath,
            String[] keys,
            Object val
    ) {
        var paramType = joinPath.get(keys[0]).getJavaType();
        if (val == null) {
            return builder.isNull(joinPath.get(keys[0]));
        }
        return switch (param.getType()) {
            case NUMBER -> {
                if (paramType.isInstance(UUID.class)) {
                    String value = val == null ? null : (String) val;
                    yield builder.equal(joinPath.get(keys[0]), UUID.fromString(value));
                }
                if (isNumber(paramType)) {
                    yield builder.equal(joinPath.get(keys[0]), param.getValue());
                }
                yield builder.conjunction();
            }
            case BOOL -> {
                if (isBoolean(paramType)) {
                    var bool = Boolean.valueOf(param.getValue().toString());
                    yield bool ? builder.isTrue(joinPath.get(keys[0])) : builder.isFalse(joinPath.get(keys[0]));
                }
                yield builder.conjunction();
            }
            case STRING -> {
                String value = val == null ? null : (String) val;
                if (paramType.isInstance(UUID.class)) {
                    yield builder.equal(joinPath.get(keys[0]), UUID.fromString(value));
                }
                if (paramType.isEnum()) {
                    yield builder.equal(joinPath.get(keys[0]), Enum.valueOf((Class<Enum>) paramType, value));
                }
                yield builder.equal(joinPath.get(keys[0]), param.getValue());
            }
            case JSON -> {
                String value = val == null ? null : (String) val;
                if (!isJson(joinPath.getJavaType(), keys[0])) {
                    throw new SearchException("Json column should be annotated org.hibernate.annotations.@TypeDef");
                }
                if (keys.length < 2) {
                    throw new SearchException("Json Type not valid");
                }
                if (paramType.equals(HashMap.class)) {
                    yield builder.or(
                            builder.like(builder.lower(builder.function(
                                    "jsonb_extract_path_text",
                                    String.class,
                                    joinPath.get(keys[0]), builder.literal(keys[1]))), "%" + value.toLowerCase() + "%"),
                            builder.like(builder.lower(builder.function(
                                    "jsonb_extract_path_text",
                                    String.class,
                                    joinPath.get(keys[0]), builder.literal(keys[1]))), "%" + value.toLowerCase() + "%"),
                            builder.like(builder.lower(builder.function(
                                    "jsonb_extract_path_text",
                                    String.class,
                                    joinPath.get(keys[0]), builder.literal(keys[1]))), "%" + value.toLowerCase() + "%")
                    );
                }
                yield builder.equal(builder.function(
                        "jsonb_extract_path_text",
                        String.class,
                        joinPath.get(keys[0]), builder.literal(keys[1])), value);
            }
            default -> throw new SearchException("NOT SUPPORTED FOR TYPE" + param.getType());
        };
    }
}
