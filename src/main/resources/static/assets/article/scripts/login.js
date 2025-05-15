// 카카오 로그인 처리

document.addEventListener('DOMContentLoaded', function() {
    // 카카오 SDK 스크립트 로드
    loadKakaoSDK();

    // 로그인 버튼 요소 가져오기
    const kakaoLoginBtn = document.getElementById('kakaoLoginBtn');

    // 카카오 로그인 버튼 클릭 이벤트 핸들러
    kakaoLoginBtn.addEventListener('click', function() {
        loginWithKakao();
    });
});

// 카카오 SDK 로드 함수
function loadKakaoSDK() {
    const script = document.createElement('script');
    script.src = 'https://developers.kakao.com/sdk/js/kakao.js';
    script.onload = initializeKakao;
    document.head.appendChild(script);
}

// 카카오 SDK 초기화
function initializeKakao() {
    // 앱 키 설정 - 실제 키로 변경 필요
    Kakao.init('YOUR_KAKAO_JAVASCRIPT_KEY');
    console.log(Kakao.isInitialized());
}

// 카카오 로그인 함수
function loginWithKakao() {
    Kakao.Auth.login({
        success: function(authObj) {
            console.log('로그인 성공:', authObj);

            // 액세스 토큰 획득
            const accessToken = authObj.access_token;

            // 사용자 정보 요청
            Kakao.API.request({
                url: '/v2/user/me',
                success: function(response) {
                    console.log('사용자 정보:', response);

                    // 카카오 ID 토큰 추출
                    const idToken = response.id.toString();

                    // 서버로 토큰 전송
                    sendTokenToServer(idToken);
                },
                fail: function(error) {
                    console.error('사용자 정보 요청 실패:', error);
                    alert('사용자 정보를 가져오는데 실패했습니다.');
                }
            });
        },
        fail: function(error) {
            console.error('로그인 실패:', error);
            alert('로그인에 실패했습니다.');
        }
    });
}

// 서버로 토큰 전송 함수
function sendTokenToServer(idToken) {
    fetch('/article/kakao-login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ idToken: idToken })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log('서버 응답:', data);

            if (data.success) {
                // 로그인 성공, 메인 페이지로 리다이렉트
                window.location.href = data.redirectUrl || '/article';
            } else {
                alert(data.message || '로그인 처리 중 오류가 발생했습니다.');
            }
        })
        .catch(error => {
            console.error('서버 요청 실패:', error);
            alert('로그인 처리 중 오류가 발생했습니다.');
        });
}