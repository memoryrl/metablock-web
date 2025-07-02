var contentsJS = {

    contentsFileUploader: null, 
    contentsFileUid: null,
    thumbnailUid: null,
    thumbnailUploader: null,

    initUploaders: function (thumbnailUid, contentsFileUid) {
        const uploadCnt = 1;
        const imageUploadTy = ['jpg', 'jpeg', 'png', 'gif', 'JPG', 'JPEG', 'PNG', 'GIF']

        contentsJS.thumbnailUid = thumbnailUid;
        contentsJS.thumbnailUploader = new Upload("#thumbnail-upload-container", "70%", 200)
        contentsJS.thumbnailUploader.setUploadInfo(uploadCnt, imageUploadTy);
        FileUtil.loadEditFiles(contentsJS.thumbnailUid, contentsJS.thumbnailUploader);

        contentsJS.contentsFileUid = contentsFileUid;
        contentsJS.contentsFileUploader = new Upload("#contentFile-upload-container", "70%", 200)
        contentsJS.contentsFileUploader.setUploadInfo(uploadCnt);
        FileUtil.loadEditFiles(contentsJS.contentsFileUid, contentsJS.contentsFileUploader);

    },

    updateForm: async function () {
        let obj = await FormDataToObj.getParameterWithOutBlank("content-update-form");

        obj['contentFile'] = contentsJS.contentsFileUploader.getTempFileUrls();
        obj['contentFileDel'] = contentsJS.contentsFileUploader.getRemoveFileUrls();
        obj['contentFileUid'] = contentsJS.contentsFileUid;


        obj['thumbnail'] = contentsJS.thumbnailUploader.getTempFileUrls();
        obj['thumbnailDel'] = contentsJS.thumbnailUploader.getRemoveFileUrls();
        obj['thumbnailUid'] = contentsJS.thumbnailUid;


        delete obj['files[]']

        //obj['metadata'] && (obj['metadata'] = JSON.parse(obj['metadata']));
        console.log(obj);
        Http.put(`/cetus/api/contents/${content.uid}`, obj, true).then((res) => {
            Util.alert("컨텐츠 정보가 수정되었습니다.").then(() => {
                window.location.href = `/manager/cetus/contents`
            })
        }).fail((e) => {
            if (e.status === 400) {
                if (e.hasOwnProperty('responseJSON')) {
                    const response = e['responseJSON'];
                    console.log(response);
                   // ValidationError.setValidationMssg('program-update', response)
                }
            }
        });
    }

}