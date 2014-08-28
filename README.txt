
The OZONE Marketplace, similar to a commercial application store, operates
as a thin-client registry of applications and services. It enables users
to create, browse, download and use a variety of applications or software
components known as listings. Listings can be App Components, OZONE Apps,
plugins, REST & SOAP services, Web apps, and more. The Store can operate
independently, or in combination with other applications to enable search,
discovery and content management capabilities. 


Summary of each Marketplace Repo:
 - marketplace: the master Marketplace branch
 - ozone-appconfig: Grails plugin for back-end configuration
 - ozone-auditing: Common Event Format (CEF) logging plugin
 - ozone-custom-tomcat: generator to release a bundle
 - ozone-messaging: notifications plugin
 - ozone-security: security plugin
 - ozone-docs: repository of Marketplace user guides and help documentation

DEPENDENCY MODIFICATION
Modified Audit Trail Plugin File
OZONE Marketplace uses a modified version of the Audit Trail library
(http://grails.org/plugin/audit-trail). The change was made to
AuditStampASTTransformation.java.

Audit Trail
The Audit Trail software (http://grails.org/plugin/audit-trail) is
available under an Apache Software Foundation 2.0 license Version 2,
Copyright (c) 2013 GoPivotal, Inc.
The OZONE Marketplace moved and modified one of the component files from
the Audit Trail plugin:
It moved
[plugins/audit-trail-2.0.2/src/java/gorm/AuditStampASTTransformation.java]
to [src/java/gorm/AuditStampASTTransformation.java] and modified it to
work properly with the rest of the Store code.
The modified versions of these files are distributed under the Apache
Software Foundation 2.0 license Version 2
http://www.apache.org/licenses/LICENSE-2.0.html 

Grails
Grails (https://grails.org/) is available under an Apache Software
Foundation 2.0 license Version 2, Copyright 2013 GoPivotal Inc.
The OZONE Marketplace is built using the Grails Framework including
design patterns, templates and application layout. The use of these files
are distributed under the Apache Software Foundation 2.0 license Version 2
http://www.apache.org/licenses/LICENSE-2.0.html 


OZONE Marketplace
Copyright (c) 2014 Next Century Corporation
