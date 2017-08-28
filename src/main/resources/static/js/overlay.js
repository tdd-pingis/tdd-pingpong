var editorContainerSizeDifference = $(".tab-content").width() - $(".ace_editor").width();
function resizeOverlay() {
	$(".overlay").width($(".tab-content").width() - editorContainerSizeDifference)
}

$(document).ready(function() { resizeOverlay(); });
$(window).resize(function() { resizeOverlay(); });

