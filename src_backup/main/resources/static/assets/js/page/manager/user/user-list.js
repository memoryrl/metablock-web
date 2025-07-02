var userList = {

    table: null,
    depthTree: new Tree("depth-tree", false),
    depthChangeTree: new Tree("depth-change-tree", false, false),
    totalElements: 0,
    checkedRow: 0,
    selectUser: [],

    /* == 좌측 > 필터 > 그룹/부서 > 트리 초기화 == */
    initTree: function () {
        userList.depthTree.drawTree("/cetus/api/dept/tree", {})
        userList.depthTree.customTreeSelectEvent((e, d) => {
            userList.submitTable();
        });
        userList.depthTree.customTreeDeSelectEvent((e, d) => {
            userList.submitTable();
        });
    }


    /* == 우측 > 사용자 목록 테이블 > 초기화 == */
    ,initTable: function () {
        userList.table = new Table("user-table")
                                .get("/cetus/api/user")
                                .delUnUseParams()
                                .switchDiv()
                                .customTable(["deptUid", "status", "role"])
                                .checkbox('uid', '5%')
                                .add(new Column("uid").render((data, full) => full.listIndex))
                                .add(new Column("userNm").render((data, full) => {
                                    const fileImg = (full['profileId']) ? `/cetus/files/download?fileId=${full['profileId']}` : '/assets/images/page/detail/profile_noimage.png'
                                    const userInfo = `${full.userNm}(${full.userId})`
                                    return `
                                            <div class="user-info-wrapper">
                                                <img src="${fileImg}" alt="프로필" class="user-profile-img" onerror="this.src='/assets/images/page/detail/profile_noimage.png'">
                                                <div class="user-text-info">
                                                    <span>${userInfo}</span>
                                                    <span style="color: lightgray">${full.userEmail}</span>
                                                </div>
                                            </div>`;
                                }))
                                .add(new Column("groupNm"))
                                .add(new Column("positionNm"))
                                .add(new Column("deptNm"))
                                .add(new Column("statusNm").render((data, full) => {
                                    const color = (full['status'] === "APPROVED") ? "green" : (full['status'] === "WAIT") ? "blue" : "red"
                                    return `<span style="color: ${color}">${data}</span>`
                                }))
                                .add(new Column("regDt"))
                                .add(new Column("roleNm"))
                                .selectable()
                                .select(function (data) {
                                    window.location.href = `/manager/cetus/user/${data.uid}`;
                                })
                                .init()
                                .afterInit((res) => {
                                    $("#user-table tbody").on("click", ".lc-check", userList.checkEvent);
                                    $("#user-table thead .lc-check-all").on("click", userList.checkAllEvent);
                                    userList.totalElements = userList.table._data.list.length
                                    userList.changeText();
                                })
                                .afterSubmit(() => {
                                    userList.totalElements = userList.table._data.list.length
                                    userList.checkedRow = $('input[type="checkbox"].lc-check:checked').length;
                                    if ($(".lc-check-all").prop("checked")) $(".lc-check-all").prop("checked", false);
                                    userList.changeText()
                                })
    }


    /* == 우측 > 사용자 목록 테이블 > submit == */
    ,submitTable: function () {
        const obj = userList.getFormObj();
        userList.table.submit(obj);
    }

    ,getFormObj: function () {
        return obj = {
            userId: $('#userId-input').val(),
            userNm: $('#userNm-input').val(),
            userGroup: $('#userGroup').val(),                    // 배열
            userPosition: $('#userPosition').val(),              // 배열
            userStatus: $('#userStatus').val(),                  // 배열
            userAuthorCd: $('#userAuthorCd').val(),              // 배열
            userDepth: userList.depthTree.getSelectedData(),     // 배열
            startDate: $('#startDate').val(),
            endDate: $('#endDate').val()
        };
    }


    /* == 좌측 > 필터 > 초기화 == */
    ,formReset: function () {
        $("#userId-input").val('');
        $("#userNm-input").val('');
        $('#userGroup').val(null).trigger('change');
        $('#userPosition').val(null).trigger('change');
        $('#userStatus').val(null).trigger('change');
        $('#userAuthorCd').val(null).trigger('change');
        $("#startDate").val('');
        $("#endDate").val('');
        userList.depthTree.deSelectAllTree();
        userList.submitTable();
    }


    /* == 우측 > 상단 > 텍스트 세팅 == */
    ,changeText: function () {
        $("#total-num").html(`${userList.totalElements}건 중 ${userList.checkedRow} 건 선택`)
    }


    /* == 우측 > 테이블 > 체크 행 가져오기 > UID 리스트 == */
    ,getCheckRowLst: function() {
        let lst = []
        const chk = $('input[type="checkbox"].lc-check:checked')
        chk.toArray().forEach(checkbox => lst.push($(checkbox).data("num")))
        return lst
    }


    /* == 우측 > 테이블 > 체크 행 정보 가져오기 > {obj 리스트} == */
    ,getCheckRowInfo: function () {
        const lst = [];
        $('input[type="checkbox"].lc-check:checked').each(function () {
            const $checkbox = $(this);
            const dataObj = {};
            $.each(this.dataset, (key, value) => {
                dataObj[key] = value;
            });
            lst.push(dataObj);
        });
        return lst;
    }


    /* == 우측 > 테이블 > 체크박스 클릭 이벤트 == */
    ,checkEvent: function (event) {
        event.stopPropagation()
        const checkbox = $(this);
        checkbox.attr("checkbox", !checkbox.is(":checked"))
        userList.checkedRow = $('input[type="checkbox"].lc-check:checked').length;
        userList.changeText()

        if(userList.getCheckRowLst().length === userList.totalElements) $(".lc-check-all").prop("checked", true)
        else $(".lc-check-all").prop("checked", false);
    }


    /* == 우측 > 테이블 > 전체 체크박스 클릭 이벤트 == */
    ,checkAllEvent: function (event) {
        event.stopPropagation();
        const isChecked = $(this).is(":checked");
        $('input[type="checkbox"].lc-check').prop('checked', isChecked);
        userList.checkedRow = isChecked ? $('input[type="checkbox"].lc-check:checked').length : 0;
        userList.changeText()
    }



    /* == 우측 > 상단 > [그룹/부서 변경] 버튼 클릭 이벤트 == */
    ,userDeptChange: function () {
        if(userList.getCheckRowLst().length === 0) Util.alert("그룹/부서를 변경할 사용자를 선택해주세요.")
        else {
            userList.selectUser = userList.getCheckRowLst();
            const checkedUser = userList.getCheckRowInfo()
            userList.drawUserDeptModal(checkedUser).then(() => {
                $("#userDeptModal").modal('show');
                if(checkedUser.length === 1 && checkedUser[0]['deptuid'].length === 0) {
                    $("#dept-select-btn").attr('disabled', true);
                }
            })
        }
    }
    /* == 우측 > 상단 > [그룹/부서 변경] 버튼 클릭 > 팝업창 그리기 == */
    ,drawUserDeptModal: function (checkedUser) {
        return new Promise((resolve) => {
            userList.depthChangeTree
                .destroyTree()
                .onlySelectLeaf(() => {
                    if(userList.depthChangeTree.getSelectedData().length === 0) {
                        $("#dept-select-btn").attr('disabled', true);
                    }
                })
                .customTreeSelectEvent((e, d) => {
                    $("#dept-select-btn").attr('disabled', false);
                })
                .initNode( checkedUser.length === 1 ? [checkedUser[0]['deptuid']] : [] )
                .drawTree("/cetus/api/dept/tree", {});

            if(checkedUser.length !== 1) {
                $("#dept-select-btn").attr('disabled', true);
            }

            resolve(); // 작업 완료 시 resolve
        });
    }
    /* == 우측 > 상단 > [그룹/부서 변경] 버튼 클릭 > 팝업창 초기화 == */
    ,resetDeptModal: function () {
        userList.submitTable();
        userList.selectUser = [];
        $("#dept-select-btn").attr('disabled', false);
        $('#userDeptModal').modal('hide');
    }
    /* == 우측 > 상단 > [그룹/부서 변경] 버튼 클릭 > [적용] 버튼 클릭 == */
    ,changeUserDept: function () {
        const users = userList.selectUser
        const depts = userList.depthChangeTree.getSelectedData()
        if(depts.length === 0) Util.alert("변경할 그룹/부서를 선택해주세요.")
        else {
            const obj = {
                code  : "DEPT",
                users : users,
                uid   : depts[0]
            }
            Http.put('/cetus/api/user/change-info', obj, true).then(() => {
                Util.alert("사용자의 그룹/부서가 변경되었습니다.").then(() => {
                    userList.resetDeptModal();
                })
            })
        }
    }



    /* == 우측 > 상단 > [상태 변경] 버튼 클릭 이벤트 == */
    ,userStatusChange: function () {
        if(userList.getCheckRowLst().length === 0) Util.alert("상태를 변경할 사용자를 선택해주세요.")
        else {
            userList.selectUser = userList.getCheckRowLst();
            const checkedUser = userList.getCheckRowInfo()
            userList.drawUserStatusModal(checkedUser).then(() => {
                $("#userStatusModal").modal('show')
            });
        }
    }
    /* == 우측 > 상단 > [상태 변경] 버튼 클릭 > 팝업창 그리기 == */
    ,drawUserStatusModal: function (checkedUser) {
        return new Promise((resolve) => {
            const $form = $('#userStatus-form');
            $form.empty();
            const isSingleUser = checkedUser.length === 1;
            userStatus.forEach((status, index) => {
                const id = `user-status-${index}`;
                const html = `
                    <div class="form-check">
                        <input type="radio" id="${id}" name="userStatus-change" class="form-check-input" value="${status.code}" 
                                    ${isSingleUser && status.code === checkedUser[0].status ? 'checked' : ''} required>
                        <label class="form-check-label" for="${id}">${status.name}</label>
                    </div>
                    `;
                $form.append(html);
            });
            if(!isSingleUser) {
                $("#status-select-btn").attr('disabled', true);
            }
            resolve(); // 작업 완료 시 resolve
        });
    }
    /* == 우측 > 상단 > [상태 변경] 버튼 클릭 > 팝업창 초기화 == */
    ,resetStatusModal: function () {
        userList.submitTable();
        userList.selectUser = [];
        $("#status-select-btn").attr('disabled', false);
        $('#userStatusModal').modal('hide');
    }
    /* == 우측 > 상단 > [상태 변경] 버튼 클릭 > [적용] 버튼 클릭 == */
    ,changeUserStatus: function () {
        const users = userList.selectUser;
        const selected = $('#userStatus-form input[name="userStatus-change"]:checked').val();

        const obj = {
            code  : "STATUS",
            users : users,
            value : selected
        }
        Http.put('/cetus/api/user/change-info', obj, true).then(() => {
            Util.alert("사용자의 상태 정보가 변경되었습니다.").then(() => {
                userList.resetStatusModal();
            })
        });
    }



    /* == 우측 > 상단 > [권한 변경] 버튼 클릭 이벤트 == */
    ,userAuthorChange: function () {
        if(userList.getCheckRowLst().length === 0) Util.alert("권한을 변경할 사용자를 선택해주세요.")
        else {
            userList.selectUser = userList.getCheckRowLst();
            const checkedUser = userList.getCheckRowInfo()
            userList.drawUserAuthorModal(checkedUser).then(() => {
                $("#userAuthorModal").modal('show')
            });
        }
    }
    /* == 우측 > 상단 > [권한 변경] 버튼 클릭 > 팝업창 그리기 == */
    ,drawUserAuthorModal: function (checkedUser) {
        return new Promise((resolve) => {
            const $form = $('#userAuthor-form');
            $form.empty();
            const isSingleUser = checkedUser.length === 1;
            userAuthorCd.forEach((status, index) => {
                const id = `user-author-${index}`;
                const html = `
                    <div class="form-check">
                        <input type="radio" id="${id}" name="userAuthor-change" class="form-check-input" value="${status.code}" 
                                    ${isSingleUser && status.code === checkedUser[0].role ? 'checked' : ''} required>
                        <label class="form-check-label" for="${id}">${status.name}</label>
                    </div>
                    `;
                $form.append(html);
            });
            if(!isSingleUser) {
                $("#author-select-btn").attr('disabled', true);
            }
            resolve(); // 작업 완료 시 resolve
        });
    }
    /* == 우측 > 상단 > [권한 변경] 버튼 클릭 > 팝업창 초기화 == */
    ,resetAuthorModal: function () {
        userList.submitTable();
        userList.selectUser = [];
        $("#author-select-btn").attr('disabled', false);
        $('#userAuthorModal').modal('hide');
    }
    /* == 우측 > 상단 > [권한 변경] 버튼 클릭 > [적용] 버튼 클릭 == */
    ,changeUserAuthor: function () {
        const users = userList.selectUser;
        const selected = $('#userAuthor-form input[name="userAuthor-change"]:checked').val();

        const obj = {
            code  : "AUTHOR",
            users : users,
            value : selected
        }
        Http.put('/cetus/api/user/change-info', obj, true).then(() => {
            Util.alert("사용자의 권한 정보가 변경되었습니다.").then(() => {
                userList.resetAuthorModal();
            })
        });
    }


    /* == 우측 > 상단 > [리스트 다운로드] 버튼 클릭 이벤트 == */
    ,userListDownload: function () {
        $("#download-list").attr('disabled', true);
        const obj = userList.getFormObj();
        CREATE_USER_CSV('/cetus/api/user/excel', obj)
    }


    /* == 우측 > 상단 > [초대하기] 버튼 클릭 이벤트 == */
    ,inviteUser: function () {
        $("#userInviteModal").modal('show')
    }

    /* == 우측 > 상단 > [초대하기] 버튼 클릭 > [적용] 버튼 클릭 == */
    ,inviteSend: function () {
        const email = document.getElementById("invite-email").value.trim();
        const $loading = document.getElementById("invite-loading");
        const $btn = document.getElementById("invite-select-btn");

        if (!email) {
            Util.alert("이메일을 입력해주세요.");
            return;
        }

        $btn.disabled = true;

        Http.get("/cetus/api/user/checkEmail.json", { userEmail: email }).then(res => {
            const { data, message } = res;

            if (data === 0) {
                $loading.style.display = "flex";

                Http.post("/cetus/api/invite", { email: email }, true).then(() => {
                    Util.alert("초대 메일이 전송되었습니다.").then(() => {
                        $('#userInviteModal').modal('hide');
                        document.getElementById("invite-email").value = "";
                    });
                }).catch(() => {
                    Util.alert("초대 메일 전송에 실패했습니다.");
                }).then(() => {
                    $loading.style.display = "none";
                    $btn.disabled = false;
                });

            } else {
                Util.alert(message);
                $btn.disabled = false;
            }

        }).catch(() => {
            Util.alert("이메일 확인 중 오류가 발생했습니다.");
            $btn.disabled = false;
        });
    }
}