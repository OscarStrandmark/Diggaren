/* Toggle between showing and hiding the navigation menu links when the user clicks on the hamburger menu / bar icon */
function myFunction() {
    var x = document.getElementById("myLinks");
    if (x.style.display === "block") {
      x.style.display = "none";
    } else {
      x.style.display = "block";
    }
  }


var coll = document.getElementsByClassName("collapsible");
var i;

for (i = 0; i < coll.length; i++) {
  coll[i].addEventListener("click", function() {
    this.classList.toggle("active");
    var content = this.nextElementSibling;
    if (content.style.display === "block") {
      content.style.display = "none";
    } else {
      content.style.display = "block";
    }
  });
}
  
// Importerad play paus knapp

var play = false;
var audio=document.getElementById('player2');
function toggle() {
  if (play) {
    audio.pause()
  } else {
    audio.play();
  }
};
audio.onplaying = function() {
  play = true;
};
audio.onpause = function() {
  play = false;
};
$("#a").click(function(){$(this).toggleClass("fa-play-circle fa-pause-circle")})
