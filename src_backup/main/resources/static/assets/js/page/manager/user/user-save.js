var userSave = {
    depthChangeTree: new Tree("depth-change-tree", false, false),

    userIdChk: false,
    userEmailChk: false,
    objKeys: ["userNm",
            "userId",
            "userEmail",
            "userGroup",
            "userDept",
            "userPosition",
            "authorCd",
            "password",
            "approveAt",
            "status"]

    ,userDeptChange: function () {
        userSave.drawUserDeptModal().then(() => {
            $("#userDeptChangeModal").modal('show');
        })
    }

    ,drawUserDeptModal: function () {
        return new Promise((resolve) => {
            userSave.depthChangeTree
                .destroyTree()
                .onlySelectLeaf(() => {
                    if(userSave.depthChangeTree.getSelectedData().length === 0) {
                        $("#change-dept-select-btn").attr('disabled', true);
                    }
                })
                .customTreeSelectEvent((e, d) => {
                    $("#change-dept-select-btn").attr('disabled', false);
                })
                .drawTree("/cetus/api/dept/tree", {});
            resolve(); // 작업 완료 시 resolve
        });
    }

    ,chooseDept: function () {
        const datas = userSave.depthChangeTree.getSelectedData();
        const obj = userSave.depthChangeTree.getSelectDataInfo(datas[0])
        console.log(" obj : ", obj)
        $("#userDept").val(obj['data']['text'])
        $("#userDept-uid").val(datas[0])
        $("#userDeptChangeModal").modal('hide');
    }

    ,cancelChooseDept: function () {
        $("#userDeptChangeModal").modal('hide');
    },

    checkId: function () {
        const userId = $("#userId").val().trim();
        if (userId === "") {
            Util.alert("아이디를 입력해주세요.");
            return;
        }

        Http.get("/cetus/api/user/checkId.json", { userId: userId }).then(res => {
            Util.alert(res.message);
            if (res.data === 0) {
                userSave.userIdChk = true;
                $("#userId").attr("readonly", true);
                $("#userId-chk").attr("disabled", true);
            } else {
                userSave.userIdChk = false;
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
                userSave.userEmailChk = true;
                $("#userEmail").attr("readonly", true);
                $("#userEmail-chk").attr("disabled", true);
            } else {
                userSave.userEmailChk = false;
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
        if (!userSave.userIdChk) {
            Util.alert("아이디 중복 확인을 해주세요.");
            return;
        }
        if (!userSave.userEmailChk) {
            Util.alert("이메일 중복 확인을 해주세요.");
            return;
        }

        const obj = await FormDataToObj.getParameterWithOutBlank("save-form");
        const { included, excluded } = userSave.separateObjs(obj);

        builder.getFileObjs(excluded);
        const isValid = builder.validateFormBuilder("save-form", excluded);
        if (!isValid) return;

        const metaData = JSON.stringify(excluded);

        const obj2 = { ...included, metaData };

        const apiUrl ="/cetus/api/user/admin";
        const url ="/manager/cetus/user";
        const message ="계정이 추가되었습니다.";

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
            if (userSave.objKeys.includes(key)) {
                included[key] = value;
            } else {
                excluded[key] = value;
            }
        });
        return {included, excluded};
    }
};