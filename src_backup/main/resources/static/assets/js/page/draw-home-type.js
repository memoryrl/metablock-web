var drawHomeType = {

    TypeA: function(homeData, categories, typeConfig, selectedCategory) {

        const $rightCardWrap = $('<div>', {
            class: 'col-lg-9 col-md-12 col-12 home-data-list',
            css: { display: 'block' }
        });

        const $row = $('<div>', {
            class: 'row',
            css: { height: '100%' }
        });

        homeData = homeData.filter(item => item.category === (selectedCategory || categories[0]));

        const displayCount = Math.min(typeConfig.maxCount, homeData.length);
        const colSize = Math.floor(12 / displayCount);

        homeData.forEach((data, index) => {
            
            const $col = $('<div>', { class: `col-lg-${colSize} col-md-12 col-12 card-padding-5` });

            const $card = $('<div>', { class: 'card card-banner' });
            if(data.image) {
                $card.addClass('card-background');
                $card.css('background-image',  `url('${data.image}')`);
            }

            const $content1 = $('<div>', { class: 'card-padding-10' }).append(
                $('<span>', {
                    class: 'card-text-shadow',
                    text: data.category
                }),
                $('<h3>', {
                    class: 'card-text-shadow',
                    text: data.title,
                    css: { marginTop: '10px' }
                })
            );

            const $content2 = $('<div>', { class: 'card-padding-10' }).append(
                $('<div>', { class: 'slider-btn-3 btn-hover' }).append(
                    $('<a>', {
                    href: data.url,
                    class: 'btn theme-color animated',
                    text: data.label
                    })
                )
            );

            $card.append($content1, $content2);
            $col.append($card);
            $row.append($col);
        });

        $rightCardWrap.append($row);

        return $rightCardWrap;
    },

    TypeB: function(homeData, categories, typeConfig, selectedCategory) {

        const $rightCardWrap = $('<div>', {
            class: 'col-lg-9 col-md-12 col-12 home-data-list',
            css: { display: 'block' },
        });

        // row 컨테이너
        const $row = $('<div>', {
            class: 'row',
        });

        homeData = homeData.filter(item => item.category === (selectedCategory || categories[0]));

        const displayCount = Math.min(typeConfig.maxCount, homeData.length);
        const colSize = Math.floor(12 / displayCount);

        homeData.forEach((item, index) => {
            
            const $cardCol = $('<div>', { class: `col-lg-${colSize} col-md-${colSize} col-12 card-padding-5` });
            const $wrap = $('<div>', { class: 'page-wrap mb-35' });

            const $imgWrap = $('<div>', { class: 'page-img img-zoom mb-25' });
            const $imgLink = $('<a>', { href: 'javascript:void(0);' });
            const $img = $('<img>', { src: item.image, alt: '' });

            const $actionWrap = $('<div>', { class: 'page-action-wrap' });
            const $wishBtn = $('<button>', { class: 'page-action-btn-1', title: 'Wishlist' }).append($('<i>', { class: 'pe-7s-like' }));
            const $quickBtn = $('<button>', {
                class: 'page-action-btn-1 detail-btn',
                title: 'Quick View'
            }).append($('<i>', { class: 'pe-7s-look' }));

            // 두 번째 카드부터 모달 기능 추가
            if (index > 0) {
                $quickBtn.attr('data-bs-toggle', 'modal').attr('data-bs-target', '#exampleModal');
            }

            $actionWrap.append($wishBtn, $quickBtn);
            $imgLink.append($img);
            $imgWrap.append($imgLink, $actionWrap);

            const $content = $('<div>', { class: 'page-content' });
            const $title = $('<h3>').append($('<a>', { href: 'javascript:void(0);', text: item.title }));
            const $desc = $('<div>', { class: 'page-price' }).append($('<span>', { text: item.description }));

            $content.append($title, $desc);
            $wrap.append($imgWrap, $content);
            $cardCol.append($wrap);
            $row.append($cardCol);
        });

        $rightCardWrap.append($row);

        return $rightCardWrap;
    },

    TypeC: function(homeData, typeConfig) {

        let cardDisplayCount = 0, cardColSize = 0, postColSize = 0;

        if (homeData.length <= 4) {
            cardDisplayCount = homeData.length;
            cardColSize = 12;
            postColSize = Math.floor(12 / cardDisplayCount);
        } else if (homeData.length === 5) {
            cardDisplayCount = 2;
            cardColSize = 8;
            postColSize = 4;
        } else if (homeData.length <= 7) {
            cardDisplayCount = 1;
            cardColSize = 4;
            postColSize = 4;
        } else if (homeData.length >= 8) {
            cardDisplayCount = 0;
            cardColSize = 0;
            postColSize = 4;
        } else {
            cardDisplayCount = 0;
            cardColSize = 0;
            postColSize = 0;
        }

        const postDisplayCount = Math.min(9, homeData.length - cardDisplayCount); 

        let $resultCol = $();

        const $leftCol = $('<div>', {
            class: `col-lg-${cardColSize}`,
        });

        const cardData = homeData.slice(0, cardDisplayCount);
        if(cardDisplayCount > 0) {

            const $leftRow = $('<div>', {
                class: 'row'
            });
        
            // 포스트별 카드 생성
            cardData.forEach(item => {
                const $col = $('<div>', { class: `col-lg-${12 / cardDisplayCount} col-md-${12 / cardDisplayCount} col-sm-${12 / cardDisplayCount}` });

                const $wrap = $('<div>', { class: 'blog-wrap aos-init aos-animate' });

                const $imgWrap = $('<div>', { class: 'blog-img-date-wrap mb-25' });

                const $imgBox = $('<div>', { class: 'blog-img' }).append(
                    $('<a>', { href: 'javascript:void(0);' }).append(
                    $('<img>', {
                        src: item.image,
                        alt: '',
                        width: 370,
                        onerror: "this.src='/assets/images/page/thumbnail_default.png'"
                    })
                    )
                );


                const dateObj = new Date(item.date);
                const month = dateObj.toLocaleString("en-US", { month: "long" }); // "May"
                const day = dateObj.getDate();

                const $dateBox = $('<div>', { class: 'blog-date' }).append(
                    $('<h5>').append(
                        $('<span>').text(day),
                        $('<span>').text(month)
                    )
                );

                $imgWrap.append($imgBox, $dateBox);

                const $content = $('<div>', { class: 'blog-content' }).append(
                    $('<div>', { class: 'blog-meta' }).append(
                    $('<ul>').append(
                        $('<li>').html(`By:&nbsp;<span>${item.author}</span>`)
                    )
                    ),
                    $('<h3>').append(
                    $('<a>', { href: item.href, text: item.title })
                    ),
                    $('<p>').text(item.description)
                );

                    $wrap.append($imgWrap, $content);
                    $col.append($wrap);
                    $leftRow.append($col);
            });

            $leftCol.append($leftRow);

            if ($resultCol.length) $resultCol = $resultCol.add($leftCol);
            else $resultCol = $($leftCol);
        }


        for (let i = 0; i < Math.floor(12 / postColSize); i++) {

            const $rightCardWrap = $('<div>', {
                class: `col-lg-${postColSize}`,
            });

            const $sidebarWidget = $('<div>', {
                class: 'sidebar-widget mb-40 aos-init aos-animate',
                'data-aos': 'fade-up',
                'data-aos-delay': '200'
            });
        
            const itemsPerColumn = 3; 

            const postData = homeData.slice(cardDisplayCount, cardDisplayCount + 9);
            const start = i * itemsPerColumn;
            const end = start + itemsPerColumn;
            postDataSlice = postData.slice(start, end);

            if(postDisplayCount > 0) {
                
                const $latestPostWrap = $('<div>', {
                    class: 'latest-post-wrap',
                });

                // 각 포스트 DOM 생성 및 추가
                postDataSlice.forEach(item => {
                    const $post = $('<div>', { class: 'single-latest-post' });

                    const $imgBox = $('<div>', { class: 'latest-post-img' }).append(
                        $('<a>', { href: item.href }).append(
                            $('<img>', {
                                src: item.image,
                                alt: '',
                                width: 115,
                                onerror: "this.src='/assets/images/page/thumbnail_default.png'"
                            })
                        )
                    );

                    const $contentBox = $('<div>', { class: 'latest-post-content' }).append(
                        $('<span>').text(item.date),
                        $('<h4>').append($('<a>', { href: item.href, text: item.title })),
                        $('<div>', { class: 'latest-post-btn' }).append(
                        $('<a>', { href: item.href, text: item.description })
                        )
                    );

                    $post.append($imgBox, $contentBox);
                    $latestPostWrap.append($post);
                });

                // DOM 조립
                $sidebarWidget.append($latestPostWrap);
                $rightCardWrap.append($sidebarWidget);

            }

            if ($resultCol.length) $resultCol = $resultCol.add($rightCardWrap);
            else $resultCol = $($rightCardWrap);

        }

        return $resultCol;
    },

    TypeC: function(homeData, typeConfig) {

        let cardDisplayCount = 0, cardColSize = 0, postColSize = 0;

        if (homeData.length <= 4) {
            cardDisplayCount = homeData.length;
            cardColSize = 12;
            postColSize = Math.floor(12 / cardDisplayCount);
        } else if (homeData.length === 5) {
            cardDisplayCount = 2;
            cardColSize = 8;
            postColSize = 4;
        } else if (homeData.length <= 7) {
            cardDisplayCount = 1;
            cardColSize = 4;
            postColSize = 4;
        } else if (homeData.length >= 8) {
            cardDisplayCount = 0;
            cardColSize = 0;
            postColSize = 4;
        } else {
            cardDisplayCount = 0;
            cardColSize = 0;
            postColSize = 0;
        }

        const postDisplayCount = Math.min(9, homeData.length - cardDisplayCount); 

        let $resultCol = $();

        const $leftCol = $('<div>', {
            class: `col-lg-${cardColSize}`,
        });

        const cardData = homeData.slice(0, cardDisplayCount);
        if(cardDisplayCount > 0) {

            const $leftRow = $('<div>', {
                class: 'row'
            });
        
            // 포스트별 카드 생성
            cardData.forEach(item => {
                const $col = $('<div>', { class: `col-lg-${12 / cardDisplayCount} col-md-${12 / cardDisplayCount} col-sm-${12 / cardDisplayCount}` });

                const $wrap = $('<div>', { class: 'blog-wrap aos-init aos-animate' });

                const $imgWrap = $('<div>', { class: 'blog-img-date-wrap mb-25' });

                const $imgBox = $('<div>', { class: 'blog-img' }).append(
                    $('<a>', { href: 'javascript:void(0);' }).append(
                    $('<img>', {
                        src: item.image,
                        alt: '',
                        width: 370,
                        onerror: "this.src='/assets/images/page/thumbnail_default.png'"
                    })
                    )
                );


                const dateObj = new Date(item.date);
                const month = dateObj.toLocaleString("en-US", { month: "long" }); // "May"
                const day = dateObj.getDate();

                const $dateBox = $('<div>', { class: 'blog-date' }).append(
                    $('<h5>').append(
                        $('<span>').text(day),
                        $('<span>').text(month)
                    )
                );

                $imgWrap.append($imgBox, $dateBox);

                const $content = $('<div>', { class: 'blog-content' }).append(
                    $('<div>', { class: 'blog-meta' }).append(
                    $('<ul>').append(
                        $('<li>').html(`By:&nbsp;<span>${item.author}</span>`)
                    )
                    ),
                    $('<h3>').append(
                    $('<a>', { href: item.href, text: item.title })
                    ),
                    $('<p>').text(item.description)
                );

                $wrap.append($imgWrap, $content);
                $col.append($wrap);
                $leftRow.append($col);
            });

            $leftCol.append($leftRow);

            if ($resultCol.length) $resultCol = $resultCol.add($leftCol);
            else $resultCol = $($leftCol);
        }


        for (let i = 0; i < Math.floor(12 / postColSize) - cardDisplayCount; i++) {

            console.log(i);
            const $rightCardWrap = $('<div>', {
                class: `col-lg-${postColSize}`,
            });

            const $sidebarWidget = $('<div>', {
                class: 'sidebar-widget mb-40 aos-init aos-animate',
                'data-aos': 'fade-up',
                'data-aos-delay': '200'
            });
        
            const itemsPerColumn = 3; 

            const postData = homeData.slice(cardDisplayCount, cardDisplayCount + 9);
            const start = i * itemsPerColumn;
            const end = start + itemsPerColumn;
            postDataSlice = postData.slice(start, end);

            if(postDisplayCount > 0) {
                
                const $latestPostWrap = $('<div>', {
                    class: 'latest-post-wrap',
                });

                // 각 포스트 DOM 생성 및 추가
                postDataSlice.forEach(item => {
                    const $post = $('<div>', { class: 'single-latest-post' });

                    const $imgBox = $('<div>', { class: 'latest-post-img' }).append(
                        $('<a>', { href: item.href }).append(
                            $('<img>', {
                                src: item.image,
                                alt: '',
                                width: 115,
                                onerror: "this.src='/assets/images/page/thumbnail_default.png'"
                            })
                        )
                    );

                    const $contentBox = $('<div>', { class: 'latest-post-content' }).append(
                        $('<span>').text(item.date),
                        $('<h4>').append($('<a>', { class: 'clamp-multiline title', href: item.href, text: item.title })),
                        $('<div>', { class: 'latest-post-btn' }).append(
                        $('<a>', { class: 'clamp-multiline description', href: item.href, text: item.description })
                        )
                    );

                    $contentBox.find('.title').css('--line-clamp', typeConfig.titleLineClamp);
                    $contentBox.find('.description').css('--line-clamp', typeConfig.descLineClamp);

                    $post.append($imgBox, $contentBox);
                    $latestPostWrap.append($post);
                });

                // DOM 조립
                $sidebarWidget.append($latestPostWrap);
                $rightCardWrap.append($sidebarWidget);

            }

            if ($resultCol.length) $resultCol = $resultCol.add($rightCardWrap);
            else $resultCol = $($rightCardWrap);

        }

        return $resultCol;
    },

    TypeD: function(homeData, typeConfig) {
        let postColSize = 0;

        if (homeData.length <= 3) {
            postColSize = Math.floor(12 / homeData.length);
        } else if (homeData.length === 4) {
            postColSize = 6
        } else {
            postColSize = 4
        }

        const postDisplayCount = Math.min(9, homeData.length); 

        let $resultCol = $();

        for (let i = 0; i < Math.floor(12 / postColSize); i++) {

            const $rightCardWrap = $('<div>', {
                class: `col-lg-${postColSize}`,
            });

            const $sidebarWidget = $('<div>', {
                class: 'sidebar-widget mb-40 aos-init aos-animate',
                'data-aos': 'fade-up',
                'data-aos-delay': '200'
            });
        
            let itemsPerColumn = 3;

            if (postDisplayCount <= 3) {
                itemsPerColumn = 1;
            } else if (postDisplayCount <= 6) {
                itemsPerColumn = 2;
            }

            const start = i * itemsPerColumn;
            const end = start + itemsPerColumn;
            postDataSlice = homeData.slice(start, end);

            if(postDisplayCount > 0) {
                
                const $latestPostWrap = $('<div>', {
                    class: 'latest-post-wrap',
                });

                // 각 포스트 DOM 생성 및 추가
                postDataSlice.forEach(item => {
                    const $post = $('<div>', { class: 'single-latest-post' });

                    const $contentBox = $('<div>', { class: 'latest-post-content' }).append(
                        $('<span>').text(item.date),
                        $('<h4>').append($('<a>', { class: 'clamp-multiline title', href: item.href, text: item.title })),
                        $('<div>', { class: 'latest-post-btn' }).append(
                        $('<a>', { class: 'clamp-multiline description', href: item.href, text: item.description })
                        )
                    );

                    $contentBox.find('.title').css('--line-clamp', typeConfig.titleLineClamp);
                    $contentBox.find('.description').css('--line-clamp', typeConfig.descLineClamp);

                    $post.append($contentBox);
                    $latestPostWrap.append($post);
                });

                // DOM 조립
                $sidebarWidget.append($latestPostWrap);
                $rightCardWrap.append($sidebarWidget);

            }
            if ($resultCol.length) $resultCol = $resultCol.add($rightCardWrap);
            else $resultCol = $($rightCardWrap);

        }

        return $resultCol;
    }



}
