// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', function() {
    // 페이드인 애니메이션 적용
    const mainContent = document.querySelector('main');
    if (mainContent) {
        mainContent.classList.add('fade-in');
    }
    
    // 네비게이션 활성 상태 설정
    setActiveNavItem();
    
    // 카드 호버 효과
    initCardHoverEffects();
    
    console.log('Metablock 웹 애플리케이션이 로드되었습니다.');
});

// 현재 페이지에 해당하는 네비게이션 아이템 활성화
function setActiveNavItem() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.navbar-nav .nav-link');
    
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href === currentPath || (currentPath === '/' && href === '/')) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
}

// 카드 호버 효과 초기화
function initCardHoverEffects() {
    const cards = document.querySelectorAll('.card');
    
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });
}

// 유틸리티 함수들
const Utils = {
    // 로딩 스피너 표시
    showLoader: function() {
        // 필요시 구현
    },
    
    // 로딩 스피너 숨김
    hideLoader: function() {
        // 필요시 구현
    },
    
    // 알림 메시지 표시
    showAlert: function(message, type = 'info') {
        // Bootstrap 알림 컴포넌트 생성
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        // 컨테이너 상단에 삽입
        const container = document.querySelector('.container');
        if (container) {
            container.insertBefore(alertDiv, container.firstChild);
        }
    }
}; 