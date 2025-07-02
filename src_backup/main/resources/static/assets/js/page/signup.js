var signup = {
    userIdChk: false,
    userEmailChk: false,
    objKeys: ["userNm",
                "userId",
                "userEmail",
                "password",
                "approveAt",
                "authorCd",
                "status"],

    checkId: function () {
        const userId = $("#userId").val().trim();
        if (userId === "") {
            Util.alert("아이디를 입력해주세요.");
            return;
        }

        Http.get("/cetus/api/user/checkId.json", { userId: userId }).then(res => {
            Util.alert(res.message);
            if (res.data === 0) {
                signup.userIdChk = true;
                $("#userId").attr("readonly", true);
                $("#userId-chk").attr("disabled", true);
            } else {
                signup.userIdChk = false;
            }
        }).catch(() => {
            Util.alert("아이디 확인 중 오류가 발생했습니다.");
        });
    },

    checkEmail: function () {
        const userEmail = $("#userEmail").val().trim();
        if (userEmail === "") {
            Util.alert("이메일을 입력해주세요.");
            return;
        }

        Http.get("/cetus/api/user/checkEmail.json", { userEmail: userEmail }).then(res => {
            Util.alert(res.message);
            if (res.data === 0) {
                signup.userEmailChk = true;
                $("#userEmail").attr("readonly", true);
                $("#userEmail-chk").attr("disabled", true);
            } else {
                signup.userEmailChk = false;
            }
        }).catch(() => {
            Util.alert("이메일 확인 중 오류가 발생했습니다.");
        });
    },

    save: async function () {

        const password = $("#password").val();
        const confirmPassword = $("#confirmPassword").val();
        if (password !== confirmPassword) {
            Util.alert("입력하신 비밀번호가 다릅니다.");
            return;
        }
        if (!signup.userIdChk) {
            Util.alert("아이디 중복 확인을 해주세요.");
            return;
        }
        if (!signup.userEmailChk) {
            Util.alert("이메일 중복 확인을 해주세요.");
            return;
        }

        const obj = await FormDataToObj.getParameterWithOutBlank("save-form");
        const { included, excluded } = signup.separateObjs(obj);

        builder.getFileObjs(excluded);
        const isValid = builder.validateFormBuilder("save-form", excluded);
        if (!isValid) return;

        const metaData = JSON.stringify(excluded);
        const obj2 = { ...included, metaData };

        const apiUrl = "/cetus/api/user";
        const url = "/asp/home";
        const message = "회원가입이 완료되었습니다.";

        console.log("obj2<<<", obj2)

        Http.post(apiUrl, obj2, true).then(() => {
            Util.alert(message).then(() => {
                location.href = url;
            });
        }).fail((e) => {
            if (e.status === 400 && e.hasOwnProperty("responseJSON")) {
                const response = e.responseJSON;
                ValidationError.setValidationMssg("save-form", response);
            }
        });
    },

    goBack: function () {
        history.back();
    },
    separateObjs: function (obj) {
        const included = {};
        const excluded = {};
        Object.entries(obj).forEach(([key, value]) => {
            if (signup.objKeys.includes(key)) {
                included[key] = value;
            } else {
                excluded[key] = value;
            }
        });
        return {included, excluded};
    }
};