/**
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
$(document).ready(function () {
	/**
	 * 日期選擇器
	 */
	$('INPUT.dP').datepicker({
		changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd'
	}).datepicker($.datepicker.regional['zh-TW']).change(function () {
		var name = $(this).attr('name');
		if (name.match(/^since$/gi) !== null) {
			$('INPUT[name="until"]').datepicker('option', 'minDate', new Date($(this).val()));
		}
		if (name.match(/^until$/gi) !== null) {
			$('INPUT[name="since"]').datepicker('option', 'maxDate', new Date($(this).val()));
		}
	}).dblclick(function () {
		$(this).blur().datepicker('hide').val(null);
	});
});