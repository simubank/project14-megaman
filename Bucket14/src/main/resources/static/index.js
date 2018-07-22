
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
		username = obj.username;
		challenges = obj.challenges;
		// step 3: show all the other divs, hide login div

  		//$( "#result" ).html( data );
  		//alert( "Load was performed." );
	});

}

function postFromServer(params){

	$.post( "http://" + location.host + params, function( data ) {
  		$( "#result" ).html( data );
  		alert( "Load was performed." );
	});

}
// ------------------------------- test functions ---------------------------------------------

function nextDay(){
	postFromServer("/nextDay");
}

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
	
}