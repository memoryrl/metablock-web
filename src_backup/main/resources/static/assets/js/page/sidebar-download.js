var sidebarDownload = {

    index: 1,

    getSidebarDownloadList: function () {
        sidebarDownload.index = 1;
        Http.get(`/cetus/api/download-hist`).then((data) => {
            sidebarDownload.drawSidebarDownloadList(data)
        })
    }

    ,drawSidebarDownloadList: function (data) {
        var $list = $("#download-list-ul");
        $list.empty();

        if ( data === null || data.length === 0 ) {
            $list.append(
                $('<div/>').append(
                    $('<span/>').text('다운로드 항목이 없습니다.')
                )
            );
        } else {
            sidebarDownload.drawContent(data)
        }
    }

    ,drawContent: function (data) {
        var $list = $("#download-list-ul");
        $.each(data, function(index, item) {
            let listItem = $('<li/>');
            let downloadIndex = $('<div/>').addClass('download-index').append($('<span/>').text( sidebarDownload.index ));
            sidebarDownload.index = sidebarDownload.index + 1
            let downloadInfo = $('<div/>').addClass('download-info')
                .append(
                    $('<h4/>').append(
                        $('<a/>').attr('href', '#').addClass('download-link')
                            .attr('data-url', item['fileUrl']).text(item['orgFileNm'])
                    )
                )
                .append(
                    $('<div/>').addClass('download-spans').append($('<span/>').text(`* 다운로드 횟수 : ${item['downCnt']}`))
                );
            let downloadDelete = $('<div/>').addClass('download-delete')
                .append(
                    $('<a/>')
                        .attr('href', '#')
                        .addClass('delete-item')
                        .attr('data-uid', item['fileUid']) // UID 삽입
                        .append($('<i/>').addClass('pe-7s-trash')) // 아이콘 삽입
                );
            listItem.append(downloadIndex, downloadInfo, downloadDelete);
            $list.append(listItem);
        });
    }

    ,downloadFile: async function (fileUrl) {
        if (fileUrl) {
            const response = await fetch(fileUrl, {method: "POST"})
            if (response.status === 404) Util.alert("경로에 파일이 없습니다.")
            else {
                const disposition = response.headers.get("Content-Disposition")
                const matches = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/.exec(disposition)
                const filename = matches && matches[1] ? matches[1].replace(/['"]/g, "").replace("UTF-8", "") : ""
                if (!filename) {
                    Util.alert("다운로드 실패")
                    return
                }
                const blob = await response.blob()
                const link = document.createElement("a")
                link.href = window.URL.createObjectURL(blob)
                link.setAttribute("download", decodeURIComponent(filename))
                link.style.display = "none"
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link)
                sidebarDownload.getSidebarDownloadList();
            }
        }
    }

    ,deleteFile: function (fileUid) {
        Http.delete(`/cetus/api/custom-file/${fileUid}`).then(() => {
           sidebarDownload.getSidebarDownloadList();
        });
    }
}