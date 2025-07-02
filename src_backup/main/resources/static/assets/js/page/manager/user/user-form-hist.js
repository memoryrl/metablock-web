var userFormHist = {

    loginHistTable: null
    ,downloadHistTable: null
    ,statusHistTable: null

    ,initTable: function () {

        userFormHist.loginHistTable = new Table("login-hist-table")
            .pageOptions([5, 10, 15, 20], 5)
            .param("userUid", () => uid)
            .get("/cetus/api/login-hist")
            .delUnUseParams()
            .switchDiv()
            .add(new Column("loginDt").render((data, full) => full.listIndex))
            .add(new Column("loginDt").width("15%").render((data, full) => {
                const datetimeStr = data;
                if (!datetimeStr) return '';
                const [date, time] = datetimeStr.split(' ');
                return `${date}<br>${time}`;
            }))
            .add(new Column("loginIp"))
            .add(new Column("loginRegion"))
            .add(new Column("loginBrowser"))
            .add(new Column("loginAccessUrl"))
            .add(new Column("sessionId"))
            .init()
            .afterInit(() => {
                $("#login-hist-total-num").text(`총 ${userFormHist.loginHistTable._data.list.length} 건`)
            })
            .afterSubmit(() => {
                $("#login-hist-total-num").text(`총 ${userFormHist.loginHistTable._data.list.length} 건`)
            })

        userFormHist.downloadHistTable = new Table("download-hist-table")
            .pageOptions([5, 10, 15, 20], 5)
            .param("userUid", () => uid)
            .get("/cetus/api/file-log")
            .delUnUseParams()
            .switchDiv()
            .add(new Column("logUid").render((data, full) => full.listIndex))
            .add(new Column("regDt").width("15%").render((data, full) => {
                const datetimeStr = data;
                if (!datetimeStr) return '';
                const [date, time] = datetimeStr.split(' ');
                return `${date}<br>${time}`;
            }))
            .add(new Column("orgFileNm").left())
            .add(new Column("extension").center())
            .add(new Column("fileSize").center())
            .add(new Column("downloadUrl").left())
            .init()
            .afterInit(() => {
                $("#download-hist-total-num").text(`총 ${userFormHist.downloadHistTable._data.list.length} 건`)
            })
            .afterSubmit(() => {
                $("#download-hist-total-num").text(`총 ${userFormHist.downloadHistTable._data.list.length} 건`)
            })

        userFormHist.statusHistTable = new Table("status-hist-table")
            .pageOptions([5, 10, 15, 20], 5)
            .param("userUid", () => uid)
            .get("/cetus/api/user-status-hist")
            .delUnUseParams()
            .switchDiv()
            .add(new Column("histUid").render((data, full) => full.listIndex))
            .add(new Column("regDt").width("15%").render((data, full) => {
                const datetimeStr = data;
                if (!datetimeStr) return '';
                const [date, time] = datetimeStr.split(' ');
                return `${date}<br>${time}`;
            }))
            .add(new Column("statusNm").center().render((data, full) => {
                const color = (full['status'] === "APPROVED") ? "green" : (full['status'] === "WAIT") ? "blue" : "red"
                return `<span style="color: ${color}">${data}</span>`
            }))
            .add(new Column("reason").left())
            .add(new Column("regId").render((data, full) => {
                return `<span>${data}<br>(${full['regNm']})</span>`
            }))
            .init()
            .afterInit(() => {
                $("#status-hist-total-num").text(`총 ${userFormHist.statusHistTable._data.list.length} 건`)
            })
            .afterSubmit(() => {
                $("#status-hist-total-num").text(`총 ${userFormHist.statusHistTable._data.list.length} 건`)
            })

    }

    ,submitLoginHistTable: function () {
        userFormHist.loginHistTable.submit()
    }
    ,submitDownloadHistTable: function () {
        userFormHist.downloadHistTable.submit()
    }
    ,submitStatusHistTable: function () {
        userFormHist.statusHistTable.submit();
    }

    ,loginHistListDownload: function () {
        const obj = { 'userUid' : uid }
        $("#login-hist-list-download").attr('disabled', true);
        CREATE_USER_CSV('/cetus/api/login-hist/excel', obj)
    }

    ,downloadHistListDownload: function () {
        const obj = { 'userUid' : uid }
        $("#download-hist-list-download").attr('disabled', true);
        CREATE_USER_CSV('/cetus/api/file-log/excel', obj)
    }

    ,statusHistListDownload: function () {
        const obj = { 'userUid' : uid }
        $("#status-hist-list-download").attr('disabled', true);
        CREATE_USER_CSV('/cetus/api/user-status-hist/excel', obj)
    }
}