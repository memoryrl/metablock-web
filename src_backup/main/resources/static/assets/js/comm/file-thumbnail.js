/***
 * 프로필 이미지 업로더
 * => 이미지 업로드를 통해 썸네일로 이미지 확인 가능
 * */
class ThumbnailUpload {

    static downloadEndpoint_static = "/cetus/files/download";
    static downloadEndpoint2_static = "/cetus/files/download2";

    constructor(selector, width = '100%', height = 304) {
        this._selector = selector
        this._selectorId = selector.startsWith('#') ? selector.substring(1) : selector;
        this._scrollbar = null
        this._removes = []
        this._uid = null
        this._width = width
        this._height = height
        this.uploadCnt = 1  // 최대 업로드 개수
        this.uploadTy = ['jpg', 'jpeg', 'png', 'gif', 'JPG', 'JPEG', 'PNG', 'GIF']  // 업로드 가능 확장자
        this.tempUploadCnt = 0
        this.uploading = false
        this.currentfiles = {};

        /* ## 썸네일 이미지 관련 ## */
        /* -> 썸네일 이미지는 단건만 가능  */
        this.useThumbnailImg = false;       // 썸네일 이미지 보여주기 사용 여부
        this.thumbnailImgId = '';           // 썸네일 이미지 ID
        this.tempThumbnailUpload = {}       // 임시 업로드된 썸네일 이미지
        this.tumbnailStaticUpload = {}      // 업로드 완료된 썸네일 이미지
        this.thumbnailImgDelete = []        // 삭제할 썸네일 이미지
        this.thumbnailFileMap = {}          // { key, value } => { uppy_id, file_id }
        this._emptyImgSrc = "/assets/images/page/detail/profile_noimage.png"

        /* ## promise 관련 ## */
        this._usePromise = false;
        this.uploadPromise = null;
        this.deletePromise = null;

        this._checkUploadPixel = false
        this._pixelWidth = 0
        this._pixelHeight = 0

        this.uploadEndpoint   = "/api/tus/file/upload";
        this.removeEndpoint   = "/api/tus/file/storagetemp";
        this.downloadEndpoint = "/api/tus/file/download";

        this.downloadEndpoint_saved_before = "/cetus/files/download2";

        this._uploadSettings()
    }

