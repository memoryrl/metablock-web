var list = {
    
    page : 1, // 현재 페이지
    sort : "title", // 정렬기준
    keyword : "", // 검색어
    param : {categories: [], sources: [], types: [], date: [], tags: []}, // 검색 파라미터

    filterList : [],

    userUid : "",

    pageList : function() {
        this.pageInit();
        let param = this.param;
        let keyword = this.keyword;

        Http.getJson('/assets/data/3xmeta/list_data.json').then(async (res) => {
            let data = res;

            list.createSidebar(data);

            if(keyword) { data = data.filter(item => item.title.includes(keyword)); }

            if(param['categories'].length) { data = data.filter(item => item.category.some(type => param['categories'].includes(type))); }
            if(param['sources'].length) { data = data.filter(item => param['sources'].includes(item.sources));}
            if(param['types'].length) { data = data.filter(item => item.dataType.some(type => param['types'].includes(type)));}
            if(param['date'].length) { 
                data = data.filter(item => {
                    const updateDate = new Date(item.date);
                    return updateDate >= new Date(param['date'][0]) && updateDate <= new Date(param['date'][1]);
                });
            }
            if(param['tags'].length) { data = data.filter(item => item['tags'].some(type => param['tags'].includes(type)));}

            let wishList;
            if(this.userUid) {
                wishList = await Http.get("/contents/api/bookmark/list", {userUid: this.userUid});
                wishList = wishList.data
            }

            list.createList(data, wishList);
            list.createCard(data, wishList);
        })
    },

    createSidebar : function(data) {
        // http.get()

       $("#sidebar-widgets-container").empty();
        $('#provider').empty();
        $('#dataType').empty();
        $('#tags').empty();

        let param = this.param;

        Http.get('/cetus/api/contents/categories').then(res => {
            const categories = res

            const $container = $("#sidebar-widgets-container");

            categories.forEach(function (widget, index) {
                const $widget = $(`
                <div class="sidebar-widget sidebar-widget-border mb-40 pb-35" data-aos="fade-up" data-aos-delay="${200}">
                    <div class="sidebar-widget-title mb-25">
                    <h3>${widget.field}</h3>
                    </div>
                    <div class="sidebar-widget-color sidebar-list-style">
                        <ul></ul>
                    </div>
                </div>
                `);

                const $ul = $widget.find("ul");

                widget.items.forEach(function (item, i) {
                    const id = `${widget.field.toLowerCase()}_${i}`;

                    const $li = $("<li></li>");
                    const $a = $('<a href="#"></a>');
                    const $input = $('<input>', { id, type: "checkbox", value: item.code });
                    const $label = $('<label>', { for: id }).text(item.name);
                    const $span = $("<span></span>").text(item.code);

                    //if (param['categories'].includes(item.code)) $input.prop('checked', true);

                    const keysToCheck = ['categories', 'sources', 'types'];
                    const isIncluded = keysToCheck.some(key => param[key]?.includes(item.code));
                    if (isIncluded) { $input.prop('checked', true); }

                    $a.append($input, $label, $span);
                    $li.append($a);
                    $ul.append($li);

                });

                $container.append($widget);
            });
        })

       
        
        // Categories
        // const categories = data.reduce((acc, item) => {
        //     const existingCategory = acc.find(cat => cat.category === item.category);
        //     if (existingCategory) { existingCategory.value++;} 
        //     else { acc.push({ category: item.category, value: 1 }); }

        //     return acc;
        // }, []);

        // category 배열을 이름 순서대로 정렬
        // categories.sort((a, b) => a.category.localeCompare(b.category));

        // ul 요소 생성
        // var $ul = $('<ul></ul>');

        // for(let i=0; i<categories.length; i++) {
            
        //     var $li = $('<li></li>');
        //     var $a = $('<a href="#"></a>');
        //     var $input = $('<input>', {id: `category_${i+1}`, type: 'checkbox', value: categories[i]['category']});
        //     var $label = $('<label>', {for: `category_${i+1}`}).text(categories[i]['category']);
        //     var $span = $('<span></span>').text(categories[i]['value']);
            
        //     $a.append($input, $label, $span);
        //     $li.append($a);
        //     $ul.append($li);
        // }

        // categories id를 가진 요소 하위에 ul을 추가
        //('#categories').append($ul);

        // provider
        const provider = data.reduce((acc, item) => {
            const existingProvider = acc.find(cat => cat.provider === item.provider);
            if (existingProvider) { existingProvider.value++;} 
            else { acc.push({ provider: item.provider, value: 1 }); }

            return acc;
        }, []);

        // provider 배열을 이름 순서대로 정렬
        provider.sort((a, b) => a.provider.localeCompare(b.provider));

        $ul = $('<ul></ul>');
        for(let i=0; i<provider.length; i++) {
            var $li = $('<li></li>');
            var $a = $('<a href="#"></a>');
            var $input = $('<input>', {id: `provider_${i+1}`, type: 'checkbox', value: provider[i]['provider']});
            var $label = $('<label>', {for: `provider_${i+1}`}).text(provider[i]['provider']);
            var $span = $('<span></span>').text(provider[i]['value']);
            
            $a.append($input, $label, $span);
            $li.append($a);
            $ul.append($li);
        }

        // provider id를 가진 요소 하위에 ul을 추가
        $('#provider').append($ul);

        // // dataType
        // const dataType = data.reduce((acc, item) => {
        //     item.dataType.forEach(type => {
        //         const existingDataType = acc.find(cat => cat.dataType === type);
        //         if (existingDataType) { existingDataType.value++;}
        //         else { acc.push({ dataType: type, value: 1 }); }
        //     });

        //     return acc;
        // }, []);

        // // dataType 배열을 이름 순서대로 정렬
        // dataType.sort((a, b) => a.dataType.localeCompare(b.dataType));

        // $ul = $('<ul></ul>');
        // for(let i=0; i<dataType.length; i++) {
        //     var $li = $('<li></li>');
        //     var $a = $('<a href="#"></a>');
        //     var $input = $('<input>', {id: `dataType_${i+1}`, type: 'checkbox', value: dataType[i]['dataType']});
        //     var $label = $('<label>', {for: `dataType_${i+1}`}).text(dataType[i]['dataType']);
        //     var $span = $('<span></span>').text(dataType[i]['value']);
            
        //     $a.append($input, $label, $span);
        //     $li.append($a);
        //     $ul.append($li);
        // }

        // // dataType id를 가진 요소 하위에 ul을 추가
        // $('#dataType').append($ul);



        // tags
        // const tags = data.reduce((acc, item) => {
        //     item.tags.forEach(type => {
        //         const existingTags = acc.find(cat => cat.tags === type);
        //         if (existingTags) { existingTags.value++;}
        //         else { acc.push({ tags: type, value: 1 }); }
        //     });

        //     return acc;
        // }, []);

        // // dataType 배열을 이름 순서대로 정렬
        // tags.sort((a, b) => a.tags.localeCompare(b.tags));

        // $ul = $('<ul></ul>');
        // for(let i=0; i<tags.length; i++) {
        //     var $a = $('<a href="#"></a>');

        //     const $input = $("<input>", { type: "checkbox", id: `tags_${i+1}`, value: tags[i]['tags'], style: "display: none;"});
        //     const $label = $('<label>', {for: `tags_${i+1}`}).text(tags[i]['tags']);
            
        //     $a.append($input, $label);
        //     $('#tags').append($a);
        // }

        const checkboxes = $(`#categories input[type="checkbox"], 
        #provider input[type="checkbox"], 
        #dataType input[type="checkbox"],
        #tags input[type="checkbox"]`);
        
        checkboxes.each(function() {
            const value = $(this).val();
            if (param['categories'].includes(value)) $(this).prop('checked', true);
            else if (param['provider'].includes(value)) $(this).prop('checked', true); 
            else if (param['dataType'].includes(value)) $(this).prop('checked', true);
            else if (param['tags'].includes(value)) $(this).prop('checked', true);
            else $(this).prop('checked', false); // 조건에 맞지 않는 것은 unchecked
        });
    },

    createList : function(data, wishList) {
        const dataLength = data.length;
        const number = 5; // 한 페이지에 5개의 요소 표시
        const pageCount = 5; // 페이지네이션에 5개의 페이지 표시
        const totalPages = Math.ceil(dataLength / number); // 전체 페이지 수

        const startIndex = (this.page - 1) * number; // 시작 인덱스
        const endIndex = startIndex + number; // 끝 인덱스 (slice는 endIndex는 포함하지 않음)

        const startPage = (Math.ceil(this.page / pageCount) - 1) * pageCount + 1;
        const endPage = Math.min(totalPages, Math.ceil(this.page / pageCount) * pageCount);

        // 데이터 정렬
        if(this.sort === "title") { data.sort((a, b) => a.title.localeCompare(b.title)); }
        if(this.sort === "update") { data.sort((a, b) => new Date(b.updateDate) - new Date(a.updateDate)); }
        if(this.sort === "rating") { data.sort((a, b) => b.rating - a.rating); }

        data = data.slice(startIndex, endIndex);

        for (let i = 0; i < data.length; i++) {

            var $shopListWrap = $('<div>', {
                class: 'page-list-wrap mb-30 aos-init aos-animate',
                'data-aos': 'fade-up',
                'data-aos-delay': '200'
            });

            var $row = $('<div>', { class: 'row' });

            var $col1 = $('<div>', { class: 'col-lg-4 col-sm-5' });
            var $productListImg = $('<div>', { class: 'page-list-img' });
            var $productLink = $('<a>', { href: `detail/${data[i]['uid']}` });
            
            const randomNumber = Math.floor(Math.random() * 3) + 1;  // 1, 2, 3 중 랜덤 선택
            var $productImage = $('<img>', {
                //src: `/assets/images/page/list/data_0${randomNumber}.jpg`,
                src: `/assets/images/page/thumbnail_default.png`,
                alt: 'Product Style',
                class: 'portal-img',
            });
            $productLink.append($productImage);


            let like = "N"
            if (wishList && wishList.length > 0) {
                if (wishList.some(item => item['contentsUid'] === data[i]['uid'])) {
                    like = "Y";
                }
            }
            var $quickView = $('<div>', { class: 'page-list-quickview' });
            $quickView.append(
                $('<button>', { class: 'page-action-btn-2 like-content-btn', title: 'Wishlist', 'data-uid': `${data[i]['uid']}`, 'data-like': `${like}`}).append(
                    $('<i>', { class: `${(like === "Y") ? 'fa fa-heart' : 'pe-7s-like'}` })
                ),
                $('<button>', { class: 'page-action-btn-2 detail-btn', 'data-uid': `${data[i]['uid']}`, title: 'Quick View' }).append(
                    $('<i>', { class: 'pe-7s-look' })
                )
            );

            $productListImg.append($productLink, $quickView);
            $col1.append($productListImg);

            var $col2 = $('<div>', { class: 'col-lg-8 col-sm-7' });
            var $shopListContent = $('<div>', { class: 'page-list-content' });
            $shopListContent.append(
                $('<h3>').append(
                    $('<a>', { href: `detail/${data[i]['uid']}` }).text(data[i]['title'])
                )
            );

            $shopListContent.append(
                $('<div>', { class: 'page-price' }).append(
                    $('<span>').text(data[i]['description'])
                )
            );

            var $rating = $('<div>', { class: 'page-list-rating' });
            for (var j = 0; j < 5; j++) {
                $rating.append($('<i>', { class: 'ti-star' }));
            }

            $shopListContent.append($rating);

            for(let k = 0; k < data[i]['tags'].length; k++) {
                $shopListContent.append($('<span>', { class: 'tag-content' }).text(data[i]['tags'][k]));
            }

            $shopListContent.append(
                $('<p>').text(`유형 : ${this.getFilterItemName(["Categories", "ContentFormat"], data[i]['category'])}`),
                $('<p>').text(`출처 : ${this.getFilterItemName("Sources", data[i]['sources'])}`),
                $('<p>').text(`데이터 타입 : ${this.getFilterItemName("Types", data[i]['dataType'])}`),
                $('<p>').text(`업데이트 : ${data[i]['date']}`)
            )


            $col2.append($shopListContent);

            $row.append($col1, $col2);
            $shopListWrap.append($row);

            $('#shop-2').append($shopListWrap);
        }

        if(!data.length) {
            $('#shop-2').append('<div><p class="text-center">검색 결과가 없습니다.</p></div>');
        }

        // 별점
        $(".page-list-rating").each(function (index) {
            var stars =  $(this).find('.ti-star');
            Common.setRattingStar(stars, data[index]['rating']);
        })
    
        // Create the main container
        const $pagination = $('<div>', {
            class: 'pagination-style-1 aos-init',
            'data-aos': 'fade-up',
            'data-aos-delay': '200'
        });

        // Create the list element
        const $ul = $('<ul>');


        // Create the list items
        for(let j = startPage; j <= endPage; j++) {
            const $li = $('<li>').append($('<a>', {
                class: j == this.page ? 'active' : '',
                href: `javascript: list.fnPage(${j});`,
                text: j
            }));
            $ul.append($li);
        }

        // 이전페이지
        if (startPage > 1) {
            $ul.prepend($('<li>').append($('<a>', {
                class: 'next',
                href: `javascript: list.fnPage(${startPage - 1});`,
            }).html('<i class="ti-angle-double-left "></i>')));
        }
        
        // 다음페이지
        if (endPage < totalPages) {
            $ul.append($('<li>').append($('<a>', {
                href: `javascript: list.fnPage(${endPage + 1});`,
                text: '»'
            }).html('<i class="ti-angle-double-right "></i>')));
        }

        // Append the list to the container
        $pagination.append($ul);

        // Append the container to the body or a specific element
        $('#shop-2').append($pagination);

        $('#showing-list').text(`전체 ${dataLength}건${dataLength ? ` 중 ${startIndex + 1}-${Math.min(endIndex, dataLength)}` : ''}`);

    },
  
    createCard : function(data, wishList) {
        const dataLength = data.length;
        const number = 9; // 한 페이지에 9개의 요소
        const pageCount = 5; // 페이지네이션에 5개의 페이지 표시

        const totalPages = Math.ceil(dataLength / number); // 전체 페이지 수

        const startIndex = (this.page - 1) * number; // 시작 인덱스
        const endIndex = startIndex + number; // 끝 인덱스 (slice는 endIndex는 포함하지 않음)

        const startPage = (Math.ceil(this.page / pageCount) - 1) * pageCount + 1;
        const endPage = Math.min(totalPages, Math.ceil(this.page / pageCount) * pageCount);

        // 데이터 정렬
        if(this.sort === "title") { data.sort((a, b) => a.title.localeCompare(b.title)); }
        if(this.sort === "update") { data.sort((a, b) => new Date(b.updateDate) - new Date(a.date)); }
        if(this.sort === "rating") { data.sort((a, b) => b.rating - a.rating); }


        data = data.slice(startIndex, endIndex);

        for(let i =0; i<data.length; i++ ){ 
            // Create the col-lg-4 div
            var $colDiv = $('<div>', { class: 'col-lg-4 col-md-4 col-sm-6 col-12' });
            
            // Create the page-wrap div
            var $productWrap = $('<div>', { class: 'page-wrap mb-35' });
            
            // Create the page-img div
            var $productImg = $('<div>', { class: 'page-img img-zoom mb-25' });
            var $productLink = $('<a>', { href: `detail/${data[i]['uid']}` });
            
            const randomNumber = Math.floor(Math.random() * 3) + 1;  // 1, 2, 3 중 랜덤 선택
            //var $productImage = $('<img>', { class: 'portal-img', src: `/assets/images/page/list/data_0${randomNumber}.jpg`, alt: '' });
            var $productImage = $('<img>', { class: 'portal-img', src: `/assets/images/page/thumbnail_default.png`, alt: '' });
            $productLink.append($productImage);
            $productImg.append($productLink);
            
            let like = "N"
            if (wishList && wishList.length > 0) {
                if (wishList.some(item => item['contentsUid'] === data[i]['uid'])) {
                    like = "Y";
                }
            }

            var $actionWrap = $('<div>', { class: 'page-action-wrap' });
            var $wishlistBtn = $('<button>', { class: 'page-action-btn-1 like-content-btn', title: 'Wishlist', 'data-uid': `${data[i]['uid']}`, 'data-like': `${like}` }).append($('<i>', { class: `${(like === "Y") ? 'fa fa-heart' : 'pe-7s-like'}` }));
            var $quickViewBtn = $('<button>', { class: 'page-action-btn-1 detail-btn', 'data-uid': `${data[i]['uid']}`, title: 'Quick View', 'data-bs-toggle': 'modal', 'data-bs-target': '#exampleModal' }).append($('<i>', { class: 'pe-7s-look' }));
            $actionWrap.append($wishlistBtn, $quickViewBtn);
            
            // Create the page-action-2-wrap div
            var $action2Wrap = $('<div>', { class: 'page-action-2-wrap' });
            // var $addToCartBtn = $('<button>', { class: 'page-action-btn-2', title: '임시저장 항목에 추가' }).append($('<i>', { class: 'pe-7s-cart' }), ' 임시저장 항목에 추가');
            //$action2Wrap.append($addToCartBtn);
            
            // Append the action wraps to the page-img div
            $productImg.append($actionWrap, $action2Wrap);
            
            // Create the page-content div
            var $productContent = $('<div>', { class: 'page-content' });
            var $productTitle = $('<h3>').append($('<a>', { href: `detail/${data[i]['uid']}` }).text(data[i]['title']));
            var $productPrice = $('<div>', { class: 'page-price' }).append($('<span>').text(data[i]['description']));
            
            // Append the title and price to the page-content div
            $productContent.append($productTitle, $productPrice);
            
            // Append the page-img and page-content to the page-wrap div
            $productWrap.append($productImg, $productContent);
            
            // Append the page-wrap to the colDiv
            $colDiv.append($productWrap);
            
            // Finally, append the colDiv to the container (assuming a container element with id "product-container")
            $('#shop-1 .row').append($colDiv);
        }

        if(!data.length) {
            $('#shop-1 .row').append('<div><p class="text-center">검색 결과가 없습니다.</p></div>');
        }
        
        // Create the main container
        const $pagination = $('<div>', {
            id: 'card-pagination',
            class: 'pagination-style-1 aos-init',
            'data-aos': 'fade-up',
            'data-aos-delay': '200'
        });

        // Create the list element
        const $ul = $('<ul>');

        // Create the list items
        for(let j = startPage; j <= endPage; j++) {
            const $li = $('<li>').append($('<a>', {
                class: j == this.page ? 'active' : '',
                href: `javascript: list.fnPage(${j});`,
                text: j
            }));
            $ul.append($li);
        }

        // 이전페이지
        if (startPage > 1) {
            $ul.prepend($('<li>').append($('<a>', {
                class: 'next',
                href: `javascript: list.fnPage(${startPage - 1});`,
            }).html('<i class="ti-angle-double-left "></i>')));
        }
        
        // 다음페이지
        if (endPage < totalPages) {
            $ul.append($('<li>').append($('<a>', {
                href: `javascript: list.fnPage(${endPage + 1});`,
                text: '»'
            }).html('<i class="ti-angle-double-right "></i>')));
        }

        // Append the list to the container
        $pagination.append($ul);

        // Append the container to the body or a specific element
        $('#shop-1 #card-pagination').html($pagination);

        $('#showing-card').text(`전체 ${dataLength}건${dataLength ? ` 중 ${startIndex + 1}-${Math.min(endIndex, dataLength)}` : ''}`);
    },

    // 페이지 변경
    fnPage : function(selectPage) {
        this.page = selectPage;
        this.pageList();
    },

    // 탭 변경
    fnTab : function(selectTab) {
        this.fnPage(1);
        if(selectTab === "card") { $('#showing-card').show(); $('#showing-list').hide(); }
        else if(selectTab === "list") { $('#showing-list').show(); $('#showing-card').hide(); }
    },

    pageInit : function() {
        // showing text 초기화
        $('#showing-card').text("");
        $('#showing-list').text("");
        
        // sidebar 초기화
        // $('#categories').empty();
        // $('#provider').empty();
        // $('#dataType').empty();
        // $('#tags').empty();

        // list/card 초기화
        $('#shop-2').empty();
        $('#shop-1 .row').empty();
    },

    // 정수 -> 날짜 변환 (기준일 : 2024-01-01)
    dateFormat : function(amount) {
        let dateString = "1900-01-01";
        let date = new Date(dateString);

        // n일 추가
        date.setDate(date.getDate() + amount);

        // yyyy-mm-dd 형식으로 변환
        const yyyy = date.getFullYear();
        const mm = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요
        const dd = String(date.getDate()).padStart(2, '0');

        const newDateString = `${yyyy}-${mm}-${dd}`;
        
        return newDateString // ex. 2024-02-01
    },

    setSort : function(value) {
        this.sort = value;
    },

    setKeyword : function(value) {
        this.keyword = value;
    },

    likeClickEvent: async function(element) {
        const $this = $(element)
        const targetUid = $this.data('uid')
        const like = $this.data('like')

        const res = await Http.post('/contents/api/bookmark/toggle', {contentsUid: targetUid}, true);

        const changeLike = (res.data) ? 'Y' : 'N'
        const changeCss = (res.data) ? 'fa fa-heart' : 'pe-7s-like'
        const removeCss = (res.data) ? 'pe-7s-like' : 'fa fa-heart'

        $this.find('i').removeClass(removeCss).addClass(changeCss);
        $this.data('like', changeLike);

        if(sidebar){
            sidebar.getSidebarLst();
        }
    },

    getFilterItemName: function(field, code) {
        const fields = Array.isArray(field) ? field : [field];
        const codes = Array.isArray(code) ? code : [code];

        const findName = (c) => {
            for (const f of fields) {
            const fieldFilter = list.filterList.find(item => item.field === f);
            if (!fieldFilter) continue;
            const match = fieldFilter.items.find(item => item.code === c);
            if (match) return match.name; // 하나라도 찾으면 반환
            }
            return c; // 못 찾으면 코드 그대로 반환
        };

        const names = codes.map(c => findName(c));

        return Array.isArray(code) ? names.join(', ') : names[0];
    }

}