
var username;
var challenges, jars, servertime, success;


function login(){
	var signInId = $("#login").val();
	var params = "/login?id=" + signInId;
//					   http://localhost:8443/login?id=...
	$.get( "http://" + location.host + params, 
		function( data ) { // {"serverTime":"Sun Jul 22 18:09:57 EDT 2018","userName":"Tommy Bluel","numStars":0,"challenges":"","jars":""}
		// step1: convert data from string to json
		var obj = JSON.parse(data);
		// step2: fill global variables with user info
		var user = obj.userInfo
		username = user.userName;
		challenges = user.challenges;
		jars = user.jars;
		servertime = obj.serverTime;
		numStars = user.numStars;
		window.applicationDate = {
			date: obj.date,
			month: obj.month
		};
		success = obj.success;
		// step 3: show all the other divs, hide login div

		if (success) {

		var elem = document.getElementById('loginDiv');
		elem.style.display = 'none'; // hide

		var elem = document.getElementById('home_page_hidden');
		elem.style.display = ''; // show
		
		loadUserInfo();
		loadDate();

		} else {
			alert("Incorrect Credentials");
		}
		
  		//$( "#result" ).html( data );
  		//alert( "Load was performed." );
	});

}

/* this is what userinfo look like
{
	"userInfo":{
		"userName":"Santos Kunter",
		"numStars":0,
		"challenges":[
		{
			"name":"TIM HORTONS",
			"goal":6,
			"fixedUnit":3,
			"progress":0
		}
		],
		"jars":[
		{
			"name":"General Savings",
			"goal":99999.0,
			"progress":0.0
		}
		]
	},
	"serverTime":"Sun Jul 22 23:54:20 EDT 2018"
}

*/

// 
function updateUserInfo(){
	$.get( "http://" + location.host + "/userInfo", 
		function( data ) { // {"serverTime":"Sun Jul 22 18:09:57 EDT 2018","userName":"Tommy Bluel","numStars":0,"challenges":"","jars":""}
			// step1: convert data from string to json
			var obj = JSON.parse(data);
			username = obj.userName;
			numStars = obj.numStars;
			challenges = obj.challenges;
			jars = obj.jars;
			loadUserInfo();
		});

	$.get('http://' + location.host + '/serverDate', 
		function(data){
			var dateInfo = JSON.parse(data);
			window.servertime = dateInfo.serverTimeStr;
			window.applicationDate = dateInfo;

			loadDate();
		});

}

function loadUserInfo(){
	// username & stars
	$('#infoDiv h2').html('User Name: ' + username + '<br>'+'<br>'+ ' Number of stars: ' + numStars );
	// challenges inner html
	var challengesHTML = "";
	for(var i = 0; i < challenges.length; i++){
		var challenge = challenges[i];
		var progressPct = (challenge.progress / challenge.goal * 100).toFixed(2);
		var challengeHTML = 
		'<tr>' +
			'<td>' + challenge.name + '</td>' + 
			'<td>' + challenge.goal + '</td>' + 
			'<td>' + progressPct + '%</td>' + 
			'<td>' + challenge.progress + '</td>' + 
		'</tr>';
		challengesHTML = challengesHTML + challengeHTML;
	}
	$('#challangeDiv table tbody').html(challengesHTML);
	// jars HTML
	var jarsHTML = "";
	for(var i = 0; i < jars.length; i++){
		var jar = jars[i];
		var progressPct = (jar.progress / jar.goal * 100).toFixed(2);
		var jarHTML = 
		'<tr>' +
			'<td>' + jar.name + '</td>' + 
			'<td>' + jar.goal + '</td>' + 
			'<td>' + progressPct + '%</td>' + 
		'</tr>';
		jarsHTML = jarsHTML + jarHTML;
	}
	$('#bucketsDiv table tbody').html(jarsHTML);

}

function loadDate(){
	var dateStr = 'Date: ' + servertime;
	$('#dateTime h4').html(dateStr);
}

function callBackAndUpdate(data){
	console.log(JSON.stringify(data));
	updateUserInfo();
}

function callBack(data){

}

function postFromServer(params){

	$.ajax({
  		type: "POST",
  		contentType: "application/json; charset=utf-8",
  		url: 'http://' + location.host + params,
 		data: '',
 		success: callBackAndUpdate,
 		error: function(error){
 			alert(error);
 		},
  		dataType: "json"
	});

	/*$.post( "http://" + location.host + params, 
		function( data ) {
  			$( "#result" ).html( data );
  			alert( data );
			updateUserInfo();
		},
	"application/json; charset=utf-8");*/

}

function newJar(){
	var name = $('#newJarName').val();
	var goal = $('#newJarGoal').val();
	postFromServer('/createJar?name=' + name + '&goal=' + goal);
}

function getServerDate(){
	$.get('http://' + location.host + '/serverDate', 
		function(data){
			var dateInfo = JSON.parse(data);
			window.servertime = dateInfo.serverTimeStr;
			window.applicationDate = dateInfo;

			loadDate();
		});
}
// ------------------------------- test functions ---------------------------------------------

// working :)
function nextDay(){
	postFromServer("/nextDay");
}

function postTransaction(desc){
	var amount = 1;
	if(amount == 0){
		alert('transaction amount 0');
		return;
	}
	//date format: 2018-02-01T00:00:00
	var date = '2018-' + applicationDate.month + '-' + applicationDate.date + 'T00:00:00';
	var transObj = {
		type: 'CreditCardTransaction',
		description: desc,
		merchantName: desc,
		currencyAmount: amount,
		postDate: date
	}

	$.ajax({
  		type: "POST",
  		contentType: "application/json; charset=utf-8",
  		url: 'http://' + location.host + '/makeTransaction',
 		data: JSON.stringify(transObj),
 		success: callBackAndUpdate,
  		dataType: "json"
	});
}

/*
$.post('http://' + location.host + '/makeTransaction', 
		transObj, 
		function(response){
			alert(response);
		},
		"application/json; charset=utf-8");
function postTimHortonsTransactions(){
	var trans={
		accountId: "string",
      
     currencyAmount: 0,
      customerId: "string",
      description: "string",
      id: "string",
      locationCity: "string",
      "locationCountry": "string",
      "locationLatitude": 0,
      "locationLongitude": 0,
      "locationPostalCode": "string",
      "locationRegion": "string",
      "locationStreet": "string",
      "merchantCategoryCode": "string",
      "merchantId": "string",
      "merchantName": "string",
      "originalCurencyAmount": 0,
      "originalCurrency": "string",
      "originationDate": "string",
      "postBalance": 0,
      "postDate": "string",
      "source": "string",
      "type": "AppToCustomerTransfer"
	
	}
	
}*/