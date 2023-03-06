/**
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
$(document).ready(function () {
	$('INPUT#headcount,INPUT#score,INPUT#age').keyup(function () {
		$(this).val($(this).val().replace(/\D/g, ''));
	});
	function guesstimate(o) {
		$.post('guesstimate.json', {
			since: $('INPUT#since').val(),
			age: $('INPUT#age').val(),
			gender: $('INPUT[name="gender"]:checked').val(),
			occupation: $('SELECT#occupation').val(),
			district: $('SELECT#district').val()
		}, function (d) {
			try {
				if (d.reason) {
					alert(d.reason);
				}
				if (d.response) {
					$('DIV#ajaxResult').text(d.result);
				}
			} catch (e) {
				alert(e);
			}
		}, 'json');
	}
	$('INPUT#age').keyup(guesstimate);
	$('INPUT#since,INPUT[name="gender"],SELECT#occupation,SELECT#district').change(guesstimate);
	/**
	 * 日時選擇器
	 */
	if (typeof jQuery.ui !== 'undefined' && /[1-9]\.[1-9]{1,2}.[1-9]{1,2}/.test($.ui.version)) {
		$('INPUT.dtP').datetimepicker({
			changeMonth: true,
			changeYear: true,
			closeText: '確定',
			currentText: '現在',
			dateFormat: 'yy-mm-dd',
			hourText: '時',
			minuteText: '分',
			timeFormat: 'HH:mm',
			timeText: '時間'
		}).datepicker($.datepicker.regional['zh-TW']);
	}
});