var formJS = {

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
                formJS.drawInfo(data)
                saveBtn.hide()
                updateBtn.show()
                deleteBtn.show()
            })
            .init();

        new Sortable(document.querySelector('tbody'), {
            animation: 150,
            ghostClass: 'dragging',
            chosenClass: 'chosen',      // 클릭했을 때 적용되는 클래스
            dragClass: 'dragging-real', // 실제 드래그 대상에 적용되는 클래스
            onEnd: (a) => {
                if(a.oldIndex === a.newIndex) {
                    return
                }
                const rows = a.to.children;

                const result = Array.from(rows).map(row => {
                    return row.querySelector('td')?.textContent.trim();
                });

                const values =formJS.formTables[formGroup].values();

                const reordered = result.map(sortNum => {
                    const matched = values.find(item => item.sortNum == sortNum);
                    return matched.uid;
                }).filter(x => x !== null); // ← null 필터링 (혹시 못 찾은 경우 방지)
                formJS.sortColumnsDrag(reordered)
            },
        });

        formJS.formTables[formGroup] = table;
    },


    drawInfo: function (data) {
        selectedUid = data.uid;
        const val = data.required === true ? "Y" : "N";
        $('#typeSelect').val(data.type).addClass("readonly-select").niceSelect('update');
        $(`input[name='required'][value='${val}']`).prop("checked", true);
        $("#label").val(data.label);
        $("#name").val(data.name).attr("readonly", true);
        $("#name-chk").attr("disabled", true);
        $("#description").val(data.description);
        $("#placeholder").val(data.placeholder);
        $("#defaultValue").val(data.defaultValue);

        if (data.options && data.options.length > 0) {
            const updtChild = $("#save-form-child")
            updtChild.show()
            const $tbody = $("#table-body");
            $tbody.empty();
            data.options.forEach(option => {
                const newRow =
                    `<tr class="new-row">
                        <input type="hidden" name="uid" value="${option.uid}">
                        <td><input type="text" id="child-label" class="form-control" name="label" value="${option.label}" required></td>
                        <td><input type="text" id="child-name" class="form-control" name="name" value="${option.name}" required></td>
                        <td><button style="width: 40px;" type="button" class="custom-button remove-tr-btn">삭제</button></td>
                        </tr>
                    `
                $tbody.append(newRow);
            })
        } else {
            updtChild.hide()
        }
    },
    newInfo: function () {
        selectedUid = null;
        saveBtn.show()
        updateBtn.hide()
        deleteBtn.hide()
        $('#typeSelect')
            .val('')
            .removeClass("readonly-select") // 1. 실제 select를 활성화
            .niceSelect('destroy')   // 2. niceSelect 제거
            .niceSelect();
        $("input[name='required']").prop("checked", false);

        $("#label").val('');
        $("#name").val('').attr("readonly", false); // 다시 수정 가능하게
        $("#name-chk").attr("disabled", false);     // 체크 버튼도 다시 활성화
        $("#description").val('');
        $("#placeholder").val('');
        $("#defaultValue").val('');
        updtChild.hide()
        $(".new-row").remove();
    },
    sortColumns: function (direction) {
        const checkedData = $('input[name="order"]:checked');

        if (checkedData.length === 0) {
            Util.alert("선택된 컬럼이 없습니다. 컬럼을 선택해주세요");
            return;
        }
        const uid = checkedData.data('uid');
        const sortNum = checkedData.data('order');
        const formGroup = $('.description-review-topbar.nav a.active').data('type');

        const data = {uid, sortNum, formGroup, direction};

        Http.put('/cetus/api/form/order', data).then(res => {
            Util.alert("정렬이 완료되었습니다.");
            formJS.formTables[formGroup].submit();
            setTimeout(() => {
                $(`input[name="order"][data-uid="${uid}"]`).prop('checked', true);
            }, 100);
        })
            .fail(e => {
                if (e.status === 404) {
                    Util.alert(e.responseText);
                }
            })

    },
    sortColumnsDrag: function (reorderedUid) {
        const formGroup = $('.description-review-topbar.nav a.active').data('type');
        Http.put('/cetus/api/form/order', {reorderedUid, formGroup}).then(res => {
            Util.alert("정렬이 완료되었습니다.");
            formJS.formTables[formGroup].submit();
        })
            .fail(e => {
                if (e.status === 404) {
                    Util.alert(e.responseText);
                }
            })

    },
    sortOptionDrag: function (nameList) {
        Http.put("/cetus/api/form/options/order", {columnsUid: selectedUid, nameList}).then(res => {
            Util.alert("정렬이 완료되었습니다.");
            formJS.formTables[formGroup].submit();
        })
    },
    optionControl: function () {
        new Sortable(document.querySelector('#table-body'), {
            animation: 150,
            ghostClass: 'dragging',
            chosenClass: 'chosen',      // 클릭했을 때 적용되는 클래스
            dragClass: 'dragging-real', // 실제 드래그 대상에 적용되는 클래스
            onEnd: (a) => {
                if(a.oldIndex === a.newIndex) {
                    return
                }
                const rows = a.to.children;

                const result = Array.from(rows).map(row => {
                    const input = row.querySelector('td input[name="name"]');
                    return input ? input.value.trim() : null;
                });
                formJS.sortOptionDrag(result)
            },
        });
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

        Http.get("/cetus/api/form/check-name", {name, formGroup}).then(res => {
            Util.alert(res.message);
            if (res.data === 0) {
                formJS.nameChk = true;
                $("#name").attr("readonly", true);
                $("#name-chk").attr("disabled", true);
            } else {
                formJS.nameChk = false;
            }
        }).catch(() => {
            Util.alert("필드명 확인 중 오류가 발생했습니다.");
        });
    }

    , saveForm: async function () {
        if (!formJS.nameChk) {
            Util.alert("폼그룹 중복 확인을 해주세요.");
            return;
        }
        const parentJsonData = await FormDataToObj.getParameterWithOutBlank("save-form-parent");
        parentJsonData.required = parentJsonData.required === 'Y';
        if (parentJsonData.type === "CHECKBOX" || parentJsonData.type === "RADIO" ||
            parentJsonData.type === "SELECT" || parentJsonData.type === "MULTISELECT") {
            const $formC = $("#save-form-child")
            if (!$formC[0].checkValidity()) {
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
                formJS.formTables[formGroup].submit();
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
    , previewModal: function () {
        const $container = $("#previewModal .modal-body")

        const select = $("#col-num").val() || 2
        $container.empty()
        Http.get(`/asp/cetus/form/modal-preview?formGroup=SIGNUP&rowSize=${select}`)
            .then(res => {
                $container.html(res)
                $("#previewModal").modal('show')
                $(".select2").select2()
                $(".preview-select").niceSelect();
                const uploader = new Upload(`.template-file-container`, '100%', 250);
            })

    }
    , updateForm: async function () {
        const parentJsonData = await FormDataToObj.getParameterWithOutBlank("save-form-parent");
        parentJsonData.required = parentJsonData.required === 'Y';
        if (parentJsonData.type === "CHECKBOX" || parentJsonData.type === "RADIO" ||
            parentJsonData.type === "SELECT" || parentJsonData.type === "MULTISELECT") {

            const $formC = $("#save-form-child")

            if (!$formC[0].checkValidity()) {
                $formC.addClass("was-validated");
                return
            } else {
                const childJsonData = await FormDataToObj.getParameter("save-form-child");
                const result = childJsonData.label.map((lbl, idx) => ({
                    label: lbl,
                    name: childJsonData.name[idx],
                    uid: childJsonData.uid[idx] ?? null
                }));

                parentJsonData.options = result
            }
        }
        Http.put(`/cetus/api/form/${selectedUid}`, parentJsonData, true).then(res => {
            Util.alert("컬럼 정보가 수정되었습니다.").then(res => {
                formJS.formTables[formGroup].submit();
            })
        }).fail((e) => {
            if (e.status === 400) {
                if (e.hasOwnProperty('responseJSON')) {
                    const response = e['responseJSON'];
                    ValidationError.setValidationMssg('save-form-parent', response)
                }
            }
        })
    },
    deleteForm: function () {
        Http.delete(`/cetus/api/form/${selectedUid}`).then(res => {
            Util.alert("해당 컬럼이 삭제되었습니다.").then(res => {
                formJS.formTables[formGroup].submit();
                formJS.newInfo();
            })
        })
    },
    addTr: function () {
        const $tbody = $("#table-body");
        const $emptyRow = $tbody.find(".empty-tr");
        const rowCount = $tbody.find("tr").not(".empty-tr").length;

        if ($emptyRow.length > 0 && rowCount === 0) {
            $emptyRow.remove();
        }
        // <td>${rowCount + 1}</td>
        const newRow =
            `<tr class="new-row">
                <td><input type="text" id="child-label" class="form-control" name="label" required></td>
                <td><input type="text" id="child-name" class="form-control" name="name" required></td>
                <td><button style="width:40px;" type="button" class="custom-button remove-tr-btn">삭제</button></td>
            </tr>`

        $tbody.append(newRow);
    }

    ,
    removeTr() {
        const $row = $(this).closest('tr');
        const uid = $(this).closest('tr').find('input[name="uid"]').val();
        if (uid) {
            Util.confirm("저장되어 있는 데이터입니다. 정말로 삭제하시겠습니까?").then((isOk) => {
                if (isOk) {
                    Http.delete(`/cetus/api/form/option/${uid}`).then(res => {
                        Util.alert("해당 데이터가 삭제되었습니다.").then(res => {
                            $row.remove();
                            formJS.formTables[formGroup].submit();
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