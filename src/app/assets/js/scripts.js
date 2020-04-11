
jQuery(document).ready(function() {
	
    /*
        Form validation
    */
    $('.login-form input[type="text"], .login-form input[type="password"], .login-form textarea').on('focus', function() {
    	$(this).removeClass('input-error');
    });
    
    $('.login-form').on('submit', function(e) {
    	
    	$(this).find('input[type="text"], input[type="password"], textarea').each(function(){
    		if( $(this).val() == "" ) {
    			e.preventDefault();
    			$(this).addClass('input-error');
    		}
    		else {
    			$(this).removeClass('input-error');
    		}
    	});
		
		//if ( $('#form-username').val() == "bob" ) {
		//	e.preventDefault();
		//	$('#validation-errors').text('Invalid login attempt. Please try again.');
		//}
		//else if ( $('#form-password').val() == "jamie" ) {
		//	e.preventDefault();
		//	$('#validation-errors').text('The password is incorrect for username ' + $('#form-username').val());
		//}
		//else {
		//	window.location = "home.html";
		//}
		
		var formUsername = $("input").eq(0).val();
		var formPassword = $("input").eq(1).val()
		
		if (formUsername == "")
		{
			e.preventDefault();
			$('#validation-errors').text('Password cannot be blank.');
		}
		
		if (formUsername == "" || formUsername != "admin123")
		{
			e.preventDefault();
			$('#validation-errors').text('Invalid login attempt. Please try again.');
		}
		
		if (formUsername == "admin123" && formPassword != "foobar123")
		{
			e.preventDefault();
			$('#validation-errors').text('The password is incorrect for username ' + formUsername);
		}

		
		if (formUsername == "admin123" && formPassword == "foobar123")
		{	
			window.location = "home.html?username=admin123";
		}

		return false;
    	
    });
	
    
    
});
