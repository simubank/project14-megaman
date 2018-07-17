function signIn(){
	var signInId = $("#login").val();
	getFromServer("/apitest?id=" + signInId);
}


function getFromServer(params){

	$.get( "http://" + location.host + params, function( data ) {
  		$( "#result" ).html( data );
  		alert( "Load was performed." );
	});

}