


const $modifyForm = document.getElementById('modifyForm');
//수정







$modifyForm.addEventListener('submit', (e) => {
    e.preventDefault();



});




// 삭제
const deleteList = (index) => {

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('lintIndex', index);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert(`[${xhr.status}] 퀴즈를 삭제하지 못하였습니다.`);
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response['result']) {
            case 'success':
                alert('삭제 완료');
                break;
            default:
                alert('알 수 없는 이유로 실패하였습니다.');
        }
    };
    xhr.open('DELETE', '/article/modify');
    xhr.send(formData);
}


//수정 + 삭제