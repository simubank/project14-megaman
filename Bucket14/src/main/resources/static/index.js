
var username;
var challenges, jars, servertime;


function login(){
	var signInId = $("#login").val();
	var params = "/login?id=" + signInId;
//					   htttp://localhost:8443/login?id=...
	$.get( "http://" + location.host + params, 
		function( data ) { // {"serverTime":"Sun Jul 22 18:09:57 EDT 2018","userName":"Tommy Bluel","numStars":0,"challenges":"","jars":""}
		// step1: convert data from string to json
		var obj = JSON.parse(data);
		// step2: fill global variables with user info
		username = obj.userName;
		challenges = obj.challenges;
		// step 3: show all the other divs, hide login div

  		//$( "#result" ).html( data );
  		alert( "Load was performed." );
	});

}

function postFromServer(params){

	$.post( "http://" + location.host + params, 
		function( data ) {
  			$( "#result" ).html( data );
  			alert( data );
		},
	"application/json; charset=utf-8");

}

function newJar(name, goal){
	postFromServer('/createJar?name=' + name + '&goal=' + goal);
}

function getServerDate(){
	$.get('http://' + location.host + '/serverDate', 
		function(data){
			var dateInfo = JSON.parse(data);
			window.applicationDate = dateInfo;
		})
}
// ------------------------------- test functions ---------------------------------------------

// working :)
function nextDay(){
	postFromServer("/nextDay");
}

function postTransaction(amount){
	//date format: 2018-02-01T00:00:00
	var date = '2018-' + applicationDate.month + '-' + applicationDate.date + 'T00:00:00';
	var transObj = {
		type: 'CreditCardTransaction',
		description: 'TIM HORTONS TEST',
		merchantName: 'TIM HORTONS',
		currencyAmount: amount,
		postDate: date
	}

	$.ajax({
  		type: "POST",
  		contentType: "application/json; charset=utf-8",
  		url: 'http://' + location.host + '/makeTransaction',
 		data: JSON.stringify(transObj),
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