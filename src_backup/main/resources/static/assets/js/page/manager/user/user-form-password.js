var userFormPassword = {

    changePassword: false
    ,passwordInput: $("#password")
    ,passwordChkInput: $("#password-chk")
    ,cancelBtn: $('#password-cancel-btn')
    ,saveBtn: $('#password-save-btn')

    ,validatePassword: function () {
        const passwordVal = userFormPassword.passwordInput.val();
        const passwordChkVal = userFormPassword.passwordChkInput.val();

        // 버튼 활성화/비활성화
        if (passwordVal.length > 0) {
            userFormPassword.cancelBtn.attr('disabled', false);
            userFormPassword.saveBtn.attr('disabled', false);
        } else {
            userFormPassword.cancelBtn.attr('disabled', true);
            userFormPassword.saveBtn.attr('disabled', true);
        }

        const pwValid = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+~`\-={}[\]|\\:;"'<>,.?/]).{8,20}$/;
        if (passwordVal && pwValid.test(passwordVal)) {
            userFormPassword.passwordInput.removeClass('is-invalid').addClass('is-valid');
        } else {
            userFormPassword.passwordInput.removeClass('is-valid').addClass('is-invalid');
        }

        // 비밀번호 확인 체크
        if (passwordVal && passwordChkVal) {
            if (passwordVal === passwordChkVal) {
                userFormPassword.passwordChkInput.removeClass('is-invalid').addClass('is-valid');
            } else {
                userFormPassword.passwordChkInput.removeClass('is-valid').addClass('is-invalid');
            }
        }
    }

    ,submitForm: function () {
        const obj = {
            password: userFormPassword.passwordInput.val()
        }
        Http.put(`/cetus/api/user/change-password/${uid}`, obj, true).then((res) => {
            Util.alert("사용자 정보가 수정되었습니다.").then(() => {
                window.location.href = `/manager/cetus/user/${uid}`
            })
        }).fail((e) => {
            if (e.status === 400) {
                if (e.hasOwnProperty('responseJSON')) {
                    const response = e['responseJSON'];
                    ValidationError.setValidationMssg('user-password-form', response)
                }
            }
        });
    }

}