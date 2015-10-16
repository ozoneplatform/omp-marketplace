![Travis Status](https://travis-ci.org/ozoneplatform/omp-marketplace.svg)	

# OZONE Marketplace 
 
## Description

The OZONE Marketplace, similar to a commercial application store like Apple’s App Store or the Google Chrome Web Store, operates as a thin-client registry of applications and services. It enables users to create, browse, download and use a variety of applications or software components known as listings. Listings can be App Components, OZONE Apps, plugins, REST & SOAP services, Web apps, and more. 
While the Store can operate independently, using it with OWF provides users with one location for everything they need to do their jobs, from searching and discovering new tools, to starting and managing those tools or building their own workflows. 

##Summary of Repo Marketplace shares with OWF:
 - [omp-marketplace](https://github.com/ozoneplatform/omp-marketplace): the master Marketplace branch
 - [owf-appconfig](https://github.com/ozoneplatform/owf-appconfig): Grails plugin for front-end and back-end configuration
 - [owf-auditing](https://github.com/ozoneplatform/owf-auditing): Common Event Format (CEF) logging plugin
 - [owf-custom-tomcat](https://github.com/ozoneplatform/owf-custom-tomcat): generator to release a bundle
 - [owf-messaging](https://github.com/ozoneplatform/owf-messaging): notifications plugin
 - [owf-security](https://github.com/ozoneplatform/owf-security): security plugin
 - [owf-omp-docs](https://github.com/ozoneplatform/owf-omp-docs): repository of Marketplace user guides and help documentation

![Discovery Page](https://github.com/ozoneplatform/owf-omp-docs/blob/master/Images/OMP_DiscoveryPage.png)

**Marketplace Discovery Page**

![Listing Quick View](https://github.com/ozoneplatform/owf-omp-docs/blob/master/Images/OMP_ListingQuickView.png)

**Listing Quick View**

![Marketplace Scorecard Feature](https://github.com/ozoneplatform/owf-omp-docs/blob/master/Images/OMP_Scorecard.png)

**Scorecard Feature**


##DEPENDENCY & DEPENDENCY MODIFICATIONS
Marketplace uses a modified the Audit Trail Plugin File. It is a version of the Audit Trail library
(http://grails.org/plugin/audit-trail). The change was made to AuditStampASTTransformation.java.

Audit Trail
The Audit Trail software (http://grails.org/plugin/audit-trail) is available under an Apache Software Foundation 2.0 license Version 2, Copyright (c) 2013 GoPivotal, Inc. The OZONE Marketplace moved and modified one of the component files from
the Audit Trail plugin: It moved [plugins/audit-trail-2.0.2/src/java/gorm/AuditStampASTTransformation.java]
to [src/java/gorm/AuditStampASTTransformation.java] and modified it to work properly with the rest of the Store code.
The modified versions of these files are distributed under the Apache Software Foundation 2.0 license Version 2 http://www.apache.org/licenses/LICENSE-2.0.html 

Grails
Marketplace is built using a Grails framework. Grails (https://grails.org/) is available under an Apache Software Foundation 2.0 license Version 2, Copyright 2013 GoPivotal Inc. The OZONE Marketplace is built using the Grails Framework including design patterns, templates and application layout. The use of these files are distributed under the Apache Software Foundation 2.0 license Version 2 http://www.apache.org/licenses/LICENSE-2.0.html 
 
## OZONE Marketplace Copyrights
> Software (c) 2015 [Next Century Corporation](http://www.nextcentury.com/ "Next Century")

> The United States Government has unlimited rights in this software, pursuant to the contracts under which it was developed.  
 
The OZONE Widget Framework is released to the public as Open Source Software, because it's the Right Thing To Do. Also, it was required by [Section 924 of the 2012 National Defense Authorization Act](http://www.gpo.gov/fdsys/pkg/PLAW-112publ81/pdf/PLAW-112publ81.pdf "NDAA FY12").

Released under the [Apache License, Version 2](http://www.apache.org/licenses/LICENSE-2.0.html "Apache License v2").
 
## Community

[Support Guidance] (https://github.com/ozoneplatform/owf-framework/wiki/Support-Guidance): Provides information about resources including related projects.

### Google Group

[ozoneplatform-users](https://groups.google.com/forum/?fromgroups#!forum/ozoneplatform-users): This list is for users, for questions about the platform, for feature requests, for discussions about the platform and its roadmap, etc.

[Support Guidance] (https://github.com/ozoneplatform/owf-framework/wiki/Support-Guidance) : Provides information about resources including related projects.
 
### OWF GOSS Board
OWF started as a project at a single US Government agency, but developed into a collaborative project spanning multiple federal agencies.  Overall project direction is managed by "The OWF Government Open Source Software Board"; i.e. what features should the core team work on next, what patches should get accepted, etc.  Gov't agencies wishing to be represented on the board should check http://owfgoss.org for more details.  Membership on the board is currently limited to Government agencies that are using OWF and have demonstrated willingness to invest their own energy and resources into developing it as a shared resource of the community.  At this time, the board is not considering membership for entities that are not US Government Agencies, but we would be willing to discuss proposals.
 
### Contributions

#### Non-Government
Contributions to the baseline project from outside the US Federal Government should be submitted as a pull request to the core project on GitHub.  Before patches will be accepted by the core project, contributors have a signed [Contributor License Agreement](https://www.ozoneplatform.org/ContributorLicenseAgreement1-3OZONE.docx) on file with the core team.  If you or your company wish your copyright in your contribution to be annotated in the project documentation (such as this README), then your pull request should include that annotation.
 
#### Government
Contributions from government agencies do not need to have a CLA on file, but do require verification that the government has unlimited rights to the contribution.  An email to goss-support@owfgoss.org is sufficient, stating that the contribution was developed by an employee of the United States Government in the course of his or her duties. Alternatively, if the contribution was developed by a contractor, the email should provide the name of the Contractor, Contract number, and an assertion that the contract included the standard "Unlimited rights" clause specified by [DFARS 252.227.7014](http://www.acq.osd.mil/dpap/dars/dfars/html/current/252227.htm#252.227-7014) "Rights in noncommercial computer software and noncommercial computer software documentation".
 
Government agencies are encouraged to submit contributions as pull requests on GitHub.  If your agency cannot use GitHub, contributions can be emailed as patches to goss-support@owfgoss.org.

###Related projects
The OZONE Widget Framework (OWF) is a sister project to Marketplace. [OWF] (https://github.com/ozoneplatform/owf-framework) is a framework that allows data from different servers to communicate inside a browser window without sending information back to the respective servers. This unique capability allows the OWF web portal to offer decentralized data manipulation. It includes a secure, in-browser, pub-sub eventing system which allows widgets from different domains to share information. The combination of decentralized content and in-browser messaging makes OWF particularly suited for large distributed enterprises with legacy stovepipes that need to combine capability. Use it to quickly link applications and make composite tools.
 
