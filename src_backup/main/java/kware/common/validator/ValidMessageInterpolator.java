package kware.common.validator;

import cetus.annotation.DisplayName;
import kware.common.exception.SimpleException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.validation.MessageInterpolator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Locale;

@Slf4j
public class ValidMessageInterpolator extends ParameterMessageInterpolator implements MessageInterpolator {

    @Override
    public String interpolate(String messageTemplate, Context context) {
        String interpolatedMessage = super.interpolate(messageTemplate, context);

        if (interpolatedMessage != null && interpolatedMessage.contains("{0}")) {
            DisplayName displayName = getDisplayNameAnnotation(context);
            if (displayName != null) {
                String displayNameValue = displayName.value();
                interpolatedMessage = interpolatedMessage.replace("{0}", displayNameValue);
            }
        }
        return interpolatedMessage;
    }
    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        return this.interpolate(messageTemplate, context);
    }


    private DisplayName getDisplayNameAnnotation(Context context) {
        MessageInterpolatorContext mipContext = (MessageInterpolatorContext) context;
        String propertyPath = mipContext.getPropertyPath().toString();
        Class<?> rootBeanType = mipContext.getRootBeanType();

        try {
            Field field;
            Class<?> targetType = rootBeanType;

            if (propertyPath.contains(".")) {

                String[] pathParts = propertyPath.split("\\."); // "parent.codeNm" -> ["parent", "codeNm"]
                field = targetType.getDeclaredField(pathParts[0]);
                targetType = field.getType();
                field = targetType.getDeclaredField(pathParts[1]);

            } else {

                field = targetType.getDeclaredField(propertyPath);

            }

            // 필드의 모든 어노테이션 확인
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof DisplayName) {
                    return (DisplayName) annotation;
                }
            }

        } catch (NoSuchFieldException e) {
            throw new SimpleException(propertyPath + " 필드에 DisplayName 값이 누락되었습니다. 확인해주세요.");
        }

        return null;
    }
}
