package uz.project.common;

/*
    @author: Islombek Qurbonov
    Date: 12/08/2023
    Time: 18:00
*/

import lombok.experimental.UtilityClass;
import org.hibernate.annotations.Type;
import uz.project.common.exception.SearchException;
import uz.project.common.request.TypeSearch;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ReflectionUtils {

    public final static Map<Class<?>, Class<?>> PRIMITIVE = new HashMap<>();

    static {
        PRIMITIVE.put(boolean.class, Boolean.class);
        PRIMITIVE.put(byte.class, Byte.class);
        PRIMITIVE.put(short.class, Short.class);
        PRIMITIVE.put(char.class, Character.class);
        PRIMITIVE.put(int.class, Integer.class);
        PRIMITIVE.put(long.class, Long.class);
        PRIMITIVE.put(float.class, Float.class);
        PRIMITIVE.put(double.class, Double.class);
    }

    public static <T> T extractFieldValue(
            Object classObj,
            String fieldName,
            Class<T> castObject
    ) throws NoSuchFieldException, IllegalAccessException {
        var field = classObj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return castObject.cast(field.get(classObj));
    }

    public static Field getField(Class<?> targetClass, String fieldName) {
        Field field = null;
        try {
            field = targetClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            try {
                field = targetClass.getField(fieldName);
            } catch (NoSuchFieldException ignore) {
            }
            if (!targetClass.getSuperclass().equals(Object.class)) {
                return getField(targetClass.getSuperclass(), fieldName);
            }
        } finally {
            if (field != null) {
                field.setAccessible(true);
            }
        }

        return field;
    }

    public static boolean existDeclaredField(Class<?> _clazz, String fieldName) {
        return getField(_clazz, fieldName) != null;
    }

    public static boolean isJoinColumnField(Class<?> _clazz, String field, CriteriaQuery<?> query) {
        if (!existDeclaredField(_clazz, field)) return false;
        var classField = getField(_clazz, field);
        var type = classField.getType();

        boolean isManyToMany = hasAnnotation(classField, ManyToMany.class);
        boolean isOneToMany = hasAnnotation(classField, OneToMany.class);
        boolean isManyToOne = hasAnnotation(classField, ManyToOne.class);
        boolean isOneToOne = hasAnnotation(classField, OneToOne.class);
        boolean hasRelation = isManyToMany || isOneToMany || isManyToOne || isOneToOne;

        if (Collection.class.isAssignableFrom(type)) {
            var genericType = (ParameterizedType) classField.getGenericType();
            var collectionSubClass = (Class<?>) genericType.getActualTypeArguments()[0];
            query.distinct(true);
            return hasRelation && isEntity(collectionSubClass);
        }
        return hasRelation && isEntity(type);
    }

    public static boolean isEntity(Class<?> _clazz) {
        return hasAnnotation(_clazz, Entity.class);
    }

    public static boolean hasAnnotation(Class<?> _clazz, Class<? extends Annotation> annotation) {
        return _clazz.isAnnotationPresent(annotation);
    }

    public static boolean hasAnnotation(Field field, Class<? extends Annotation> annotation) {
        return field.isAnnotationPresent(annotation);
    }

    public static boolean isJson(Class<?> target, String field) {
        if (!existDeclaredField(target, field)) return false;
        return hasAnnotation(getField(target, field), Type.class);
    }

    public static boolean isNumber(Class<?> paramType) {
        if (paramType.isPrimitive()) {
            paramType = PRIMITIVE.get(paramType);
        }
        return Number.class.isAssignableFrom(paramType);
    }

    public static boolean isBoolean(Class<?> paramType) {
        if (paramType.isPrimitive()) {
            paramType = PRIMITIVE.get(paramType);
        }
        return Boolean.class.isAssignableFrom(paramType);
    }

    public static LocalDate toLocalDate(Object param) {
        LocalDate localDate;
        if (LocalDate.class.equals(param.getClass())) {
            localDate = (LocalDate) param;
        } else {
            localDate = DateUtil.toLocalDate(param.toString(), TypeSearch.FORMAT_LOCALDATE);
            if (localDate == null) {
                throw new SearchException("LocalDate pattern should be 2020.05.14");
            }
        }
        return localDate;
    }

    public static LocalDateTime toLocalDateTime(Object param) {
        LocalDateTime localDateTime;
        if (LocalDateTime.class.equals(param.getClass())) {
            localDateTime = (LocalDateTime) param;
        } else {
            localDateTime = DateUtil.toLocalDateTime(param.toString(), TypeSearch.FORMAT_LOCALDATETIME);
        }
        return localDateTime;
    }
}
