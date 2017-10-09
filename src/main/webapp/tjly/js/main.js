jQuery(function($) {
	if(top !== window) {
		try	{
			$(top.document).prop("title", $(document).prop("title"));
		}catch(ex) {
			if(console) {
				console.log(ex);
			}
		}
	}
	$("#select-all").click("click", function(e) {
		var checked = this.checked;
		$(this).closest("table").find("tbody input:checkbox").prop("checked", checked);
	});
});
