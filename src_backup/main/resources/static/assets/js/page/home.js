var home = {

    startPage: "/asp/home",

    /**
     *  home 데이터를 통해 메인 화면 그리기
     * */
    drawMainContent: function() {
        Http.getJson('/assets/data/home_data.json').then((res) => {
            home.drawMenuCategory01(res['mainContents']['menu1'])
            home.drawMenuCategory02(res['mainContents']['menu2'])
            /*home.drawPost(res['secondContents'])*/
        })
    },

    /**
     * 1번째 메뉴 카테고리(좌측) 영역 그리기
     * */
    drawMenuCategory01: function(data) {
        const h3_01 = $("#menu-category-h3-01")
        h3_01.html(`${data['name']} 카테고리`)

        const menuUid = data['uid']
        const moreBtn01 = $("#menu-category-more-01")
        moreBtn01.attr('data-uid', menuUid)

        const category01 = $("#menu-category-list-01")
        category01.empty()
        data['category'].forEach((obj, index) => {
            // 1단계: <li> 생성
            const listItem = $("<li></li>");

            // 2단계: <a> 생성
            let link = $("<a></a>").attr("href", "javascript:void(0);") // href 속성 추가
                .attr("data-uid", obj['uid']) // data-uid 속성 추가
                .attr("id", `category01-${obj['uid']}`); // id 속성 추가
            if(index === 0) {
                link.addClass('active');
                home.showListDiv01(obj['uid'])
            }

            // 3단계 : <span> 생성
            const span = $("<span></span>").text(obj['name']); // 이름 추가

            // 4단계 : <a>링크에 <span>추가
            link.append(span);

            // 5단계: <a>를 <li>에 추가
            listItem.append(link);

            // 4단계: <li>를 <ul>에 추가
            category01.append(listItem);
        })
    },

    /**
     * 2번째 메뉴 카테고리(좌측) 영역 그리기
     * */
    drawMenuCategory02: function(data) {
        const h3_02 = $("#menu-category-h3-02")
        h3_02.html(`${data['name']} 카테고리`)

        const menuUid = data['uid']
        const moreBtn02 = $("#menu-category-more-02")
        moreBtn02.attr('data-uid', menuUid)

        const category02 = $("#menu-category-list-02")
        category02.empty()
        data['category'].forEach((obj, index) => {
            // 1단계: <li> 생성
            const listItem = $("<li></li>");

            // 2단계: <a> 생성
            let link = $("<a></a>").attr("href", "javascript:void(0);") // href 속성 추가
                .attr("data-uid", obj['uid']) // data-uid 속성 추가
                .attr("id", `category02-${obj['uid']}`); // id 속성 추가
            if(index === 0) {
                link.addClass('active');
                home.showListDiv02(obj['uid']);
            }

            // 3단계 : <span> 생성
            const span = $("<span></span>").text(obj['name']); // 이름 추가

            // 4단계 : <a>링크에 <span>추가
            link.append(span);

            // 5단계: <a>를 <li>에 추가
            listItem.append(link);

            // 4단계: <li>를 <ul>에 추가
            category02.append(listItem);
        })
    },

    /**
     * 게시글 영역 그리기
     * */
    drawPost: function(data) {
        const postLeftRow = $("#post-wrap-left-row-01")
        const postRightRow = $("#post-wrap-right-latest-01")

        postLeftRow.empty()
        postRightRow.empty()

        for(let i=0; i<data.length; i++) {
            const obj = data[i]
            if(i<2) {
                postLeftRow.append(home.drawLeftPost(obj))
            } else if(i >=2 && i<5) {
                postRightRow.append(home.drawRightPost(obj))
            }
        }
    },

    /**
     * 좌측 게시글 영역 그리기
     * */
    drawLeftPost: function(data) {
        // 데이터를 기준으로 동적으로 블로그 항목을 만들기 위한 예시
        let postWrap = $('<div/>').addClass('col-lg-6 col-md-6 col-sm-6');
        let blogWrap = $('<div/>').addClass('blog-wrap aos-init aos-animate');
        let blogImgDateWrap = $('<div/>').addClass('blog-img-date-wrap mb-25');

        // 블로그 이미지 영역
        let blogImg = $('<div/>').addClass('blog-img');
        let imgLink = $('<a/>').attr('href', 'javascript:void(0);').attr('data-uid', data['uid']).addClass('post-main-img');
        let img = $('<img/>').attr('src', data['img']).attr('alt', '').attr('width', 370); // 너비 고정
        imgLink.append(img);
        blogImg.append(imgLink);

        // 블로그 날짜 영역
        const [year, month, day] = data['date'].split("-"); // 날짜를 쪼개서 분리
        let blogDate = $('<div/>').addClass('blog-date');
        let dateContent = $('<h5/>').html(`<span>${year}</span>${month}.${day}`);
        blogDate.append(dateContent);

        // 블로그 내용
        let blogContent = $('<div/>').addClass('blog-content');
        let blogMeta = $('<div/>').addClass('blog-meta');
        let metaList = $('<ul/>');
        let metaItem = $('<li/>').html(`By: <a href="javascript:void(0);" class="post-rgtrUid" data-uid="${data['rgtrUid']}"> ${data['rgtrNm']}</a>`)
        metaList.append(metaItem);
        blogMeta.append(metaList);

        let postTitle = $('<h3/>');
        let titleLink = $('<a/>').attr('href', 'javascript:void(0);').text(data['title']).attr('data-uid', data['uid']).addClass('post-title')
        postTitle.append(titleLink);

        let description = $('<p/>').html(data['content']);

        blogContent.append(blogMeta, postTitle, description);

        // 모든 요소를 조합
        blogImgDateWrap.append(blogImg, blogDate);
        blogWrap.append(blogImgDateWrap, blogContent);
        postWrap.append(blogWrap);

        return postWrap
    },

    /**
     * 우측 게시글 영역 그리기
     * */
    drawRightPost: function(data) {
        // single-latest-post를 생성
        let singleLatestPost = $('<div/>').addClass('single-latest-post');

        // latest-post-img를 생성
        let latestPostImg = $('<div/>').addClass('latest-post-img');
        let imgLink = $('<a/>').attr('href', 'javascript:void(0);').attr('data-uid', data['uid']).addClass('post-main-img');
        let img = $('<img/>').attr('src', data['img']).attr('alt', '').attr('width', 115); // 너비 고정
        imgLink.append(img);
        latestPostImg.append(imgLink);

        // latest-post-content를 생성
        let latestPostContent = $('<div/>').addClass('latest-post-content');
        const { year, month, day } = DateUtil.dateToEng(data['date'])
        let postSpan = $('<span/>').html(`${data['date']}`)
        let postTitle = $('<h4/>');
        let titleLink = $('<a/>').attr('href', 'javascript:void(0);').text(data['title']).attr('data-uid', data['uid']).addClass('post-title');
        postTitle.append(titleLink);

        const content = home.truncateText(data['content'])
        let latestPostBtn = $('<div/>').addClass('latest-post-btn');
        let btnLink = $('<a/>').attr('href', 'javascript:void(0);').text(content).attr('data-uid', data['uid']).addClass('post-content');
        latestPostBtn.append(btnLink);

        // single-latest-post에 이미지와 콘텐츠 추가
        latestPostContent.append(postSpan, postTitle, latestPostBtn);
        singleLatestPost.append(latestPostImg, latestPostContent);

        return singleLatestPost
    },

    /**
     * 게시글 내용 보여주는 영역에서 특정 길이만큼 보여주고 자르기
     * */
    truncateText: function(text, maxLength = 20) {
        if(text.length > maxLength) {
            return text.substring(0, maxLength) + "...";
        } else return text;
    },

    /**
     * [page07: 상세페이지] 이동
     * */
    viewContentPage: function(uid) {
        console.log(`uid : ${uid}`)
        window.location.href = '/asp/detail'
    },

    /**
     * 1번째 메뉴 카테고리(좌측) 영역 클릭시 이벤트
     * */
    category1ClickEvent: function(element) {
        const dataUid = $(element).data('uid');
        if ($(element).hasClass('active')) {
            $(element).removeClass('active');
            home.showCategory1EmptyDiv();
        } else {
            $('#menu-category-list-01.category-list a').removeClass('active');
            $(element).addClass('active');
            home.showListDiv01(dataUid);
        }
    },

    /**
     * 1번째 메뉴 우측 컨텐츠 영역 empty div 보여주기
     * */
    showCategory1EmptyDiv: function() {
        $("#menu-category-right-empty-01").css('display', 'block');
        $("#menu-category-right-card-01").css('display', 'none');
    },

    /**
     * 1번째 메뉴 우측 컨텐츠 영역 관련 데이터 가져오기
     * */
    showListDiv01: function(uid) {
        /**
         * uid : 선택된 카테고리 uid
         * */
        Http.getJson('/assets/data/home_data.json').then((res) => {
            const categories = res['mainContents']['menu1']['category']
            const result = categories.find(obj => obj['uid'] === uid)
            const name = result['name']
            const recommends = result['recommend']
            home.drawListDiv01(recommends, name)
        })
    },

    /**
     * 1번째 메뉴 우측 컨텐츠 영역 그리기
     * */
    drawListDiv01: function(list, name) {
        /**
         * list : 해당 카테고리 하위에 추천 컨텐츠 리스트
         * name : 해당 카테고리 명
         * */
        const rightCard01 = $("#menu-category-right-card-01")
        rightCard01.removeClass('loaded')

        $("#menu-category-right-empty-01").css('display', 'none')
        rightCard01.css('display', 'block')

        const rightRow01 = $('#menu-category-right-row-01')

        // 임시 컨테이너 생성
        let tempContainer = $('<div/>');

        list.forEach(obj => {
            let colDiv = $('<div/>').addClass('col-lg-6 col-md-12 col-12 card-padding-5');

            // card card-banner div 생성
            let cardBannerDiv = $('<div/>').addClass('card card-banner');

            // 첫 번째 padding div 생성
            let paddingDiv1 = $('<div/>').addClass('card-padding-10');
            let spanElement = $('<span/>').text(name);
            let headingElement = $('<h3/>').css('margin-top', '10px').text(obj['title']);
            paddingDiv1.append(spanElement).append(headingElement);

            // 두 번째 padding div 생성
            let paddingDiv2 = $('<div/>').addClass('card-padding-10');
            let sliderBtnDiv = $('<div/>').addClass('slider-btn-3 btn-hover');
            let anchorElement = $('<a/>').attr('href', 'javascript:void(0)')
                .attr('data-uid', obj['uid'])
                .addClass('btn theme-color animated content-section')
                .html('컨텐츠 다운로드 &nbsp; &gt;');
            sliderBtnDiv.append(anchorElement);
            paddingDiv2.append(sliderBtnDiv);

            // 구조 연결
            cardBannerDiv.append(paddingDiv1).append(paddingDiv2);
            colDiv.append(cardBannerDiv);

            // 임시 컨테이너에 연결
            tempContainer.append(colDiv)
        })

        rightRow01.empty().append(tempContainer.children())
        setTimeout(() => {
            rightCard01.addClass('loaded');
        }, 150); // DOM 업데이트 후 로딩 완료
    },

    /**
     * 2번째 메뉴 카테고리(좌측) 영역 클릭시 이벤트
     * */
    category2ClickEvent: function(element) {
        const dataUid = $(element).data('uid')
        if($(element).hasClass('active')) {
            $(element).removeClass('active')
            home.showCategory2EmptyDiv()
        } else {
            $('#menu-category-list-02.category-list a').removeClass('active')
            $(element).addClass('active')
            home.showListDiv02(dataUid)
        }
    },

    /**
     * 2번째 메뉴 우측 컨텐츠 영역 empty div 보여주기
     * */
    showCategory2EmptyDiv: function() {
        $("#menu-category-right-empty-02").css('display', 'block')
        $("#menu-category-right-card-02").css('display', 'none')
    },

    /**
     * 2번째 메뉴 우측 컨텐츠 영역 관련 데이터 가져오기
     * */
    showListDiv02: function(uid) {
        /**
         * uid : 선택된 카테고리 uid
         * */
        Http.getJson('/assets/data/home_data.json').then((res) => {
            const categories = res['mainContents']['menu2']['category']
            const result = categories.find(obj => obj['uid'] === uid)
            const name = result['name']
            const recommends = result['recommend']
            home.drawListDiv02(recommends, name)
        })
    },

    /**
     * 2번째 메뉴 우측 컨텐츠 영역 그리기
     * */
    drawListDiv02: function(list, name) {
        /**
         * list : 해당 카테고리 하위에 추천 컨텐츠 리스트
         * name : 해당 카테고리 명
         * */

        const rightCard02 = $("#menu-category-right-card-02")
        rightCard02.removeClass('loaded')

        $("#menu-category-right-empty-02").css('display', 'none')
        rightCard02.css('display', 'block')

        const rightRow02 = $('#menu-category-right-row-02')

        // 임시 컨테이너 생성
        let tempContainer = $('<div/>');

        list.forEach(obj => {
            // 최상위 div 생성
            let colDiv = $('<div/>').addClass('col-lg-4 col-md-4 col-sm-6 col-12');

            // page-wrap div 생성
            let productWrap = $('<div/>').addClass('page-wrap mb-35');

            // page-img div 생성
            let productImgDiv = $('<div/>').addClass('page-img img-zoom mb-25');
            let productLink = $('<a/>').attr('href', "javascript:void(0);")
                .attr('data-uid', obj['uid'])
                .addClass('img-content-section')
            let productImg = $('<img/>').attr('src', obj['img']).attr('alt', '').attr('width', 270); // 너비 고정
            productLink.append(productImg);
            let productActionWrap = $('<div/>').addClass('page-action-wrap');

            const like = (obj['interest'] === "Y") ? 'fa fa-heart' : 'pe-7s-like'
            let wishlistButton = $('<button/>').addClass(`page-action-btn-1 like-content-btn`)
                .attr('title', 'Wishlist')
                .attr('data-like', obj['interest'])
                .attr('data-uid', obj['uid'])
                .html(`<i class="${like}"></i>`);
            let quickViewButton = $('<button/>').addClass('page-action-btn-1 view-content-btn')
                .attr('title', 'Quick View')
                .attr('data-uid', obj['uid'])
                .html('<i class="pe-7s-look"></i>');
            productActionWrap.append(wishlistButton, quickViewButton);
            productImgDiv.append(productLink, productActionWrap);

            // page-content div 생성
            let productContentDiv = $('<div/>').addClass('page-content');
            let productTitle = $('<h3/>').html(`<a href="javascript:void(0);" class="content-title" data-uid="${obj['uid']}">${obj['title']}</a>`);
            let productPrice = $('<div/>').addClass('page-price').html(`<span>${obj['content']}</span>`);
            productContentDiv.append(productTitle, productPrice);

            // 구조 연결
            productWrap.append(productImgDiv, productContentDiv);
            colDiv.append(productWrap);

            // 임시 컨테이너 연결
            tempContainer.append(colDiv)
        })

        rightRow02.empty().append(tempContainer.children())
        setTimeout(() => {
            rightCard02.addClass('loaded');
        }, 150); // DOM 업데이트 후 로딩 완료
    },

    /**
     * 1번째 메뉴 [컨텐츠 다운로드] 버튼 클릭 이벤트
     * */
    category1ContentDownload: function(uid) {
        /** [컨텐츠 다운로드] 버튼 클릭 이벤트 */
        console.log(`uid : ${uid}`)
        window.location.href = '/asp/detail'
    },

    /**
     * 1번째, 2번째 메뉴 카테고리(좌측) 영역에서 [더보기] 버튼 클릭 이벤트
     * */
    ListContentPage: function (uid) {
        console.log(`uid : ${uid}`)
        window.location.href = '/asp/list'
    },

    /**
     * 2번째 메뉴 컨텐츠(우측) 영역에서 [좋아요] 클릭 이벤트
     * */
    likeClickEvent: function(element) {
        const $this = $(element)
        const dataUid = $this.data('uid')
        const like = $this.data('like')
        if (like === 'Y') {
            $this.find('i').removeClass('fa fa-heart').addClass('pe-7s-like')
            $this.data('like', 'N')
        } else if (like === 'N') {
            $this.find('i').removeClass('pe-7s-like').addClass('fa fa-heart')
            $this.data('like', 'Y')
        }
    }
}
