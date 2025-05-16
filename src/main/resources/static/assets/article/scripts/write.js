// 작성
document.addEventListener('DOMContentLoaded', () => {
    const $writeForm = document.getElementById('writeForm');
    const tbody = document.getElementById('questionTable');

    //작성
    $writeForm.onsubmit = (e) => {
        e.preventDefault();

        const title = $writeForm['Title'].value.trim();
        const info = $writeForm['info'].value.trim();
        const questions = Array.from($writeForm.querySelectorAll('input[name="question[]"]')).map(input => input.value.trim());
        const answers = Array.from($writeForm.querySelectorAll('input[name="answer[]"]')).map(input => input.value.trim());

        if (!title || !info) {
            alert("제목과 설명을 입력해 주세요.");
            return;
        }

        if (answers.some(answer => !answer)) {
            alert("비어있는 정답 칸이 있습니다.");
            return;
        }

        if (title.length > 255 || info.length > 255 || questions.some(q => q.length > 255) || answers.some(a => a.length > 255)) {
            alert("입력값이 너무 깁니다. (최대 255자)");
            return;
        }

        const formData = new FormData();
        formData.append('Title', title); // title 주의
        formData.append('info', info);
        questions.forEach(question => formData.append('questions[]', question));
        answers.forEach(answer => formData.append('answers[]', answer));

        const xhr = new XMLHttpRequest();
        xhr.open('POST', '/article/write');
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) return;
            if (xhr.status < 200 || xhr.status >= 300) {
                alert(`[${xhr.status}] 잠시 후 다시 시도해 주세요`);
                return;
            }

            const response = JSON.parse(xhr.responseText);
            if (response.result === 'success') {
                location.href = `/article/${response.index}`;
            } else {
                alert("작성에 실패하였습니다.");
            }
        };
        xhr.open('POST', '/article/write');
        xhr.send(formData);
    };




    // 이미지 드롭
    tbody.addEventListener('drop', async (e) => {
        e.preventDefault();
        const dropTarget = e.target.closest('.img-box');
        if (!dropTarget) {
            return;
        }

        const img = dropTarget.querySelector('img');
        const hiddenInput = dropTarget.parentElement.querySelector('input[name="question[]"]');
        const items = e.dataTransfer.items;

        for (let i = 0; i < items.length; i++) {
            const item = items[i];

            if (item.kind === 'string' && (item.type === 'text/uri-list' || item.type === 'text/plain')) {
                item.getAsString((url) => {
                    if (url && url.startsWith('http') && url.length <= 255) {
                        img.src = url;
                        hiddenInput.value = url;
                    } else {
                        alert("잘못된 이미지 주소이거나 주소의 길이가 255자를 초과하였습니다.");
                    }
                });
                return;
            }
            if (item.kind === 'file') {
                alert("파일은 지원하지 않습니다. 이미지만 드롭해 주세요.");
                return;
            }
        }
    });

    tbody.addEventListener('dragover', (e) => e.preventDefault());

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
