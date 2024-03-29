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
  var leftRoundsElement = document.getElementById('leftRounds');
  if (leftRoundsElement == null || (leftRoundsElement != null && leftRoundsElement.value > 0)) {
    document.addEventListener('keydown', keyDownFunc);
    document.addEventListener('keyup', keyUpFunc);
  }
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

function setSpinAnimationDisplayed(){
    var xhr = new XMLHttpRequest();
    var url = '/one-armed-programmer/game/' + document.getElementById("gameId").value + '/spin-animation-off';
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () { };
    xhr.send();
}

function removeActionListeners() {
  document.removeEventListener('keydown', keyDownFunc);
  document.removeEventListener('keyup', keyUpFunc);
}

function onLoadActions() {
  addActionListeners();
  spinAnimation();
}

function spinAnimation() {

  var gameId = document.getElementById("gameId").value;
  if (gameId == null)
    return;

  var lastSpinMapElement = document.getElementById("lastSpinMap");
  if (lastSpinMapElement == null)
    return;

  setSpinAnimationDisplayed();

  var lastSpinMap = JSON.parse(lastSpinMapElement.value);
  var sectionCount = document.getElementById("sectionCount").value;
  var showSpinAnimationElement = document.getElementById("spinAnimation");
  var showSpinAnimation = showSpinAnimationElement != null ? showSpinAnimationElement.value : 'false';

  if (showSpinAnimation === 'false') {
    for (let i = 1; i <= sectionCount; i++) {
      var spins = lastSpinMap["" + i];
      var j = spins.length - 1;

      for (let x = -2; x <= 2; x++) {
        var elementIdSuffix = getElementIdSuffix(x);
        slotElement = document.getElementById("slot_" + i + elementIdSuffix);

        spinObj = getSpinObject(spins, j, x);

        slotElement.className = "bi bi-" + replaceWordSeparator(spinObj.sign);
        slotElement.style.display = "block";
      }
    }
    return;
  }

  var maxSpinCount = getMaxSpinCount(sectionCount, lastSpinMap);

  for (let j = 0; j < maxSpinCount; j++) {
    setTimeout(() => {
      for (let i = 1; i <= sectionCount; i++) {
        var spins = lastSpinMap["" + i];
        if (spins.length > j) {
          for (let x = -2; x <= 2; x++) {
            elementIdSuffix = getElementIdSuffix(x);
            slotElement = document.getElementById("slot_" + i + elementIdSuffix);

            spinObj = getSpinObject(spins, j, x);

            slotElement.className = "bi bi-" + replaceWordSeparator(spinObj.sign);
            slotElement.style.display = "block";
            if (spins.length - 1 == j && x == 0) {
              slotElement.style.animation = 'pulse 0.5s normal'
            }
          }
        }
      }

      winAmountElement = document.getElementById("winAmount");
      for (let i = 1; i <= sectionCount; i++) {
        if (winAmountElement != null && winAmountElement.value > 0 && j == maxSpinCount - 1) {
          slotElement = document.getElementById("slot_" + i);
          slotElement.style.color = 'green';
          document.getElementById("winAmountInfo").style.display = "block";
        }
      }

    }, (j) * 150);
  }
}

function replaceWordSeparator(signName) {
  return signName.replace('_', '-').toLowerCase();
}

function getMaxSpinCount(sectionCount, lastSpinMap) {
  var max = 0;
  for (let j = 1; j <= sectionCount; j++) {
    var spinCount = lastSpinMap["" + j].length;
    if (max <= spinCount) {
      max = spinCount;
    }
  }
  return max;
}
function getElementIdSuffix(x) {
  if (x == 0)
    return "";

  return x < 0
    ? ('_u' + Math.abs(x))
    : ('_d' + Math.abs(x));
}

function getSpinObject(spins, elementIdx, x) {
  spinsLength = spins.length
  targetIdx = elementIdx;
  if (x == 0)
    targetIdx = elementIdx;
  else if (elementIdx + x < 0)
    targetIdx = spinsLength - x;
  if (elementIdx + x >= spinsLength)
    targetIdx = x - 1;
  else
    targetIdx = elementIdx + x;

  return spins.at(targetIdx);
}