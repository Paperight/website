$(function(){
	
	/**
	 * CKEditor attachment by CSS classes
	 * add classes "editor" + ["editor-simple"] | ["editor-advanced"] 
	 */
	$('textarea.editor').each(function(){
		var e = $(this), editor = null;
		if(e.hasClass('editor-full'))
		{
			editor = CKEDITOR.replace(this, {
				toolbar: 'Full',
				removePlugins: 'save'
			});
		}
		else if(e.hasClass('editor-basic'))
		{
			editor = CKEDITOR.replace(this, {
				toolbar: 'Basic', 
				removePlugins: 'elementspath,save',
				resize_enabled: false
			});
		}
	});
	
});
