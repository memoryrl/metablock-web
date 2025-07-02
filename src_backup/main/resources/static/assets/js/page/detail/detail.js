var detail = {

    lblTextData : {},

    fnPageSearch : function(){

        Http.getJson('/assets/data/detail_data.json').then(async (data) => {

        //detail.lblTextData = data;

        // 페이지 상단 영역 조회
        const contentData = data["contentData"];
        // detail.setDetailContentData(contentData);

        // 탭-데이터 단건 조회
        const tabTableData = data["tab_data"].detailData;
        // detail.setDetailTableData(tabTableData);

        // 하위 탭-데이터 미리보기
        const subTabDataPreview = data["tab_data"].dataPreview;
        // detail.setSubTabDataPreview(subTabDataPreview);

        // 하위 탭-샘플 시각화
        const subTabSampleVisualization = data["tab_data"].sampleVisualization;
        detail.setSubTabSampleVisualization(subTabSampleVisualization);

        // 관련 데이터 조회
        const relatedData = data.relatedData;
        await detail.setRelatedData(relatedData);

        detail.initializeSwiper(); 


      });
    },

    setDataTable: function (jsonStr) {

        const jsonObj = JSON.parse(jsonStr);

        const $tableBody = $('#tab1-table tbody');
        $tableBody.empty(); // 기존 내용 제거

        // 2개의 열로 나누기
        const itemsPerRow = 2;
        const totalItems = jsonObj.labels.length;
        const totalRows = Math.ceil(totalItems / itemsPerRow);

        for (let i = 0; i < totalRows; i++) {
            const row = $('<tr>');

            for (let j = 0; j < itemsPerRow; j++) {
                const index = i * itemsPerRow + j;
                if (index < totalItems) {
                    const item = jsonObj.labels[index];
                    const value = Array.isArray(jsonObj[item.key])
                        ? jsonObj[item.key].join(', ')
                        : jsonObj[item.key] || '';

                    row.append(`
              <td class="col-2 width1">${item.label}</td>
              <td class="col-4">${value}</td>
            `);
                }
            }

            $tableBody.append(row);
        }
    },

    // 페이지 상단 영역 조회
    setDetailContentData : function(data){

        let productDetailContentEle = $(".page-details-content");
        productDetailContentEle.find("#contentTitle").text(data.contentTitle);
        productDetailContentEle.find("#descriptTitle").text(data.descriptTitle);
        productDetailContentEle.find("#description").text(data.description);

        let productDetailImgEle = $(".page-details-img-wrap");
        const randomNumber = Math.floor(Math.random() * 3) + 1;  // 1, 2, 3 중 랜덤 선택
        const dataImgUrl = `/assets/images/page/list/data_0${randomNumber}.jpg`;

        // productDetailImgEle.find(".easyzoom-pop-up.img-popup").attr("href", data.dataImgUrl);
        // this.updateEasyZoom('.easyzoom', data.dataImgUrl, data.dataImgUrl);
        productDetailImgEle.find(".easyzoom-pop-up.img-popup").attr("href", dataImgUrl);
        this.updateEasyZoom('.easyzoom', dataImgUrl, dataImgUrl);

        this.getTagList(data.tagList);

        // 상품 평점 조회
        Common.setRattingStar($(".page-rating .ti-star"), data.rating);
    },

    updateEasyZoom : function (imageContainer, thumbnail, highRes) {

        // 업데이트할 EasyZoom 요소
        const easyZoomApi = $(imageContainer).data('easyZoom');
        
        // 기존 인스턴스 해제
        if (easyZoomApi) {
            easyZoomApi.teardown();
        }
    
        // 새 이미지 설정
        $(imageContainer).html(`
            <a href="${highRes}">
                <img src="${thumbnail}" alt="Updated Image" />
            </a>
        `);
    
        // EasyZoom 재초기화
        $(imageContainer).easyZoom();
    },

    // 태그 리스트 생성
    getTagList : function(arrTagList){

      let listTagWrap = $(".list-tag-pnl ul.tag");

      for(let i=0; i<arrTagList.length; i++){

        let tagLiEle = $("<li/>");
        tagLiEle.addClass("tag-content");

        let tagAEle = $("<a/>");
        tagAEle.attr("href", arrTagList[i]["link"]);
        tagAEle.text(arrTagList[i]["title"]);

        tagLiEle.append(tagAEle);
        listTagWrap.append(tagLiEle);
      }
    },

    // 탭-데이터 단건 조회
    setDetailTableData : function(data){

        let resultObj = Object.keys(data);
        let specTableResEle = $(".specification-wrap.table-responsive");

        for(let i=0; i<resultObj.length; i++){

            specTableResEle.find("#" + resultObj[i]).text(data[resultObj[i]]);
        }
    },

    // 하위 탭-데이터 미리보기
    setSubTabDataPreview : function(dataObj){

        let tableGridListEle = $(".wishlist-table-content .table-content");
        let listData = dataObj.data;
        let headerData = listData.header;
        let bodyData = listData.body;
        
        // 초기화
        tableGridListEle.find("thead tr, tbody tr").remove();
        
        // 타이틀 영역 세팅
        let headerTrEle = $("<tr/>");
        
        for(let i=0; i<headerData.length; i++){

            let headerThEle = $("<th/>");
            headerThEle.text(headerData[i]);
            headerTrEle.append(headerThEle);
        }
        tableGridListEle.find("thead").append(headerTrEle);

        // 바디 영역 세팅
        for(let i=0; i<bodyData.length; i++){

            let bodyTrEle = $("<tr/>");

            for(let j=0; j<bodyData[i].length; j++){

                let bodyTdEle = $("<td/>");
                bodyTdEle.text(bodyData[i][j].value);
                bodyTrEle.append(bodyTdEle);
            }

            tableGridListEle.find("tbody").append(bodyTrEle);
        }
    },

    // 하위 탭-샘플 시각화
    setSubTabSampleVisualization : function(dataObj){

        this.setEchartBar({
            rendarID : $("#searchTrendTimeZoneBoard").get(0),
            color1 : "#596fc0",
            color2 : "#9eca7f",
            dataList1 : dataObj.data.dataList1,
            dataList2 : dataObj.data.dataList2
        });
    },

    // 관련 데이터 조회
    setRelatedData : async function(dataObj){

      // 항목 초기화
      let swiperWrapperEle = $(".swiper-wrapper");
      swiperWrapperEle.find(".swiper-slide").remove();

      for(let i=0; i<dataObj.length; i++){

          let swiperSlideEle = $("<div/>", {class: "swiper-slide"});
          let productWrapEle = $("<div/>", {
              "class": "page-wrap",
              "data-aos" : "fade-up",
              "data-aos-delay" : "200"
          });
          let productImgEle = $("<div/>", {class: "page-img img-zoom mb-25"});
          let productImgAEle = $("<a/>", {href: "#"});
          let productImgAImgEle = $("<img/>", {src: dataObj[i].dataImgUrl});

          productImgAEle.append(productImgAImgEle);
          productImgEle.append(productImgAEle);

          //if(dataObj[i].type === "cart"){

            const like = (dataObj[i]['interest'] === "Y") ? 'fa fa-heart' : 'pe-7s-like'
            let productActionWrapEle = $("<div/>", {class : "page-action-wrap"});
            let btnWishlistEle = $("<button/>", {
              "class" : "page-action-btn-1",
              "title" : "Wishlist"
            }).html(`<i class="${like}"></i>`);
            
            btnWishlistEle.attr("data-like", dataObj[i]['interest'])
            .attr("data-uid", dataObj[i]['uid'])
            .click(function(e){
              detail.likeClickEvent(this);
            });

            productActionWrapEle.append(btnWishlistEle);

            let btnQuickViewEle = $("<button/>", {
              "class" : "page-action-btn-1",
              "title" : "Quick View",
              "data-bs-toggle" : "modal",
              "data-bs-target" : "#exampleModal"
            }).html('<i class="pe-7s-look"></i>');

            btnQuickViewEle.attr("data-uid", dataObj[i]['uid'])
            .click(function(e){
              let uid = $(e.currentTarget).attr("data-uid");
              console.log(`uid : ${uid}`)
              window.location.href = '/asp/detail'
            });

            productActionWrapEle.append(btnQuickViewEle);

            let productAction2WrapEle = $("<div/>", {class : "page-action-2-wrap"});
            // let btnAddToCartEle = $("<button/>", {
            //   "class" : "page-action-btn-2",
            //   "title" : "Add To Cart"
            // }).html('<i class="pe-7s-cart"></i> 임시저장 항목에 추가');
            // productAction2WrapEle.append(btnAddToCartEle);

            productImgEle.append(productActionWrapEle);
            productImgEle.append(productAction2WrapEle);
          //}

          let productContentEle = $("<div/>", {class: "page-content"});
          let productContentH3Ele = $("<h3/>"); 
          let productContentH3AEle = $("<a/>", {href: "#"}).text(dataObj[i].dataTitle);
          
          productContentH3Ele.append(productContentH3AEle);
          productContentEle.append(productContentH3Ele);

          let productPriceEle = $("<div/>", {class: "page-price"});

          if(dataObj[i].type === "cart"){

            if(typeof dataObj[i].price === "object"){

              let productOldPriceEle = $("<span/>", {"class" : "old-price"}).css({"font-size" : "13px"}).text(dataObj[i].price.oldPrice);              
              let productNewPriceEle = $("<span/>", {"class" : "new-price"}).css({"font-size" : "13px"}).text(dataObj[i].price.newPrice);
              productPriceEle.append(productOldPriceEle);
              productPriceEle.append(productNewPriceEle);
            }
            else {

              let productPriceSpanEle = $("<span/>").css({"font-size" : "13px"}).text(dataObj[i].price);
              productPriceEle.append(productPriceSpanEle);
            }
          }
          else {

            let productPriceSpanEle = $("<span/>").css({"font-size" : "13px"}).text(dataObj[i].description);
            productPriceEle.append(productPriceSpanEle);
          }
          
          productContentEle.append(productPriceEle);

          productWrapEle.append(productImgEle);
          productWrapEle.append(productContentEle);

          swiperSlideEle.append(productWrapEle);
          swiperWrapperEle.append(swiperSlideEle);
      }

      await this.initializeSwiper(); 

      if (this.mySwiper) {
          this.mySwiper.update(); // Swiper 업데이트
      } else {
          console.error("Swiper 인스턴스가 존재하지 않습니다.");
      }
    },

    mySwiper : null,

    // Swiper 초기화 함수
    initializeSwiper : function () {

        this.mySwiper = new Swiper('.swiper-container', {
            navigation: {               // 네비게이션 버튼
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev',
            },
            slidesPerView: 'auto',
            spaceBetween: 30
            //loop: true
        });

        if (!this.mySwiper) {
            console.error("Swiper 인스턴스 초기화 실패");
        }
    },

    repaintSwiper : function(){

        
    },

    setEchartBar : function (argObj){

      var emphasisStyle = {
        itemStyle: {
        shadowBlur: 10,
        shadowColor: 'rgba(0,0,0,0.3)'
        }
      };

      var option = {
        tooltip: {},
        xAxis: {
          data: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
          //name: 'Month',
          axisLabel: {
            interval: 0,
            fontSize: 8
          },
          axisLine: { onZero: true },
          splitLine: { show: false },
          splitArea: { show: false }
        },
        yAxis: {
          name: 'Data',
          axisLabel: {
            fontSize: 8
          },
          axisLine: { onZero: true },
          splitLine: { show: true },
          splitArea: { show: false }
        },
        grid: {
          left: 30,
          right: 30,
          top: 40,
          bottom: 70
        },
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: '100%'
          },
          {
            start: 0,
            end: 20,
            bottom: '5px'
          }
        ],
        series: [
          {
            name: 'bar',
            type: 'bar',
            stack: 'one',
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowColor: argObj.color1
              }
            },
            data: argObj.dataList1
          },
          {
            name: 'bar2',
            type: 'bar',
            stack: 'two',
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowColor: argObj.color2
              }
            },
            data: argObj.dataList2
          }
        ]
      };

      detail.setDataToEchart(argObj.rendarID, option);
    },

    setEchartMap : function(argObj){

        echarts.registerMap('USA', argObj.paramObj, {
            Alaska: {
              left: -131,
              top: 25,
              width: 15
            },
            Hawaii: {
              left: -110,
              top: 28,
              width: 5
            },
            'Puerto Rico': {
              left: -76,
              top: 26,
              width: 2
            }
        });

        var option = {

            geo: {
                map: 'USA',
                roam: true,
                itemStyle: {
                  areaColor: '#e7e8ea'
                }
            },
            tooltip: {},
            legend: {},
            series: [
            this.randomPieSeries([-86.753504, 33.01077], 15),
            this.randomPieSeries([-116.853504, 39.8], 25),
            this.randomPieSeries([-99, 31.5], 30),
            this.randomPieSeries(
                // it's also supported to use geo region name as center since v5.4.1
                +echarts.version.split('.').slice(0, 3).join('') > 540
                ? 'Maine'
                : // or you can only use the LngLat array
                    [-69, 45.5],
                12
            )
            ]
        };

        detail.setDataToEchart(argObj.rendarID, option);
    },

    setDataToEchart : function(rendarID, argOption){
      
      let myChart = echarts.init(rendarID);
      
      var option = argOption;
      
      myChart.hideLoading();
      option && myChart.setOption(option);
      
      $(window).resize(function() {
        
        myChart.resize();
      });
    },

    randomPieSeries : function (center, radius) {

        const data = ['A', 'B', 'C', 'D'].map((t) => {
          return {
            value: Math.round(Math.random() * 100),
            name: 'Category ' + t
          };
        });
        return {
          type: 'pie',
          coordinateSystem: 'geo',
          tooltip: {
            formatter: '{b}: {c} ({d}%)'
          },
          label: {
            show: false
          },
          labelLine: {
            show: false
          },
          animationDuration: 0,
          radius,
          center,
          data
        };
    },

    fnPageMove : function(strUrl){

        // 현재 페이지의 컨텍스트 경로 추출
        const contextPath = window.location.pathname.split('/')[1]; // 'asp'

        // 이동할 페이지 설정
        const targetPage = strUrl;

        // 새 URL 생성
        const targetUrl = "/" + contextPath + targetPage;

        // 페이지 이동
        window.location.href = targetUrl;
    },

    getCurrentTimestamp : function () {

      const now = new Date();
    
      // 각 구성 요소를 두 자리로 맞춤
      const yyyy = now.getFullYear();
      const MM = String(now.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
      const dd = String(now.getDate()).padStart(2, '0');
      const hh = String(now.getHours()).padStart(2, '0');
      const mm = String(now.getMinutes()).padStart(2, '0');
      const ss = String(now.getSeconds()).padStart(2, '0');
    
      // 조합
      return `${yyyy}${MM}${dd}${hh}${mm}${ss}`;
    },

    // 파일다운로드
    fnFileDownload : function(){
      let filename = "데이터 리스트_" + this.getCurrentTimestamp() + ".xlsx";   // -> file name 지정 가능
        Http.excel('/cetus/api/user/excel', {}, filename).then(res => {
            if(res) {
                Util.alert("파일 다운로드가 완료되었습니다.")
            }
        });
    },

    likeClickEvent: async function (element) {
        const $this = $(element)
        const targetUid = $this.data('uid')
        const res = await Http.post('/contents/api/bookmark/toggle', {contentsUid: targetUid}, true);

        const like = $this.data('like')
        if (like === 'Y') {
            $this.find('i').removeClass('fa fa-heart').addClass('pe-7s-like')
            $this.data('like', 'N')
        } else if (like === 'N') {
            $this.find('i').removeClass('pe-7s-like').addClass('fa fa-heart')
            $this.data('like', 'Y')
        }
    },

};