    _uploadSettings() {
        const {Dashboard, Tus} = Uppy
        this._uppy = new Uppy.Uppy({
            locale, // uppy.locale.js
            autoProceed: true,
            onBeforeFileAdded: (file) => {
                const fileExtension = file.extension

                // 새로운 파일 업로드
                if (!this.uploading) {

                    if (this.tempUploadCnt === this.uploadCnt) {
                        Util.alert(`최대 업로드 가능한 파일 개수는 ${this.uploadCnt}개입니다.`)
                        return false;
                    }

                    if (!this.uploadTy.includes(fileExtension)) {
                        Util.alert("업로드 불가능한 확장자입니다.")
                        return false;
                    }
                }

                // 기존 파일 다시 업로드
                if (this.uploading) {

                    if (this.tempUploadCnt === this.uploadCnt) {
                        Util.alert(`최대 업로드 가능한 파일 개수는 ${this.uploadCnt}개입니다. 확인해주세요.`)
                    }

                    if (!this.uploadTy.includes(fileExtension)) {
                        Util.alert("업로드 불가능한 확장자가 있습니다. 확인해주세요.")
                    }
                }

                if (this._uid != null) {
                    this.tempUploadCnt++
                    file.meta = {
                        ...file.meta,
                        uid: this._uid
                    }
                }

                return file
            }
        })

        this._uppy.use(Dashboard, {
            inline: true,
            target: this._selector,
            width: this._width,
            height: this._height,
            showRemoveButtonAfterComplete: true
        })

        this.upload_uuid = this.getUUID();


        // upload file ...
        this._uppy.use(Tus, {
            endpoint: this.uploadEndpoint,
            onShouldRetry: this._onShouldRetry,
            chunkSize: 50000000,
            headers: {
                'X-TUS-UUID': this.upload_uuid
                , 'Authorization': ""
            },
            onBeforeRequest: (req, file) => {
                this.currentfiles[req._url] = file;
            },
            onAfterResponse: (req, res) => {
                let status = res.getStatus();
                if (status == 200) {
                    let value64 = res.getHeader("X-Response-Data");
                    if (value64 != null && value64 != '') {
                        let url = req.getURL();
                        let value = atob(value64);
                        let jsonObject = JSON.parse(value);
                        this.currentfiles[url].meta.fileId = jsonObject.path;
                    }
                    let valueNfs64 = res.getHeader("X-Response-NfsInfo");
                    if (valueNfs64 != null && valueNfs64 != '') {
                        let url = req.getURL();
                        let value = atob(valueNfs64);
                        let jsonObject = JSON.parse(value);
                    }
                }
            }
        })

        this._uppy.on('upload', () => {
            // 해당 시점은 파일 업로드 시작 시점에 무조건 실행
        });

        this._uppy.on('file-added', (file) => {
            const _this =  this
            /**
             * [임시저장] 파일에 대해서 픽셀 체크
             * */
            if(_this._checkUploadPixel && !file.meta.saved) {
                const img = new Image();
                img.src = (file.preview) ? file.preview : URL.createObjectURL(file.data);
                img.onload = () => {
                    if (img.width >= _this._pixelWidth || img.height > _this._pixelHeight) {
                        Util.alert(`권장 이미지 사이즈 : ${_this._pixelWidth}px X ${_this._pixelHeight}px`)
                    }
                }
            }
        })

        this._uppy.on('upload-progress', (file, progress) => {
            // upload-progress
        });

        // drag&drop 은 Https 에서만 지원
        this._uppy.on('upload-success', (file, upload) => {
            file.meta.saved = false
            if (!file.meta.saved)
                file.meta.name = file.name
        });

        // 전체 업로드 파일에 대해 '한번에' 처리
        this._uppy.on('complete', (result) => {
            this.uploading = false;
            this.currentfiles = {};

            if(this.useThumbnailImg) {
                if(result['successful'].length !== 0) {

                    /**
                     * 이미지 미리보기를 이용하는 경우
                     * */
                    const file = result['successful'][0];
                    const fileId = file['meta']['fileId'];
                    this.thumbnailFileMap[fileId] = file.id;
                    $(`#${this.thumbnailImgId}`).attr("src", `${this.downloadEndpoint_saved_before}?fileId=${fileId}`).attr("data-id", fileId);
                    this.tempThumbnailUpload = this._setUploadFileObj(file);

                    /**
                     * [임시저장] 파일에 대해서 [promise]를 이용해 업로드 완료 시점을 잡는 경우
                     * */
                    if(this._usePromise) {
                        const obj = this.tempThumbnailUpload
                        if(this.uploadPromise) {
                            this.uploadPromise({
                                submit: true,
                                file: obj
                            })
                        }
                    }
                }
            }
        });

        this._uppy.on('file-removed', (file, reason) => {
            // file-removed
        })

        this._uppy.on('error', (error) => {
            console.error(error.stack);
        });
    }

    /**
     * 전달 받은 {file} 정보들을 통해 필요한 정보들로 obj 구성
     * */
    _setUploadFileObj(file) {
        return {
            orgFileNm: file.meta.name,
            fileType: file.meta.type,
            fileId: file.meta.fileId,
            saved: (file.meta.saved == false) ? 'N' : 'Y',
            extension: file.extension
        }
    }

    /**
     * 이미지 업로드, 삭제 시점을 잡아야 하는 경우 사용
     * */
    usePromise() {
        this._usePromise = true;
    }

    /**
     * 기존 업로더가 아닌 다른 버튼 클릭을 통해 이미지를 업로드 해야 하는 경우 사용
     * */
    openFileUpload() {
        const DashboardTab = $(`#${this._selectorId} .uppy-DashboardTab-btn`).first();
        if( DashboardTab.length ) {
            DashboardTab.click();
        } else {
            $(`#${this._selectorId} .uppy-DashboardContent-addMore`).click();
            setTimeout(() => {
                $(`#${this._selectorId} .uppy-DashboardTab-btn`).click();
            }, 100);  // 0.1초 후 실행
        }
    }

