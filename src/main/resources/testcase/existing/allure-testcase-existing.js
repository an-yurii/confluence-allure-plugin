/**
 * Initialisation and logic for the panel.
 */
(function($) {

    // The tab registers itself when the Link Browser is created.
    AJS.bind("dialog-created.link-browser", function (e, linkBrowser) {

        const ACCESS_TOKEN = "unknown"
        var key = 'allureTestCaseExisting',    // This panel's key: must match the ID of the web-item link and panel template
            $idField,        // The jQueryfied link input element.
            thisPanel,       // A reference to this panel, stored when the panel is created
            tab,             // A reference to the tab
            $link,
            $testCaseId;

        // Define the available Link Browser Advanced tab methods.
        tab = linkBrowser.tabs[key] = {

            // called when the panel is created - register event handlers here
            createPanel: function (context) {
                console.log("createPanel: function (context)")
                console.log(context)
                console.log(linkBrowser)
                console.log(Confluence)

                thisPanel = context.baseElement;
                $idField = $( "#testcase-id" )

                // prevent enter submitting any forms when the button is disabled
                thisPanel.find("form").keydown(function(e) {
                    if(e.keyCode == 13 && !linkBrowser.isSubmitButtonEnabled()) {
                        e.preventDefault();
                    }
                });
//                $linkField.keyup(function (e) {
//                    AJS.log("link field keyup");
//                    var url = $linkField.val();
//                    var linkObj = url ? Confluence.Link.makeExternalLink(url) : null;
//                    if (linkObj) {
//                        linkBrowser.setLink(linkObj); // will enable the Submit button when a URL is added
//                    }
//                });

                $( "#refreshButton" ).click(function() {
                    AJS.log("#refreshButton.click() called.");
                    fetch("http://localhost:1990/confluence/rest/allure/1.0/testcase/" + $idField.val())
                        .then(response => {
                            console.log(response)
                            // indicates whether the response is successful (status code 200-299) or not
                            if (!response.ok) {
                                throw new Error(`Request failed with status ${reponse.status}`)
                            }
                            return response.json()
                        })
                        .then(data => {
                            console.log(data)

                            var linkObj = Confluence.Link.makeExternalLink(data.url)
                            var text = "[" + data.id + "] " + data.name;
//                            linkObj.body.text = text;
//                            linkObj.body.html = text;
                            linkBrowser.setLink(linkObj); // will enable the Submit button when a URL is added
                            aliasField = $( "#alias" );
                            aliasField.val(text);

                            console.log("--- aliasField")
                            console.log(aliasField)
                        })
                        .catch(error => console.log(error))
                });
            },

            // Called when the panel is selected
            onSelect: function () {
                // Defer focus to after dialog is shown, gets around AJS.Dialog tabindex issues
                setTimeout(function() {
                    $idField.focus();
                    console.log("onSelect - $link")
                    console.log($link)
                    if ($testCaseId != null) {
                        $idField.val($testCaseId);
                    }
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
                var isAllureLink = linkObj.getHref().startsWith('https://allure.tinkoff.ru');
                if (isAllureLink) {
                    $link = linkObj;
                    var url = new URL($link.getHref());
                    var urlPathSegments = url.pathname.split("/")
                    $testCaseId = urlPathSegments[urlPathSegments.length - 1];
                }
                console.log("handlesLink: function (linkObj)")
                console.log($link)
                console.log($testCaseId)
                return isAllureLink;
            }
        };
    });

})(jQuery);
