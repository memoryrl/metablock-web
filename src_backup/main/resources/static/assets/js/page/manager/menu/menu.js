var menuJS = {

     menuForm: $("#menu-form")
    ,menuTree: new Tree("menu-tree", true, false)    // 노드 추가 가능 , 체크박스 미사용
    ,authorSelect: $("#author-select")

    ,initMenuTree: function () {
        menuJS.menuTree.drawTree("/cetus/api/menu/tree", () => {
            return { authorCd: $("#author-select").val() };
        });

        menuJS.menuTree.customAddFunction("메뉴 추가", (data) => {
            menuJS.menuAddFunction(data);
        });

        menuJS.menuTree.customTreeSelectEvent((e, d) => {
            menuJS.formReset();
            menuJS.menuSelectEvent(e, d);
        });
    }

    ,menuAddFunction: function (data) {
        var inst = $.jstree.reference(data.reference),
            obj = inst.get_node(data.reference);
        const menu = {
            authorCd: $("#author-select").val(),
            upperMenuNo: obj.id,
            menuNm: "새 메뉴",
            sortNo: obj.children.length + 1,
        };
        Http.post("/cetus/api/menu", menu, true).done((result) => {
            Util.alert("메뉴가 추가되었습니다.").then(() => {
                menuJS.formReset();
                menuJS.menuTree.treeRefresh();
            });
        })
        .fail(() => {
            Util.alert("메뉴를 추가하지 못했습니다.");
        });
    }

    ,menuSelectEvent: function (e, data) {
        let nodeData = data.node.data;
        let nodeDataKeys = Object.keys(nodeData);
        let formInputs = $("#menu-form input[type=text], textarea");

        // 메뉴 상세정보 조회
        $.each(formInputs, function (index, item) {
            let nodeDataKey = nodeDataKeys.find((item) => {
                return item == $(this).attr("name");
            });
            $(this).val(nodeData[nodeDataKey]);
        });

        // [메뉴명]
        $(`#menu-form #menu-nm`).val(nodeData['text']);

        // [사용 여부]
        $(`#menu-form [name=useAt][value=${nodeData.useAt}]`).prop("checked", true);

        // [루트 메뉴 코드]
        const rootMenuCd = nodeData['rootMenuCd']
        if(rootMenuCd && (rootMenuCd != null) && (rootMenuCd.length !== 0)) {
            $("#menu-form #rootMenuCd").val(`${nodeData['rootMenuCdNm']}(${rootMenuCd})`)
        }

        // [메뉴 스타일]
        $("#menu-form #menuStyle").val(nodeData['menuStyle']).attr('selected', 'selected');
        $("#menu-form #menuStyle1").val(nodeData.menuStyle1 ?? '');
        $("#menu-form #menuStyle2").val(nodeData.menuStyle2 ?? '');

        // [순서][메뉴 스타일 1,2][등록자, 수정자]
        $("#menu-form #menu-no").val(nodeData['no'])
        $("#reg-dt").text(nodeData.regDt ? `(${nodeData.regDt})` : "");
        $("#reg-nm").text(nodeData.regNm ?? "");
        $("#updt-dt").text(nodeData.updtDt ? `(${nodeData.updtDt})` : "");
        $("#updt-nm").text(nodeData.updtNm ?? "");

        // [수정][삭제] 버튼
        $("#update-btn, #delete-btn").css("visibility", "visible");
        $("#menuUrl-choose").prop('disabled', false);
    }

    ,menuDeleteEvent: function () {
        const datas = menuJS.menuTree.getSelectedData();
        const node = menuJS.menuTree.getSelectDataInfo(datas[0])
        Util.confirm("삭제하시겠습니까?").then((isOk) => {
            if (isOk) {
                if (node.children.length) Util.alert("하위 메뉴가 존재하여 메뉴를 삭제하지 못했습니다 ");
                else {
                    const menuNo = node['id']
                    Http.delete(`/cetus/api/menu/${menuNo}`)
                        .done((result) => {
                            Util.alert("관리자 메뉴가 삭제되었습니다.").then(() => {
                                menuJS.formReset();
                                menuJS.menuTree.treeRefresh();
                            });
                        })
                        .fail(() => {
                            Util.alert("관리자 메뉴를 삭제하지 못했습니다.");
                        });
                }
            }
        });
    }

    ,updateMenu: function () {
        FormDataToObj.getParameter("menu-form").then((obj) => {
            const menuNo = $("#menu-no").val()
            const programUid = menuPoplist.programUid
            Util.confirm("수정하시겠습니까?").then((isOk) => {
                if (isOk) {
                    const putData = { ...obj, programUid };
                    Http.put(`/cetus/api/menu/${menuNo}`, putData, true)
                        .done((result) => {
                            Util.alert("관리자 메뉴가 수정되었습니다.").then(() => {
                                menuJS.menuTree.treeRefresh();
                                menuJS.formReset();
                            });
                        })
                        .fail(() => {
                            Util.alert("관리자 메뉴를 수정하지 못했습니다.");
                        });
                }
            });
        });
    }

    ,formSubmit: function (element, event) {
        const form = $(element)[0];
        if (form.checkValidity() === false) {
            $(element).addClass("invalid");
        } else {
            menuJS.updateMenu();
        }
        event.preventDefault();
        $(element).addClass("was-validated");
    }

    ,formReset: function () {
        menuJS.menuForm[0].reset();
        $("#reg-dt, #reg-nm, #updt-dt, #updt-nm").text("");
        $("#menuUrl-choose").prop('disabled', true);
        menuJS.menuForm.removeClass("was-validated");
        menuPoplist.programUid = null;
    }
}