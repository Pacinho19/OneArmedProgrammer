var timer;
var timerWorking = false;
function progressLoading(){
                timerWorking = true;
                clearInterval(timer);

                var min = 0;
                var max = document.getElementById("progressBar").max;

                var i = 0;
                var step = 1;

                timer = setInterval(function(){
                  if(i >= max){
                    i = max;
                    step = -1;
                  }if(i <= min){
                    i = min;
                    step = 1;
                  }

                  var nextPbValue =  i + step;

                  document.getElementById("progressBar").value = nextPbValue;
                  i = nextPbValue;
                }, 1);
}

document.addEventListener('keydown', (e) => {
      if (isSpaceKey(e) && !timerWorking){
        progressLoading();
      }
});

document.addEventListener('keyup', (e) => {
      if (isSpaceKey(e)){
        clearInterval(timer);
        timerWorking = false;
        spin();
      }
});

function isSpaceKey(e){
    return e.key == " " || e.code == "Space" || e.keyCode == 32;
}

var stompClient = null;
var privateStompClient = null;

socket = new SockJS('/ws');
privateStompClient = Stomp.over(socket);
privateStompClient.connect({}, function (frame) {
    var gameId = document.getElementById('gameId').value;
    privateStompClient.subscribe('/reload-board/' + gameId, function (result) {
        updateBoard(result);
    });
});

stompClient = Stomp.over(socket);

function updateBoard(result) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == XMLHttpRequest.DONE) {
            $("#board").replaceWith(xhr.responseText);
        }
    }
    xhr.open('GET', "/one-armed-programmer/game/" + document.getElementById("gameId").value + "/board/reload", true);
    xhr.send(null);
}

function spin(){
    var xhr = new XMLHttpRequest();
    var url = '/one-armed-programmer/game/' + document.getElementById("gameId").value + '/spin';
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () { };

    spinValue = document.getElementById("progressBar").value;

    let spinObj = {value:spinValue};
    var data = JSON.stringify(spinObj);
    xhr.send(data);
}