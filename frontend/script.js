var apiUrl = '192.168.137.1:5050';


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

// var coll = document.getElementsByClassName("dropbtn");
// var i;

// for (i = 0; i < coll.length; i++) {
//   coll[i].addEventListener("click", function() {
//     this.classList.toggle("active");
//     var content = this.nextElementSibling;
//     if (content.style.display === "block") {
//       content.style.display = "none";
//     } else {
//       content.style.display = "block";
//     }
//   });
// }

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


// Changes the playing radio channel on the website to the channel given as a parameter, only P2 and P3 works so far
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

// Returns the cookie with the given identifier in the parameter
// parameter - variable name of cookie
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

function setCookie(name, value) {
	document.cookie = name + '=' + value;
}

/* When the user clicks on the button,
toggle between hiding and showing the dropdown content */
function toggleDropdown() {
	document.getElementById("myDropdown").classList.toggle("show");
}
  
// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
	if (!event.target.matches('.dropbtn')) {
		var dropdowns = document.getElementsByClassName("dropdown-content");
		var i;
		for (i = 0; i < dropdowns.length; i++) {
			var openDropdown = dropdowns[i];
			if (openDropdown.classList.contains('show')) {
				openDropdown.classList.remove('show');
			}
		}
	}
}

function saveSongID(songName, artistName) {
	$.ajax({
		url: apiUrl + '/spotify/search',
		type: 'POST',
		body: {
			auth: getCookie('accessToken'),
			type: 'song',
			query: songName + ' ' + artistName
		},
		success: function(result) {
			var songID = result.items[0].id;
			setCookie('songID', songID);
		},
		error: function(error) {
			console.log('Error: ' + error.Message);
		}
	});	
}

function updateSongInfo() {
	var nowPlaying = '';
	$.ajax({
		url: apiUrl + '/SR/currentlyPlaying',
		type: 'POST',
		body: {
			channelID: getCookie('channelID')
		},
		success: function(result) {
			if(result['playingSongName'] != null) {
				var songName = result['playingSongName'];
				var artistName = result['playingSongArtist'];
				var startTime = new Date(result['nextSongStartTime']);
				var startTime = startTime.getMilliseconds() - new Date().getMilliseconds;
				nowPlaying = songName + ' - ' + artistName;
				saveSongID(songName, artistName);
			} else {
				nowPlaying = 'Just nu spelas ingen musik';
			}
			$('#nowPlaying').html(nowPlaying);
			setTimeout(updateSongInfo(), startTime);
		},
		error:function(request, status, error){
			console.log(request.statusText)
		}
	});

	$.ajax({
		url: apiUrl + '/spotify/recommendation',
		type: 'POST',
		body: {
			authorization: getCookie('accessToken'),
			trackID: getCookie('songID')
		},
		success: function(result) {
			var recommendedName = callback['trackName'];
			var recommendedArtist = callback['artistName'];
			$('#recommendedSong').html(recommendedName + ' - ' + recommendedArtist);
		},
		error:function(request, status, error){
			console.log(request.statusText)
		}
	});
}

$(document).ready(function() {
	updateSongInfo();
})

function addToPlaylist(songID, playlistID) {
	$.ajax( {
		url: apiUrl + '/spotify/playlist/add',
		type: 'POST',
		body: {
		playlist_id: playlistID,
		auth: getCookie('accessToken'),
		track_id: getCookie('songID')
		},
		success: function(result) {
			alert('Song added to playlist');
		},
		error:function(request, status, error){
			console.log(request.statusText)
		}
	});
}


// When website is loaded, run function to create dropdown menu with playlists
$(document).ready(function(){
  var access_token = getCookie("accessToken");
  // Get request is made to fetch playlists for current user
  $.ajax({   
      body: {
        'Accept' : 'application/json',
        'Content-Type' : 'application/json',
        'auth' : access_token
      },
      url: apiUrl + '/spotify/playlist/fetch',
      type:"POST",
      success: function(result) {
        //  on success loops through playlists and creates a button for each of them in the dropdown menu
        for(var key in result.items) {
          btn = $('<div />', {
            class: "spellista",
            text : result.items[key].name,
            type  : 'div',
            value : result.items[key].id,
            on    : {
               click: function() {
                   addToPlaylist('songname', result.items[key].id);
               }
            }
          });
        $('.dropdown-content').append(btn);
        }

      },
      error:function(request, status, error){
        console.log(request.statusText)
      }
  })
});