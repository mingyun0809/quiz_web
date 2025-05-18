// quizResultChart.js
document.addEventListener('DOMContentLoaded', function() {
    const quizResultContent = document.getElementById('quizResultContent');
    if (!quizResultContent) {
        console.error('Quiz result content container not found.');
        return;
    }

    // HTML의 data-percentage 속성에서 값을 가져옴
    const percentageText = quizResultContent.dataset.percentage;
    if (percentageText === undefined) {
        console.error('Data attribute "data-percentage" not found on quizResultContent.');
        return;
    }
    const percentage = parseFloat(percentageText);




    const graphBars = document.querySelectorAll('#graphContainer>.graph-bar');
    const totalBars = graphBars.length;

    if (totalBars === 0) {
        console.warn('No graph bars found.');
        return;
    }

    let barsToColor = 0;
    if (!isNaN(percentage) && percentage >= 0 && percentage <= 100) {
        barsToColor = Math.round((percentage / 100) * totalBars);
    }


    let fillColor;
    if (percentage >= 91) {
        fillColor = '#4CAF50';
    } else if (percentage >= 61) {
        fillColor = '#8BC34A';
    } else if (percentage >= 31) {
        fillColor = '#FFC107';
    } else {
        fillColor = '#FF5722';
    }


    if (percentage === 0 && barsToColor === 0) {
        fillColor = '#e0e0e0';
    }



    graphBars.forEach(bar => {
        bar.style.backgroundColor = '#e8e8e8';
    });



    for (let i = 0; i < barsToColor; i++) {
        if (graphBars[i]) {
            graphBars[i].style.backgroundColor = fillColor;
        }
    }

    if (barsToColor === 0 && percentage > 0) {
        if (totalBars > 0 && graphBars[0]) {
            graphBars[0].style.backgroundColor = fillColor;
        }
    }
});