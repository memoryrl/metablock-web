const chartTreeData = {
    chart: null,
    fullData: null,
    currentNode: null,
    historyStack: [],
    filterValues: [],

    init: function () {
        this.chart = echarts.init(document.getElementById("mindmap-chart"));

        fetch("/assets/data/chart_tree_data.json")
            .then(res => res.json())
            .then(json => {
                this.fullData = json;
                this.currentNode = this.fullData.name;
                this.generateFilter(this.currentNode);
                this.render(this.currentNode);
            });

        window.addEventListener("resize", () => this.chart && this.chart.resize());

        $(document).on("change", "#sidebar-widgets-container input[type='checkbox']", () => {
            this.filterValues = this.getSelectedFilters();
            this.render(this.currentNode, true);
        });

        $(document).on("click", "#sidebar-widgets-container a[data-checkbox-id]", function (e) {
            if (e.target.tagName === "LABEL" || e.target.tagName === "INPUT") return;
            const checkboxId = $(this).data("checkbox-id");
            const $checkbox = $("#" + checkboxId);
            $checkbox.prop("checked", !$checkbox.prop("checked")).trigger("change");
            e.preventDefault();
        });
    },

    findNodeByName: function (name, node = this.fullData) {
        if (node.name === name) return node;
        if (!node.children) return null;
        for (const child of node.children) {
            const found = this.findNodeByName(name, child);
            if (found) return found;
        }
        return null;
    },

    isWorkTitle: function (name) {
        return this.fullData.works?.some(w => w.title === name);
    },

    getWorkDetailTree: function (title) {
        const work = this.fullData.works.find(w => w.title === title);
        if (!work) return [];

        const fields = [
            { key: "cast", label: "출연진" },
            { key: "type", label: "유형" },
            { key: "region", label: "지역" },
            { key: "broadcaster", label: "방송사" },
            { key: "category", label: "카테고리" },
            { key: "language", label: "언어" },
            { key: "format", label: "편성 형식" },
            { key: "rating", label: "시청 등급" }
        ];

        return fields.map(f => {
            const values = work[f.key];
            const children = Array.isArray(values)
                ? values.map(v => ({ name: v }))
                : [{ name: values }];
            return { name: f.label, children };
        });
    },

    getAutoLinkedWorks: function (filterValue) {
        const titles = [];
        for (const w of this.fullData.works || []) {
            for (const key of ["cast", "type", "region", "broadcaster", "category", "language", "format", "rating"]) {
                const val = w[key];
                if ((Array.isArray(val) && val.includes(filterValue)) || val === filterValue) {
                    titles.push(w.title);
                }
            }
        }
        return [...new Set(titles)].map(t => ({ name: t }));
    },

    applyDepthColor: function (node, depth = 0) {
        if (this.isWorkTitle(node.name)) {
            node.itemStyle = { color: "#FF4500", borderColor: "#FF4500", borderWidth: 1 };
            node.symbol = "circle";
            node.symbolSize = 30;
            node.children?.forEach(child => {
                child.itemStyle = { color: "#32CD32", borderColor: "#333", borderWidth: 1 };
                child.symbol = "circle";
                child.symbolSize = 30;
                child.children?.forEach(grandChild => {
                    grandChild.itemStyle = { color: "#FFD700", borderColor: "#333", borderWidth: 1 };
                    grandChild.symbol = "circle";
                    grandChild.symbolSize = 30;
                });
            });
        } else {
            let color = depth === 0 ? "#1E90FF" : (depth === 1 ? "#32CD32" : "#FFD700");
            node.itemStyle = { color: color, borderColor: "#333", borderWidth: 1 };
            node.symbol = "circle";
            node.symbolSize = 30;
            node.children?.forEach(child => this.applyDepthColor(child, depth + 1));
        }
    },

    render: function (nodeName, isFilter = false) {
        this.currentNode = nodeName;

        let displayNode = { name: nodeName };
        let children = [];

        if (this.isWorkTitle(nodeName)) {
            const rawTree = this.getWorkDetailTree(nodeName);
            const selected = this.getSelectedFilters();
            children = rawTree.filter(node =>
                selected.length === 0 || selected.includes(node.name)
            );
        } else {
            const found = this.findNodeByName(nodeName);
            if (found && found.children?.length > 0) {
                const selected = this.getSelectedFilters();
                children = found.children
                    .filter(c => selected.length === 0 || selected.includes(c.name))
                    .map(c => ({ name: c.name }));
            } else {
                children = this.getAutoLinkedWorks(nodeName);
            }
        }

        if (children.length > 0) {
            displayNode.children = children;
        }

        const safeNode = JSON.parse(JSON.stringify(displayNode));
        this.applyDepthColor(safeNode);

        this.chart.dispose();
        this.chart = echarts.init(document.getElementById("mindmap-chart"));

        this.chart.setOption({
            tooltip: { trigger: "item", triggerOn: "mousemove" },
            series: [{
                type: "tree",
                data: [safeNode],
                orient: "vertical",
                top: "15%", bottom: "15%", left: "10%", right: "10%",
                symbolSize: 16,
                label: { position: "top", align: "center", fontSize: 14 },
                leaves: { label: { position: "bottom", align: "center" } },
                lineStyle: { curveness: 0.2, opacity: 0.6 },
                expandAndCollapse: false,
                animationDuration: 300
            }]
        });

        this.chart.off("click");
        this.chart.on("click", (params) => {
            const clicked = params.data.name;
            if (clicked !== this.currentNode) {
                this.historyStack.push(this.currentNode);
            }
            this.filterValues = [];
            this.currentNode = clicked;
            this.generateFilter(clicked);
            this.render(clicked);
        });

        if (!isFilter) {
            this.updatePathDisplay();
        }
    },

    generateFilter: function (nodeName) {
        let children = [];

        if (this.isWorkTitle(nodeName)) {
            const tree = this.getWorkDetailTree(nodeName);
            children = tree.map(node => node.name);
        } else {
            const found = this.findNodeByName(nodeName);
            children = found?.children?.map(c => c.name) || [];
        }

        const $container = $("#sidebar-widgets-container");
        $container.find(".dynamic-filter-widget").remove();

        const $widget = $(`
            <div class="sidebar-widget sidebar-widget-border mb-40 pb-35 dynamic-filter-widget" data-aos="fade-up" data-aos-delay="200">
                <div class="sidebar-widget-title mb-25">
                    <h3>필터</h3>
                </div>
                <div class="sidebar-widget-color sidebar-list-style"><ul></ul></div>
            </div>
        `);

        const $ul = $widget.find("ul");

        children.forEach((child, idx) => {
            const id = `tree_filter_${idx}`;
            const $li = $(`
                <li>
                    <a href="javascript:void(0);" data-checkbox-id="${id}">
                        <input type="checkbox" id="${id}" value="${child}">
                        <label for="${id}" style="cursor:pointer;">${child}</label>
                    </a>
                </li>
            `);
            $ul.append($li);
        });

        $container.append($widget);
    },

    getSelectedFilters: function () {
        return $("#sidebar-widgets-container input[type='checkbox']:checked")
            .map((_, el) => el.value).get();
    },

    goBack: function () {
        if (this.historyStack.length === 0) return;
        const previous = this.historyStack.pop();
        this.filterValues = [];
        this.generateFilter(previous);
        this.render(previous);
    },

    updatePathDisplay: function () {
        const path = [];

        for (let i = 0; i < this.historyStack.length; i++) {
            if (i === 0 || this.historyStack[i] !== this.historyStack[i - 1]) {
                path.push(this.historyStack[i]);
            }
        }

        // 현재 노드가 바로 전과 다르면 추가
        if (this.historyStack.length === 0 || this.historyStack[this.historyStack.length - 1] !== this.currentNode) {
            path.push(this.currentNode);
        }

        const displayId = this === chartTreeData ? "treeNodePathDisplay" : "forceNodePathDisplay";
        document.getElementById(displayId).innerText = path.join(" > ");
    }
};