    /**
     * 기존 업로더가 아닌 다른 버튼 클릭을 통해 이미지를 업로드 해야 하는 경우 사용
     * (+) 업로드 완료 시점을 잡아야 하는 경우 사용
     * */
    openFileUploadPromise() {
        return new Promise((resolve, reject) => {
            this.openFileUpload();
            this.uploadPromise = resolve;
        });
    }

    /**
     * 업로드 한 썸네일 이미지 정보 가져오기 (임시저장 상태 이미지)
     * */
    getTempThumbnailFile() {
        return this.tempThumbnailUpload;
    }

    /**
     * 업로드 한 썸네일 이미지 정보 가져오기 (저장 상태 이미지)
     * */
    getThumbnailStaticUpload() {
        return this.tumbnailStaticUpload;
    }

    /**
     * 삭제할 썸네일 이미지 리스트 가져오기
     * @return list[]
     * */
    getRemoveFile() {
        return this.thumbnailImgDelete;
    }

    /**
     * 삭제할 이미지 PUSH
     * */
    setRemoveFile(file = {}) {
        this.thumbnailImgDelete.push(file)
    }

    /*
    * 이미지 삭제 (+) 삭제 시점 잡기
    * */
    removeFilePromise(file, reason) {
        return new Promise((resolve, reject) => {
            this.deletePromise = resolve;
            this.removeThumbnailFile(file, reason);
        });
    }

    /**
     * 썸네일 이미지 삭제하기
     * - case1 : db에 저장 되지 않은 이미지
     * - case2 : db에 저장된 이미지
     * */
    removeThumbnailFile(file, reason) {
        if ( reason === 'removed-by-user' ) {

            const uppy_id = this.thumbnailFileMap[file['fileId']]
            if(uppy_id) {
                this._uppy.removeFile(uppy_id);
            }

            this.tempUploadCnt --

            if(this.useThumbnailImg) {
                const $thumbnailElement = $(`#${this.thumbnailImgId}`);
                const thumbnail_file_id = $thumbnailElement.length ? $thumbnailElement.attr('data-id') : null;
                if (thumbnail_file_id && thumbnail_file_id === String(file.fileId)) {
                    $thumbnailElement.attr("src", this._emptyImgSrc);
                }
            }

            if ( file.saved === "N") {
                // 등록되기 전에 임시폴더에 있는 경우 삭제
                if(file.fileId) {
                    if( this.tempThumbnailUpload.hasOwnProperty('fileId') && this.tempThumbnailUpload['fileId'] === file.fileId ) {
                        this.tempThumbnailUpload = {}
                    }
                    if( this.tumbnailStaticUpload.hasOwnProperty('fileId') && this.tumbnailStaticUpload['fileId'] === file.fileId) {
                        this.tumbnailStaticUpload = {}
                    }
                    Http.delete(`${this.removeEndpoint}/${file.fileId}`);
                }
            } else {
                if( this.tempThumbnailUpload.hasOwnProperty('fileId') && this.tempThumbnailUpload['fileId'] === file.fileId ) {
                    this.tempThumbnailUpload = {}
                }
                if( this.tumbnailStaticUpload.hasOwnProperty('fileId') && this.tumbnailStaticUpload['fileId'] === file.fileId) {
                    this.tumbnailStaticUpload = {}
                }
                this.thumbnailImgDelete.push({
                    orgFileNm: file.orgFileNm,
                    fileType : file.fileType,
                    fileId   : file.fileId,
                    saved    : file.saved,
                    extension: file.extension
                });
            }

            /**
             * 업로드 파일에 대해서 [promise]를 이용해 이미지 삭제 시점을 잡는 경우
             * */
            if(this._usePromise) {
                if(this.deletePromise) {
                    this.deletePromise({
                        submit: true,
                        file: file
                    })
                }
            }
        }
    }

