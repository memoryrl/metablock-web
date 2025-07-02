var bbsJS = {

    table : null,

    initTable: function () {

        bbsJS.table = new Table("bbs-table")
            .get("/cetus/api/bbs")
            .delUnUseParams()
            .switchDiv()
            .add(new Column("bbsUid").center().render((data, full) => full.listIndex))
            .add(new Column("bbsNm"))
            .add(new Column("bbsTpCdNm"))
            .add(new Column("useAt").flagName('사용', '미사용'))
            .add(new Column("bbscttCnt").center())
            .add(new Column("regDt"))
            .add(new Column("regNm"))
            .add(new Column("bbsUid").center().render(((data, full) => {
                return `<button type="button" class="custom-button" id="bbsctt-list" data-uid="${data}">게시글 목록<br>보러가기</button>`
            })))
            .selectable()
            .select(function (data) {
                window.location.href = `/manager/cetus/bbs/${data.bbsUid}`;
            })
            .init();
    }

    ,searchTable: function () {
        FormDataToObj.getParameterWithOutBlank("search-form-id").then(obj => bbsJS.table.submit(obj))
    }

    ,toggleAtchInputs: function (enabled) {
        $('#atchNum, #uploadCpcty').prop('disabled', !enabled);
        if (!enabled) {
            $('#atchNum, #uploadCpcty').val('');
        }
    }

    ,saveBbs: async function () {
        if(!bbsJS.checkAtchValidation()) {
            Util.alert("첨부파일을 사용하시는 경우, <br> 첨부파일의 개수와 최대 용량값은 필수입니다.")
            return false;
        }
        const obj = await FormDataToObj.getParameterWithOutBlank("save-bbs");
        if(obj['atchAt'] === 'Y') {
            obj['uploadCpcty'] = bbsJS.formatMbToByte();
        } else {
            obj['atchNum'] = 0
            obj['uploadCpcty'] = null
        }
        Http.post(`/cetus/api/bbs`, obj, true).then((res) => {
            Util.alert("게시판 정보가 저장되었습니다.").then(() => {
                window.location.href = `/manager/cetus/bbs`
            })
        }).fail((e) => {
            if (e.status === 400) {
                if (e.hasOwnProperty('responseJSON')) {
                    const response = e['responseJSON'];
                    ValidationError.setValidationMssg('save-bbs', response)
                }
            }
        });
    }

    ,updateBbs: async function () {
        if(!bbsJS.checkAtchValidation()) {
            Util.alert("첨부파일을 사용하시는 경우, <br> 첨부파일의 개수와 최대 용량값은 필수입니다.")
            return false;
        }
        const obj = await FormDataToObj.getParameterWithOutBlank("update-bbs");
        if(obj['atchAt'] === 'Y') {
            obj['uploadCpcty'] = bbsJS.formatMbToByte();
        } else {
            obj['atchNum'] = 0
            obj['uploadCpcty'] = null
        }
        Http.put(`/cetus/api/bbs/${bbsUid}`, obj, true).then((res) => {
            Util.alert("게시판 정보가 수정되었습니다.").then(() => {
                window.location.href = `/manager/cetus/bbs`
            })
        }).fail((e) => {
            if (e.status === 400) {
                if (e.hasOwnProperty('responseJSON')) {
                    const response = e['responseJSON'];
                    ValidationError.setValidationMssg('update-bbs', response)
                }
            }
        });
    }

    ,deleteBbs: async function () {
        const count = await Http.get(`/cetus/api/bbs/${bbsUid}/child`);
        if (count !== 0) {
            Util.alert("게시판 하위로 게시글이 존재하여 삭제가 불가능합니다.")
            return false;
        }

        Util.confirm("삭제하시겠습니까?").then((isOk) => {
            if (isOk) {
                Http.delete(`/cetus/api/bbs/${bbsUid}`).then(() => {
                    Util.alert("게시판이 삭제되었습니다.").then(() => {
                        window.location.href = '/manager/cetus/bbs'
                    })
                });
            }
        });
    }

    ,formatMbToByte: function () {
        let bVal = null;
        const mbVal = parseFloat($('#uploadCpcty').val());
        if (!isNaN(mbVal)) {
            bVal = Math.floor(mbVal * 1024 * 1024);
        }
        return bVal;
    }
    
    ,formatByteToMb: function () {
        const originalVal = parseInt($('#uploadCpcty').val());
        if (!isNaN(originalVal) && originalVal >= 1024 * 1024) {
            const mbVal = (originalVal / (1024 * 1024)).toFixed(2);
            const mbValFloor = Math.floor(mbVal);
            $('#uploadCpcty').val(mbValFloor);
        }
    }

    ,checkAtchValidation: function () {
        let validation = true;
        if($('input[name="atchAt"]:checked').val() === 'Y') {
            if( $("#atchNum").val().length === 0 || $("#uploadCpcty").val().length === 0 ) {
                validation = false;
            }
        }
        return validation;
    }
}