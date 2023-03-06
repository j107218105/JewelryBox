/**
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
$(document).ready(function () {
	/**
	 * 翻頁器
	 */
	$('FORM#search A.paginate').click(function (e) {
		e.preventDefault();
		var p = $(this).attr('tabindex');
		$(this).parents('FORM').find('SELECT[name="p"] OPTION').each(function () {
			if ($(this).val() === p) {
				$(this).siblings('OPTION:selected').removeAttr('selected');
				$(this).attr({selected: true});
				this.form.submit();
			}
		});
		return false;
	});
	/**
	 * 跳頁器
	 */
	$('FORM#search SELECT[name="p"]').change(function () {
		this.form.submit();
	});
	/**
	 * 選單收合
	 */
	$('UL#navParts>LI').click(function () {
		$('UL#navParts>LI').removeClass('openCart');
		$(this).toggleClass('openCart');
	});
	/**
	 * 不解釋
	 */
	$('A.ajax').click(function (e) {
		e.preventDefault();
		$.post($(this).attr('href'), function (d) {
			if (d.reason) {
				alert(d.reason);
			}
			if (d.response) {
				location.reload();
			}
		}, 'json');
		return false;
	});
	/**
	 * 當 HTTP method 為 DELETE 的 AJAX 處理
	 */
	$('A.delete').click(function (e) {
		e.preventDefault();
		$.ajax($(this).attr('href'), function (d) {
			if (d.reason) {
				alert(d.reason);
			}
			if (d.response) {
				location.reload();
			}
		}, 'json');
		return false;
	});
});