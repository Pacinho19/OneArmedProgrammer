var timer;
var timerWorking = false;
function progressLoading() {
  timerWorking = true;
  clearInterval(timer);

  var min = 10;
  var max = document.getElementById("progressBar").max;

  var i = 0;
  var step = 1;

  timer = setInterval(function () {
    if (i >= max) {
      i = max;
      step = -1;
    } if (i <= min) {
      i = min;
      step = 1;
    }

    var nextPbValue = i + step;

    document.getElementById("progressBar").value = nextPbValue;
    i = nextPbValue;
  }, 1);
}

var keyDownFunc = (e) => {
  if (isSpaceKey(e) && !timerWorking) {
    progressLoading();
  }
};

var keyUpFunc = (e) => {
  if (isSpaceKey(e)) {
    clearInterval(timer);
    timerWorking = false;
    spin();
  }
}

function addActionListeners() {
  document.addEventListener('keydown', keyDownFunc);
  document.addEventListener('keyup', keyUpFunc);
}

function isSpaceKey(e) {
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
      addActionListeners();
      spinAnimation();
    }
  }
  xhr.open('GET', "/one-armed-programmer/game/" + document.getElementById("gameId").value + "/board/reload", true);
  xhr.send(null);
}

function spin() {
  var xhr = new XMLHttpRequest();
  var url = '/one-armed-programmer/game/' + document.getElementById("gameId").value + '/spin';
  xhr.open("POST", url, true);
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.onreadystatechange = function () { };

  spinValue = document.getElementById("progressBar").value;

  let spinObj = { value: spinValue };
  var data = JSON.stringify(spinObj);
  xhr.send(data);

  removeActionListeners();
}

function removeActionListeners() {
  document.removeEventListener('keydown', keyDownFunc);
  document.removeEventListener('keyup', keyUpFunc);
}

function onLoadActions(){
    addActionListeners();
    spinAnimation();
}

function spinAnimation() {
  var gameId = document.getElementById("gameId").value;
  if (gameId == null)
    return;

  var lastSpinMapElement = document.getElementById("lastSpinMap");
  if(lastSpinMapElement==null)
    return;

  var lastSpinMap =  JSON.parse(lastSpinMapElement.value);
  var sectionCount = document.getElementById("sectionCount").value;

  for (let i = 1; i <= sectionCount; i++) {
    setTimeout(() => {
      slotElement = document.getElementById("slot_" + i);

      if (slotElement != null) {
        var spins =lastSpinMap[""+i]

        if(spins!==null ){
            slotElement.className = "bi bi-" + replaceWordSeparator(spins.at(spins.length - 1).sign);
            slotElement.style.display = "block";
            slotElement.style.animation = 'pulse 1s normal'
        }
      }

    }, (i + 1) * 500);
  }
}

function replaceWordSeparator(signName){
    return signName.replace('_','-').toLowerCase();
}