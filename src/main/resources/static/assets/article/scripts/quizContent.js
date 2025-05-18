document.addEventListener('DOMContentLoaded', function () {
    const quizContainer = document.getElementById('quizContainer');
    if (!quizContainer) {
        console.error('Quiz container not found');
        return;
    }

    let currentQuizId = parseInt(quizContainer.dataset.quizId);
    let currentItemIndex = parseInt(quizContainer.dataset.itemIndex);

    const quizInput = document.getElementById('quizInput');
    const submitAnswerLink = document.getElementById('submitLink'); // 화살표 버튼을 감싸는 <a> 태그
    const quizImageEl = document.getElementById('currentQuizImage');
    const quizLabel = document.getElementById('quizLabel'); // input과 submitAnswerLink를 감싸는 label

    let quizState = 'ANSWERING'; // ANSWERING, SHOWING_RESULT

    // 결과 표시용 DOM 요소들 (한 번만 생성)
    let resultDisplayContainer = null;
    let answerResultMessageDiv = null;
    let correctAnswerDisplayDiv = null;

    // 필수 UI 요소 확인
    if (!submitAnswerLink || !quizInput || !quizImageEl || !quizLabel) {
        console.error('Error: Essential UI elements are missing.');
        return;
    }

    // 결과 표시 영역을 생성하고 DOM에 추가하는 함수
    function initializeResultDisplayArea() {
        if (!resultDisplayContainer) { // 한 번만 생성
            resultDisplayContainer = document.createElement('div');
            resultDisplayContainer.id = 'resultDisplayContainer';
            resultDisplayContainer.style.marginTop = '20px';
            resultDisplayContainer.style.textAlign = 'center';

            answerResultMessageDiv = document.createElement('div');
            answerResultMessageDiv.id = 'answerResultMessage';
            answerResultMessageDiv.style.fontWeight = 'normal';
            answerResultMessageDiv.style.fontSize = '1em';
            answerResultMessageDiv.style.marginBottom = '10px';

            correctAnswerDisplayDiv = document.createElement('div');
            correctAnswerDisplayDiv.style.fontWeight='bold';
            correctAnswerDisplayDiv.id = 'correctAnswerDisplay';
            correctAnswerDisplayDiv.style.fontSize = '1.2em';
            correctAnswerDisplayDiv.style.color='white';

            resultDisplayContainer.appendChild(answerResultMessageDiv);
            resultDisplayContainer.appendChild(correctAnswerDisplayDiv);

            // resultDisplayContainer를 quizLabel 다음에 삽입 (초기 위치)
            // quizContainer의 자식으로, quizLabel의 다음 형제로 삽입
            if (quizLabel.parentNode === quizContainer) { // quizLabel이 quizContainer의 직계 자식인지 확인
                if (quizLabel.nextSibling) {
                    quizContainer.insertBefore(resultDisplayContainer, quizLabel.nextSibling);
                } else {
                    quizContainer.appendChild(resultDisplayContainer);
                }
            } else {
                // 예외 처리: quizLabel의 부모가 quizContainer가 아닌 경우, quizContainer의 마지막에 추가
                console.warn('quizLabel is not a direct child of quizContainer. Appending resultDisplayContainer to quizContainer.');
                quizContainer.appendChild(resultDisplayContainer);
            }
        }
        resultDisplayContainer.style.display = 'none'; // 초기에는 숨김
    }

    initializeResultDisplayArea(); // 페이지 로드 시 결과 표시 영역 초기화

    // 버튼을 원하는 위치로 옮기거나 원래 위치로 복원하는 함수
    function arrangeElements(showResult) {
        if (showResult) {
            // 순서: 메시지 -> 정답 -> 버튼
            quizInput.style.display = 'none';
            resultDisplayContainer.style.display = 'block';

            // 버튼(submitAnswerLink)을 resultDisplayContainer 다음으로 이동
            if (submitAnswerLink.parentNode) { // 버튼이 DOM에 연결되어 있다면
                // resultDisplayContainer의 부모(quizContainer)에, resultDisplayContainer 다음 형제로 submitAnswerLink 삽입
                resultDisplayContainer.parentNode.insertBefore(submitAnswerLink, resultDisplayContainer.nextSibling);
            }

        } else {
            // 초기 상태 (다음 문제): 입력창 -> 버튼 -> (숨겨진 메시지/정답 영역)
            quizInput.style.display = 'inline-block'; // 또는 'block'
            resultDisplayContainer.style.display = 'none';

            // 버튼(submitAnswerLink)을 다시 quizLabel의 자식으로 (원래 HTML 구조대로)
            // quizLabel의 마지막 자식으로 추가 (원래 input 다음에 있었으므로, 더 정확하려면 insertBefore 사용)
            if (submitAnswerLink.parentNode !== quizLabel) {
                quizLabel.appendChild(submitAnswerLink);
            }
        }
    }


    submitAnswerLink.addEventListener('click', function (e) {
        e.preventDefault();

        if (quizState === 'ANSWERING') {
            const userAnswer = quizInput.value;

            if (!userAnswer.trim()) {
                if (answerResultMessageDiv && correctAnswerDisplayDiv && resultDisplayContainer) {
                    answerResultMessageDiv.textContent = '정답을 입력해주세요';
                    answerResultMessageDiv.style.color = 'orange';
                    correctAnswerDisplayDiv.textContent = '';
                    resultDisplayContainer.style.display = 'block';
                }
                return;
            }

            const formData = new FormData();
            formData.append('itemIndex', currentItemIndex);
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
                    if (answerResultMessageDiv && correctAnswerDisplayDiv && resultDisplayContainer) {
                        answerResultMessageDiv.textContent = data.message; // "정답!" 또는 "오답!"
                        answerResultMessageDiv.style.color = data.correct ? 'white' : 'white'; // 초록색 또는 빨간색
                        correctAnswerDisplayDiv.textContent = data.correctAnswer; // Controller에서 "correctAnswer" 키로 보낸 실제 정답

                        arrangeElements(true);
                    }
                    quizState = 'SHOWING_RESULT';
                })
                .catch(error => {
                    console.error('Error checking answer:', error);
                    if (answerResultMessageDiv && resultDisplayContainer) {
                        answerResultMessageDiv.textContent = '답변 확인 중 오류가 발생했습니다.';
                        answerResultMessageDiv.style.color = 'red';
                        correctAnswerDisplayDiv.textContent = '';
                        resultDisplayContainer.style.display = 'block';

                    }
                });

        } else if (quizState === 'SHOWING_RESULT') {
            fetch(`/article/quiz/next?quizId=${currentQuizId}&currentItemIndex=${currentItemIndex}`)
                .then(response => {
                    if (!response.ok) {
                        if (response.status === 404 || response.status === 204) return null;
                        return response.text().then(text => { throw new Error('Next question fetch error: ' + text); });
                    }
                    const contentType = response.headers.get("content-type");
                    if (contentType && contentType.indexOf("application/json") !== -1) {
                        return response.json().catch(() => null);
                    }
                    return null;
                })
                .then(nextQuestionData => {
                    if (nextQuestionData && nextQuestionData.question) {
                        if (quizImageEl) {
                            quizImageEl.src = nextQuestionData.question;
                        }
                        currentItemIndex = nextQuestionData.itemIndex;
                        if (quizContainer) {
                            quizContainer.dataset.itemIndex = currentItemIndex;
                        }
                        quizInput.value = '';

                        arrangeElements(false);

                        quizState = 'ANSWERING';
                    } else {
                        window.location.href = `/article/quizResult/${currentQuizId}`;
                    }
                })
                .catch(error => {
                    console.error('Error fetching next question:', error);
                    if (answerResultMessageDiv && resultDisplayContainer) {
                        answerResultMessageDiv.textContent = '다음 문제 로딩 중 오류가 발생했습니다.';
                        answerResultMessageDiv.style.color = 'red';
                        correctAnswerDisplayDiv.textContent = '';
                        resultDisplayContainer.style.display = 'block';

                    }
                });
        }
    });
});