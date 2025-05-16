document.addEventListener('DOMContentLoaded', function () {
    const quizContainer = document.getElementById('quizContainer');
    if (!quizContainer) {
        console.error('Quiz container not found');
        return;
    }

    let currentQuizId = parseInt(quizContainer.dataset.quizId);
    let currentItemIndex = parseInt(quizContainer.dataset.itemIndex); // 초기 itemIndex

    const quizInput = document.getElementById('quizInput');
    const submitAnswerLink = document.getElementById('submitLink'); // <a> 태그
    // const quizButton = document.getElementById('quizButton'); // submitAnswerLink로 제어
    const quizImageDiv = document.getElementById('quizImage'); // 이미지 감싸는 div
    const quizLabel = document.getElementById('quizLabel');

    // quizState와 answerResultDiv를 addEventListener 바깥 스코프에서 선언 및 초기화
    let quizState = 'ANSWERING'; // 초기 상태: 답변 입력 중
    let answerResultDiv = null;  // 맨 처음엔 null, 필요시 생성

    if (!submitAnswerLink || !quizInput || !quizLabel) {
        console.error('Error: Essential UI elements are missing.');
        return;
    }

    // 답변 결과 Div를 생성하거나 가져오는 함수 (DOM에 추가하는 로직 포함)
    function ensureAnswerResultDiv() {
        if (!answerResultDiv) { // DOM에 answerResultDiv가 아직 없으면 생성
            const existingDiv = document.getElementById('answerResult');
            if (existingDiv) {
                answerResultDiv = existingDiv;
            } else {
                answerResultDiv = document.createElement('div');
                answerResultDiv.id = 'answerResult';
                answerResultDiv.style.marginTop = '20px';
                answerResultDiv.style.textAlign = 'center';
                answerResultDiv.style.fontWeight = 'bold';
                answerResultDiv.style.fontSize = '1.2em';

                // quizLabel 다음, 즉 입력창과 버튼 아래에 결과 메시지 표시
                if (quizLabel && quizLabel.parentNode) {
                    quizLabel.parentNode.insertBefore(answerResultDiv, quizLabel.nextSibling);
                } else if (quizContainer) { // Fallback
                    quizContainer.appendChild(answerResultDiv);
                }
            }
        }
        return answerResultDiv;
    }

    submitAnswerLink.addEventListener('click', function (e) {
        e.preventDefault();
        const resultDisplayDiv = ensureAnswerResultDiv(); // 여기서 div를 가져오거나 생성 및 삽입

        if (quizState === 'ANSWERING') {
            const userAnswer = quizInput.value;

            if (!userAnswer.trim()) {
                resultDisplayDiv.textContent = '정답을 입력해주세요';
                resultDisplayDiv.style.color = 'orange';
                return;
            }

            const formData = new FormData();
            formData.append('itemIndex', currentItemIndex); // 현재 문제의 itemIndex
            formData.append('userAnswer', userAnswer);
            formData.append('quizId', currentQuizId);

            fetch('/article/quiz/checkAnswer', {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (!response.ok) {
                        return response.text().then(text => {
                            throw new Error('Network response was not ok: ' + response.statusText +
                                ' (status: ' + response.status + ') - Server response: ' + text);
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    resultDisplayDiv.textContent = data.message;
                    resultDisplayDiv.style.color = data.correct ? 'green' : 'red';

                    if (quizInput) {
                        quizInput.style.display = 'none'; // 입력창 숨기기
                    }
                    quizState = 'SHOWING_RESULT'; // 상태 변경 -> 다음 클릭은 '다음 문제' 로직
                })
                .catch(error => {
                    console.error('Error checking answer:', error);
                    resultDisplayDiv.textContent = '답변 확인 중 오류가 발생했습니다.';
                    resultDisplayDiv.style.color = 'red';
                });

        } else if (quizState === 'SHOWING_RESULT') {
            // --- 다음 문제 로딩 로직 ---
            fetch(`/article/quiz/next?quizId=${currentQuizId}&currentItemIndex=${currentItemIndex}`)
                .then(response => { // fetch의 첫 번째 then은 Response 객체를 받음
                    if (!response.ok) {
                        if (response.status === 404 || response.status === 204) return null; // 다음 문제 없음
                        return response.text().then(text => { throw new Error('Next question fetch error: ' + text); });
                    }
                    const contentType = response.headers.get("content-type");
                    if (contentType && contentType.indexOf("application/json") !== -1) {
                        return response.json().catch(() => null); // JSON 파싱, 실패 시 null
                    }
                    return null; // JSON이 아니면 다음 문제 없다고 간주
                })
                .then(nextQuestionData => { // 여기가 실제 다음 문제 데이터를 다루는 곳
                    if (nextQuestionData && nextQuestionData.question) {
                        const imageEl = document.getElementById('currentQuizImage'); // HTML에 id="currentQuizImage" 필요
                        if (imageEl) {
                            imageEl.src = nextQuestionData.question;
                        } else {
                            console.error('Element with id "currentQuizImage" not found.');
                        }

                        currentItemIndex = nextQuestionData.itemIndex; // currentItemIndex 업데이트!
                        if (quizContainer) { // quizContainer가 있다면 data 속성도 업데이트
                            quizContainer.dataset.itemIndex = currentItemIndex;
                        }


                        if (quizInput) {
                            quizInput.value = ''; // 입력창 비우기
                            quizInput.style.display = 'inline-block'; // 입력창 다시 보이기
                        }
                        resultDisplayDiv.textContent = ''; // 이전 결과 메시지 지우기
                        quizState = 'ANSWERING'; // 다시 답변 받는 상태로
                    } else {
                        // 다음 문제가 없으면 퀴즈 종료
                        window.location.href=`/article/quizResult/${currentQuizId}`
                        // resultDisplayDiv.style.color = 'blue';
                        // if (quizInput) quizInput.style.display = 'none';
                        // submitAnswerLink.style.display = 'none'; // 버튼도 숨김
                        // quizState = 'ENDED';
                    }
                })
                .catch(error => {
                    console.error('Error fetching next question:', error);
                    resultDisplayDiv.textContent = '다음 문제 로딩 중 오류가 발생했습니다.';
                    resultDisplayDiv.style.color = 'red';
                });
        }
    });
});