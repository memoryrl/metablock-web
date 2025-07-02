class Table {
    _resetVariables(id) {
        this._id = id
        this._empty = {data: null, defaultContent: ''}
        this._order = [1, 'desc']
        this._useOrdering = false                   // 헤더를 통한 소팅 사용 여부
        this._afterInit = null
        this._afterSubmit = null
        this._url = ''
        this._index = 0
        this._columns = []
        this._columnDefs = []
        this._element = $('#' + id)
        this._scroll = {}
        this._nowPage = 1
        this._cntPerPage = 10
        this._data = []
        this._params = []
        this._lengthMenu = [10, 25, 50, 100]
        this._paging = true
        this._info = true
        this._lengthChange = true
        this._pagingType = "full_numbers"
        this._selected = null
        this._onSelect = null
        this._margin = 1
        this._submitData = {}
        this._lastSubmitData = {}
        this._doFirstSubmit = false
        this._checkBaseVar = ''
        this._checkboxes = []
        this._ajaxConfig = {}
        this._fixedConfig = {}
        this._infoText = '전체'
        this._lastOrderColumn = ''
        this._wait4resize = false
        this._useLoading = false
        this._spinner = null
        this._backdrop = null
        this._resizeTimer = null
        this.listenerResetCallbacks = []

        // table div 설정
        this._switchDiv = false             // [총 x건] 숨기기 + 좌측 아래 {lengthMenu} + 우측 아래 페이징 번호
        this._hideAll = false               // 전부 숨기기

        this._options = {}
        this._submitColumns = false;
        this._delUnUseParams = false;       // [param]으로 넘길때 사용하지 않는 컬럼들 제거

        this._customChkBoxTable = false;
        this._checkboxData = [];
    }

    constructor(id) {
        this._resetVariables(id)
        this._table = null

        $('.dataTables_filter .form-control').removeClass('form-control-sm');
        /*$('.dataTables_length .form-select').removeClass('form-select-sm').removeClass('form-control-sm');*/
    }

    options(options) {
        this._options = options
        return this
    }

    useOrder() {
        this._useOrdering = true
        return this
    }


    switchDiv() {
        this._switchDiv = true
        return this
    }

    hideAll() {
        this._hideAll = true
        return this
    }

    customTable(checkboxData = [""]) {
        this._customChkBoxTable = true
        this._checkboxData = checkboxData;
        return this;
    }

    delUnUseParams() {
        this._delUnUseParams = true
        return this
    }

    _dom() {

        // 모두 숨기기
        if (this._hideAll) {
            return '<"d-flex justify-content-between mx-' + this._margin + ' row my-1 align-items-center"'
                + '>'
                + 'rt'
                + '<"d-flex justify-content-between mx-' + this._margin + ' row my-1 align-items-center"'
                + '<"col-md-6 col-12">'
                + '<"col-md-6 col-12">'
                + '>'
        }

        // [총 x건] 숨기기 + 좌측 아래 {lengthMenu} + 우측 아래 페이징 번호
        if (this._switchDiv) {
            return '<"d-flex justify-content-between mx-' + this._margin + ' row my-1 align-items-center"'
                + '>'
                + 'rt'
                + '<"d-flex justify-content-between mx-' + this._margin + ' row my-1 align-items-center"'
                + '<"col-md-6 col-12"l>'
                + '<"col-md-6 col-12"p>'
                + '>'
        }

        // (기본)
        return `<"d-flex justify-content-between mx-${this._margin} row my-1 align-items-center"`
            + '<"col-md-6 col-12"l>'
            + '<"col-md-6 col-12 dataTables_info lc-layouts text-secondary text-md-end">'
            + '>'
            + 'irt'
            + '<"d-flex justify-content-between mx-' + this._margin + ' row my-1 align-items-center"'
            + '<"col-12"p>'
            + '>'
    }

    _language(options) {
        return {
            info: `${this._infoText} _TOTAL_건`,
            zeroRecords: `검색 결과가 없습니다.`,
            infoEmpty: `${this._infoText} 0건`,
            lengthMenu: '보기 _MENU_',
            infoFiltered: '',
            paginate: {
                previous: '<i class="ti-angle-left">',
                next: '<i class="ti-angle-right">',
                first: '<i class="ti-angle-double-left">',
                last: '<i class="ti-angle-double-right">',
            },
            processing: '<i class="fa fa-spinner fa-spin fa-3x fa-fw"></i> ',
            ...options
        }
    }

    _selection() {
        return {
            style: 'multi',
        }
    }

    _config() {
        /**
         *  테이블의 소팅 능력 사용여부를 결정할 수 있다 : this._userOrdering (default: false)
         *  => 컬럼 생성시에 [this.def.orderable = false]이 기본으로 들어가도록 설정했다.
         *  => 특정 컬럼에 대한 소팅을 사용하기 위해선 [column]에 대해 .order() 를 이용한다.
         */
        const _this = this
        return {
            autoWidth: false,
            paging: this._paging,
            info: this._info,
            lengthChange: this._lengthChange,
            ordering: this._useOrdering,
            dom: this._dom(),
            language: this._language(),
            select: this._selection(),
            order: this._order,
            pagingType: this._pagingType,
            ...this._scroll,
            ...this._options,
            initComplete() {
                if (_this._afterInit != null)
                    _this._afterInit(_this)

                // const resizeListener = () => this.resizeColumns()
                // window.addEventListener('resize', resizeListener)
                // _this.listenerResetCallbacks.push(() => window.removeEventListener('resize', resizeListener))
            },
        }
    }

    _initCheckboxes() {
        this._checkboxes = []

        const $box = this._element.find('.lc-check')
        const $all = $(`#${this._id}_wrapper .dataTables_scrollHead`).find('.lc-check-all')
        const $columnbox = this._element.find(".lc-check-column");

        $all.off('click')
        if ($all.prop('checked')) $all.click()

        const _this = this

        const allClickListener = function () {
            const checked = $(this).prop('checked')
            $box.prop('checked', checked)
            $columnbox.length ? $columnbox.prop("checked", checked) : ""
            _this._checkboxes = checked ? [..._this.values()] : []
        }
        const boxChangeListener = function (e) {
            const num = String($(this).data('num'))
            const row = _this.values().find(x => String(x[_this._checkBaseVar]) === num)

            const checked = $(this).prop("checked")
            checked
                ? _this._checkboxes.push(row)
                : (_this._checkboxes = _this._checkboxes.filter((x) => String(x[_this._checkBaseVar]) !== num));

            if ($columnbox.length) {
                const columnbox = $columnbox.filter((index, x) => {
                    return String($(x).data("rownum")) === num
                })
                columnbox.prop("checked", checked);
            }

            $all.prop('checked', _this._checkboxes.length > 0)
        };

        $box.on('change', boxChangeListener)
        $all.on('click', allClickListener)

        this.listenerResetCallbacks.push(() => $box.off('change', boxChangeListener))
        this.listenerResetCallbacks.push(() => $all.off('click', allClickListener))
    }

    _loading(flag) {
        if (!this._useLoading) return

        const wrapper = $(`#${this._id}_wrapper`)
        if (this._spinner == null) {
            this._spinner = $(document.createElement('div'))
            this._spinner.addClass('spinner-border')
            this._backdrop = $(document.createElement('div'))
            this._backdrop.addClass('table-backdrop')
            this._backdrop.append(this._spinner)
            wrapper.after(this._backdrop)
        }
        if (flag) {
            this._backdrop.removeClass('table-backdrop-hide')
            wrapper.addClass('table-blur')
        } else {
            this._backdrop.addClass('table-backdrop-hide')
            wrapper.removeClass('table-blur')
        }
    }


    /* order : [index = 0, orderBy = 'desc'] */
    order(...orders) {
        this._order = []
        orders.forEach(([index, orderBy]) => {
            this._order.push([index, orderBy]);
        });
        return this
    }

    loading(useLoading = true) {
        this._useLoading = useLoading
        return this
    }

    infoText(text = this._infoText) {
        this._infoText = text
        return this
    }

    margin(size = 1) {
        this._margin = size
        return this
    }

    fixed(values) {
        const data = []
        const columns = []
        this._data = values
        if (Object.keys(values).length) {
            const heads = this._columns.map(col => {
                columns.push({title: col.title})
                col.render = this._columnDefs.find(x => x.targets === col._index).render
                return col
            })
            values.map(val => {
                const tr = []
                heads.forEach(head => {
                    const v = val[head.data]
                    tr.push(head.render ? head.render(v, null, val) : v)
                })
                data.push(tr)
            })
        }
        this._url = ''
        this._ajaxConfig = {}
        this._fixedConfig = {
            data,
            columns,
            drawCallback: () => {
                if (this._table)
                    this.resizeColumns()
                else this._loading(false)
                if (this._afterSubmit != null)
                    this._afterSubmit(values, this._table)
                this._initCheckboxes()
            },
        }
        return this
    }

    get(url) {
        this._url = url
        return this
    }

    afterInit(callback) {
        this._afterInit = callback.bind(this)
        return this
    }

    error(callback) {
        this._error = callback.bind(this)
        return this
    }

    afterSubmit(callback) {
        this._afterSubmit = () => {
            // this._element.find('th').each((i, e) => $(e).removeClass('sorting_desc'))
            callback.bind(this)()
        }
        return this
    }

    checkbox(uniqueVarName, width = '10%', uniqueVarName2 = null) {
        const index = this._index++
        this._columns.push({
            ...this._empty,
            _index: index
        })
        this._checkBaseVar = uniqueVarName

        const customs = this._customChkBoxTable
        const chkField = this._checkboxData

        this._columnDefs.push({
            width: width,
            targets: index,
            orderable: false,
            render: function (data, type, full) {
                if (customs) {
                    const dataAttrs = chkField.map(field => {
                        const value = full[field] ?? '';
                        return `data-${field}="${value}"`;
                    }).join(' ');
                    return `<div class="form-check d-flex justify-content-center">
                                <input type="checkbox" class="lc-check form-check-input dt-checkboxes"
                                    data-num="${full[uniqueVarName]}" ${dataAttrs}>
                            </div>`;
                } else {
                    return `<div class="form-check d-flex justify-content-center">
                                <input type="checkbox" class="lc-check form-check-input dt-checkboxes" data-num="${full[uniqueVarName]}">
                            </div>`;
                }
            }
        })
        return this
    }

    scroll(x = false) {
        this._scroll = {
            scrollX: x,
        }
        if (x) this._element.addClass('scroll-x')
        return this
    }

    add(column) {
        if (column instanceof Column) {
            const index = this._index++
            column.def.targets = index
            this._columns.push({
                ...column.col,
                _index: index
            })
            this._columnDefs.push(column.def)
        }
        return this
    }

    param(name, callback) {
        this._params.push({
            name,
            get: callback
        })
        return this
    }

    info(flag = true) {
        this._info = flag
        return this
    }

    paging(flag = true) {
        this._paging = flag
        return this
    }

    lengthChange(flag = true) {
        this._lengthChange = flag
        return this
    }

    pageOptions(array = this._lengthMenu, defaultValue = this._cntPerPage) {
        this._lengthMenu = array
        this._cntPerPage = defaultValue
        return this
    }

    selectable() {
        const _this = this
        const resetCallback = function () {
            if ($(this).find('.dataTables_empty').length) return

            if (_this._selected != null)
                _this._selected.removeClass('selected')
            _this._selected = $(this).addClass('selected')

            if (_this._onSelect) {
                const row = _this._table.row($(this))
                if (Object.keys(_this._fixedConfig).length)
                    _this._onSelect(_this._data[row.index()], row.index())
                else _this._onSelect(row.data(), row.index())
            }
        }

        this._element.on('click', 'tbody tr', function (event) {
            // 체크박스를 클릭했을 경우 이벤트 중지
            if ($(event.target).is('input[type="checkbox"]')) {
                event.stopPropagation();
                return;
            }
            resetCallback.call(this, event);
        })
        this.listenerResetCallbacks.push(() => _this._element.off('click', 'tbody tr', resetCallback))
        return this
    }

    select(callback) {
        this._onSelect = callback
        return this
    }

    disableFirstSubmit() {
        this._doFirstSubmit = true
        return this
    }

    isInit() {
        return this._table !== null
    }

    clear() {
        this.init()
    }

    destroy(draw = false) {
        if (this.isInit()) {
            this._table.clear()
            if (draw) this._table.draw()
            else {
                this._table.destroy()
                this._element.off();
            }
        }
    }

    reset() {
        this.listenerResetCallbacks.forEach(callback => callback())
        this.destroy()
        this._resetVariables()
    }

    map(callback) {
        this._mapFunc = callback
        return this
    }

    init() {
        this.destroy()

        const _this = this

        const pageChangeListener = () => this.submit(null, false)
        this._element.on('page.dt', pageChangeListener)
        this.listenerResetCallbacks.push(() => _this._element.off('page.dt', pageChangeListener))

        if (this._url.length > 0) {
            const _this = this
            this._fixedConfig = {}
            this._ajaxConfig = {
                serverSide: true,
                columns: this._columns,
                columnDefs: this._columnDefs,
                deferLoading: _this._doFirstSubmit ? [0] : null,
                pageLength: _this._cntPerPage,
                lengthMenu: _this._lengthMenu,
                drawCallback: function () {
                    if (_this._table)
                        _this.resizeColumns()
                    else _this._loading(false)
                    if (_this._afterSubmit != null)
                        _this._afterSubmit(_this._data, _this._table)
                    _this._initCheckboxes()
                },
                ajax: {
                    url: this._url,
                    headers: {
                        'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
                    },
                    data: function (params) {

                        // 만일 헤더를 통한 소팅 기능을 이용한다면 ..
                        // -> 해당 컬럼명 : order
                        // -> 해당 오더링 : dir
                        if (_this._useOrdering) {
                            if (params.hasOwnProperty('order') && params['order'].length !== 0) {
                                const col_name_index = params['order'][0]['column']
                                if (_this._columns[col_name_index] && _this._columns[col_name_index]['orderColumn']) {
                                    params['orderParam'] = _this._columns[col_name_index]['orderColumn']
                                    params['orderDir'] = params['order'][0]['dir']
                                }
                            }
                        }

                        // param에서 필요 없는 column 부분은 delete -> 우선은 리빙랩 보고서쪽에만 적용 (추후에는 전체 적용?)
                        if (_this._delUnUseParams) delete params.columns

                        if (params.length !== _this._cntPerPage) {
                            params.pageNumber = 1

                            _this._nowPage = 1

                        } else {
                            params.pageNumber = _this._nowPage
                        }

                        params.size = params.length;
                        _this._cntPerPage = params.size;

                        for (const param of _this._params)
                            params[param.name] = param.get()

                        // [param]에서 필요 없는 column 부분은 아예 delete
                        if (!_this._delUnUseParams) {
                            if (_this._submitColumns == false) // 컬럼은 [submit]하지 않도록 옵션으로 처리함
                                params.columns.length = 0;
                        }

                        if (_this._submitData == null && _this._lastSubmitData != null)
                            _this._submitData = _this._lastSubmitData

                        if (_this._submitData) {
                            for (const name in _this._submitData)
                                params[name] = _this._submitData[name]
                            _this._lastSubmitData = _this._submitData
                            _this._submitData = null
                        }
                    },
                    dataFilter: function (res) {

                        try {

                            let parsed = JSON.parse(res)    // { list:[], countPage, endPage, next, prev, size, startPage, totalElement, totalPages }

                            if (!!parsed.data) {
                                parsed = parsed.data
                            }

                            if (_this._mapFunc) {
                                parsed = parsed.map(_this._mapFunc)
                            }

                            const data = _this._data = parsed   // { list:[], countPage, endPage, next, prev, size, startPage, totalElement, totalPages }

                            if (Array.isArray(data)) {
                                return JSON.stringify({
                                    recordsFiltered: data.number ? data.totalElements : 0,
                                    recordsTotal: data.number ? data.totalElements : 0,
                                    data
                                })
                            }

                            _this._table.page(data.number - 1)

                            return JSON.stringify({
                                recordsFiltered: data.number ? data.totalElements : 0,
                                recordsTotal: data.number ? data.totalElements : 0,
                                data: data.list ?? [],
                            })

                        } catch (e) {
                            console.error(e)
                            Util.alert('조회 오류')
                            return '[]'
                        }
                    },
                    dataSrc: function (json) {

                        /**
                         * > json : { recordsFiltered, recordsTotal: data:[] }
                         * */

                        var data = json.data
                        if ((json.recordsTotal === 0) && (json.data.length !== 0)) json.recordsTotal = json.data.length
                        let idx = 0
                        for (let i = 0; i < data.length; i++) {
                            data[idx++].listIndex = ((json.recordsTotal - (_this._nowPage - 1) * _this._cntPerPage) - i);
                        }
                        return data
                    }
                    , error: function (xhr, textStatus, errorThrown) {
                        const url = _this._url
                        if (url.startsWith("/manager") || url.startsWith("/system") || url.startsWith("/artist")) {
                            if (xhr.status === 401) {
                                Util.alert("로그인이 만료되었습니다.\n다시 로그인 해주세요.").then(() => {
                                    window.location.href = xhr.redirect;
                                })
                            } else {
                                // Handle other error scenarios
                                console.error('DataTables AJAX Error:', errorThrown);
                            }
                        }
                    }
                }
                , createdRow(row, data, dataIndex) {
                    // custom {row}
                }
            }
        }

        try {
            this._table = this._element
                .removeAttr('width')
                .DataTable({
                    ...this._config(),
                    ...this._ajaxConfig,
                    ...this._fixedConfig
                })
            if (this._checkBaseVar.length)
                $(this._table.column(0).header()).html('<div class="form-check d-flex justify-content-center"><input type="checkbox" class="lc-check-all form-check-input"></div>');
        } catch (e) {
            console.error(e)
        }

        return this
    }

    submit(data = null, reload = true, submitColumns = true) {

        this._loading(true)
        const info = this._table.page.info()

        if (this._paging) {
            this._nowPage = info.page + 1
            this._cntPerPage = info.length
        }

        this._submitColumns = submitColumns

        const ajax = this._table.ajax
        if (data) this._submitData = data
        if (reload) {
            this._table.page(0)
            ajax.reload()
        }
    }

    getIndexByName(name) {
        return this._columns.find(col => col.data === name)?._index
    }


    filter(index, value) {
        this._table.column(index).search(value, false, true)
    }

    isColumnVisible(index) {
        return this._table.column(index).visible()
    }

    showColumn(index, show = true) {
        this._table.column(index).visible(show)
        this.resizeColumns()
    }

    getCheckedRows() {
        return [...this._checkboxes]
    }

    _resizeColumns() {
        this._loading(true)

        this._table.columns.adjust()
        const all = this._element.find('tr')
        const th = $(all.get(0)).find('th')
        const width = th.map((_, e) => $(e).css('width'))

        all.each((_, tr) => {
            if (_ !== 0)
                $(tr).find('td').each((i, e) =>
                    $(e).css('width', width[i]))
        })

        if (this._resizeTimer != null) {
            clearTimeout(this._resizeTimer)
            this._resizeTimer = null
        }
        this._resizeTimer = setTimeout(() => this._loading(false), 500)
    }

    resizeColumns(force = false) {
        if (!force) {
            if (this._wait4resize) return
            this._wait4resize = true

            setTimeout(() => {
                this._resizeColumns()
                this._wait4resize = false
            }, 500)
        } else {
            this._resizeColumns()
        }
    }

    values() {
        return Array.isArray(this._data)
            ? this._data
            : this._data.list ?? []
    }

    clearSelect() {
        if (this._selected != null) {
            this._selected.removeClass('selected')
            this._selected = null
        }
    }
}

