/**
 * Initialisation and logic for the panel.
 */
(function($) {

    // The tab registers itself when the Link Browser is created.
    AJS.bind("dialog-created.link-browser", function (e, linkBrowser) {

        var key = 'allureTestCaseNew',    // This panel's key: must match the ID of the web-item link and panel template
            $nameField,      // The jQueryfied link input element.
            thisPanel,       // A reference to this panel, stored when the panel is created
            tab;             // A reference to the tab

        // Define the available Link Browser Advanced tab methods.
        tab = linkBrowser.tabs[key] = {

            // called when the panel is created - register event handlers here
            createPanel: function (context) {
                console.log("createPanel: function (context)")
                console.log(context)

                thisPanel = context.baseElement;
                $nameField = $( "#testcase-name" );
//                $nameField = thisPanel.find("input[name='destination']");

                // prevent enter submitting any forms when the button is disabled
                thisPanel.find("form").keydown(function(e) {
                    if(e.keyCode == 13 && !linkBrowser.isSubmitButtonEnabled()) {
                        e.preventDefault();
                    }
                });
                $nameField.keyup(function (e) {
//                    AJS.log("link field keyup");
//                    console.log(Confluence.Link);
//                    var url = $nameField.val();
//                    var linkObj = url ? Confluence.Link.makeExternalLink(url) : null;
//                    if (linkObj) {
//                        linkBrowser.setLink(linkObj); // will enable the Submit button when a URL is added
//                    }
                });

                $( "#createButton" ).click(function() {
                    AJS.log("#createButton.click() called.");
                    console.log(Confluence.Link);
                    var url = "https://allure.tinkoff.ru/project/90/test-cases/542213";
                    var linkObj = url ? Confluence.Link.makeExternalLink(url) : null;
                    if (linkObj) {
                        var text = "[542213] " + $nameField.val();
                        linkObj.body.text = text;
                        linkObj.body.html = text;
                        console.log(linkObj.body);
                        linkBrowser.setLink(linkObj); // will enable the Submit button when a URL is added
                    }
                });
            },

            // Called when the panel is selected
            onSelect: function () {
                // Defer focus to after dialog is shown, gets around AJS.Dialog tabindex issues
                setTimeout(function() {
                    $nameField.focus();
                });
            },

            // Called when this panel is no longer selected
            onDeselect: function () {
            },

            // Called when the submit button is clicked, before the location is retrieved
            preSubmit: function () {
            },


            // Called before the dialog opens to determine which tab to highlight
            handlesLink: function (linkObj) {
                // return true if the link should be cause this panel to be selected when editing
                return false;
            }
        };
    });

})(jQuery);
