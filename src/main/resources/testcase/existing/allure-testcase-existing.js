/**
 * Initialisation and logic for the panel.
 */
(function($) {

    // The tab registers itself when the Link Browser is created.
    AJS.bind("dialog-created.link-browser", function (e, linkBrowser) {

        const ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ2LmtyaXpoYW5vdnNraXkiLCJzY29wZSI6WyJvcGVuaWQiXSwiaXNzIjoiQWxsdXJlIFRlc3RPcHMiLCJleHAiOjE2MzgzMjMxNDEsImlhdCI6MTYzODI2NTU0MSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BVURJVE9SIl0sImNsaWVudF9pZCI6ImFsbHVyZS1lZS1nYXRld2F5IiwianRpIjoiZWExMDcxNTMtZTVhZS00NjU2LWFlYjktNjkzZjU1NjU4M2E0In0.pXhMvT9zYmGZjgWD10fAnNlyPsNQTnrrueirSsV-wdN8313ptnZf5A_Wm_ZbS7rLNWZLhZU5btwLBOhTLbZOKJ6TluxXD0bDa7GeJ2xpqCPJ_I-A-gvYlfx18ioEEMvd9Bg19qyCE338DTrfjjCzh9o86UzTJAzsDjBUHUh-FtesZixJqEv1MF7bxAas8gtKml0OGzl4pYOaJWuMgIAxZwwIYtsNQjdIthMdhqvtQnf9WOUU30Z4GRmHBhDWHsMMxr2p71gRLwYqSrNFB3y26mgiqG1YYHbRRDT9-sQ55nKxgOVAm6XkQHHSlrb-GiIHmjiBe57CtKdKExES2O9Ovg"
        var key = 'allureTestCaseExisting',    // This panel's key: must match the ID of the web-item link and panel template
            $linkField,      // The jQueryfied link input element.
            thisPanel,       // A reference to this panel, stored when the panel is created
            tab;             // A reference to the tab

        // Define the available Link Browser Advanced tab methods.
        tab = linkBrowser.tabs[key] = {

            // called when the panel is created - register event handlers here
            createPanel: function (context) {
                console.log("createPanel: function (context)")
                console.log(context)
                console.log(linkBrowser)
                console.log(Confluence)

                thisPanel = context.baseElement;
                $linkField = thisPanel.find("input[name='destination']");

                // prevent enter submitting any forms when the button is disabled
                thisPanel.find("form").keydown(function(e) {
                    if(e.keyCode == 13 && !linkBrowser.isSubmitButtonEnabled()) {
                        e.preventDefault();
                    }
                });
                $linkField.keyup(function (e) {
                    AJS.log("link field keyup");
                    var url = $linkField.val();
                    var linkObj = url ? Confluence.Link.makeExternalLink(url) : null;
                    if (linkObj) {
                        linkBrowser.setLink(linkObj); // will enable the Submit button when a URL is added
                    }
                });

                $( "#refreshButton" ).click(function() {
                    AJS.log("#refreshButton.click() called.");
//                    fetch("https://staging.allure.tinkoff.ru/api/rs/testcase/209030", {
                    fetch("https://allure.tinkoff.ru/api/rs/testcase/880337", {
                        "headers": {
                            "Authorization": "Bearer " + ACCESS_TOKEN
                        }
                    })
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
                        })
                        .catch(error => console.log(error))
                });
            },

            // Called when the panel is selected
            onSelect: function () {
                // Defer focus to after dialog is shown, gets around AJS.Dialog tabindex issues
                setTimeout(function() {
                    $linkField.focus();
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
                console.log('handlesLink: function (linkObj)')
                console.log(linkObj)
                return false;
//                return  .startsWith('https://allure.tinkoff.ru');
            }
        };
    });

})(jQuery);
