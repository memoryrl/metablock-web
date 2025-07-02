var programJS = {

    table : null
    ,leftSlideUploader: null
    ,leftSlideUid: null
    ,rightSlideUploader: null
    ,rightSlideUid: null
    ,mainLogoImgUploader: null
    ,mainLogoUid: null
    ,companyLogoImgUploader: null
    ,companyLogoUid: null

    ,initTable: function () {
        programJS.table = new Table("program-table")
            .get("/cetus/api/program")
            .delUnUseParams()
            .switchDiv()
            .add(new Column("uid").center().render((data, full) => full.listIndex))
            .add(new Column("progrmNm").left())
            .add(new Column("url").left())
            .add(new Column("useAt").flagName('사용', '미사용'))
            .add(new Column("regDt"))
            .add(new Column("regNm"))
            .selectable()
            .select((data) => {
                window.location.href = `/manager/cetus/program/${data['uid']}`
            })
            .init();
    }

    ,initUploaders: function (leftUid, rightUid, mainLogoUid, companyLogoUid) {
        const uploadCnt = 1;
        const uploadTy = ['jpg', 'jpeg', 'png', 'gif', 'JPG', 'JPEG', 'PNG', 'GIF']

        // 왼쪽 슬라이드
        programJS.leftSlideUid = leftUid;
        programJS.leftSlideUploader = new Upload("#leftSlideImg-upload-container", "70%", 200)
        programJS.leftSlideUploader.setUploadInfo(uploadCnt, uploadTy);
        FileUtil.loadEditFiles(programJS.leftSlideUid, programJS.leftSlideUploader);

        // 오른쪽 슬라이드
        programJS.rightSlideUid = rightUid;
        programJS.rightSlideUploader = new Upload("#rightSlideImg-upload-container", "70%", 200)
        programJS.rightSlideUploader.setUploadInfo(uploadCnt, uploadTy);
        FileUtil.loadEditFiles(programJS.rightSlideUid, programJS.rightSlideUploader);

        // 메인 로고 이미지
        programJS.mainLogoUid = mainLogoUid;
        programJS.mainLogoImgUploader = new Upload("#mainLogoImg-upload-container", "100%", 200)
        programJS.mainLogoImgUploader.setUploadInfo(uploadCnt, uploadTy);
        FileUtil.loadEditFiles(programJS.mainLogoUid, programJS.mainLogoImgUploader);

        // 회사 로고 이미지
        programJS.companyLogoUid = companyLogoUid;
        programJS.companyLogoImgUploader = new Upload("#companyLogoImg-upload-container", "100%", 200)
        programJS.companyLogoImgUploader.setUploadInfo(uploadCnt, uploadTy);
        FileUtil.loadEditFiles(programJS.companyLogoUid, programJS.companyLogoImgUploader);
    }

    ,searchTable: function () {
        FormDataToObj.getParameterWithOutBlank("search-form-id").then(obj => programJS.table.submit(obj))
    }

    ,saveForm: async function () {
        let obj = await FormDataToObj.getParameterWithOutBlank("program-save");

        // 양쪽 슬라이드 이미지
        obj['leftImg'] = programJS.leftSlideUploader.getTempFileUrls();
        obj['rightImg'] = programJS.rightSlideUploader.getTempFileUrls();

        // 추가 root 일 경우 이미지
        if(obj['isRootUrl'] === 'Y') {
            obj['logoImg'] = programJS.mainLogoImgUploader.getTempFileUrls();
            obj['companyImg'] = programJS.companyLogoImgUploader.getTempFileUrls();
        }

        delete obj['files[]']

        if(obj['isRootUrl'] === 'Y') {
            const isValid = programJS.checkFileValidation()
            if(!isValid) {
                Util.alert("루트 URL의 경우 <br> 상단 왼쪽, 상단 오른쪽, 메인 로고 이미지는 필수값입니다.")
                return false;
            }
        }

        Http.post(`/cetus/api/program`, obj, true).then((res) => {
            Util.alert("프로그램 정보가 저장되었습니다.").then(() => {
                window.location.href = `/manager/cetus/program`
            })
        }).fail((e) => {
            if (e.status === 400) {
                if (e.hasOwnProperty('responseJSON')) {
                    const response = e['responseJSON'];
                    ValidationError.setValidationMssg('program-save', response)
                }
            }
        });
    }

    ,updateForm: async function () {
        let obj = await FormDataToObj.getParameterWithOutBlank("program-update");

        // 양쪽 슬라이드 이미지
        obj['leftImg'] = programJS.leftSlideUploader.getTempFileUrls();
        obj['leftImgDel'] = programJS.leftSlideUploader.getRemoveFileUrls();
        obj['leftImgUid'] = programJS.leftSlideUid;

        obj['rightImg'] = programJS.rightSlideUploader.getTempFileUrls();
        obj['rightImgDel'] = programJS.rightSlideUploader.getRemoveFileUrls();
        obj['rightImgUid'] = programJS.rightSlideUid;

        // 추가 root 일 경우 이미지
        if(obj['isRootUrl'] === 'Y') {

            obj['logoImg'] = programJS.mainLogoImgUploader.getTempFileUrls();
            obj['logoImgDel'] = programJS.mainLogoImgUploader.getRemoveFileUrls();

            obj['companyImg'] = programJS.companyLogoImgUploader.getTempFileUrls();
            obj['companyImgDel'] = programJS.companyLogoImgUploader.getRemoveFileUrls();
        }

        obj['logoImgUid'] = programJS.mainLogoUid;
        obj['companyImgUid'] = programJS.companyLogoUid;

        delete obj['files[]']

        if(obj['isRootUrl'] === 'Y') {
            const isValid = programJS.checkFileValidation()
            if(!isValid) {
                Util.alert("루트 URL의 경우 <br> 상단 왼쪽, 상단 오른쪽, 메인 로고 이미지는 필수값입니다.")
                return false;
            }
        }

        Http.put(`/cetus/api/program/${uid}`, obj, true).then((res) => {
            Util.alert("프로그램 정보가 수정되었습니다.").then(() => {
                window.location.href = `/manager/cetus/program`
            })
        }).fail((e) => {
            if (e.status === 400) {
                if (e.hasOwnProperty('responseJSON')) {
                    const response = e['responseJSON'];
                    ValidationError.setValidationMssg('program-update', response)
                }
            }
        });
    }
    
    ,checkFileValidation: function () {
        const leftSlideImg = $(`#leftSlideImg-upload-container .uppy-Dashboard-Item`);
        const rightSlideImg = $(`#rightSlideImg-upload-container .uppy-Dashboard-Item`);
        const logoImg = $(`#mainLogoImg-upload-container .uppy-Dashboard-Item`);
        return(leftSlideImg.length !== 0 && rightSlideImg !== 0 && logoImg.length !== 0)
    }
}