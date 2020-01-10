
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

var coll = document.getElementsByClassName("dropbtn");
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

var coll = document.getElementsByClassName("pcollapsible");
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

function addToPlayList(id) {
  console.log(id);
}

function radio(station) {
  audio = document.getElementById('player2');
  audio.pause();
  if(station == "p2") {
    audio.src = "https://sverigesradio.se/topsy/direkt/2562-hi-mp3";
    $("#nowPlaying").html("Nu spelas radiokanal P2");

  } else if(station == "p3") {
    audio.src = "https://sverigesradio.se/topsy/direkt/164-hi-mp3";
    $("#nowPlaying").html("Nu spelas radiokanal P3");
  }
  audio.load();
  if(play == true) {
    audio.play();
  }
}

function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  for(var i = 0; i <ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

// When website is loaded, run function to create dropdown menu with playlists
$(document).ready(function(){
  var access_token = getCookie("accessToken");
  // Get request is made to fetch playlists for current user
  $.ajax({
      
      headers: {
        'Accept' : 'application/json',
        'Content-Type' : 'application/json',
        'Authorization' : 'Bearer ' + access_token
      },
      url: "https://api.spotify.com/v1/me/playlists",
      type:"GET",
      dataType: 'JSON',
      success: function(result) {
        // loops through playlists and creates a button for each of them in the dropdown menu
        for(var key in result.items) {
          btn = $('<a />', {
            class: "spellista",
            text : result.items[key].name,
            value : result.items[key].id,
            on    : {
               click: function() {
                   alert ( this.value );
                   // function to add song to playlist
               }
            }
          });
        $('.dropdown-content').append(btn);
        }

      },
      error:function(error){
        console.log('Error ' + error)
      }
  })
});