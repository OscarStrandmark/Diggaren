

var express = require('express'); // Express web server framework
var request = require('request'); // "Request" library
var cors = require('cors');
var querystring = require('querystring');
var cookieParser = require('cookie-parser');
var httpRequest = require('xmlhttprequest').XMLHttpRequest;
var bodyParser = require('body-parser');

var client_id = 'e9f8cccef4a84a71b2a925f5455fa84c'; // Your client id
var client_secret = 'ed8e7c22f630455bbdcb3fdd46027456'; // Your secret
var redirect_uri = 'http://localhost:8888/callback'; // Your redirect uri

/**
 * Generates a random string containing numbers and letters
 * @param  {number} length The length of the string
 * @return {string} The generated string
 */
var generateRandomString = function(length) {
  var text = '';
  var possible = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

  for (var i = 0; i < length; i++) {
    text += possible.charAt(Math.floor(Math.random() * possible.length));
  }
  return text;
};

var stateKey = 'spotify_auth_state';

var app = express();

app.use(express.static(__dirname, {index: 'login.html'}))
   .use(cors())
   .use(cookieParser())
   .use(bodyParser.json());


app.get('/home', function(req, res) {
    res.sendFile(__dirname + '/index.html');
});

app.post('/fetch', function(req, res) {
    var http_request;
    http_request = new httpRequest();
    http_request.onreadystatechange = function () {
      if(http_request.readyState==4 && http_request.status==200) {
        res.set('Content-Type', 'application/json');
        res.send(JSON.stringify(http_request.responseText))
      }
    };
    http_request.open("POST", "http://localhost:5050/spotify/playlist/fetch");
    http_request.withCredentials = true;
    http_request.setRequestHeader("Content-Type", "application/json");
    http_request.send(JSON.stringify({
      'auth': req.body.auth
    }));
});

app.post('/recommendation', function(req, res) {
  var http_request;
  http_request = new httpRequest();
  http_request.onreadystatechange = function () {
    if(http_request.readyState==4 && http_request.status==200) {
      res.set('Content-Type', 'application/json');
      res.send(JSON.stringify(http_request.responseText))
    }
  };
  http_request.open("POST", "http://localhost:5050/spotify/recommendation");
  http_request.withCredentials = true;
  http_request.setRequestHeader("Content-Type", "application/json");
  http_request.send(JSON.stringify({
    'authorization': req.body.auth,
    'trackID' : req.body.trackID
  }));
});

app.post('/currently', function(req, res) {
  var http_request;
    http_request = new httpRequest();
    http_request.onreadystatechange = function () {
      if(http_request.readyState==4 && http_request.status==200) {
        res.set('Content-Type', 'application/json');
        res.send(JSON.stringify(http_request.responseText));
      }
    };
    http_request.open("POST", "http://localhost:5050/SR/currentlyPlaying");
    http_request.withCredentials = true;
    http_request.setRequestHeader("Content-Type", "application/json");
    http_request.send(JSON.stringify({
      'channelID': req.body.channelID
    }));
});

app.post('/addSong', function(req, res) {
  var http_request;
    http_request = new httpRequest();
    http_request.onreadystatechange = function () {
      if(http_request.readyState==4 && http_request.status==200) {
        res.set('Content-Type', 'application/json');
        res.send(JSON.stringify(http_request.responseText));
      }
    };
    http_request.open("POST", "http://localhost:5050/spotify/search");
    http_request.withCredentials = true;
    http_request.setRequestHeader("Content-Type", "application/json");
    http_request.send(JSON.stringify({
      'playlist_id': req.body.playlist_id,
      'auth': req.body.auth,
      'track_id': req.body.track_id
    }));
  });

app.post('/search', function(req, res) {
  var http_request;
    http_request = new httpRequest();
    http_request.onreadystatechange = function () {
      if(http_request.readyState==4 && http_request.status==200) {
        res.set('Content-Type', 'application/json');
        res.send(JSON.stringify(http_request.responseText));
      }
    };
    http_request.open("POST", "http://localhost:5050/spotify/search");
    http_request.withCredentials = true;
    http_request.setRequestHeader("Content-Type", "application/json");
    http_request.send(JSON.stringify({
      'auth': req.body.auth,
      'type': req.body.type,
      'query': req.body.query
    }));
});

// get request is made from the browser
app.get('/login', function(req, res) {

  var state = generateRandomString(16);
  res.cookie(stateKey, state);

  // your application requests authorization
  var scope = 'user-read-private user-read-email playlist-read-private';
  res.redirect('https://accounts.spotify.com/authorize?' +
    querystring.stringify({
      client_id: client_id,
      response_type: 'code',
      scope: scope,
      redirect_uri: redirect_uri,
      state: state, 
      show_dialog:true
    }));
});

// after user is logged in to spotify, user gets redirected to /callback
app.get('/callback', function(req, res) {

    // your application requests refresh and access tokens
    // after checking the state parameter
  
    var code = req.query.code || null;
    var state = req.query.state || null;
    var storedState = req.cookies ? req.cookies[stateKey] : null;
  
    if (state === null || state !== storedState) {
      res.redirect('/#' +
        querystring.stringify({
          error: 'state_mismatch'
        }));
    } else {
      res.clearCookie(stateKey);
      var authOptions = {
        url: 'https://accounts.spotify.com/api/token',
        form: {
          code: code,
          redirect_uri: redirect_uri,
          grant_type: 'authorization_code'
        },
        headers: {
          'Authorization': 'Basic ' + (new Buffer(client_id + ':' + client_secret).toString('base64'))
        },
        json: true
      };
      // post request to the spotify api
      request.post(authOptions, function(error, response, body) {
        if (!error && response.statusCode === 200) {
          // if post request was successful, save the tokens to the application
          var access_token = body.access_token,
              refresh_token = body.refresh_token;

          res.cookie('accessToken', access_token);
          res.cookie('refreshToken', refresh_token);
          
          var options = {
            url: 'https://api.spotify.com/v1/me',
            headers: { 'Authorization': 'Bearer ' + access_token },
            json: true
          };
  
          // use the access token to access the Spotify Web API
          request.get(options, function(error, response, body) {
            console.log(body);
          });
          
          
          // we can also pass the token to the browser to make requests from there
          // res.redirect('/home?' +
          //   querystring.stringify({
          //     access_token: access_token,
          //     refresh_token: refresh_token
          //   }));
          res.redirect('/home');
        } else {
          res.redirect('/#' +
            querystring.stringify({
              error: 'invalid_token'
            }));
        }
      });
    }
  });

  app.get('/refresh', function(req, res) {
    console.log("Refreshing access token")
    // requesting access token from refresh token
    var refresh_token = req.cookies['refreshToken'];
    var authOptions = {
      url: 'https://accounts.spotify.com/api/token',
      headers: { 'Authorization': 'Basic ' + (new Buffer(client_id + ':' + client_secret).toString('base64')) },
      form: {
        grant_type: 'refresh_token',
        refresh_token: refresh_token
      },
      json: true
    };
  
    request.post(authOptions, function(error, response, body) {
      if (!error && response.statusCode === 200) {
        var access_token = body.access_token;
        res.cookie('accessToken', access_token);
        res.redirect('/home');
      }
    });
  });

  
  console.log('Listening on 8888');
  app.listen(8888);
  