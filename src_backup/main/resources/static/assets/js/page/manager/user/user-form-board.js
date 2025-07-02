var userFormBoard = {

     bbscttTabTable : null
    ,answerTabTable : null
    ,bbsctt: {
        id: "bbsctt-hist-table",
        totalElements: 0,
        checkedRow: 0
    }
    ,answer: {
        id: "answer-hist-table",
        totalElements: 0,
        checkedRow: 0
    }
    ,selectedBbsctt: []

    ,initTab: function () {
         return new Promise((resolve) => {
             userFormBoard.setMainTab().then(() => {
                 userFormBoard.setSubTab();
             });
             resolve();
         })
    }

    ,setMainTab: function () {
        const savedTab1 = sessionStorage.getItem("bbsctt-tab");
         return new Promise((resolve) => {
             if (savedTab1) {
                 $('.tab-pane-custom').removeClass('show active');
                 $(savedTab1).addClass('show active');
                 $('.tab-header-custom-board a').removeClass('active');
                 $(`.tab-header-custom-board a[data-target="${savedTab1}"]`).addClass('active');
             } else {
                 $('.tab-header-custom-board a').first().addClass('active');
                 $('.tab-pane-custom').first().addClass('show active');
                 const target = $('.tab-header-custom-board a').first().data('target');
                 sessionStorage.setItem("bbsctt-tab", target);
                 sessionStorage.removeItem("bbsctt-tab-header")
             }
             resolve();
         })
    }

    ,setSubTab: function () {
        return new Promise((resolve) => {
            const savedTab1 = sessionStorage.getItem("bbsctt-tab");
            const savedTab2 = sessionStorage.getItem("bbsctt-tab-header")
            if(savedTab2) {
                if(savedTab1 === "#bbsctt-hist") {
                    const $tabLinks = $(".bbsctt-header a");
                    $tabLinks.removeClass("active");
                    const $targetTab = $(`.bbsctt-header a[data-target="${savedTab2}"]`);
                    $targetTab.addClass("active");
                    const uid = $targetTab.data("uid");
                    $("#bbsctt-hist-tab").val(uid);
                } else if(savedTab1 === "#answer-hist") {
                    const $tabLinks = $(".answer-header a");
                    $tabLinks.removeClass("active");
                    $(".answer-header a").removeClass("active");
                    const $targetTab = $(`.answer-header a[data-target="${savedTab2}"]`);
                    $targetTab.addClass("active");
                    const uid = $targetTab.data("uid");
                    $("#answer-hist-tab").val(uid);
                }
            } else {
                if (savedTab1 === "#bbsctt-hist") {
                    const $tabLinks = $(".bbsctt-header a");
                    $tabLinks.removeClass("active");
                    const $first = $(".bbsctt-header a").first();
                    if ($first.length) {
                        $first.addClass("active");
                        sessionStorage.setItem("bbsctt-tab-header", $first.data("target"));
                        const uid = $first.data("uid");
                        $("#bbsctt-hist-tab").val(uid);
                    }
                } else if (savedTab1 === "#answer-hist") {
                    const $tabLinks = $(".answer-header a");
                    $tabLinks.removeClass("active");
                    const $first = $(".answer-header a").first();
                    if ($first.length) {
                        $first.addClass("active");
                        sessionStorage.setItem("bbsctt-tab-header", $first.data("target"));
                        const uid = $first.data("uid");
                        $("#answer-hist-tab").val(uid);
                    }
                }
            }
            resolve();
        })
    }

    ,initTable: function () {
        userFormBoard.bbscttTabTable = new Table(userFormBoard.bbsctt.id)
            .pageOptions([5, 10, 15], 5)
            .get("/cetus/api/bbsctt")
            .param("bbsUid", () => $("#bbsctt-hist-tab").val())
            .param("regUid", () => userUid)
            .delUnUseParams()
            .switchDiv()
            .checkbox('bbscttUid', '5%')
            .add(new Column("bbscttUid").center().render((data, full) => full.listIndex))
            .add(new Column("bbscttNm").left().render((data, full) => {
                return `<span class="hover-custom">${data}</span>`
            }))
            .add(new Column("openAt").center().render((data, full) => {
                const color = (data === "Y") ? "green" : "red"
                const text = (data === "Y") ? "공개" : "비공개"
                return `<span style="color: ${color}">${text}</span>`
            }))
            .selectable()
            .select((data) => {
                window.location.href = `/asp/cetus/bbsctt/view/${data['bbscttUid']}`
            })
            .init()
            .afterInit(() => {
                $(`#${userFormBoard.bbsctt.id} tbody`).on("click", ".lc-check", function (e) {
                    userFormBoard.checkEvent(e, true);
                });
                $(`#${userFormBoard.bbsctt.id} thead`).on("click", ".lc-check-all", function (e) {
                    userFormBoard.checkAllEvent(e, true);
                });
                userFormBoard.bbsctt.totalElements = userFormBoard.bbscttTabTable._data.list.length;
            })
            .afterSubmit(() => {
                userFormBoard.bbsctt.totalElements = userFormBoard.bbscttTabTable._data.list.length;
                userFormBoard.bbsctt.checkedRow = $(`#${userFormBoard.bbsctt.id} input[type="checkbox"].lc-check:checked`).length;
                if ($(`#${userFormBoard.bbsctt.id} .lc-check-all`).prop("checked")) $(`#${userFormBoard.bbsctt.id} .lc-check-all`).prop("checked", false);
                userFormBoard.changeTextBbsctt();
            });


        userFormBoard.answerTabTable = new Table(userFormBoard.answer.id)
            .pageOptions([5, 10, 15], 5)
            .get("/cetus/api/answer")
            .param("bbsUid", () => $("#answer-hist-tab").val())
            .param("regUid", () => userUid)
            .delUnUseParams()
            .switchDiv()
            .checkbox('answerUid', '5%')
            .add(new Column("answerUid").center().render((data, full) => full.listIndex))
            .add(new Column("bbscttNm").left().render((data, full) => {
                return `<span class="hover-custom">${data}</span>`
            }))
            .add(new Column("answerCnt").left().render((data, full) => {
                return `<span class="hover-custom">${Editor.getFirstParagraphText(data)} ...</span>`
            }))
            .init()
            .selectable()
            .select((data) => {
                window.location.href = `/asp/cetus/bbsctt/view/${data['bbscttUid']}`
            })
            .afterInit(() => {
                $(`#${userFormBoard.answer.id} tbody`).on("click", ".lc-check", function (e) {
                    userFormBoard.checkEvent(e, false);
                });
                $(`#${userFormBoard.answer.id} thead`).on("click", ".lc-check-all", function (e) {
                    userFormBoard.checkAllEvent(e, false);
                });
                userFormBoard.answer.totalElements = userFormBoard.answerTabTable._data.list.length;
            })
            .afterSubmit(() => {
                userFormBoard.answer.totalElements = userFormBoard.answerTabTable._data.list.length;
                userFormBoard.answer.checkedRow = $(`#${userFormBoard.answer.id} input[type="checkbox"].lc-check:checked`).length;
                if ($(`#${userFormBoard.answer.id} .lc-check-all`).prop("checked")) $(`#${userFormBoard.answer.id} .lc-check-all`).prop("checked", false);
                userFormBoard.changeTextAnswer();
            });
    }

    ,changeTextBbsctt: function () {
        $("#bbsctt-hist-total-num").html(`${userFormBoard.bbsctt.totalElements}건 중 ${userFormBoard.bbsctt.checkedRow} 건 선택`)
        const isDisable = (userFormBoard.bbsctt.totalElements === 0)
        $("#bbsctt-hist-status-change").prop('disabled', isDisable);
        $("#bbsctt-hist-list-download").prop('disabled', isDisable);
    }

    ,changeTextAnswer: function () {
        $("#answer-hist-total-num").html(`${userFormBoard.answer.totalElements}건 중 ${userFormBoard.answer.checkedRow} 건 선택`)
        const isDisable = (userFormBoard.answer.totalElements === 0)
        $("#answer-hist-delete-row").prop('disabled', isDisable);
        $("#answer-hist-list-download").prop('disabled', isDisable);
    }

    ,getCheckRowLst: function(isBbsctt = true) {
        let lst = []
        const tableId = (isBbsctt) ? userFormBoard.bbsctt.id : userFormBoard.answer.id
        const chk = $(`#${tableId} input[type="checkbox"].lc-check:checked`)
        chk.toArray().forEach(checkbox => lst.push($(checkbox).data("num")))
        return lst
    }

    ,checkEvent: function (event, isBbsctt = true) {
        event.stopPropagation()
        const checkbox = $(this);
        checkbox.attr("checkbox", !checkbox.is(":checked"))
        const tableId = (isBbsctt) ? userFormBoard.bbsctt.id : userFormBoard.answer.id

        if(isBbsctt) {

            userFormBoard.bbsctt.checkedRow = $(`#${tableId} input[type="checkbox"].lc-check:checked`).length;
            userFormBoard.changeTextBbsctt();
            if(userFormBoard.getCheckRowLst(isBbsctt).length === userFormBoard.bbsctt.totalElements) $(`#${tableId} .lc-check-all`).prop("checked", true)
            else $(`#${tableId} .lc-check-all`).prop("checked", false);

        } else {

            userFormBoard.answer.checkedRow = $(`#${tableId} input[type="checkbox"].lc-check:checked`).length;
            userFormBoard.changeTextAnswer();
            if(userFormBoard.getCheckRowLst(isBbsctt).length === userFormBoard.answer.totalElements) $(`#${tableId} .lc-check-all`).prop("checked", true)
            else $(`#${tableId} .lc-check-all`).prop("checked", false);

        }
    }

    ,checkAllEvent: function (event, isBbsctt = true) {
        event.stopPropagation();
        const isChecked = $(event.currentTarget).is(":checked");
        const tableId = (isBbsctt) ? userFormBoard.bbsctt.id : userFormBoard.answer.id
        $(`#${tableId} input[type="checkbox"].lc-check`).prop('checked', isChecked);
        if(isBbsctt) {
            userFormBoard.bbsctt.checkedRow = isChecked ? $(`#${tableId} input[type="checkbox"].lc-check:checked`).length : 0;
            userFormBoard.changeTextBbsctt();
        } else {``
            userFormBoard.answer.checkedRow = isChecked ? $(`#${tableId} input[type="checkbox"].lc-check:checked`).length : 0;
            userFormBoard.changeTextAnswer();
        }
    }

    ,submitBbscttTable: function () {
        userFormBoard.bbscttTabTable.submit();
    }

    ,submitAnswerTable: function () {
        userFormBoard.answerTabTable.submit();
    }

    ,bbscttOpenAtChange: async function (changeVal = '') {
        const uids = userFormBoard.getCheckRowLst(true);
        if (uids.length === 0) Util.alert("상태값을 변경할 행을 선택해주세요.")
        else {
            userFormBoard.popupCustom1();
            let result = await popup.openTextPopup();
            if(result) {
                const obj = {openAt: changeVal, uids}
                Http.put('/cetus/api/bbsctt/change-openAt', obj, true).then(() => {
                    Util.alert("게시물 정보가 수정되었습니다.").then(() => {
                        window.location.href = `/manager/cetus/user/${uid}`
                    })
                })
            }
        }
    }

    ,deleteBbsctt: async function () {
        const uids = userFormBoard.getCheckRowLst(true);
        if (uids.length === 0) Util.alert("삭제할 행을 선택해주세요.")
        else {
            userFormBoard.popupCustom2(true)
            let result = await popup.openTextPopup();
            if (result) {
                const obj = {uids}
                Http.put('/cetus/api/bbsctt/delete-several', obj, true).then(() => {
                    Util.alert("게시물이 삭제되었습니다.").then(() => {
                        window.location.href = `/manager/cetus/user/${uid}`
                    })
                })
            }
        }
    }

    ,downloadBbscttList: function () {
         $("#bbsctt-hist-list-download").prop('disabled', true);
        const obj = {
            "bbsUid": $("#bbsctt-hist-tab").val(),
            "regUid": userUid
        }
        CREATE_USER_CSV('/cetus/api/bbsctt/excel', obj)
    }

    ,downloadAnswerList: function () {
        $("#answer-hist-list-download").prop('disabled', true);
        const obj = {
            "bbsUid": $("#answer-hist-tab").val(),
            "regUid": userUid
        }
        CREATE_USER_CSV('/cetus/api/answer/excel', obj)
    }

    ,deleteAnswer: async function () {
        const uids = userFormBoard.getCheckRowLst(false);
        if (uids.length === 0) Util.alert("삭제할 행을 선택해주세요.")
        else {
            userFormBoard.popupCustom2(false)
            let result = await popup.openTextPopup();
            if (result) {
                const obj = {uids}
                Http.put('/cetus/api/answer/delete-several', obj, true).then(() => {
                    Util.alert("댓글이 삭제되었습니다.").then(() => {
                        window.location.href = `/manager/cetus/user/${uid}`
                    })
                })
            }
        }
    }

    ,popupCustom1: function () {
         popup.setTextPopupTitle("게시물 공개 여부 수정");
         popup.setTextPopupSpan(`
            <span style="font-size: 17px;">선택한 게시물을 비공개 처리 하시겠습니까?</span>
            <br>
            <span style="color: firebrick">* 추후 다시 공개 처리 할 수 있습니다.</span>
        `);
         popup.setTextPopupLeftBtn('btn-black', '취소', false);
         popup.setTextPopupRightBtn('btn-red', '확인', true);
    }

    ,popupCustom2: function (isBbsctt = true) {
         const text = (isBbsctt) ? "게시물" : "댓글"
         popup.setTextPopupTitle(`${text} 삭제`);
         popup.setTextPopupSpan(`
            <span style="font-size: 17px;">선택한 ${text}들을 삭제 하시겠습니까?</span>
            <br>
            <span style="color: firebrick">* 삭제된 ${text}들은 복구가 불가능합니다.</span>
         `);
         popup.setTextPopupLeftBtn('btn-black', '취소', false);
         popup.setTextPopupRightBtn('btn-red', '확인', true);
    }
}