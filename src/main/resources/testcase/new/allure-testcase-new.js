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
                console.log("context = ")
                console.log(context)
                console.log("linkBrowser = ")
                console.log(linkBrowser)
                console.log("Confluence = ")
                console.log(Confluence)

                thisPanel = context.baseElement;
                $nameField = $( "#testcase-name" );

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
                    var testcaseName = $nameField.val()
                    console.log(testcaseName)

                    var originTestcase = {
                        name: testcaseName,
                        projectId: 90,
                        links: [
                            {
                                name: "Список провайдеров услуг",
                                url: "http://localhost:1990/confluence/pages/viewpage.action?pageId=3178498"
                            }
                        ]
                    }

                    fetch("http://localhost:1990/confluence/rest/allure/1.0/testcase/", {
                        method: 'POST',
                        headers: {
                              'Content-Type': 'application/json'
                            },
                        body: JSON.stringify(originTestcase)
                    })
                        .then(response => {
                            console.log(response)
                            // indicates whether the response is successful (status code 200-299) or not
                            if (!response.ok) {
                                throw new Error(`Request failed with status ${reponse.status}`)
                            }
                            return response.json()
                        })
                        .then(testcase => {
                            console.log(testcase)
                            console.log(testcase.name)
                            var $testcaseName = testcase.name;

                            var linkObj = Confluence.Link.makeExternalLink(testcase.url)
                            var text = "[" + testcase.id + "] " + testcase.name;
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
                    $nameField.focus();
                    aliasField = $( "#alias" );
                    $nameField.val(aliasField.val());
                    aliasField.val("");
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
