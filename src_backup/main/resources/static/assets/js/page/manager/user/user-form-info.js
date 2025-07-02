var userFormInfo = {

    depthChangeTree: new Tree("depth-change-tree", false, false)
    ,objKeys: ["userNm", "userEmail", "userGroup", "userDept", "userPosition", "userAuthor", "userStatus", "changeReason"]

    ,changeEmail: false
    ,checkEmail: true
    ,originEmailVal: $("#userEmail-origin").val()
    ,emailChangeBtn: $("#userEmail-change")
    ,emailInput: $("#userEmail")

    ,userEmailChangeBtnClick: function () {

        /* == 이메일 변경 -> 이메일 중복 체크 == */
        if(!userFormInfo.changeEmail) {

            userFormInfo.changeEmail = true;
            userFormInfo.checkEmail = false;
            userFormInfo.emailChangeBtn.text("중복 체크");
            userFormInfo.emailInput.prop('disabled', false).prop('readonly', false);

        } else {

            if( userFormInfo.emailInput.val() === userFormInfo.originEmailVal ) {
                Util.confirm("기존 이메일 정보와 동일합니다. <br> 그대로 사용하시겠습니까?").then((isOk) => {
                    if(isOk) {
                        userFormInfo.checkEmail = true;
                        userFormInfo.emailChangeBtn.prop('disabled', true);
                        userFormInfo.emailInput.prop('disabled', true).prop('readonly', true);
                    }
                })
            } else {

                const userEmail = userFormInfo.emailInput.val().trim();
                if(userEmail.length === 0) {
                    Util.alert("이메일 값을 입력해주세요.");
                    return;
                }

                Http.get("/cetus/api/user/checkEmail.json", { userEmail: userEmail }).then(res => {
                    Util.alert(res.message);
                    if (res.data === 0) {
                        userFormInfo.checkEmail = true;
                        userFormInfo.emailInput.prop('disabled', true);
                        userFormInfo.emailChangeBtn.prop('disabled', true);
                    } else {
                        userFormInfo.checkEmail = false;
                    }
                }).catch(() => {
                    Util.alert("이메일 확인 중 오류가 발생했습니다.");
                });
            }

        }

    }

    ,userDeptChange: function (deptUid) {
        userFormInfo.drawUserDeptModal(deptUid).then(() => {
            $("#userDeptChangeModal").modal('show');
            if(deptUid.length === 0) {
                $("#change-dept-select-btn").attr('disabled', true);
            }
        })
    }
    ,drawUserDeptModal: function (deptUid) {
        return new Promise((resolve) => {
            userFormInfo.depthChangeTree
                .destroyTree()
                .onlySelectLeaf(() => {
                    if(userFormInfo.depthChangeTree.getSelectedData().length === 0) {
                        $("#change-dept-select-btn").attr('disabled', true);
                    }
                })
                .customTreeSelectEvent((e, d) => {
                    $("#change-dept-select-btn").attr('disabled', false);
                })
                .initNode( [ deptUid ] )
                .drawTree("/cetus/api/dept/tree", {});
            resolve(); // 작업 완료 시 resolve
        });
    }
    ,chooseDept: function () {
        const datas = userFormInfo.depthChangeTree.getSelectedData();
        const obj = userFormInfo.depthChangeTree.getSelectDataInfo(datas[0])
        $("#userDept").val(obj['data']['text'])
        $("#userDept-uid").val(datas[0])
        $("#userDeptChangeModal").modal('hide');
    }
    ,cancelChooseDept: function () {
        $("#userDeptChangeModal").modal('hide');
    }

    ,submitForm: async function () {

        const previousStatus = $('#status-before').val();
        const currentStatus = $('#userStatus').val();
        const changeReason = $("#changeReason").val()
        if( ( previousStatus !== currentStatus ) && ( changeReason.length === 0) )  {
            Util.alert("사용자의 상태값을 변경하시는 경우, <br> 해당 사유 입력은 필수입니다.")
            return false;
        }

        if( userFormInfo.changeEmail && !userFormInfo.checkEmail ) {
            Util.alert("이메일 중복 체크를 진행해주세요.")
            return;
        }

        // 1. 전체 form 정보
        let obj = await FormDataToObj.getParameterWithOutBlank("user-info-form");
        if( previousStatus !== currentStatus ) {

            obj['userStatus'] = currentStatus
            obj['changeReason'] = changeReason

        } else delete obj['userStatus']

        if( obj.hasOwnProperty("files[]") ) delete obj["files[]"]

        // 2. 기본 form key 값이랑 obj 분리
        const {included, excluded} = userFormInfo.separateObjs(obj)
        included['userEmail'] = userFormInfo.emailInput.val();
        builder.getFileObjs(excluded);

        // 3. {form-builder} validation check
        const isValid = builder.validateFormBuilder("user-info-form", excluded);
        if( isValid ) {

            // 4. json-string 형태로 변경
            const metaData = JSON.stringify(excluded)

            // 5. 저장(수정)
            const obj2 = { ...included, metaData }
            Http.put(`/cetus/api/user/${uid}`, obj2, true).then((res) => {
                Util.alert("사용자 정보가 수정되었습니다.").then(() => {
                    window.location.href = `/manager/cetus/user/${uid}`
                })
            }).fail((e) => {
                if (e.status === 400) {
                    if (e.hasOwnProperty('responseJSON')) {
                        const response = e['responseJSON'];
                        ValidationError.setValidationMssg('user-info-form', response)
                    }
                }
            });
        }

    }

    ,cancelForm: function () {
        Util.confirm("수정중이신 정보는 저장되지 않습니다. <br> 작성중이신 정보를 취소하시겠습니까?").then((isOk) => {
            if(isOk) {
                window.location.href = `/manager/cetus/user/${uid}`
            }
        })
    }

    ,separateObjs: function (obj) {
        const included = {};
        const excluded = {};
        Object.entries(obj).forEach(([key, value]) => {
            if (userFormInfo.objKeys.includes(key)) {
                included[key] = value;
            } else {
                excluded[key] = value;
            }
        });
        return {included, excluded}
    }


}