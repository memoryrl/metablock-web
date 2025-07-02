class FileUtil {

    static loadEditFiles(uid, upload) {
        const file = { container: $('.file-container'), textEmpty: '첨부된 파일이 없습니다.' }
        if ( uid ) {
            upload.setUid(uid)
            Http.get('/cetus/files/list.json', {fileUid : uid}).then(res => {
                if ( res.data && Array.isArray(res.data) ) {
                    if ( res.data.length > 0 ) {
                        file.container.html('')
                        res.data.forEach(x => upload.addStaticFile(x, x.saved !== 'Y'))
                    }
                    else file.container.html(file.textEmpty)
                }
            })
        }
        else {
            upload.setUid(0)
            file.container.html(file.textEmpty)
        }
    }

    static async loadEditFilesViewMode(uid, domId) {

        const file = {textEmpty: '첨부된 파일이 없습니다.'}
        if (uid) {

            const res = await Http.get('/cetus/files/list.json', {fileUid: uid});
            if(res.data && Array.isArray(res.data)) {
                if (res.data.length > 0) {
                    res.data.forEach(x => {
                        this.createViewFileItem(x, $(`#${domId}`))
                    })
                } else $(`#${domId}`).html(file.textEmpty)
            }
        } else {
            $(`#${domId}`).html(file.textEmpty)
        }
    }

    static createViewFileItem(file, dom) {
        const filed = `<div class="addfile_item">
                                <div class="subject">
                                    <span class="name">
                                        <a href="javascript:void(0);" onclick="FileUtil.fileDownload('${file.fileId}');return false;" download="" />
                                            ${file.orgFileNm}
                                    </span>
                                </div>
                            </div>`
        dom.append(filed)
    }

    static async fileDownload(fileId) {
        const response = await Http.get(`/cetus/files/check-file`, {"fileId": fileId})
        if (!response) Util.alert("해당 경로에 파일이 없습니다.")
        else {
            window.location.href = `/cetus/files/download?fileId=${fileId}&download=Y`
        }
    }

}