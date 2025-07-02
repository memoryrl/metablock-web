// ckeditor5-config.js
import {
    ClassicEditor,
    Alignment,
    AutoLink,
    Autosave,
    BlockQuote,
    Bold,
    Code,
    CodeBlock,
    Essentials,
    FontBackgroundColor,
    FontColor,
    FontFamily,
    FontSize,
    GeneralHtmlSupport,
    Heading,
    HorizontalLine,
    ImageBlock,
    ImageInline,
    ImageInsert,
    ImageInsertViaUrl,
    ImageResize,
    ImageStyle,
    ImageToolbar,
    ImageUpload,
    ImageUploadProgress,
    Indent,
    IndentBlock,
    Italic,
    Link,
    List,
    Paragraph,
    SelectAll,
    SimpleUploadAdapter,
    Strikethrough,
    Table,
    TableColumnResize,
    TableToolbar,
    Underline,
    Undo
} from 'ckeditor5';

import koTranslations from '/assets/ckeditor/ko.js';

const editorConfig = {
    toolbar: {
        items: [
            'undo',
            'redo',
            '|',
            'heading',
            '|',
            'fontSize',
            'fontFamily',
            'fontColor',
            'fontBackgroundColor',
            '|',
            'bold',
            'italic',
            'underline',
            'code',
            'strikethrough',
            '|',
            'horizontalLine',
            'link',
            'insertImage',
            'insertTable',
            'blockQuote',
            /*'codeBlock',*/
            '|',
            'alignment',
            '|',
            'bulletedList',
            'numberedList',
            'indent',
            'outdent'
        ],
        shouldNotGroupWhenFull: false
    },
    plugins: [
        Alignment,
        AutoLink,
        Autosave,
        BlockQuote,
        Bold,
        Code,
        CodeBlock,
        Essentials,
        FontBackgroundColor,
        FontColor,
        FontFamily,
        FontSize,
        GeneralHtmlSupport,
        Heading,
        HorizontalLine,
        ImageBlock,
        ImageInline,
        ImageInsert,
        ImageInsertViaUrl,
        ImageResize,
        ImageStyle,
        ImageToolbar,
        ImageUpload,
        ImageUploadProgress,
        Indent,
        IndentBlock,
        Italic,
        Link,
        List,
        Paragraph,
        SelectAll,
        SimpleUploadAdapter,
        Strikethrough,
        Table,
        TableColumnResize,
        TableToolbar,
        Underline,
        Undo
    ],
    fontFamily: {
        supportAllValues: true
    },
    fontSize: {
        options: [10, 12, 14, 'default', 18, 20, 22],
        supportAllValues: true
    },
    heading: {
        options: [
            {
                model: 'paragraph',
                title: 'Paragraph',
                class: 'ck-heading_paragraph'
            },
            {
                model: 'heading1',
                view: 'h1',
                title: 'Heading 1',
                class: 'ck-heading_heading1'
            },
            {
                model: 'heading2',
                view: 'h2',
                title: 'Heading 2',
                class: 'ck-heading_heading2'
            },
            {
                model: 'heading3',
                view: 'h3',
                title: 'Heading 3',
                class: 'ck-heading_heading3'
            },
            {
                model: 'heading4',
                view: 'h4',
                title: 'Heading 4',
                class: 'ck-heading_heading4'
            },
            {
                model: 'heading5',
                view: 'h5',
                title: 'Heading 5',
                class: 'ck-heading_heading5'
            },
            {
                model: 'heading6',
                view: 'h6',
                title: 'Heading 6',
                class: 'ck-heading_heading6'
            }
        ]
    },
    htmlSupport: {
        allow: [
            {
                name: /^.*$/,
                styles: true,
                attributes: true,
                classes: true
            }
        ]
    },
    image: {
        toolbar: ['imageTextAlternative', '|', 'imageStyle:inline', 'imageStyle:wrapText', 'imageStyle:breakText', '|', 'resizeImage']
    },
    initialData: '',
    link: {
        addTargetToExternalLinks: true,
        defaultProtocol: 'https://',
        decorators: {
            toggleDownloadable: {
                mode: 'manual',
                label: 'Downloadable',
                attributes: {
                    download: 'file'
                }
            }
        }
    },
    placeholder: '',
    table: {
        contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells']
    }
    // ,extraPlugins: [MyCustomUploadAdapterPlugin]
};

window.ClassicEditor = ClassicEditor;
window.editorConfig = editorConfig;