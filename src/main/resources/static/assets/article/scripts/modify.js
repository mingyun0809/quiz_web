//수정
document.addEventListener('DOMContentLoaded', () => {
    const $modifyForm = document.getElementById('modifyForm');
    const tbody = document.getElementById('questionTable');

    // 처음에 QnA 하나 추가
    tbody.appendChild(createQuestionBox());

    // 수정
    $modifyForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const title = document.getElementById('modifyTitle').value;
        const info = document.getElementById('modifyInfo').value;
        const index = new URL(location.href).searchParams.get('index');
        const questionInputs = document.querySelectorAll('input[name="question[]"]');
        const answerInputs = document.querySelectorAll('input[name="answer[]"]');

        const questions = [];
        const answers = [];

        questionInputs.forEach(input => questions.push(input.value));
        answerInputs.forEach(input => answers.push(input.value));

       // const params = new URLSearchParams();
       // params.append('index', index);
       // params.append('Title', title);
       // params.append('info', info);
       // questions.forEach(q => params.append('question', q));
       // answers.forEach(a => params.append('answer', a));

        const data = {
            index: index,
            title: title,
            info: info,
            questions: questions,
            answers: answers,
        }

        const xhr = new XMLHttpRequest();
        xhr.open('PATCH', '/article/modify', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    const result = JSON.parse(xhr.responseText);
                    if (result.result === 'success') {
                        alert('수정 완료!');
                        window.location.href = '/article/playList';
                    } else {
                        alert('수정 실패: 중복 제목 혹은 서버 오류');
                    }
                } else {
                    alert('서버 오류가 발생했습니다. (' + xhr.status + ')');
                }
            }
        };
        xhr.send(JSON.stringify(data));
    });

    // 삭제
    $modifyForm.addEventListener('click', function (e) {
        const button = e.target.closest('button[name="delete"]');
        if (button) {
            const listIndex = button.dataset['listIndex'];
            const itemIndex = button.dataset['itemIndex'];

            if (confirm('해당 퀴즈를 삭제할까요?')) {
                deleteQuestion(listIndex, itemIndex);
            }
        }
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
                <label class="caption">질문</label>
            </th>
            <td>
                <div class="row">
                    <input required class="Q" maxlength="255" minlength="1" name="question[]" type="text" placeholder="본래 이미지 주소">
                </div>
            </td>
            <th scope="row" class="col">
                <label class="caption">정답</label>
            </th>
            <td>
                <div class="row">
                    <input required class="A" maxlength="255" minlength="1" name="answer[]" type="text" placeholder="본래 정답">
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

        // 삭제 버튼에 데이터 속성 추가
        const deleteButton = tr.querySelector('button[name="minus"]');
        deleteButton.dataset['listIndex'] = 'some_list_index'; // listIndex 값 지정
        deleteButton.dataset['itemIndex'] = 'some_item_index'; // itemIndex 값 지정

        return tr;
    }
});

//삭제
document.getElementById('deleteListButton').addEventListener('click', () => {
    if (!confirm('이 퀴즈 전체를 삭제하시겠습니까?')) return;

    const xhr = new XMLHttpRequest();
    const params = new URLSearchParams();
    params.append('index', new URL(location.href).searchParams.get('index'));

    xhr.open('DELETE', '/article/modify');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) return;

        if (xhr.status >= 200 && xhr.status < 300) {
            const response = JSON.parse(xhr.responseText);
            if (response.result === 'success') {
                alert('퀴즈가 삭제되었습니다.');
                window.location.href = '/article/playList';
            } else {
                alert('삭제에 실패했습니다.');
            }
        } else {
            alert('서버 오류가 발생했습니다. (' + xhr.status + ')');
        }
    };

    xhr.send(params.toString());
});

