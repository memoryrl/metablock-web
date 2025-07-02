/***
 * 여러개의 파일 업로드를 위한 업로더
 * */
class Upload {
    constructor(selector, width = '100%', height = 304) {
        this._selector = selector
        this._scrollbar = null
        this._removes = []
        this._uid = null
        this._width = width
        this._height = height
        this.uploadCnt = 100  // 최대 업로드 개수
        this.uploadTy = ['zip', 'text', 'pptx', 'xls', 'ppt', 'xlsx', 'jpg', 'hwp' ,'pdf', 'doc' ,'jpeg', 'png', 'gif', 'txt', 'csv', 'mp4', 'json']  // 업로드 가능 확장자
        this.tempUploadCnt = 0
        this.uploading = false

        this.checkUploadTotalSize = false;
        this.uploadMaxSize = 5              // 최대 업로드 용량 (기본 MB) -> 5 MB
        this.uploadMaxTy = 'MB'             // 업로드 기본 단위
        this.uploadMaxValue = 1024 * 1024   // 1MB in Bytes
        this.uploadTotalSize = 0;           // 총 업로드 용량

        this.currentfiles = {};

        this._checkUploadPixel = false
        this._pixelWidth = 0
        this._pixelHeight = 0

        this._hiddenSuccessBar = null

        this.isAutoProceed = true
        this._isVariableEndpoint = false;

        if(this._isVariableEndpoint){
            this.uploadEndpoint   = "/api/_fs_tus/file/upload";
            this.removeEndpoint   = "/api/_fs_tus/file/storagetemp";
            this.downloadEndpoint = "/api/_fs_tus/file/download";
        }else{
            this.uploadEndpoint   = "/api/tus/file/upload";
            this.removeEndpoint   = "/api/tus/file/storagetemp";
            this.downloadEndpoint = "/api/tus/file/download";
        }

        this.jwtToken = this.getJwtToken();
        this._uploadSettings()
    }

    static uploadWithHeightWidth(selector, width = '100%', height = 304) {
        return new Upload(selector, false, true, width, height)
    }

