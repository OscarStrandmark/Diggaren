
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
$(".w-10 >a> #a").click(function(){$(this).toggleClass("fa-play-circle fa-pause-circle")})

function addToPlayList(id) {
  console.log(id);
}

// When website is loaded, run function to create dropdown menu with playlists
$(document).ready(function(){
  var access_token = document.cookie.replace('accessToken=', '');
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
          btn = $('<button />', {
            text : result.items[key].name,
            type  : 'button',
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