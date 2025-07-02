var bbscttJS = {

    table: null
    ,bbscttCntEditor: null   // 게시글 등록,수정,조회 에디터
    ,answerCntEditor: null   // 댓글 입력 에디터
    ,uploader : null         // 등록, 수정시 사용되는 업로더
    ,answerCnts: {}

    /* [게시글 목록] > 테이블 초기화 */
    ,initTable: function () {
        const openAt = (role === "USER") ? "Y" : null
        const orderByNotice = (bbs['bbsTpCd'] === 'NOTICE') ? "Y" : "N"
        bbscttJS.table = new Table("bbsctt-table")
            .get("/cetus/api/bbsctt")
            .param("bbsUid", () => bbsUid)
            .param("openAt", () => openAt)
            .param("orderByNotice", () => orderByNotice)
            .delUnUseParams()
            .switchDiv()
            .add(new Column("bbscttUid").center().render((data, full) => full.listIndex))
            .add(new Column("regUid").render((data, full) => {
                const fileImg = (full['regProfileId']) ? `/cetus/files/download?fileId=${full['regProfileId']}` : '/assets/images/page/detail/profile_noimage.png'
                return `
                    <div class="user-info-wrapper">
                        <img src="${fileImg}" alt="프로필" class="user-profile-img" onerror="this.src='/assets/images/page/detail/profile_noimage.png'">
                        <div class="user-text-info">
                            <span>${full['regNm']}</span>
                            <span style="color: lightgray">${full['regEmail']}</span>
                        </div>
                    </div>`;
            }))
            .add(new Column("bbscttNm").left())
            .selectable()
            .select(function (data) {
                window.location.href = `/asp/cetus/bbsctt/view/${data['bbscttUid']}`;
            })

        if(bbs['atchAt'] === 'Y') {
            bbscttJS.table
                .add(new Column("fileCnt").center().render((data) => {
                    return `${data}개`
                }))
        }

        if(bbs['answerUseAt'] === 'Y') {
            bbscttJS.table
                .add(new Column("answerCnt").center().render((data) => {
                    return `${data}개`
                }))
        }

        bbscttJS.table
            .add(new Column("regDt").center())
            .add(new Column("rdCnt").center())
            .init();
    }

    /* [게시글 수정 / 등록 / 조회 ] > 폼 세팅 */
    ,initForm: async function (fileUid, bbscttCnt = '', viewMode = false) {

        // {게시글 내용}
        bbscttJS.bbscttCntEditor = new Editor("bbscttCnt", '', true);
        bbscttJS.bbscttCntEditor = await bbscttJS.bbscttCntEditor.init(viewMode);
        bbscttJS.bbscttCntEditor.setEditorData(bbscttCnt)


        if(bbs['atchAt'] === 'Y') {
            if(!viewMode) {
                bbscttJS.settingUploader(fileUid);
            } else {
                await FileUtil.loadEditFilesViewMode(fileUid, "upload-container")
            }
        }
    }

    ,settingUploader: function (fileUid) {
        bbscttJS.uploader = new Upload("#upload-container");
        bbscttJS.uploader.setUploadInfo(bbs['atchNum'])
        bbscttJS.uploader.setUploadSize(bbs['uploadCpcty'], 'B')
        FileUtil.loadEditFiles(fileUid, bbscttJS.uploader);
        bbscttJS.uploaderEvent();
        bbscttJS.uploader._uppy.on("complete", () => {
            bbscttJS.uploaderEvent();
        });
        bbscttJS.uploader._uppy.on('file-removed', () => {
            bbscttJS.uploaderEvent();
        });
    }

    ,uploaderEvent: function () {
        const totalSizeBytes = bbscttJS.uploader.getUploadTotalSize();
        const totalSizeMB = (totalSizeBytes / (1024 * 1024)).toFixed(2);
        const maxSizeMB = bbscttJS.formatByteToMb(bbs['uploadCpcty']);
        const progressPercent = Math.min((totalSizeMB / maxSizeMB) * 100, 100).toFixed(2);
        const isBigger = (totalSizeMB > maxSizeMB);
        $("#max-uploadCpcty").text(maxSizeMB);
        $('#total-uploadCpcty').text(totalSizeMB);
        $('#bbscttAtchProgressBar').css('width', `${progressPercent}%`).css('background-color', isBigger ? '#db3f3f' : '#5f9def').attr('aria-valuenow', progressPercent);
        $('#uploadCpcty-span').css('color', isBigger ? '#db3f3f' : '#000000');
    }

    /* [게시글 등록] > 폼 저장 */
    ,saveForm: async function () {

        const validation = bbscttJS.uploader.checkFileValidation()
        if(!validation) {
            Util.alert("업로드하신 첨부파일 목록을 확인해주세요.")
            return false;
        }

        thumbnailModal.chooseThumbNail().then(async () => {

            let obj = await FormDataToObj.getParameterWithOutBlank("bbsctt-save-form");

            if(bbs['atchAt'] === 'Y') {
                obj['fileAdd'] = bbscttJS.uploader.getTempFileUrls();
                obj['fileDel'] = bbscttJS.uploader.getRemoveFileUrls();
            }

            obj['bbscttCnt'] = bbscttJS.bbscttCntEditor.getEditorData()
            obj['bbsUid'] = bbsUid;
            obj['thumbnailSrc'] = thumbnailModal.thumbNailImg
            delete obj['files[]']
            Http.post(`/cetus/api/bbsctt`, obj, true).then((res) => {
                Util.alert("게시글 정보가 저장되었습니다.").then(() => {
                    window.location.href = `/asp/cetus/bbsctt/${bbsUid}`
                })
            }).fail((e) => {
                if (e.status === 400) {
                    if (e.hasOwnProperty('responseJSON')) {
                        const response = e['responseJSON'];
                        ValidationError.setValidationMssg('bbsctt-save-form', response)
                        const errorElement = document.getElementById(`bbscttCnt-error`);
                        if (response.hasOwnProperty('bbscttCnt')) errorElement.textContent = response['bbscttCnt']
                        else errorElement.textContent = ''
                    }
                }
            });
        });
    }

    /* [게시글 수정] > 폼 수정 */
    ,updateForm: async function () {

        const validation = bbscttJS.uploader.checkFileValidation()
        if(!validation) {
            Util.alert("업로드하신 첨부파일 목록을 확인해주세요.")
            return false;
        }

        thumbnailModal.chooseThumbNail().then(async () => {
            let obj = await FormDataToObj.getParameterWithOutBlank("bbsctt-update-form");

            if(bbs['atchAt'] === 'Y') {
                obj['fileAdd'] = bbscttJS.uploader.getTempFileUrls();
                obj['fileDel'] = bbscttJS.uploader.getRemoveFileUrls();
            }

            obj['fileUid'] = fileUid; // 첨부파일은 사용안하더라도, 기존 {fileUid} 값은 유지
            obj['bbscttCnt'] = bbscttJS.bbscttCntEditor.getEditorData()
            obj['thumbnailSrc'] = thumbnailModal.thumbNailImg
            delete obj['files[]']

            Http.put(`/cetus/api/bbsctt/${bbscttUid}`, obj, true).then((res) => {
                Util.alert("게시글 정보가 수정되었습니다.").then(() => {
                    window.location.href = `/asp/cetus/bbsctt/${bbsUid}`
                })
            }).fail((e) => {
                if (e.status === 400) {
                    if (e.hasOwnProperty('responseJSON')) {
                        const response = e['responseJSON'];
                        ValidationError.setValidationMssg('bbsctt-update-form', response)
                        const errorElement = document.getElementById(`bbscttCnt-error`);
                        if(response.hasOwnProperty('bbscttCnt')) errorElement.textContent = response['bbscttCnt']
                        else errorElement.textContent = ''
                    }
                }
            });
        });

    }

    /* [게시글 수정] > 게시글 삭제 */
    ,deleteBbsctt: function () {
        Util.confirm("정말로 삭제하시겠습니까?").then((isOk) => {
            if(isOk) {
                Http.delete(`/cetus/api/bbsctt/${bbscttUid}`).then(() => {
                    Util.alert("삭제되었습니다.").then(() => {
                        window.location.href = `/asp/cetus/bbsctt/${bbsUid}`
                    });
                });
            }
        })
    }

    /* [게시글 조회] > 댓글 작성 폼 초기화 */
    ,initAnswer: async function () {
        bbscttJS.answerCntEditor = new Editor("answerCnt", '', false);
        bbscttJS.answerCntEditor = await bbscttJS.answerCntEditor.init();
    }

    /* [게시글 조회] > 댓글 저장 */
    ,saveAnswer: function () {
        const obj = {
            bbscttUid: bbscttUid,
            answerCnt: bbscttJS.answerCntEditor.getEditorData()
        }
        Http.post('/cetus/api/answer', obj, true).then((res) => {
            Util.alert("댓글이 작성되었습니다.").then(() => {
                window.location.reload();
            })
        }).fail((e) => {
            if (e.status === 400) {
                if (e.hasOwnProperty('responseJSON')) {
                    const response = e['responseJSON'];
                    const errorElement = document.getElementById(`answerCnt-error`);
                    if(response.hasOwnProperty('answerCnt')) errorElement.textContent = response['answerCnt']
                    else errorElement.textContent = ''
                }
            }
        });
    }

    /* [게시글 조회] > 댓글 수정 */
    ,updateAnswer:function (answerUid) {
        const viewDiv = $(`#answer-editor-view-div-${answerUid}`)
        const editDiv = $(`#answer-editor-edit-div-${answerUid}`)
        viewDiv.css('display', 'none');
        editDiv.css('display', 'block');

        const viewButtonDiv = $(`#answer-view-button-div-${answerUid}`)
        const editButtonDiv = $(`#answer-edit-button-div-${answerUid}`)
        viewButtonDiv.css('display', 'none');
        editButtonDiv.css('display', 'flex');
    }

    /* [게시글 조회] > 댓글 삭제 */
    ,deleteAnswer: function (answerUid) {
        Util.confirm('정말 삭제하시겠습니까?').then((isOk) => {
            if(isOk) {
                Http.delete(`/cetus/api/answer/${answerUid}`).then(() => {
                    Util.alert("해당 댓글이 삭제되었습니다.").then(() => {
                        window.location.reload();
                    })
                })
            }
        })
    }

    /* [게시글 조회] > 댓글 토글 */
    ,toggleAnswerCnt: function (element) {
        const $el = $(element);
        const uid = $el.data("uid");
        let isFold = $el.data("isfold");

        isFold = !isFold;
        $el.data("isfold", isFold);
        $el.text(isFold ? "▼" : "▲");

        const $editor = $(`#answer-editor-view-div-${uid} .ck-editor__editable`);
        if (isFold) {
            $editor.attr("style", "overflow: hidden; -webkit-line-clamp: 1; -webkit-box-orient: vertical; display: -webkit-box; padding: 0px 10px !important;");
        } else {
            $editor.css("overflow", "auto");
            $editor.css({
                "-webkit-line-clamp": "",
                "-webkit-box-orient": "",
                "display": "",
                "padding": ""
            });
        }
    }

    /* [게시글 조회] > 댓글 목록 세팅 => { answerUid : viewEditor, editEditor, editInitData } */
    ,initAnswerListForm: async function (answers = []) {

        for (const item of answers) {
            const answerUid = item['answerUid'];
            const viewId = `answer-editor-view-${answerUid}`
            const editId = `answer-editor-edit-${answerUid}`
            let viewEditor = new Editor(viewId, item['answerCnt'], false);
            viewEditor = await viewEditor.init(true);
            let editEditor = new Editor(editId, item['answerCnt'], false);
            editEditor = await editEditor.init();

            bbscttJS.answerCnts[answerUid] = {
                viewEditor,
                editEditor,
                editInitData: item['answerCnt']
            }

            const $editor = $(`#answer-editor-view-div-${answerUid} .ck-editor__editable`);
            $editor.attr("style", "overflow: hidden; -webkit-line-clamp: 1; -webkit-box-orient: vertical; display: -webkit-box; padding: 0px 10px !important;");
        }

    }

    /* [게시글 조회] > 댓글 수정 */
    ,editSaveAnswer: function (uid) {
        const editor = bbscttJS.answerCnts[uid]['editEditor']
        const obj = { answerCnt: editor.getEditorData() }
        Http.put(`/cetus/api/answer/${uid}`, obj, true).then(() => {
            Util.alert("댓글 정보가 수정되었습니다.").then(() => {
                window.location.reload();
            })
        }).fail((e) => {
            if (e.status === 400) {
                if (e.hasOwnProperty('responseJSON')) {
                    const response = e['responseJSON'];
                    const errorElement = document.getElementById(`answerCnt-${uid}-error`);
                    if(response.hasOwnProperty('answerCnt')) {
                        errorElement.textContent = response['answerCnt']
                    }
                    else errorElement.textContent = ''
                }
            }
        });
    }

    /* [게시글 조회] > 댓글 수정 취소 */
    ,cancelSaveAnswer: function (uid) {
        const editor = bbscttJS.answerCnts[uid]['editEditor']
        const editInitData = bbscttJS.answerCnts[uid]['editInitData']
        editor.setEditorData(editInitData);

        const viewDiv = $(`#answer-editor-view-div-${uid}`)
        const editDiv = $(`#answer-editor-edit-div-${uid}`)
        viewDiv.css('display', 'block');
        editDiv.css('display', 'none');

        const viewButtonDiv = $(`#answer-view-button-div-${uid}`)
        const editButtonDiv = $(`#answer-edit-button-div-${uid}`)
        viewButtonDiv.css('display', 'flex');
        editButtonDiv.css('display', 'none');

        const errorElement = document.getElementById(`answerCnt-${uid}-error`);
        errorElement.textContent = ''
    }

    ,formatByteToMb: function (byteVal) {
        if (!isNaN(byteVal) && byteVal >= 1024 * 1024) {
            const mbVal = (byteVal / (1024 * 1024)).toFixed(2);
            byteVal = Math.floor(mbVal);
        }
        return byteVal;
    }
}