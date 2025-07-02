var detail05 = {

    onloadIframe: function() {

        const iframe = document.getElementById('swagger-frame');
        const iframeDoc = iframe.contentDocument || iframe.contentWindow.document;
        // 상단 헤더 제거
        const style = document.createElement('style');
        style.innerHTML = `
                  .topbar, .information-container, .swagger-ui .scheme-container {
                    display: none !important;
                  }
                  .models {
                    display: none !important;
                  }
                  .swagger-ui .opblock-tag:hover {
                    background: transparent !important;
                    cursor: default !important;
                  }
                  button.expand-operation {
                    display: none !important;
                  }
    
                  body {
                    background: transparent !important;
                  }
    
                  .swagger-ui .opblock .opblock-summary-method,
                  .opblock-summary-path,
                  .nostyle,
                  .opblock-summary-description {
                    font-family: "Pretendard", "Noto Sans KR", sans-serif !important;
                  }
    
                  .renderedMarkdown {
                    font-size: 14px !important;
                    font-family: "Pretendard", "Noto Sans KR", sans-serif !important;
                  }
    
                  .swagger-ui .parameters-col_description input[type=text] {
                    font-size: 14px !important;
                    font-family: "Pretendard", "Noto Sans KR", sans-serif !important;
                  }
                  
                  button.arrow-btn {
                      background-color: transparent;
                      border: none;
                  }
    
                  .opblock-summary-path {
                    padding: 0 20px 0px 10px !important;
                    color: black !important;
                    font-weight: 500 !important;
                  }
    
                  .opblock-post:hover .opblock-summary-method,
                  .opblock-post:hover .opblock-summary-path,
                  .opblock-post:hover .opblock-summary-description {
                    font-weight: bold !important;
                  }
    
                  .opblock-put:hover .opblock-summary-method,
                  .opblock-put:hover .opblock-summary-path,
                  .opblock-put:hover .opblock-summary-description {
                    font-weight: bold !important;
                  }
    
                  .opblock-get:hover .opblock-summary-method,
                  .opblock-get:hover .opblock-summary-path,
                  .opblock-get:hover .opblock-summary-description {
                    font-weight: bold !important;
                  }
    
                  .opblock-delete:hover .opblock-summary-method,
                  .opblock-delete:hover .opblock-summary-path,
                  .opblock-delete:hover .opblock-summary-description {
                    font-weight: bold !important;
                  }
    
                  .opblock-post,
                  .opblock-put,
                  .opblock-get,
                  .opblock-delete {
                    border-radius: 8px !important;
                  }
    
                  .opblock-summary-description {
                    color: gray !important;
                  }
    
                  .swagger-ui .opblock-tag {
                    margin: 0 0 15px !important;
                  }
                  .swagger-ui .opblock-tag {
                    border-bottom: none !important;
                  }
                  .opblock-summary {
                    padding: 10px 10px 10px !important;
                  }
                  .opblock-summary-method {
                    border-radius: 20px !important;
                  }
    
                  .swagger-ui table tbody tr td:first-of-type {
                    min-width: 10em !important;
                  }
    
                  .parameters-container .table-container {
                    overflow-x: auto !important;
                    max-width: 100% !important;
                  }
                  .parameters-container .table-container table {
                    min-width: 600px !important;
                  }
                  @media (max-width: 768px) {
                    .table-container {
                      overflow-x: auto !important;
                    }
                  }
    
                  .responses-inner {
                    overflow-x: auto !important;
                    max-width: 100% !important;
                  }
                  .responses-inner table {
                    min-width: 600px !important;
                  }
                  @media (max-width: 768px) {
                    .responses-inner {
                      overflow-x: auto !important;
                    }
                  }
                `;
        iframeDoc.head.appendChild(style);

        // setTimeout(() => {
        //     const iframeDoc = document.getElementById("swagger-frame").contentDocument || document.getElementById("swagger-frame").contentWindow.document;
        //     const opblockTag = iframeDoc.querySelector('.opblock-tag')
        //     if(opblockTag) {
        //         opblockTag.click();
        //         opblockTag.style.pointerEvents = 'none';
        //     };

        //     const summaries = iframeDoc.querySelectorAll('.opblock-summary');

        //     summaries.forEach(summary => {
        //         // 이미 아이콘이 있으면 패스
        //         if (summary.querySelector('.arrow-icon')) return;

        //         // SVG 생성 함수
        //         const createIcon = (direction) => {
        //             const svg = iframeDoc.createElementNS("http://www.w3.org/2000/svg", "svg");
        //             svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
        //             svg.setAttribute("width", "16");
        //             svg.setAttribute("height", "16");
        //             svg.setAttribute("fill", "currentColor");
        //             svg.setAttribute("class", `bi arrow-icon ${direction === 'down' ? 'bi-chevron-down' : 'bi-chevron-up'}`);
        //             svg.setAttribute("viewBox", "0 0 16 16");

        //             const path = iframeDoc.createElementNS("http://www.w3.org/2000/svg", "path");
        //             path.setAttribute("fill-rule", "evenodd");
        //             path.setAttribute("d", direction === 'down'
        //                 ? "M1.646 4.646a.5.5 0 0 1 .708 0L8 10.293l5.646-5.647a.5.5 0 0 1 .708.708l-6 6a.5.5 0 0 1-.708 0l-6-6a.5.5 0 0 1 0-.708"
        //                 : "M7.646 4.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1-.708.708L8 5.707l-5.646 5.647a.5.5 0 0 1-.708-.708z"
        //             );

        //             svg.appendChild(path);
        //             return svg;
        //         };

        //         // 기본 아이콘 추가 (down)
        //         const icon = createIcon('down');
        //         summary.appendChild(icon);

        //         // 클릭 이벤트로 아이콘 토글
        //         summary.addEventListener('click', () => {
        //             const currentIcon = summary.querySelector('.arrow-icon');
        //             const isExpanded = summary.parentElement.classList.contains('is-open');

        //             // 아이콘 교체
        //             if (currentIcon) currentIcon.remove();
        //             summary.appendChild(createIcon(isExpanded ? 'down' : 'up'));
        //         });
        //     });

        // }, 2500);
    }

}