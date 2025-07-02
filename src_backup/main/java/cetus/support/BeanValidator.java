package cetus.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import cetus.annotation.Key;
import cetus.annotation.Label;
import cetus.annotation.Length;
import cetus.annotation.MaxLength;
import cetus.annotation.OnlyNumber;
import cetus.annotation.Pattern;
import cetus.annotation.Range;
import cetus.annotation.Required;

public class BeanValidator {
    public static void validate(Object bean) {
        validate(bean, null);
    }

    public static void validate(Object bean, Class<? extends Annotation> clazz) {
        // getter 로 변경
        Field[] fields = bean.getClass().getDeclaredFields();
        for(Field f : fields) {
            f.setAccessible(true);

            // Label이 설정되지 않으면 Validator를 동작시키지 않는다.
            // 오류메시지와도 관련있음
            Label label = Reflector.getTypedAnnotation(f, Label.class);
            if(label == null) {
                continue;
            }

            String fieldLabel = label.value();
            String fieldName = f.getName();
            Object fieldValue = Reflector.getValue(bean, f);
            Class<?> fieldType = f.getType();

            Annotation[] annotations = Reflector.getAnnotations(f, clazz);
            for(Annotation a : annotations) {
                ValidateInner(a, fieldLabel, fieldName, fieldValue, fieldType, bean);
            }
        }
    }


    private static void ValidateInner(Annotation a, String fieldLabel, String fieldName, Object fieldValue, Class<?> fieldType, Object bean) {
        if(a.annotationType().equals(Key.class)) {
            Asserts.notNull(fieldLabel, fieldName, fieldValue);
        }
        if(a.annotationType().equals(Required.class)) {
            Asserts.notNull(fieldLabel, fieldName, fieldValue);
        }
        if(a.annotationType().equals(Range.class)) {
            if(isEmpty(fieldValue)) { return; }
            Range range = (Range)a;
            Asserts.range(fieldLabel, fieldName, fieldValue, range.min(), range.max());
        }
        if(a.annotationType().equals(MaxLength.class)) {
            if(isEmpty(fieldValue)) { return; }
            MaxLength maxlength = (MaxLength)a;
            Asserts.maxLength(fieldLabel, fieldName, fieldValue, maxlength.value());
        }
        if(a.annotationType().equals(Pattern.class)) {
            if(isEmpty(fieldValue)) { return; }
            Pattern pattern = (Pattern)a;
            Asserts.pattern(fieldLabel, fieldName, fieldValue, pattern.example(), pattern.regex());
        }
        if(a.annotationType().equals(Length.class)) {
            if(isEmpty(fieldValue)) { return; }
            Length length = (Length)a;
            Asserts.length(fieldLabel, fieldName, fieldValue, length.value());
        }
        if(a.annotationType().equals(OnlyNumber.class)) {
            if(isEmpty(fieldValue)) { return; }
            Asserts.onlyNumber(fieldLabel, fieldName, fieldValue);
        }
    }

    private static boolean isEmpty(Object value) {
        if(isNull(value)) return true;
        if(String.valueOf(value).isEmpty()) return true;
        return false;
    }

    private static boolean isNull(Object value) {
        if(value == null) return true;
        return false;
    }
}
