/**
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
$(document).ready(function () {
	/**
	 * 翻頁器
	 */
	$('FORM#pagination A.paginate').click(function (e) {
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
	$('FORM#pagination SELECT[name="p"]').change(function () {
		this.form.submit();
	});
	/**
	 * 手風琴收合
	 */
	$('NAV>UL>LI>A').click(function () {
		$(this).parents('NAV').find('LI.toggled').removeClass('toggled');
		$(this.parentNode).toggleClass('toggled');
	});
	/**
	 * 
	 */
	$('A[href].ajax').click(function (e) {
		e.preventDefault();
		$.post($(this).attr('href'), function (d) {
			try {
				if (d.reason) {
					alert(d.reason);
				}
				if (d.response) {
					location.reload();
				}
			} catch (e) {
				alert(e);
			}
		}, 'json');
		return false;
	});
	/**
	 * 日時選擇器
	 */
	if (typeof jQuery.ui !== 'undefined' && /[1-9]\.[1-9]{1,2}.[1-9]{1,2}/.test($.ui.version)) {
		$('INPUT.dP').datepicker({
			changeMonth: true,
			changeYear: true,
			dateFormat: 'yy-mm-dd'
		}).datepicker($.datepicker.regional['zh-TW']);
//		$('INPUT.dtP').datetimepicker({
//			changeMonth: true,
//			changeYear: true,
//			closeText: '確定',
//			currentText: '現在',
//			dateFormat: 'yy-mm-dd',
//			hourText: '時',
//			minuteText: '分',
//			timeFormat: 'HH:mm',
//			timeText: '時間'
//		}).datepicker($.datepicker.regional['zh-TW']);
	}
});