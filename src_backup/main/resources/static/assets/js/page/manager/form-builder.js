var codeJS = {

    formTables: {},
    codeTable: null,
    isAvailable: false,
    nameChk: false,

    createTable: function (formGroup) {
        const containerId = `${formGroup.toLowerCase()}-table`;
        const table = new Table(containerId)
            .get(`/cetus/api/form?formGroup=${formGroup}`)
            .delUnUseParams()
            .hideAll()
            .add(new Column("uid").render((data, full) => {
                return `<input type="radio" class="order-column" name="order" id="order-${data}" data-order="${full.sortNum}" data-uid="${data}">`
            }))
            .add(new Column("sortNum").center())
            .add(new Column("type").center())
            .add(new Column("label").center())
            .add(new Column("name").center())
            .add(new Column("required", "-").center().render((data, full) => {
                return data ? "필수" : "허용"
            }).center())

            .add(new Column("regDt").center())
            .add(new Column("userNm").center())
            .selectable()
            .select(function (data) {
                if ($(event.target).is('input[type="radio"]')) {
                    return; // radio 클릭은 무시
                }
                location.href = `/asp/cetus/form/${data.uid}`
            })
            .init();

        codeJS.formTables[formGroup] = table;
    },
    sortColumns: function (direction) {
        const checkedData = $('input[name="order"]:checked');

        if(checkedData.length === 0) {
            Util.alert("선택된 컬럼이 없습니다. 컬럼을 선택해주세요");
            return;
        }
        const uid = checkedData.data('uid');
        const sortNum = checkedData.data('order');
        const formGroup = $('.description-review-topbar.nav a.active').data('type');

        const data = {uid, sortNum, formGroup, direction };

        Http.put('/cetus/api/form/order', data).then(res => {
            Util.alert("정렬이 완료되었습니다.");
            codeJS.formTables[formGroup].submit();
            setTimeout(() => {
                $(`input[name="order"][data-uid="${uid}"]`).prop('checked', true);
            }, 100);
        })
        .fail(e => {
            if(e.status === 404) {
                Util.alert(e.responseText);
            }
        })

    },
    checkName: function () {
        const name = $("#name").val()
        const formGroup = $("#groupSelect").val()

        if (!name) {
            Util.alert("필드명을 입력해주세요.");
            return;
        }
        if (!formGroup) {
            Util.alert("폼그룹을 선택해주세요.");
            return;
        }

        Http.get("/cetus/api/form/check-name", { name, formGroup }).then(res => {
            Util.alert(res.message);
            if (res.data === 0) {
                codeJS.nameChk = true;
                $("#name").attr("readonly", true);
                $("#name-chk").attr("disabled", true);
            } else {
                codeJS.nameChk = false;
            }
        }).catch(() => {
            Util.alert("필드명 확인 중 오류가 발생했습니다.");
        });
    }
    , saveForm: async function () {

        if (!codeJS.nameChk) {
            Util.alert("폼그룹 중복 확인을 해주세요.");
            return;
        }
        const parentJsonData = await FormDataToObj.getParameterWithOutBlank("save-form-parent");
        parentJsonData.required = parentJsonData.required === 'Y';
        if (parentJsonData.type === "CHECKBOX" || parentJsonData.type === "RADIO" ||
            parentJsonData.type === "SELECT" || parentJsonData.type === "MULTISELECT") {
            const $formC = $("#save-form-child")
            if(!$formC[0].checkValidity()) {
                $formC.addClass("was-validated");
            } else {
                const childJsonData = await FormDataToObj.getParameter("save-form-child");
                const result = childJsonData.label.map((lbl, idx) => ({
                    label: lbl,
                    name: childJsonData.name[idx]
                }));
                parentJsonData.options = result
            }
        }

        Http.post("/cetus/api/form", parentJsonData, true).then(res => {
            Util.alert("컬럼 정보가 등록되었습니다.").then(res => {
                location.href = "/asp/cetus/form"
            })
        }).fail((e) => {
            if (e.status === 400) {
                if (e.hasOwnProperty('responseJSON')) {
                    const response = e['responseJSON'];
                    ValidationError.setValidationMssg('save-form-parent', response)
                    $('#typeSelect').niceSelect();

                    if ($('#typeSelect').hasClass('is-invalid')) {
                        $('#typeSelect').next('.nice-select').addClass('is-invalid');
                    } else {
                        $('#typeSelect').next('.nice-select').removeClass('is-invalid');
                    }
                }
            }
        })
    }

    , updateForm: async function () {
        const parentJsonData = await FormDataToObj.getParameterWithOutBlank("update-form-parent");
        const uid = parentJsonData.uid;
        delete parentJsonData['uid']
        parentJsonData.required = parentJsonData.required === 'Y';
        if (parentJsonData.type === "CHECKBOX" || parentJsonData.type === "RADIO" ||
            parentJsonData.type === "SELECT" || parentJsonData.type === "MULTISELECT") {

            const $formC = $("#update-form-child")
            if(!$formC[0].checkValidity()) {
                $formC.addClass("was-validated");
                return
            } else {
                const childJsonData = await FormDataToObj.getParameter("update-form-child");
                const result = childJsonData.label.map((lbl, idx) => ({
                    label: lbl,
                    name: childJsonData.name[idx],
                    uid: childJsonData.uid[idx] ?? null
                }));

                parentJsonData.options = result
            }
        }

        Http.put(`/cetus/api/form/${uid}`, parentJsonData, true).then(res => {
            Util.alert("컬럼 정보가 수정되었습니다.").then(res => {
                location.reload()
            })
        }).fail((e) => {
            if (e.status === 400) {
                if (e.hasOwnProperty('responseJSON')) {
                    const response = e['responseJSON'];
                    ValidationError.setValidationMssg('update-form-parent', response)
                }
            }
        })
    },
    deleteForm: function (uid) {
       Http.delete(`/cetus/api/form/${uid}`).then(res => {
           Util.alert("해당 컬럼이 삭제되었습니다.").then(res => {
              location.href = '/asp/cetus/form';
           })
       })
    },
    addTr: function () {
        const $tbody = $("#table_body");
        const $emptyRow = $tbody.find(".empty-tr");
        const rowCount = $tbody.find("tr").not(".empty-tr").length;

        if ($emptyRow.length > 0 && rowCount === 0) {
            $emptyRow.remove();
        }

        const newRow =
            `<tr class="new-row">
                <td>${rowCount + 1}</td>
                <td><input type="text" id="child-label" class="form-control" name="label" required></td>
                <td><input type="text" id="child-name" class="form-control" name="name" required></td>
                <td><button type="button" class="custom-button remove-tr-btn">삭제</button></td>
            </tr>`

        $tbody.append(newRow);
    }

    ,
    removeTr(element) {
        const $row = $(element).closest('tr');
        const uid = $(element).closest('tr').find('input[name="uid"]').val();
        if(uid) {
            Util.confirm("저장되어 있는 데이터입니다. 정말로 삭제하시겠습니까?").then((isOk) => {
                if(isOk) {
                    Http.delete(`/cetus/api/form/option/${uid}`).then(res => {
                        Util.alert("해당 데이터가 삭제되었습니다.").then(res => {
                            $row.remove();
                        })
                    })
                }
            })
            return;
        }
        const rowCount = $("#table_body tr").not(".empty-tr").length;
        if (rowCount === 1) {
            Util.alert("최소 1개의 자식 코드가 필요합니다.");
            return false;
        } else {
            $row.remove();
            const $tbody = $("#table_body");
            $tbody.find("tr").not(".empty-tr").each(function (index) {
                $(this).find("td:first").text(index + 1); // 첫 번째 <td>에 인덱스 값 업데이트
            });
        }
    }
}