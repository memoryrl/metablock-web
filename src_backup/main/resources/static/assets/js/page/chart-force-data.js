const chartForceData = {
    chart: null,
    fullData: null,
    currentNode: null,
    historyStack: [],
    filterValues: [],

    init: function () {
        this.chart = echarts.init(document.getElementById("force-chart"));

        fetch("/assets/data/chart_force_data.json")
            .then(res => res.json())
            .then(json => {
                this.fullData = json;
                this.render("컨텐츠", true);
            });

        window.addEventListener("resize", () => this.chart && this.chart.resize());

        $(document).on("change", "#sidebar-widgets-container input[type='checkbox']", () => {
            this.filterValues = this.getSelectedFilters();
            this.render(this.currentNode, false);
        });

        $(document).on("click", "#sidebar-widgets-container a[data-checkbox-id]", function (e) {
            if (e.target.tagName === "LABEL" || e.target.tagName === "INPUT") return;
            const checkboxId = $(this).data("checkbox-id");
            const $checkbox = $("#" + checkboxId);
            $checkbox.prop("checked", !$checkbox.prop("checked")).trigger("change");
            e.preventDefault();
        });
    },

    render: function (nodeName, regenerateFilter = false) {
        this.currentNode = nodeName;

        const nodeMap = new Map(this.fullData.nodes.map(n => [n.name, n]));
        const clickedNode = nodeMap.get(nodeName);
        if (!clickedNode) return;

        if (regenerateFilter) {
            this.generateFilter(nodeName);
            this.filterValues = [];
        }

        const nodeSet = new Set([nodeName]);
        const linkSet = new Set();
        const selected = this.filterValues;
        const isNodeCategory = (name, category) => nodeMap.get(name)?.category === category;

        this.fullData.links.forEach(link => {
            const [src, tgt] = [link.source, link.target];
            if (src === nodeName || tgt === nodeName) {
                const neighbor = src === nodeName ? tgt : src;
                const shouldInclude = selected.length === 0 || selected.includes(neighbor);
                if (shouldInclude) {
                    nodeSet.add(neighbor);
                    linkSet.add(JSON.stringify(link));
                }
            }
        });

        const filteredNodes = this.fullData.nodes.filter(n => nodeSet.has(n.name));
        const filteredLinks = Array.from(linkSet).map(s => JSON.parse(s));

        this.chart.setOption({
            tooltip: { trigger: "item", formatter: "{b}" },
            series: [{
                type: "graph",
                layout: "force",
                roam: true,
                label: { show: true, position: "right" },
                force: { repulsion: 200, edgeLength: 200 },
                categories: this.fullData.categories,
                data: filteredNodes,
                links: filteredLinks,
                lineStyle: { curveness: 0.3 }
            }]
        });

        this.chart.off("click");
        this.chart.on("click", (params) => {
            const clicked = params.data.name;
            if (clicked !== this.currentNode) {
                this.historyStack.push(this.currentNode);
                this.render(clicked, true);
            }
        });

        this.updatePathDisplay();
    },

    generateFilter: function (nodeName) {
        const nodeMap = new Map(this.fullData.nodes.map(n => [n.name, n]));
        const links = this.fullData.links;
        const filterItems = new Set();

        for (const link of links) {
            if (link.source === nodeName) filterItems.add(link.target);
            else if (link.target === nodeName) filterItems.add(link.source);
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

        Array.from(filterItems).forEach((item, idx) => {
            const id = `force_filter_${idx}`;
            const $li = $(`
                <li>
                    <a href="javascript:void(0);" data-checkbox-id="${id}">
                        <input type="checkbox" id="${id}" value="${item}">
                        <label for="${id}" style="cursor:pointer;">${item}</label>
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
        this.render(previous, true);
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
