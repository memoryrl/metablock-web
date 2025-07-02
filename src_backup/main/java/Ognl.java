import cetus.util.StringUtil;

/**
 * MyBatis 동적 질의 조건식 체크를 위한 헬퍼 클래스
 *
 */
public class Ognl {

    public static boolean isEmpty(Object object) {
        return StringUtil.isEmpty(object);
    }

    public static boolean isNotEmpty(Object object) {
        return StringUtil.isNotEmpty(object);
    }

    public static boolean equals(Object object1, Object object2) {
        return StringUtil.equals(String.valueOf(object1), String.valueOf(object2));
    }

    public static boolean notEquals(Object object1, Object object2) {
        return !equals(object1, object2);
    }
}
