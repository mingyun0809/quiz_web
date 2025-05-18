//필터 조회


  const $playList = document.getElementById('playList');

  const loadMyQuizList = () => {
      $playList.innerHTML = '';

      const xhr = new XMLHttpRequest();
      xhr.onreadystatechange = () => {
          if (xhr.readyState !== XMLHttpRequest.DONE) return;

          if (xhr.status < 200 || xhr.status >= 300) {
              alert(`[${xhr.status}] 내가 만든 퀴즈를 불러오지 못했습니다. 다시 시도해주세요.`);
              return;
          }

          const lists = JSON.parse(xhr.responseText);
          let listHTML = '';
          for (const list of lists) {
              listHTML += `
                  <a class="play-item" href="/article/modify?index=${list['index']}">
                      <img class="img" alt="" src="/assets/article/images/no-img.png">
                      <span class="caption">${list['title']}</span>
                      <span class="info">${list['info'].length > 60 ? list['info'].slice(0, 60) + '... [축약]' : list['info']}</span>
                  </a>`;
          }

          $playList.innerHTML = listHTML;
      };

      xhr.open('GET', '/article/madeList');
      xhr.send();
  };

  window.addEventListener('load', loadMyQuizList);
  window.addEventListener('load', loadMyQuizList);