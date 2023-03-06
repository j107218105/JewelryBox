/**
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
$(document).ready(function () {
	$.ajaxSetup({cache: true});
	$.getScript('//connect.facebook.net/zh_TW/all.js', function () {
		FB.init({
			appId: '754547857961632',
			version: 'v2.2',
			cookie: true,
			status: true
		});
	});
	function statusChange(r) {
		if (r.status === 'connected') {
			console.log("response.status === 'connected'");
			FB.api('/v2.2/me?fields=id,birthday,gender,name,email', function (response) {
				console.log(JSON.stringify(response));
			});
			if (r.authResponse) {
				$.post('logIn.asp', {
					accessToken: r.authResponse.accessToken,
					expiry: r.authResponse.expiresIn
				}, function (d) {
					if (d.reason) {
						alert(d.reason);
					}
					if (d.result) {
						location.replace(d.result);
					}
				}, 'json');
			}
		} else {
			console.log("response.status !== 'connected'");
			FB.login(function (r2) {
				if (r2.authResponse) {
					FB.api('/v2.2/me?fields=id,birthday,gender,name,email', function (response) {
						console.log(JSON.stringify(response));
					});
					$.post('logIn.asp', {
						accessToken: r2.authResponse.accessToken,
						expiry: r2.authResponse.expiresIn
					}, function (d) {
						if (d.reason) {
							alert(d.reason);
						}
						if (d.result) {
							location.replace(d.result);
						}
					}, 'json');
				}
			}, {scope: 'public_profile,email,user_birthday'});
		}
	}
	$('BUTTON#facebook').click(function (e) {
		e.preventDefault();
		FB.getAuthResponse(function (r) {
			statusChange(r);
		});
		FB.getLoginStatus(function (r) {
			statusChange(r);
		});
		return false;
	});
	$('BUTTON#google').click(function () {
		gapi.auth.signIn({
			'clientid': '825214074388-1ud84gnrn3lirqkmvldi4crlbl75stqs.apps.googleusercontent.com',
			'cookiepolicy': 'single_host_origin', //uri
			'approvalprompt': 'force',
			'callback': function (aR) {
				if (aR) {
					if (aR['status']['signed_in']) {
						gapi.auth.setToken(aR);
						$.post('signIn.asp', {
							accessToken: aR['access_token'],
							expiry: aR['expires_in']
						}, function (d) {
							if (d.reason) {
								alert(d.reason);
							}
							if (d.result) {
								location.replace(d.result);
							}
						}, 'json');
					} else {
						console.log(75 + '\t' + aR['error']);
					}
				}
			},
			'requestvisibleactions': 'http://schema.org/CommentAction http://schema.org/ReviewAction',
			'scope': 'profile https://www.googleapis.com/auth/plus.login email',
			'theme': 'dark'
		});
	});
});