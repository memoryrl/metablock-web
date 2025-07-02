var homeJS = {

    homeDataList:[],
    typeConfig: [],

    // list 데이터 조회
    drawMainContent: function(homeData) {

        let categories = homeJS.getCategoryList(homeData);

        homeJS.homeDataList.push({ categories, homeData });
        homeJS.drawHomeData(homeData, categories, homeJS.typeConfig)
    },

    handleCategoryClick: function(target) {
        const $categoryList = $(target).closest('ul.category-list');
        $categoryList.find('a').removeClass('active');
        $(target).addClass('active');
        
        const selectedCategory = $(target).text();

        const $wrap = $(target).closest('.checkout-wrap');
        const $row = $wrap.find('.container .row');
        const homDataListIndex = $wrap.data('uid') - 1;

        const $homeDataList = $wrap.find('.home-data-list');

        let dataDom = homeJS.drawDataList(
            homeJS.homeDataList[homDataListIndex]['homeData'], 
            homeJS.homeDataList[homDataListIndex]['categories'], 
            homeJS.typeConfig, 
            selectedCategory
        );

        $homeDataList.remove();
        $row.append(dataDom)
    },

    drawHomeData: function(homeData, categories, typeConfig) {

        const $root = $('.checkout-main-area');
        const $wrap = $('<div>', {
            class: 'checkout-wrap pb-95',
            id: 'menu-category-wrap-01',
            'data-uid': $('.checkout-wrap').length + 1,
            'data-aos': 'fade-up',
            'data-aos-delay': '200'
        });
        
        const $container = $('<div>', { class: 'container' });
        const $row = $('<div>', { class: 'row' });


        if(categories.length) {
            let categoryDom = homeJS.drawHomeCategory(categories);
            $row.append(categoryDom);
        }
        let dataDom = homeJS.drawDataList(homeData, categories, typeConfig);

        $row.append(dataDom);
        $container.append($row);
        $wrap.append($container);
        $root.append($wrap);
    },

    

    drawHomeCategory: function(categories) {
        const $colLeft = $('<div>', { class: 'col-lg-3 col-md-12 col-12' });

        const $sidebarWidget = $('<div>', {
            class: 'sidebar-widget sidebar-widget-border mb-40 aos-init aos-animate',
            'data-aos': 'fade-up',
            'data-aos-delay': '200'
        });


        const $title = $('<div>', { class: 'sidebar-widget-title mb-25' }).append(
            $('<h3>', { id: 'menu-category-h3-01', text: '카테고리' })
        );

        const $listWrapper = $('<div>', { class: 'sidebar-widget-size sidebar-list-style' });
        const $ul = $('<ul>', {
        id: 'menu-category-list-01',
        class: 'category-list'
        });


        categories.forEach((name, index) => {
            const uid = index + 1;
            const $li = $('<li>');
            const $a = $('<a>', {
            href: 'javascript:void(0);',
            id: `category01-${uid}`,
            'data-uid': uid,
            class: index === 0 ? 'active' : ''
            }).append($('<span>').text(name));

            $li.append($a);
            $ul.append($li);
        });

        $listWrapper.append($ul);

        const $moreWrapper = $('<div>', { class: 'page-more-update-wrapper' }).append(
            $('<div>', { class: 'page-more-update btn-hover' }).append(
                $('<a>', {
                href: 'javascript:void(0);',
                id: 'menu-category-more-01',
                html: '더보기 &nbsp; &gt;'
                })
            )
        );

        $sidebarWidget.append($title, $listWrapper, $moreWrapper);
        $colLeft.append($sidebarWidget);
        
        return $colLeft;

    },

    drawDataList: function(homeData, categories, typeConfig, selectedCategory) {

        // homeJS.drawType(homeData, categories, typeConfig);

        const type = homeJS.getTypeByList(homeData);
        typeConfig = typeConfig.find(item => item.type === type);

        if(type === 'A') return drawHomeType.TypeA(homeData, categories, typeConfig, selectedCategory);
        if(type === 'B') return drawHomeType.TypeB(homeData, categories, typeConfig, selectedCategory);
        if(type === 'C') return drawHomeType.TypeC(homeData, typeConfig);
        if(type === 'D') return drawHomeType.TypeD(homeData, typeConfig);
        
    },


    getTypeByList: function(list) {
        const hasCategory = list.every(item => item.category);

        if(hasCategory) {
            if (list.some(item => !item.desc && item.label)) {
                // a 조건: desc 없고 label 있는 항목이 하나라도 있을 때
                return "A";
            } else {
                // b 조건: 나머지 경우
                return "B";
            }
        }
        else {
            if (list.some(item => item.image)) {
                // image가 있는 항목이 하나라도 있을 때
                return "C";
            } else {
                // 모든 항목에 image가 없을 때
                return "D";
            }
        }
    },

    // homeData에서 카테고리 list 생성
    getCategoryList: function(homeData) {
        const categories = homeData
            .map(item => item.category)
            .filter(category => category !== undefined && category !== null);

        return [...new Set(categories)];
    },

    setTypeConfig: function(configData) {
        homeJS.typeConfig.push(...configData);
    },

}
