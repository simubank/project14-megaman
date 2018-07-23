function signIn(){
	var signInId = $("#login").val();
	getFromServer("/apitest/" + signInId);
}


function getFromServer(params){
	$.get( "http://localhost:8080" + params, function( data ) {
  		$( "#result" ).html( data );
  		alert( "Load was performed." );
	});
}