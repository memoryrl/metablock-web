var menuPoplist = {

     programDatatable: null
    ,programUid: null
    ,programInfo: ''

    ,makeTable: function () {
        menuPoplist.programDatatable = new Table("program-datatable")
            .get("/cetus/api/program/menu")
            .pageOptions([5, 7, 10, 15], 5)
            .param("browseText", () => $("#browseText").val())
            .param("useAt", () => "Y" )
            .scroll(true)
            .delUnUseParams()
            .add(new Column("uid").render((data, full) => {
                    return full.listIndex;
            }))
            .add(new Column("progrmNm").left())
            .add(new Column("url").left())
            .selectable()
            .select(function (data) {
                menuPoplist.programInfo = {...data};
            })
            .init();

        $(".dataTables_filter .form-control").removeClass("form-control-sm");
        $(".dataTables_length .form-select").removeClass("form-select-sm").removeClass("form-control-sm");
    }

    ,hideTableModal: function () {
        $("#browseText").val("");
        menuPoplist.programInfo = null;
        menuPoplist.programDatatable.destroy();
    }

    ,selectBtnClickEvent: function () {
        if (menuPoplist.programInfo) {
            menuPoplist.programUid = menuPoplist.programInfo.uid
            $("#url").val(menuPoplist.programInfo.url);
            $("#menuProgramModal").modal("hide");
        } else {
            Util.alert("프로그램 정보가 선택되지 않았습니다.");
        }
    }
}