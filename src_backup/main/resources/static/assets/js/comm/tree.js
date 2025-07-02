class Tree {

    /**
    * @param id {string} : div-id
    * @param can_add {boolean} : 트리에 속성 추가 가능 여부
    * @param use_checkbox {boolean} : 체크박스 사용 여부
    * */
    constructor(id, can_add = false, use_checkbox = true) {
        this.id = id;
        this._treeDiv = $(`#${id}`)
        this.can_add = can_add;
        this.use_checkbox = use_checkbox;

        this._addFunction = null;
        this.add_text = "메뉴 추가";

        // 선택, 미선택 이벤트
        this._treeSelectEvent = null;
        this._treeDeSelectEvent = null;

        // 가장 마지막 leaf 만 선택 가능 여부
        this.only_select_leaf = false;
        this._ifNotLeafEvent = null;

        this.initValues = []                // node 생성시 미리 선택될 node 세팅
    }

    onlySelectLeaf(callback) {
        this.only_select_leaf = true;
        this._ifNotLeafEvent = callback;
        return this;
    }

    customAddFunction(add_text = "메뉴 추가", callback) {
        this.add_text = add_text;
        this._addFunction = callback;
        return this;
    }

    customTreeSelectEvent(callback) {
        this._treeSelectEvent = callback;
        return this;
    }

    customTreeDeSelectEvent(callback) {
        this._treeDeSelectEvent = callback;
        return this;
    }

    deSelectAllTree() {
        this._treeDiv.jstree('deselect_all');
        return this;
    }

    destroyTree() {
        const oldTree = this._treeDiv.jstree(true);
        if(oldTree) {
            oldTree.destroy();
        }
        return this;
    }

    initNode(values = []) {
        this.initValues = values;
        return this;
    }

    selectNode(value) {
        this._treeDiv.jstree('select_node', `#${value}`);
        return this;
    }

    selectNodes( values = [] ) {
        values.forEach(value => {
            this._treeDiv.jstree('select_node', `#${value}`);
        });
        return this;
    };

    drawTree(url, param = {}) {

        const plugins = ["wholerow", "contextmenu"]; // 기본 플러그인
        if (this.use_checkbox) plugins.push("checkbox"); // 조건부로 checkbox 추가

        this._treeDiv.jstree({
            core: {
                check_callback: true,
                data: (obj, callback) => {
                    const actualParam = typeof param === "function" ? param() : param;
                    this._getTreeData(url, actualParam, obj, callback)
                },
            },
            plugins: plugins,
            contextmenu: {
                items: ($node) => {
                    const items = {};
                    if ( this.can_add && typeof this._addFunction === 'function' ) {
                        items.Create = {
                            separator_before: false,
                            separator_after: false,
                            label: this.add_text,
                            action: (data) => {
                                // 사용자 정의 동작 실행
                                this._addFunction(data);
                            },
                        };
                    }
                    return items;
                },
            },
        });

        const initValues = this.initValues;
        this._treeDiv.on('loaded.jstree', function () {
            if(initValues.length !== 0) {
                initValues.forEach(value => {
                    $(this).jstree('select_node', `#${value}`);
                });
            }
        });

        /* 트리 체크 선택 이벤트 */
        this._treeDiv.on("select_node.jstree", (e, d) => {
            if (this.only_select_leaf) {
                const tree = this._treeDiv.jstree(true);
                const isLeaf = tree.is_leaf(d.node);

                if (!isLeaf) {
                    tree.deselect_node(d.node);
                    this._ifNotLeafEvent();
                    return; // leaf가 아니면 이후 로직은 실행 안 함
                }
            }
            // 트리 체크박스 클릭 이벤트
            if(this._treeSelectEvent) {
                this._treeSelectEvent(e, d);
            }
        })

        /* 트리 체크 선택 해제 이벤트 */
        this._treeDiv.on("deselect_node.jstree", (e, d) => {
            // 트리 체크박스 해제 이벤트
            if(this._treeDeSelectEvent) {
                this._treeDeSelectEvent(e, d);
            }
        })

        return this;
    }

    _getTreeData(url, param, obj, callback) {;
        Http.get(url, param).done((result) => {
            let dataList = result;
            let dataMap = {};
            let data = [];
            for (let i = 0; i < dataList.length; i++) {
                const node = {
                    id: dataList[i]['no'],
                    text: dataList[i]['text'],
                    data: dataList[i],
                    state: {
                        opened: true,
                    },
                };
                dataMap[node.id] = node;

                if (!dataList[i]['upperNo']) {
                    // 루트일 때
                    node.children = [];
                    data.push(node);
                    continue;
                } else {
                    var parent = dataMap[node.data.upperNo];
                    if (!parent) {
                        continue;
                    }
                    if (!parent.children) {
                        parent.children = [];
                    }
                    parent.children.push(node);
                }
            }
            callback(data);
        })
        .fail(() => {
            Util.alert("메뉴를 불러오지 못했습니다.");
        });
    }

    getSelectedData() {
        return this._treeDiv.jstree("get_selected") ?? []
    }


    getSelectDataInfo(node) {
        return this._treeDiv.jstree("get_node", `#${node}`);
    }

    treeRefresh() {
        this._treeDiv.jstree("refresh");
    }

}