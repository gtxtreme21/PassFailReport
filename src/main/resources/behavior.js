function toggleContents(elmId){
	var elm = document.getElementById(elmId);
	
	if(elm.style.display == 'none'){
		elm.style.display = '';
	}
	else{
		elm.style.display = 'none';
	}
}