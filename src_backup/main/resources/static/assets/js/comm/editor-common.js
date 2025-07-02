class Editor {

    /**
    * @param id {string} : div id 값
    * @param content {string} : ckeditor 컨텐츠 내용
    * @param use_uploader {boolean} : 파일 업로더 사용여부
    * */
    constructor(id, content = '', use_uploader = true) {
        this.id = id;
        this.content = content;
        this.use_uploader = use_uploader;
        this.ckeditor = null;
    }


    async init(isViewMode = false) {

        const config = this.use_uploader ? editorConfig : editorConfig2;

        if(this.use_uploader) {
            config.extraPlugins = [
                function(editor) {
                    editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
                        return new MyUploadAdapter(loader)
                    }
                }
            ]
        }

        try {

            // 공통 CKEditor 초기화
            this.ckeditor = await ClassicEditor.create(document.querySelector(`#${this.id}`), config);

            // 에디터 내용 설정
            this.ckeditor.setData(this.content);

            // 뷰 모드일 경우 처리
            if (isViewMode) {
                const toolbarElement = this.ckeditor.ui.view.toolbar.element;
                if (toolbarElement) {
                    toolbarElement.style.display = 'none';
                }
                this.ckeditor.enableReadOnlyMode(this.id);
            }

            return this; // Editor 인스턴스 리턴

        }  catch (error) {

            console.error(`CKEditor(${this.id}) 초기화 오류:`, error);
            return null;

        }

    }

    getEditor() {
        return this.ckeditor;
    }

    getEditorData() {
        return this.ckeditor?.getData?.() || "";
    }

    setEditorData(data) {
        if (this.ckeditor) {
            this.ckeditor.setData(data);
        }
    }

    static getFirstParagraphText(html) {
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');
        const firstP = doc.querySelector('p');
        if (firstP) {
            return firstP.textContent.trim();
        }
        return '';
    }
}