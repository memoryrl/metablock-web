class Formbuilder {

    constructor(fields, metadata) {

        this._fields = fields;
        this._metadata = metadata;
        this.uploaders = {};
        this.imageUploaders = {};

        this._select2 = fields.filter(x => x.type === 'MULTISELECT');
        this._initSelect(this._select2);

        this._file = fields.filter(x => x.type === 'FILE');
        this._initFileUploaders(this._file);


        this._images = fields.filter(x => x.type === 'IMAGE');
        this._initImageUploaders(this._images);
    }

    validateFormBuilder(formId, obj = {}) {
        const isValidFields = this._validateFields(formId, obj);
        const isValidFiles = this._validateFiles(formId);
        return (isValidFields && isValidFiles);
    }

    _validateFields(formId, obj = {}) {
        let isValid = true;
        this._fields.forEach(field => {
            const { name, required, label, type } = field;
            const value = obj[name];
            const errorElement = document.querySelector(`#${formId} #${name}-error`);
            if (required && ("FILE" !== type && "IMAGE" !== type)) {
                if (value === undefined || value === null || value === '') {
                    if (errorElement) {
                        const message = `${label} 정보를 입력하세요.`
                        errorElement.textContent = message;
                    }
                    isValid = false;
                } else {
                    if (errorElement) errorElement.textContent = '';
                }
            }
        });
        return isValid;
    }

    _validateFiles(formId) {
        let isValid = true;
        this._fields.forEach(field => {
            const { name, required, label, type } = field;
            const errorElement = document.querySelector(`#${formId} #${name}-error`);
            if (required) {
                if("FILE" === type) {

                    const fileItems = $(`#${formId} #${name}-doc .uppy-Dashboard-Item`);
                    if(fileItems.length === 0) {
                        if (errorElement) errorElement.textContent = `${label} 정보는 필수 값입니다.`;
                        isValid = false;
                    } else {
                        if (errorElement) errorElement.textContent = '';
                    }

                } else if("IMAGE" === type) {

                    const fileItems = $(`#${formId} #${name}-image .uppy-Dashboard-Item`);
                    if(fileItems.length === 0) {
                        if (errorElement) errorElement.textContent = `${label} 정보는 필수 값입니다.`;
                        isValid = false;
                    } else {
                        if (errorElement) errorElement.textContent = '';
                    }

                }
            }
        });
        return isValid;
    }

    getFileObjs(obj) {

        // 이미지 업로더가 있다면 -> 정보 가져오기
        if(Object.keys(this.imageUploaders).length > 0) {
            Object.entries(this.imageUploaders).forEach(([key, value]) => {
                const uploader  = value;
                let FileAdd = [], FileDel = [];
                const uploadThumbnail = uploader.getTempThumbnailFile();
                if(uploadThumbnail.hasOwnProperty('fileId')) FileAdd.push(uploadThumbnail);
                FileDel = uploader.getRemoveFile();
                obj[key] = {
                    fileUid: uploader.getUid() === 0 ? null : uploader.getUid(),
                    fileAdd: FileAdd,
                    fileDel: FileDel
                }
            });
        }

        // 첨부파일이 있다면 -> 정보 가져오기
        if(Object.keys(this.uploaders).length > 0) {
            Object.entries(this.uploaders).forEach(([key, value]) => {
                const uploader  = value;
                const FileAdd = uploader.getTempFileUrls()
                const FileDel = uploader.getRemoveFileUrls()
                obj[key] = {
                    fileUid: uploader.getUid() === 0 ? null : uploader.getUid(),
                    fileAdd: FileAdd,
                    fileDel: FileDel
                }
            });
        }
    }

    _initSelect(list) {
        list.forEach(x => {
            $(`#${x.name}`).select2({
                placeholder: x.placeholder,
                language: {
                    noResults: () => "검색 결과가 없습니다."
                }
            });
        });
    }

    _initFileUploaders(list) {
        list.forEach(x => {
            const uploader = new Upload(`#${x.name}-doc`, '100%', 250);
            this.uploaders[x.name] = uploader;
            this._initUploader(x.name, uploader);
        });
    }

    _initUploader(name, uploader) {
        const fileUid = this._metadata.hasOwnProperty(name) ? this._metadata[name] : null
        FileUtil.loadEditFiles(fileUid, uploader);
        uploader.setUploadInfo(10);
    }

    _initImageUploaders(list) {
        list.forEach(x => {
            const uploader = new ThumbnailUpload(`#${x.name}-image`);
            this.imageUploaders[x.name] = uploader
            this._initThumbnail(x.name, uploader);
        });
        this._initImageUploaderButtons(list);
    }

    _initImageUploaderButtons(list) {
        list.forEach(x => {
            const uploader = this.imageUploaders[x.name];
            $(document).on('click', `#upload-btn-${x.name}`, function () {
                uploader.openFileUpload();
            });
            $(document).on('click', `#delete-btn-${x.name}`, function () {
                const targets = [
                    uploader.getTempThumbnailFile(),
                    uploader.getThumbnailStaticUpload()
                ];
                for (const file of targets) {
                    if (file.hasOwnProperty('fileId')) {
                        uploader.removeThumbnailFile(file, 'removed-by-user');
                    }
                }
            });
        });
    }

    _initThumbnail(name, uploader) {
        const fileUid = this._metadata.hasOwnProperty(name) ? this._metadata[name] : null
        FileUtil.loadEditFiles(fileUid, uploader);
        uploader.setThumbNail(`${name}-id-1`);
    }

}