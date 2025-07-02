var sidebar = {

    totalCnt: 0,
    contentLst: [],
    userObj : {},
    isLoginCheck: function () {
        const user = typeof this.userObj !== "undefined" && this.userObj ? this.userObj : {};
        return user.userId && user.userId.trim() !== "";
    },
    getSidebarLst: async function () {
        await Http.get('/contents/api/bookmark/list').then((res) => {
            sidebar.drawSidebarLst(res['data']);
        })
    },
    drawSidebarLst: function (data = []) {
        sidebar.contentLst = []; // data setting 하기 전, init
        sidebar.totalCnt = data?.length || 0;
        var $list = $("#data-list-ul");
        $list.empty();

        data?.forEach(item => {
            sidebar.contentLst.push(item.contentsUid);
            let url = (item.targetType === 'WISH_ARTIST' ? '/guro/artist/detail/' : '/guro/artist/work/') + item.contentsUid;

            let listItem = $('<li/>');
            let strTitle = item?.title || "정보가 없습니다";
            
            // 임시이미지 처리
            let tempImgUrl = "/assets/images/page/detail/profile_noimage.png";

            if(item.targetType === 'WISH_WORK'){

                tempImgUrl = "/assets/images/page/detail/profile_noimage_work.png";
            }

            let contentImg = $('<div/>').addClass('cart-img')
                                        .append($('<a/>').attr('href', url)
                                        .append($('<img/>').attr({
                                            'src' : '/cetus/files/download?fileId=' + item.fileId,
                                            'alt' : '',
                                            'onerror' : 'this.src="' + tempImgUrl + '"'
                                        })));

            let contentTitle = $('<div/>').addClass('cart-title')
                                          .append($('<h4/>').append($('<a/>').attr('href', url).text(strTitle)))
                                          .append($('<span/>').text(item.description));

            let contentDel = $('<div/>').addClass('cart-delete')
                                        .append($('<a/>').attr('href', 'javascript:void(0);').addClass('delete-item')
                                        .attr('data-uid', item.contentsUid)
                                        // .attr('data-target', item.targetType)
                                        .text('×'));

            // 리스트 아이템에 요소 추가
            listItem.append(contentImg, contentTitle, contentDel);
            $list.append(listItem);
        });

        // 즐겨찾기 항목이 없을 때 메시지 처리
        if(sidebar.totalCnt === 0){
        
            sidebar.setEmptyMsg();
        }
        
        sidebar.settingTotalCount();
        $('.delete-item')?.on("click", function (e) {
            e.preventDefault();
            sidebar.deleteContent($(this));
        });
    },
    deleteContent: async function ($btn) {
        const targetUid = $btn.data("uid");
        const targetType = $btn.data('target');

        const res = await Http.delete('/contents/api/bookmark/delete', {contentsUid: targetUid});
        if (res.code === 0) {
            sidebar.contentLst = sidebar.contentLst.filter(item => item !== targetUid);
            $btn.closest("li").remove();
            sidebar.totalCnt = sidebar.totalCnt - 1;
            sidebar.settingTotalCount();
        }

        // 즐겨찾기 항목이 없을 때 메시지 처리
        if(sidebar.totalCnt === 0){
        
            sidebar.setEmptyMsg();
        }
    },
    settingTotalCount() {
        $("#total-data").text(`${sidebar.totalCnt}개`)
        const total = sidebar.totalCnt === 0 ? '0' : sidebar.totalCnt < 10 ? sidebar.totalCnt.toString().padStart(2, '0') : sidebar.totalCnt.toString();
        $("#total-content-shopbag").text(total)
    },
    downloadContent: function () {
        /*console.log("다운받기 버튼 클릭 : ", sidebar.contentLst)*/
    },
    seeAll: function () {
        /*console.log("데이터 담기 전체보기 버튼 클릭...")*/
    },
    setEmptyMsg: function(){

        let listItem = $('<li/>');
        let contentTitle = $('<div/>').addClass('cart-title').append($('<span/>').text("정보가 없습니다."));

        listItem.append(contentTitle);
        $("#data-list-ul").append(listItem);
    }
}