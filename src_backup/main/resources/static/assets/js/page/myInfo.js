var myInfoJS = {
    
     changeEmail: false
    ,checkEmail: true
    ,originEmailVal: $("#userEmail-origin").val()
    ,emailChangeBtn: $("#userEmail-change")
    ,emailInput: $("#userEmail")
    
    ,passwordChange: false
    ,passwordInput: $("#password")
    ,passwordChkInput: $("#confirmPassword")
    ,passwordChangeBtn: $("#password-change")

    ,objKeys: ["userNm", "userEmail", "password"]
    
    ,passwordChangeBtnClick: function() {
        if(!myInfoJS.passwordChange) {
            myInfoJS.passwordChange = true;
            myInfoJS.passwordInput.prop('disabled', false).prop('readonly', false);
            myInfoJS.passwordChkInput.prop('disabled', false).prop('readonly', false);
            myInfoJS.passwordChangeBtn.prop('disabled', true);
        }
    }

    ,validatePassword: function () {
        const passwordVal = myInfoJS.passwordInput.val();
        const passwordChkVal = myInfoJS.passwordChkInput.val();

        const pwValid = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+~`\-={}[\]|\\:;"'<>,.?/]).{8,20}$/;
        if (passwordVal && pwValid.test(passwordVal)) {
            myInfoJS.passwordInput.removeClass('is-invalid').addClass('is-valid');
        } else {
            myInfoJS.passwordInput.removeClass('is-valid').addClass('is-invalid');
        }

        // 비밀번호 확인 체크
        if (passwordVal && passwordChkVal) {
            if (passwordVal === passwordChkVal) {
                myInfoJS.passwordChkInput.removeClass('is-invalid').addClass('is-valid');
            } else {
                myInfoJS.passwordChkInput.removeClass('is-valid').addClass('is-invalid');
            }
        }
    }

    ,userEmailChangeBtnClick: function () {

        /* == 이메일 변경 -> 이메일 중복 체크 == */
        if(!myInfoJS.changeEmail) {

            myInfoJS.changeEmail = true;
            myInfoJS.checkEmail = false;
            myInfoJS.emailChangeBtn.text("중복 체크");
            myInfoJS.emailInput.prop('disabled', false).prop('readonly', false);

        } else {

            if( myInfoJS.emailInput.val() === myInfoJS.originEmailVal ) {
                Util.confirm("기존 이메일 정보와 동일합니다. <br> 그대로 사용하시겠습니까?").then((isOk) => {
                    if(isOk) {
                        myInfoJS.checkEmail = true;
                        myInfoJS.emailChangeBtn.prop('disabled', true);
                        myInfoJS.emailInput.prop('disabled', true).prop('readonly', true);
                    }
                })
            } else {

                const userEmail = myInfoJS.emailInput.val().trim();
                if(userEmail.length === 0) {
                    Util.alert("이메일 값을 입력해주세요.");
                    return;
                }

                Http.get("/cetus/api/user/checkEmail.json", { userEmail: userEmail }).then(res => {
                    Util.alert(res.message);
                    if (res.data === 0) {
                        myInfoJS.checkEmail = true;
                        myInfoJS.emailInput.prop('disabled', true);
                        myInfoJS.emailChangeBtn.prop('disabled', true);
                    } else {
                        myInfoJS.checkEmail = false;
                    }
                }).catch(() => {
                    Util.alert("이메일 확인 중 오류가 발생했습니다.");
                });
            }
        }
    }

    ,saveForm: async function () {

        if (myInfoJS.changeEmail && !myInfoJS.checkEmail) {
            Util.alert("이메일 중복 체크를 진행해주세요.")
            return;
        }
        
        if(myInfoJS.passwordChange && (myInfoJS.passwordInput.val().length === 0 || myInfoJS.passwordChkInput.val().length === 0)) {
            Util.alert("비밀번호 및 비밀번호 확인을 입력해주세요.");
            return;
        }

        // 1. 전체 form 정보
        let obj = await FormDataToObj.getParameterWithOutBlank("update-form");

        // 2. 기본 form key 값이랑 obj 분리
        const {included, excluded} = myInfoJS.separateObjs(obj)
        included['userEmail'] = myInfoJS.emailInput.val();
        builder.getFileObjs(excluded);

        // 3. {form-builder} validation check
        const isValid = builder.validateFormBuilder("update-form", excluded);
        if(isValid) {

            // 4. json-string 형태로 변경
            const metaData = JSON.stringify(excluded)

            // 5. 저장(수정)
            const obj2 = { ...included, metaData }

            Http.put(`/cetus/api/user/my-info/${uid}`, obj2, true).then((res) => {
                Util.alert("내 정보가 수정되었습니다.").then(() => {
                    window.location.reload();
                })
            }).fail((e) => {
                if (e.status === 400) {
                    if (e.hasOwnProperty('responseJSON')) {
                        const response = e['responseJSON'];
                        ValidationError.setValidationMssg('update-form', response)
                    }
                }
            });
        }
    }

    ,separateObjs: function (obj) {
        const included = {};
        const excluded = {};
        Object.entries(obj).forEach(([key, value]) => {
            if (myInfoJS.objKeys.includes(key)) {
                included[key] = value;
            } else {
                excluded[key] = value;
            }
        });
        return {included, excluded}
    }
}