class Column {
    constructor(name = '', defaultValue = '') {
        this.col = name.length > 0
            ? {name, data: name, defaultContent: defaultValue}
            : {data: null, defaultContent: defaultValue}

        this.def = {
            targets: -1,
        }

        // 기본 헤더를 통한 소팅 기능은 FALSE
        this.def.orderable = false
    }

    fixedTitle(title) {
        this.col.title = title
        return this
    }

    width(value) {
        this.def.width = value
        return this
    }

    order(orderColumn = '') {
        // 해당 헤더를 통한 소팅을 이용할 것인지
        this.def.orderable = true
        this.col.orderColumn = orderColumn
        return this
    }

    clazz(value = '') {
        this.def.className = value
        return this
    }

    center(value = '') {
        this.def.className = "text-center"
        return this
    }

    left(value = '') {
        this.def.className = "text-left"
        return this
    }

    right(value = '') {
        this.def.className = "text-end"
        return this
    }

    render(callback) {

        this.def.render = function (data, type, full) {
            if (callback != null)
                return callback.bind(this)(data, full)
            return data
        }
        return this
    }

    switchToggle(isDisable = false, clickEvent = null) {
        this.def.render = function (data, type, full) {
            const disable = (isDisable) ? 'disabled' : ''
            const tf = (data === 'Y') ? 'checked' : ''
            if(clickEvent) {
                return `<div class="form-check form-switch d-flex justify-content-center align-items-center m-0 h-100">
                            <input class="form-check-input swtich-chk" type="checkbox" role="switch" id="table-toggle" ${tf} ${disable}
                                onclick="${clickEvent.name}('${encodeURIComponent(JSON.stringify(full))}', this.checked)">
                        </div>`;
            } else {
                return `<div class="form-check form-switch d-flex justify-content-center align-items-center m-0 h-100">
                            <input class="form-check-input swtich-chk" type="checkbox" role="switch" id="table-toggle" ${tf} ${disable}>
                        </div>`;
            }
        }
        return this
    }

    flagName(y = "사용", n = "미사용") {
        this.def.render = function (data, full) {
            switch (data) {
                case 'Y':
                    return y
                case 'N':
                    return n
                default:
                    return ""
            }
        }
        return this
    }

    checkbox(uniqueVarName, search, result) {
        const colNm = this.col.data
        this.def.render = function (data, type, full) {

            const checkbox = `<div class="form-check d-flex justify-content-center">
                <input class="form-check-input lc-check-column" type="checkbox" name="${colNm}" data-rownum="${full[uniqueVarName]}" value="Y" ${data == "Y" ? "checked" : ""} />
            </div>
            `;

            if (search && result) {
                return (full[search] === result ? checkbox : "")
            } else {
                return checkbox
            }

        };
        return this;
    }

    cellClazz(callback) {

        this.def.createdCell = function (td, cellData, rowData, row, col) {
            if (callback != null)
                return callback.bind(this)(td, cellData, rowData, row, col)
        }
        return this
    }


    /*
    renderIdx(callback) {
        this.def.render = function(data, type, full) {
            if (callback != null)
                return callback.bind(this)(data, full)
            return data
        }
        return this
    }
    */
}