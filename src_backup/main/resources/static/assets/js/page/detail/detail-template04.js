var detailtpl04 = {

    isContentFold : true,
    pageNumber : 1,
    type: '',
    contentsUid: null,

    setContentUid: function (uid) {
      detailtpl04.contentsUid = uid;
    },

    tabHeaderLoading: function () {
        Http.get(`/cetus/api/contents/comments/type-cnt?contentsUid=${detailtpl04.contentsUid}`).then((data) => {
            detailtpl04.setDetailRatingCnt(data);
        }).then(() => {
            detailtpl04.tabLoading();
        })

    },

    tabLoading: function () {
        Http.get(`/cetus/api/contents/comments/page?pageNumber=${detailtpl04.pageNumber}&type=${detailtpl04.type}&contentsUid=${detailtpl04.contentsUid}`).then(async (data) => {
            detailtpl04.setDetailRatingListData(data);
        })
    },

    setDetailRatingCnt: function (obj) {
        let totalCountWrapEle = $(".sidebar-list-style-2");
        totalCountWrapEle.find("#ALL_CNT").text(obj['total']);
        totalCountWrapEle.find("#OPINION_CNT").text(obj['opinion']);
        totalCountWrapEle.find("#QUESTION_CNT").text(obj['question']);
        totalCountWrapEle.find("#REPORT_CNT").text(obj['report']);

        const avgRating = $("#page-content-avg-rating");
        avgRating.empty(); // 기존 내용 비우기
        const totalRating = Math.round(obj['ratingAvg']); // 정수로 반올림
        for (let i = 1; i <= 5; i++) {
            const filled = i <= totalRating ? ' filled' : '';
            avgRating.append(`<i class="ti-star${filled}"></i>`);
        }
    },

    setDetailRatingListData : function(dataObj){
        if(dataObj['list'].length !== 0) {
            $('.blog-details-wrapper .blog-comment-wrapper').children().remove();
            let listData = dataObj['list'];
            for(let i=0; i<listData.length; i++){
                this.setCommentListEle(listData[i]);
            }
            this.setPageNationEle(dataObj);
        } else {
            detailtpl04.setCommentEmptyDiv();
        }
    },

    setCommentEmptyDiv: function () {

        $('.blog-details-wrapper .blog-comment-wrapper').append(`<div class="pagination-style-1 aos-init aos-animate" data-aos="fade-up" data-aos-delay="200"><ul>
                                                                 <span>데이터가 없습니다.</span></ul></div>`)
    },

    setCommentListEle : function(dataObj){

        let commentWrapperEle = $("<div/>", {
            "class" : "single-comment-wrapper single-comment-border",
            "data-aos" : "fade-up",
            "data-aos-delay" : "400"
        });

        // 프로필 사진 영역
        let commentUserImgWrapEle = $("<div/>", {"class" : "blog-comment-img"}).css({
            "width": "120px",
            "height": "120px",
            "min-width": "120px"
        });
        const src = (dataObj['regProfileId']) ? `/cetus/files/download?dataObj=${dataObj['regProfileId']}` : '/assets/images/page/detail/profile_noimage.png'
        let commentUserImgEle = $("<img/>", {"src" : src})
            .css({
                "background" : "#f4f4f4",
                "border" : "1px solid #f4f4f4",
                "width" : "120px",
                "height": "120px"
            })
            .attr("onerror", "this.src='/assets/images/page/detail/profile_noimage.png'");

        //let commentUserImgEle = $("<i/>", {"class" : "pe-7s-user"}).css({"font-size" : "80px"});
        commentUserImgWrapEle.append(commentUserImgEle);

        // 컨텐츠 영역
        let commentContentEle = $("<div/>", {"class" : "blog-comment-content"}).css({"width": "calc(100% - 120px)"});
        let commentInfoWrapEle = $("<div/>", {"class" : "comment-info-reply-wrap"});
        let commentPEle = $("<p/>").text(dataObj.comment);

        // 내용을 기본으로 접힌 상태로 할지?
        if(this.isContentFold){
            commentPEle.addClass("fold");
            commentContentEle.css("width", "calc(100% - 140px)");
        }

        commentContentEle.append(commentInfoWrapEle);
        commentContentEle.append(commentPEle);

        let commentInfoEle = $("<div/>", {"class" : "comment-info"});
        let commentInfoSectionEle = $("<div/>").css({
            "display" : "flex",
            "gap" : "20px",
            "align-items" : "baseline",
            "width" : "100%",
            "justify-content" : "space-between"
        });
        let dateSpanEle = $("<span/>").text(dataObj['regDt']);
        let sectionSpanEle = $("<span/>").css({"color": "#e97730"}).text(dataObj['typeStr']);
        commentInfoSectionEle.append(dateSpanEle).append(sectionSpanEle);
        commentInfoEle.append(commentInfoSectionEle);

        let commentInfoSection2Ele = $("<div/>").css({
            "display" : "flex",
            "gap" : "20px",
            "align-items" : "baseline",
            "width" : "100%",
            "justify-content" : "space-between"
        });
        let commentInfoH4Ele = $("<h4/>").text(dataObj['regNm']);
        let commentInfoRatingEle = $("<div/>", {"class" : "result-rating"});

        for(let i=0; i<5; i++){
            let iStarEle = $("<i/>", {"class" : "ti-star"});
            commentInfoRatingEle.append(iStarEle);
        }

        commentInfoSection2Ele.append(commentInfoH4Ele).append(commentInfoRatingEle);
        commentInfoEle.append(commentInfoSection2Ele);

        commentInfoWrapEle.append(commentInfoEle);

        let commentToggleEle = $("<div/>", {"class" : "comment-reply"});
        let btnToggleEle = $("<a/>", {
            "class" : (this.isContentFold)? "ti-angle-down" : "ti-angle-up",
            "href" : "javascript:void(0);",
            "data-isfold" : (this.isContentFold)? true : false
        }).on("click", function(e){
            if($(e.currentTarget).attr("data-isfold") === "true"){
                $(e.currentTarget).attr({
                    "class" : "ti-angle-up",
                    "data-isfold" : false
                });
                commentContentEle.css("width", "calc(100% - 120px)");
            }
            else {
                $(e.currentTarget).attr({
                    "class" : "ti-angle-down",
                    "data-isfold" : true
                });
                commentContentEle.css("width", "calc(100% - 140px)");
            }
            void commentPEle.get(0).offsetHeight;
            commentPEle.toggleClass('fold');

            e.preventDefault();
            e.stopPropagation();
        });
        commentToggleEle.append(btnToggleEle);
        commentInfoWrapEle.append(commentToggleEle);

        commentWrapperEle.append(commentUserImgWrapEle);
        commentWrapperEle.append(commentContentEle);

        $("#ratingList .blog-comment-wrapper").append(commentWrapperEle).ready(function(){
            Common.setRattingStar(commentInfoRatingEle.find(".ti-star"), dataObj['ratings']);
        });
    },

    setPageNationEle : function(dataObj){
        let paginationEle = $('<div>', {
            "class": "pagination-style-1 aos-init",
            "data-aos": "fade-up",
            "data-aos-delay": "200"
        });

        let ulEle = $("<ul>");

        for(let j=0; j < Math.ceil(dataObj['totalElements'] / 2); j++) {
            let aEle = $('<a>', {
                class : j+1 == this.pageNumber ? 'active' : '',
                href : `javascript: detailtpl04.fnPage(${j+1});`,
                text : j+1
            });
            let liEle = $('<li>').append(aEle);
            ulEle.append(liEle);
        }

        paginationEle.append(ulEle);

        $("#ratingList .blog-comment-wrapper").append(paginationEle);
    },

    fnPage : function(selectPage) {
        $("#ratingList .blog-comment-wrapper").children().remove();
        this.pageNumber = selectPage;
        detailtpl04.tabLoading();
    },

    ratingSave: function (obj) {
        const _obj = {
            contentsUid: contentUid,
            type: obj.ratingType,
            ratings: obj.rating,
            comment: obj.detailContents
        }
        Http.post(`/cetus/api/contents/comments`, _obj, true).then((res) => {
            Util.alert("평점 정보가 저장되었습니다.").then(() => {
                detailtpl04.tabHeaderLoading();
            })
        });
    }
};