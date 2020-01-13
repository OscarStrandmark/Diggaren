var apiUrl = 'http://localhost:5050';

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

	document.cookie = 'channelID=163';

	$('#p3').toggleClass('selected');
	$('#p2').toggleClass('selected');
	updateSongInfo();
  } else if(station == "p3") {
    audio.src = "https://sverigesradio.se/topsy/direkt/164-hi-mp3";

	document.cookie = 'channelID=164';

	$('#p3').toggleClass('selected');
	$('#p2').toggleClass('selected');
	updateSongInfo();
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

// Sets a given cookie in the browser
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

// Saves the currently playing songs Spotify ID to a cookie
function saveSongID(songName, artistName) {
	var data = {};
	data.auth = getCookie('accessToken');
	data.type = 'track';
	data.query = songName + " " + artistName;
	console.log(data.query);
	$.ajax({
		url: '/search',
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify(data),
		success: function(result) {
			console.log(result);
			var songID = result.tracks[0].id;
			setCookie('songID', songID);
		},
		error: function(error) {
			console.log('Error: ' + error.Message);
		}
	});	
}

// Gets the currently playing song from the radio and presents it in the browser
function updateSongInfo() {
	var nowPlaying = 'Just nu spelas ingen musik.';
	var data = {};
	data.channelID = getCookie('channelID');
	// Request is sent to fetch currently playing song
	$.ajax({
		type: 'POST',
		url: '/currently',
		data: JSON.stringify(data),
		contentType: 'application/json',
		success: function(result) {
			// If there is a song currently playing, save the ID to cookie and present the song info to the browser
			result = JSON.parse(result);
			if(result['playingSongName'] != null) {
				var songName = result['playingSongName'];
				var artistName = result['playingSongArtist'];
				var startTime = new Date(result['nextSongStartTime']);
				var startTime = startTime.getMilliseconds() - new Date().getMilliseconds;
				nowPlaying = songName + ' - ' + artistName;
				saveSongID(songName, artistName);
				//setTimeout(updateSongInfo(), startTime);
			} 
			$('#nowPlaying').html(nowPlaying);
		},
		error:function(request, status, error){
			console.log(request.statusText)
		}
	});
	// Request is sent to fetch recommendations based on the currently playing song
	$.ajax({
		url: '/recommendation',
		type: 'POST',
		data: JSON.stringify({
			'authorization': getCookie('accessToken'),
			'trackID': getCookie('songID')
		}),
		contentType: 'application/json',
		success: function(result) {
			result = JSON.parse(result);
			var recommendedName = result['trackName'];
			var recommendedArtist = result['artistName'];
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

// Adds the given song to the given playlist on Spotify
function addToPlaylist(songID, playlistID) {
	var data = {};
	data.playlist_id = playlistID;
	data.auth = getCookie('accessToken');
	data.track_id = getCookie('songID');
	$.ajax( {
		url: '/addSong',
		type: 'POST',
		contentType: 'application/json',
		body: JSON.stringify(data),
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

	var data = {};
	data.auth = access_token;
//   Get request is made to fetch playlists for current user
  $.ajax({
      type:"POST",
      url: '/fetch',
      data: JSON.stringify(data),
	  contentType: 'application/json',
      success: function(result) {
		//  on success loops through playlists and creates a button for each of them in the dropdown menu
		result = JSON.parse(result);
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
      error: function(request, status, error){
        console.log(request.statusText)
      }
  })
});

//Changes the radio channel depending on the pseudo channel
function pseudoChannel(type) {
  $.ajax({
    url: "/pseudoChannel", 
    type: "POST", 
    contentType: "application/json",
    body: JSON.stringify({
      "auth": getCookie("accessToken"), 
      "type": type
    }), 
    success: function(result){
      radio(result["channel"]);
        console.log("success!");
    }, error: function(request, status, error){
      console.log(request);
    }

  })
}