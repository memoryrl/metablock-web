package kware.common.config.support;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum LoginErrorMessage {

    BadCredentialsException( "5회 이상 실패 시 계정이 비활성화 됩니다."),
    UsernameNotFoundException("등록되지 않은 관리자입니다. \n 관리자 등록을 해주세요."),
    AccountExpiredException("계정이 만료되었습니다."),
    CredentialsExpiredException( "비밀번호가 만료되었습니다. \n 비밀번호를 변경해주세요."),
    DisabledException( "권한이 없는 페이지입니다."),
    DisabledException_STOP( "정지된 계정입니다. \n 관리자에게 문의해주세요."),
    DisabledException_WAIT( "현재 가입승인 대기 상태입니다. \n 관리자의 승인을 기다려주세요."),
    LockedException( "비활성화된 계정입니다. \n 비밀번호 변경 후 다시 로그인 해주세요."),
    AuthorizationException("권한이 없는 페이지입니다."),
    NoneException("알 수 없는 에러 입니다.");

    private String value;

    LoginErrorMessage(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    private static final Map<String, LoginErrorMessage> descriptions =
            Collections
                    .unmodifiableMap(
                            Stream.of(values())
                                    .collect(
                                            Collectors.toMap(LoginErrorMessage::name, Function.identity())
                                    )
                    );

    public static LoginErrorMessage findOf(String findValue) {
        return Optional.ofNullable(descriptions.get(findValue)).orElse(NoneException);
    }

}
