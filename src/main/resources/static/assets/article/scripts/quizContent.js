document.addEventListener('DOMContentLoaded', function (){
    const quizContainer=document.getElementById('quizContainer');

    const currentItemIndex=quizContainer.dataset.itemIndex;

    let currnetQuestionArrayIndex=0;

    const quizInput=document.getElementById('quizInput');
    const submitAnswerLink=document.getElementById('submitLink');
    const quizButton=document.getElementById('quizButton');
    const quizImageDiv=document.getElementById('quizImage');
    const quizLabel=document.getElementById('quizLabel');

    if (!submitAnswerLink){
        console.error('Error');
        return;
    }
    submitAnswerLink.addEventListener('click', function (e){
        e.preventDefault();
        const userAnswer=quizInput.value;
        let answerResultDiv=document.getElementById('answerResult');
        if (!answerResultDiv){
            answerResultDiv=document.createElement('div');
            answerResultDiv.id = 'answerResult'
            answerResultDiv.style.marginTop = '20px';
            answerResultDiv.style.textAlign = 'center';
            answerResultDiv.style.fontWeight = 'bold';
            answerResultDiv.style.fontSize = '1.2em';
            if (quizImageDiv&&quizImageDiv.parentNode&&quizLabel){
                quizImageDiv.parentNode.insertBefore(answerResultDiv, quizLabel);
            } else if (quizContainer){
                quizContainer.appendChild(answerResultDiv);
            }
        }
        if (!userAnswer.trim()){
            answerResultDiv.textContent='정답을 입력해주세요';
            answerResultDiv.style.color='orange';
            return;
        }
        const formData=new FormData();
        formData.append('itemIndex', currentItemIndex);
        formData.append('userAnswer', userAnswer);

        fetch('/article/quiz/checkAnswer',{
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    // 서버 응답이 JSON이 아닐 수도 있으므로, 먼저 텍스트로 읽어 에러 메시지에 포함
                    return response.text().then(text => {
                        throw new Error('Network response was not ok: ' + response.statusText +
                            ' (status: ' + response.status + ') - Server response: ' + text);
                    });
                }
                return response.json(); // ★★★ 응답 본문을 JSON으로 파싱합니다. 이것도 Promise를 반환합니다. ★★★
            })
            .then(data=>{
               answerResultDiv.textContent=data.message;
               if (data.correct){
                   answerResultDiv.style.color='green';
                } else {
                   answerResultDiv.style.color='red';

               }

               if (quizInput){
                   quizInput.style.display='none';

                   currnetQuestionArrayIndex++;

               }
            })

    })
})