    getUUID() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 3 | 8);
            return v.toString(16);
        });
    }

    setThumbNail(thumbnailImgId) {
        this.useThumbnailImg = true;
        this.thumbnailImgId = thumbnailImgId;
    }

    setUploadInfo(uploadCnt = 0, uploadTy = []) {
        this.uploadCnt = uploadCnt
        this.uploadTy = uploadTy
    }

    checkUploadPixel(width = 0, height = 0){
        this._checkUploadPixel = true
        this._pixelWidth = width
        this._pixelHeight = height
    }

    _onShouldRetry(err, retryAttempt, options, next) {
        const res = err?.originalResponse
        const status = res?.getStatus()
        if ( status === 405 ) {
            try { signModal.show() }
            catch (e) { window.location.href = '/login' }
            finally { return false }
        }
        return next(err)
    }

    /**
     * @param fileUid 0일때 등록(임시), 0이 아닐 때 수정
     */
    setUid(fileUid) {
        this._uid = fileUid
    }

    getUid() {
        return this._uid
    }


    /**
     * 파일 추가
     * @param file 전체 파일 데이터
     * @param isTemp 임시 파일 여부(file.saved !== 'Y')
     */
    addStaticFile(file, isTemp) {
        this.uploading = true;

        const name = file['fileNm'] ? file['fileNm'] : file.orgFileNm
        const b64nm = btoa(encodeURIComponent(name));
        const options = {}
        if ( file.fileType.toString().includes('image'))
            options.preview = `${this.downloadEndpoint}/${file.filePath}/${b64nm}`

        const id = this._uppy.addFile({
            id: file.fileId,
            name: file['fileNm'] ? file['fileNm'] : file.orgFileNm,
            type: file.fileType,
            data: {
                size: file.fileSize,
                lastModified: new Date(file['regDt']).getTime()
            },
            meta: {
                url: `${this.downloadEndpoint}/${file['filePath']}/${b64nm}`,
                saved: !isTemp,
                fileId: file.fileId
            },
            ...options
        })
        this._uppy.setFileState(id, {
            progress: {
                uploadComplete: true,
                uploadStarted: true
            }
        })

        if(this.useThumbnailImg) {
            this.tumbnailStaticUpload = file;
            this.thumbnailFileMap[file.fileId] = id;
            $(`#${this.thumbnailImgId}`)
                .attr("src", `${this.downloadEndpoint}/${file['filePath']}/${b64nm}`)
                .attr("data-id", file.fileId);
        }
    }

    /**
     * 파일 추가 (저장 전)
     * @param file 전체 파일 데이터
     * @param isTemp 임시 파일 여부(file.saved !== 'Y')
     */
    addStaticFile_before(file, isTemp) {
        this.uploading = true;

        const name = file['orgFileNm'] ? file['orgFileNm'] : file.orgFileNm
        const b64nm = btoa(encodeURIComponent(name));
        const options = {}
        if ( file.fileType.toString().includes('image'))
            options.preview = `${this.downloadEndpoint_saved_before}?fileId=${file.fileId}`

        const id = this._uppy.addFile({
            id: file.fileId,
            name: file['fileNm'] ? file['fileNm'] : file.orgFileNm,
            type: file.fileType,
            data: {
                size: file.fileSize,
                lastModified: new Date(file['regDt']).getTime()
            },
            meta: {
                url: `${this.downloadEndpoint_saved_before}?fileId=${file.fileId}`,
                saved: !isTemp,
                fileId: file.fileId
            },
            ...options
        })

        this._uppy.setFileState(id, {
            progress: {
                uploadComplete: true,
                uploadStarted: true
            }
        })

        if(this.useThumbnailImg) {
            this.tempThumbnailUpload = file;
            this.thumbnailFileMap[file.fileId] = id;
            $(`#${this.thumbnailImgId}`)
                .attr("src", `${this.downloadEndpoint_saved_before}?fileId=${file.fileId}`)
                .attr("data-id", file.fileId);
        }
    }
}