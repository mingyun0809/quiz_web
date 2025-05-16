//조회
const $playList = document.getElementById('playList');

const loadQuestions = () => {
    $playList.innerHTML = '';

    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert(`[${xhr.status}] 리스트를 불러오지 못하였습니다. 잠시 후 다시 시도해 주세요`)
            return;
        }
        const lists = JSON.parse(xhr.responseText);
        let listBody = '';
        for (const list of lists) {
            listBody += `
            <a class="play-item" href="/article/quiz?index=${list['index']}">
                <img class="img" alt="" src="/assets/article/images/no-img.png"> <!-- 이미지url -->
                <span class="caption" id="title">${list['title']}</span>
                <span class="info" id="info">${list['info'].length > 60 ? list['info'].slice(0, 60) + '... [축약]' : list['info']}</span>
            </a>`;
        }
        $playList.innerHTML = listBody;
    };
    xhr.open('GET', '/article/playList');
    xhr.send();
};

window.addEventListener('load', loadQuestions);