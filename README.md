Allure TestOps plugin for Confluence
Allure TestOps plugin for Confluence
====================================

Plugin is based on:
- [Wikipedia macro sample](https://developer.atlassian.com/server/confluence/creating-a-wikipedia-macro/)
- [Confluence insert link dialog](https://developer.atlassian.com/server/confluence/extending-the-confluence-insert-link-dialog/)
- [REST plugin module](https://developer.atlassian.com/server/framework/atlassian-sdk/rest-plugin-module/)

## Start confluence server 

* atlas-run   -- installs this plugin into the product and starts it on localhost
* atlas-debug -- same as atlas-run, but allows a debugger to attach at port 5005
* atlas-help  -- prints description for all commands in the SDK

* atlas-mvn package -- re-package your add-on and reinstall it using QuickReload

Atlassian solutions require Oracle Java 8. Use `./server-start.sh` to configure JVM.  

Confluence instance will start on `http://localhost:1990/confluence`.

REST service is available through debugging panel `http://localhost:1990/confluence/plugins/servlet/restbrowser#/resource/allure-1-0-testcase`.
Drop checkbox "Show only public APIs"

## Helpful documentation

- https://developer.atlassian.com/server/confluence/writing-confluence-plugins/
- https://developer.atlassian.com/server/confluence/macro-tutorials-for-confluence/
- https://developer.atlassian.com/server/confluence/confluence-plugin-module-types/
- https://developer.atlassian.com/server/framework/atlassian-sdk/creating-an-admin-configuration-form/
- https://developer.atlassian.com/server/confluence/accessing-confluence-components-from-plugin-modules/
