function signIn(){
	var signInId = $("#login").val();
	getFromServer("/login?id=" + signInId);
}


function getFromServer(params){

	$.get( "http://" + location.host + params, function( data ) {
  		$( "#result" ).html( data );
  		alert( "Load was performed." );
	});

}

function postFromServer(params){

	$.post( "http://" + location.host + params, function( data ) {
  		$( "#result" ).html( data );
  		alert( "Load was performed." );
	});

}

function nextDay(){
	postFromServer("/nextDay");
}