// 작성
document.addEventListener('DOMContentLoaded', () => {
    const $writeForm = document.getElementById('writeForm');
    const tbody = document.getElementById('questionTable');

    // 처음에 QnA 하나 추가
    tbody.appendChild(createQuestionBox());
    //작성
    $writeForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const title = document.getElementById('writeTitle').value;
        const info = document.getElementById('writeInfo').value;
        const questionInputs = document.querySelectorAll('input[name="question[]"]');
        const answerInputs = document.querySelectorAll('input[name="answer[]"]');

        const questions = [];
        const answers = [];

        questionInputs.forEach(input => questions.push(input.value));
        answerInputs.forEach(input => answers.push(input.value));

        const params = new URLSearchParams();
        params.append('Title', title);
        params.append('info', info);
        questions.forEach(q => params.append('question', q));
        answers.forEach(a => params.append('answer', a));

        const xhr = new XMLHttpRequest();
        xhr.open('POST', '/article/write', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    const result = JSON.parse(xhr.responseText);
                    if (result.result === 'success') {
                        alert('작성 완료! 퀴즈가 만들어졌습니다.');
                        window.location.href = '/article/playList';
                    } else {
                        alert('작성 실패: 중복 제목 혹은 서버 오류');
                    }
                } else {
                    alert('서버 오류가 발생했습니다. (' + xhr.status + ')');
                }
            }
        };
        xhr.send(params.toString());
    });




    // +- 버튼
    tbody.addEventListener('click', (e) => {
        const button = e.target.closest('button');
        if (!button) return;

        const tr = button.closest('tr.QnA');

        if (button.name === 'plus') {
            const newRow = createQuestionBox();
            tbody.appendChild(newRow);
        }

        if (button.name === 'minus') {
            const rows = tbody.querySelectorAll('tr.QnA');
            if (rows.length <= 1) {
                alert('문제는 최소 1개 이상 있어야 합니다.');
                return;
            }
            tr.remove();
        }
    });
    // 질문 추가 삭제
    function createQuestionBox() {
        const tr = document.createElement('tr');
        tr.classList.add('QnA');
        tr.innerHTML = `
            <th scope="row" class="col">
                <span class="caption">질문</span>
            </th>
            <td>
                <div class="row">
                    <input required class="Q" maxlength="255" minlength="1" name="question[]" type="text" placeholder="이미지 주소를 입력해 주세요">
                </div>
            </td>
            <th scope="row" class="col">
                <span class="caption">정답</span>
            </th>
            <td>
                <div class="row">
                    <input required class="A" maxlength="250" minlength="1" name="answer[]" type="text"
                           placeholder="정답을 입력해 주세요">
                </div>
            </td>
            <td class="stretch"></td>
            <td>
                <div class="row buttons">
                    <button class="button" name="plus" type="button">
                        <img alt="" class="icon" src="/assets/article/images/plus.png">
                    </button>
                    <button class="button" name="minus" type="button">
                        <img alt="" class="icon" src="/assets/article/images/minus.png">
                    </button>
                </div>
            </td>
        `;
        return tr;
    }
});
