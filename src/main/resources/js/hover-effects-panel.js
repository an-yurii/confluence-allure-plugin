AJS.toInit(function($) {
    var webItemLinkId = 'image-hover-effects';
    var panelClass = 'image-hover-effects';
    var $img;
    var $panelContent;

    AJS.bind('dialog-created.image-properties', function(event, data) {
        $img = $(data.img);
        var match = $img.attr('class').match(/hover-(fade-(low|medium|high))/);
        var fadeSelection = match ? match[1] : '';
        $panelContent = $(Confluence.Templates.ImageProperties.HoverEffects.panel({
            fadeSelection: fadeSelection
        }));
        console.log(Confluence.Editor);
        Confluence.Editor.ImageProps.registerPanel(webItemLinkId, $panelContent, panelClass, saveFn);
    });

    function saveFn() {
        var fadeSelection = $panelContent.find('input[name="fade"]:checked').attr('id');
        $img.removeClass('hover-fade-low hover-fade-medium hover-fade-high');
        if (fadeSelection !== 'fade-none') {
            $img.addClass('hover-' + fadeSelection);
        }
    }
});