    _uploadSettings() {
        const {Dashboard, Tus} = Uppy
        this._uppy = new Uppy.Uppy({
            locale, // uppy.locale.js
            autoProceed: this.isAutoProceed,
            onBeforeFileAdded: (file) => {
                const fileExtension = file.extension
                const tempTotalSize = this.uploadTotalSize + file.data.size

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

                    if ( this.checkUploadTotalSize && ((this.uploadMaxSize * this.uploadMaxValue) < tempTotalSize)) {
                        const maxSizeInByte = this.uploadMaxSize * this.uploadMaxValue;
                        const maxSizeInMb = Math.floor(maxSizeInByte / (1024 * 1024));
                        Util.alert(`최대 총 업로드 가능 용량은 ${maxSizeInMb} MB입니다.`);
                        return false;
                    }
                }

                // 기존 파일 다시 업로드
                if (this.uploading) {

                    if (this.tempUploadCnt === this.uploadCnt) {
                        Util.alert(`최대 업로드 가능한 파일 개수는 ${this.uploadCnt}개입니다. <br> 확인해주세요.`)
                    }

                    if (!this.uploadTy.includes(fileExtension)) {
                        Util.alert("업로드 불가능한 확장자가 있습니다. <br> 확인해주세요.")
                    }

                    if ( this.checkUploadTotalSize && ((this.uploadMaxSize * this.uploadMaxValue) < tempTotalSize)) {
                        const maxSizeInByte = this.uploadMaxSize * this.uploadMaxValue;
                        const maxSizeInMb = Math.floor(maxSizeInByte / (1024 * 1024));
                        Util.alert(`최대 총 업로드 가능 용량은 ${maxSizeInMb} MB입니다. <br> 확인해주세요.`);
                    }

                }

                if (this._uid != null) {
                    this.tempUploadCnt++
                    this.uploadTotalSize = this.uploadTotalSize + file.data.size
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
            endpoint: this.uploadEndpoint, //this.uploadEndpoint,
            onShouldRetry: this._onShouldRetry,
            chunkSize: 50000000,
            //retryDelays: [0, 1000, 3000, 5000],  //default
            headers: {
                'X-TUS-UUID': this.upload_uuid     //로드밸런싱에 사용하도록
                , 'Authorization': this._isVariableEndpoint ? "Bearer " + this.jwtToken : ""
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
        });

        this._uppy.on('upload', () => {
            // 해당 시점은 파일 업로드 시작 시점에 무조건 실행
        });


        this._uppy.on('file-added', (file) => {
            // 파일 추가 시점
            const _this =  this
            if( _this._checkUploadPixel && !file.meta.saved ) {
                const img = new Image();
                img.src = (file.preview) ? file.preview : URL.createObjectURL(file.data);
                img.onload = () => {
                    if ( (img.width >= _this._pixelWidth) || (img.height > _this._pixelHeight) ) {
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
            this._updateUI()
            file.meta.saved = false
            if (!file.meta.saved)
                file.meta.name = file.name

            if( (this._hiddenSuccessBar) && (this._hiddenSuccessBar.length > 0) ) {
                setTimeout(() => {
                    const uppys = $(`#${this._hiddenSuccessBar} .uppy-Root .uppy-Dashboard .uppy-Dashboard-inner .uppy-Dashboard-innerWrap .uppy-Dashboard-progressindicators .uppy-StatusBar`)
                    uppys.attr("aria-hidden", "true");
                }, 1000);
            }
        });

        // 전체 업로드 파일에 대해 '한번에' 처리
        this._uppy.on('complete', (result) => {
            this.uploading = false;
            this.currentfiles = {};
        });

        // 파일 삭제 시점
        this._uppy.on('file-removed', (file, reason) => {
            this.uploadTotalSize = this.uploadTotalSize - file.data.size
            this._removeFile(file, reason)
            this._updateUI()
        })

        this._uppy.on('error', (error) => {
            console.error(error.stack);
        });
    }

    getJwtToken(){
        if(this._isVariableEndpoint){
            let res = Http.getSync('/cetus/files/bigfile/jwttoken.json');
            if(res != null){
                return res.data.jwtToken;
            }
        }
        return null;
    }

    checkUploadPixel(width = 0, height = 0){
        this._checkUploadPixel = true
        this._pixelWidth = width
        this._pixelHeight = height
    }

    getFiles() {
        return this._uppy.getFiles();
    }

    getUUID() { // UUID v4 generator in JavaScript (RFC4122 compliant)
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 3 | 8);
            return v.toString(16);
        });
    }

    /**
     * @param uploadCnt 업로드 가능한 첨부파일 최대 개수
     * @param uploadMaxTy 업로드 가능한 확장자
     */
    setUploadInfo(uploadCnt = 100, uploadTy = ['zip', 'text', 'pptx', 'xls', 'ppt', 'xlsx', 'jpg', 'hwp' ,'pdf', 'doc' ,'jpeg', 'png', 'gif', 'txt', 'csv', 'mp4', 'json']) {
        this.uploadCnt = uploadCnt
        this.uploadTy = uploadTy
    }

    /**
     * @param uploadMaxSize 업로드 가능한 최대 사이즈
     * @param uploadMaxTy 최대 사이즈에 따른 확장자
     */
    setUploadSize(uploadMaxSize = 0, uploadMaxTy = 'MB'){
        this.checkUploadTotalSize = true;
        this.uploadMaxSize = uploadMaxSize;
        this.uploadMaxTy = uploadMaxTy
        switch (uploadMaxTy) {
            case 'B':
                this.uploadMaxValue = 1;
                break;
            case 'KB':
                this.uploadMaxValue = 1024;
                break;
            case 'MB':
                this.uploadMaxValue = Math.pow(1024, 2); // 1024 * 1024
                break;
            case 'GB':
                this.uploadMaxValue = Math.pow(1024, 3);
                break;
            case 'TB':
                this.uploadMaxValue = Math.pow(1024, 4);
                break;
            default:
                this.uploadMaxValue = 1; // 기본값 B
        }
    }

    /*
    * 첨부파일에 대한 조건 체크
    * */
    checkFileValidation() {
        const validation1 = (this.tempUploadCnt <= this.uploadCnt)
        // 추후 확장자 validation 추가?
        const validation3 = (this.uploadMaxSize * this.uploadMaxValue >= this.uploadTotalSize)
        return (validation1 && validation3);
    }

    /*
    * 현재까지 업로드 총 용량 구하기
    * */
    getUploadTotalSize() {
        return this.uploadTotalSize;
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


    _getUI() {
        const obj = $(this._selector).find('.uppy-Dashboard-files > div[role="presentation"]')
        return obj.length ? obj[0] : null
    }

    _onLoad() {
        const ui = this._getUI()
        this._scrollbar = new PerfectScrollbar(ui)
        $(this._selector).find('.uppy-DashboardContent-back').remove()
    }

    _updateUI() {
        const ui = this._getUI()
        if ( ui ) {
            if ( this._scrollbar )
                this._scrollbar.update()
            else this._onLoad()
        }
        else {
            if ( this._scrollbar )
                this._scrollbar.destroy()
            setTimeout(this._onLoad, 500)
        }
    }

    // uppy가 자동으로 해당 url을 이용해서 삭제명령을 tus를 통해서 보내고
    // ,삭제가 완료되면 아음 명령을 수행하도록 한다.
    _removeFile(file, reason) {
        if ( reason === 'removed-by-user' ) {
            this.tempUploadCnt --

            if(file.meta.saved === undefined){

                // 1. upload 진행중에 중단하고 삭제하는 경우

            } else if ( file.meta.saved == false){

                // 2. 등록되기 전에 임시폴더에 있는 경우 삭제
                if(file.meta.fileId)
                    Http.delete(`${this.removeEndpoint}/${file.meta.fileId}`);

            }else{
                // 3. 등록된 데이터의 경우 서버에서 삭제 필요함
                this._removes.push({	orgFileNm: file.meta.name,
                    fileType : file.meta.type,
                    fileId   : file.meta.fileId,
                    saved    : file.meta.saved == false ? 'N':'Y',
                    extension: file.extension
                });
            }
        }
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
     * 임시 저장된 파일정보 CommonFile 리스트
     */
    getTempFileUrls() {
        let allFiles = this._uppy.getFiles();
        let tempFiles =  allFiles.filter(x => !x.meta.saved).map(x => ({
            orgFileNm: x.meta.name,
            fileType : x.meta.type,
            fileId   : x.meta.fileId,
            saved    : x.meta.saved  == false ? 'N':'Y',
            extension: x.extension
        }));
        return tempFiles;
    }

    /**
     * 파일 URL 리스트
     * @returns {string[]}
     */
    getFileUrls() {
        return this._uppy.getFiles().map(x => x.meta.url)
    }

    /**
     * 삭제 파일 URL 리스트
     * @returns {string[]}
     */
    getRemoveFileUrls() {
        return this._removes
    }

    /**
     * 수정된 내용이 있는지
     * @returns {boolean}
     */
    isModified() {
        return this._uppy.getFiles().length > 0 || this._removes.length > 0
    }

    /**
     * 모든 업로드가 완료되었는지
     * @returns {boolean}
     */
    isCompleted() {
        return this._uppy.getFiles().reduce((acc, cur) => acc && cur.progress.uploadComplete, true)
    }

    /**
     * 임시 파일이 있는지
     * @returns {boolean}
     */
    hasTempFiles() {
        return this._uppy.getFiles().reduce((acc, cur) => acc || !cur.meta.saved, false)
    }

    /**
     * 초기화
     */
    reset() {
        if ( this._uppy ) {
            this._uppy.getFiles().forEach(x => this._uppy.removeFile(x.id))
            this._removes = []
        }
    }

    /**
     * 파일 추가
     * @param file 전체 파일 데이터
     * @param isTemp 임시 파일 여부(file.saved !== 'Y')
     */
    addStaticFile(file, isTemp) {
        this.uploading = true;
        const name = file.fileNm ? file.FileNm : file.orgFileNm
        const b64nm = btoa(encodeURIComponent(name));
        const options = {}
        if ( file.fileType.toString().includes('image'))
            options.preview = `${this.downloadEndpoint}/${file.filePath}/${b64nm}`

        const id = this._uppy.addFile({
            id: file.fileId,
            name: file.fileNm ? file.FileNm : file.orgFileNm,
            type: file.fileType,
            data: {
                size: file.fileSize,
                lastModified: new Date(file.regDt).getTime()
            },
            meta: {
                url: `${this.downloadEndpoint}/${file.filePath}/${b64nm}`,
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
    }
}