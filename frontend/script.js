var apiUrl = 'http://localhost:5050';
setCookie('channelID',-1);

// var previousSongs = {};
var index = 0;
var timeout;

// previousSongs["songName"] = 'songID';

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

function radio(channel, channelID) {
    console.log('clicked channel');
    setCookie('channelID', channelID);
    updateSongInfo();
    getRecommendation();
    $('.nowPlayingChannel').html('Spelas just nu på ' + channel + ':');
    console.log(getCookie('channelID'));
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
function savetrackID(songName, artistName) {
    var data = {};
    data.auth = getCookie('accessToken');
    data.type = 'track';
    data.query = songName + " " + artistName;
    $.ajax({
        url: '/search',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(result) {
            var json = JSON.parse(result);
            if(!json.tracks.items[0].id){
                setCookie('trackID', -1);
            } else {
                var trackID = json.tracks.items[0].id;
                setCookie('trackID', trackID);
                // archiveSong(songName,artistName,trackID);
            } 
        },
        error: function(request, status, error) {
            console.log('Search: ' + status);
        }
    });	
}

function getRecommendation() {
    // Request is sent to fetch recommendations based on the currently playing song
    console.log("Recommendation running on: " + getCookie('trackID'));
    $.ajax({
        url: '/recommendation',
        type: 'POST',
        data: JSON.stringify({
            'auth': getCookie('accessToken'),
            'trackID': getCookie('trackID')
        }),
        contentType: 'application/json',
        success: function(result) {
            result = JSON.parse(result);
            var recommendedName = result['trackName'];
            var recommendedArtist = result['artistName'];
            console.log("Recommendation works");
            $('#recommendedSong').html(recommendedName + ' - ' + recommendedArtist);
        },
        error:function(request, status, error){
            console.log("Recommendation: " + status);
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
                savetrackID(songName, artistName);
                // window.clearTimeout(timeout);
                // timeout = window.setTimeout(archiveSong(),startTime);

                // updatePrevious();
            } 
            $('#nowPlaying').html(nowPlaying);
            
        },
        error:function(request, status, error){
            console.log("Currently: " + status)
        }
    });
}

// function archiveSong(songName,artistName,trackID){
//     var stringToShow = songName + ' - ' + artistName;
//     previousSongs[stringToShow] = trackID;
// }



// function updatePrevious(playlists) {
//     var i = 0;
//     if(!playlists) {
//         console.log('Could not fetch playlists');
//     } else {
//         for(var key in previousSongs){
//             var dropdown = "<div class='dropdown'><button class='dropbtn' onclick='toggleDropdown()'>Lägg till låt</button><div id='myDropdown$(i)' class='dropdown-content'>";
//             i++;
            
//             dropdown += '</div></div>';
//             $('#previousSong').append(dropdown);
//                     for(var playlist in playlists) {
//                         btn = $('<div />', {
//                             class: "spellista",
//                             text : playlists.items[song].name,
//                             type  : 'div',
//                             value : playlists.items[song].id,
//                             on    : {
//                                 click: function() {
//                                     addToPlaylist(getCookie('trackID'), this.value);
//                                 }
//                             }
//                         });
//                         $('.dropdown-content').append(btn);
//                     }
//         }

//     }
// }

function addChannelButtons() {
    

    var getChannels = $.get('/channels', function() {
            console.log('channels success');
        })
            .done(function() {
                var channels = JSON.parse(getChannels.responseText);
                for(var channel in channels) {
                    if(channels.hasOwnProperty(channel)) {
                        let button = $('<div />', {
                            class: "channels",
                            text: channel,
                            value: channels[channel],
                            on: {
                                click: function() {
                                    radio(this.textContent, this.getAttribute('value'));
                                }
                            }
                        });
                        $('.channel-content').append(button);
                    }
                }

            })
            .fail(function() {
                console.log('channels failed');
            })
            .always(function() {
                console.log('channels finished');
            });
}

// Adds the given song to the given playlist on Spotify
function addToPlaylist(trackID, playlistID) {
    var data = {};
    data.playlist_id = playlistID;
    data.auth = getCookie('accessToken');
    data.track_id = getCookie('trackID');
    $.ajax( {
        url: '/addSong',
        type: 'POST',
        contentType: 'application/json',
        body: JSON.stringify(data),
        success: function(result) {
            alert('Song added to playlist');
        },
        error:function(request, status, error){
            console.log("Add Song: " + status);
        }
    });
}



// When website is loaded, run function to create dropdown menu with playlists


function getPlaylists(data){
    var response = null;
    $.ajax({
        type:"POST",
        url: '/fetch',
        data: JSON.stringify({
            'auth': data
        }),
        contentType: 'application/json',
        success: function(response) {
            let result = JSON.parse(response);
            console.log("Fetch works: " + result);
            for(var key in result.items) {
                console.log('adding playlist');
                btn = $('<div />', {
                    class: "spellista",
                    text : result.items[key].name,
                    type  : 'div',
                    value : result.items[key].id,
                    on    : {
                        click: function() {
                            addToPlaylist(getCookie('trackID'), result.items[key].id);
                        }
                    }
                });
                $('.playlists').append(btn);
            }
            // updatePrevious(result.items);
            return result;
        },
        error: function(request, status, error){
            console.log("Fetch: " + status);
            return null;
        }
    })
}

function getChannelName(channelID) {
    var channelName = $.get('/channelName?' + channelID)
        .done(function() {
            return channelName.responseText;
        })
}

//Changes the radio channel depending on the pseudo channel
function pseudoChannel(type) {
    $.ajax({
        url: "/pseudoChannel", 
        type: "POST", 
        contentType: "application/json",
        data: JSON.stringify({
            "auth": getCookie("accessToken"), 
            "type": type
        }), 
        success: function(result){
            result = JSON.parse(result);
            radio(getChannelName(result["channel"]), result['channel']);
        }, error: function(request, status, error){
            console.log("Pseudo channel: " + status);
        }

    })
}



$(document).ready(function(){
    addChannelButtons();
    var access_token = getCookie("accessToken");
    getPlaylists(access_token);
    
});
