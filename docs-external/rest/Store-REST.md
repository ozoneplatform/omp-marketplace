# Store 7.16.1 REST Documentation
The following APIs reference Marketplace 7.16.1. They are subject to change [in future versions].

# Group API

## optional_title [/api]

### Retrieve application metadata such as name, version, build date and build number [GET]

+ Response 200 (application/json)
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"name":"Marketplace","version":"7.5.0","buildNumber":"1","buildDate":"2013-11-20T05:00:00Z"}

# Group Category

## optional_title [/public/category/$id]

### Retrieve a Category [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":{"id":1,"title":"Analytics and Analysis","createdBy":{"id":1,"username":"System","name":"System"},"description":"Services, tools and solutions to analyze data","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-08-28T19:29:57Z","uuid":"7f21741e-b8b9-485e-b34c-daf6c7670007","createdDate":"2013-08-28T19:29:57Z"}}


## optional_title [/public/category]

### Retrieves all Categories [GET]


+ Request
    + Headers


    + Body

            {"max": 4, "sort": "title", "order": "asc", "offset": 1}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":{"id":1,"title":"Analytics and Analysis","createdBy":{"id":1,"username":"System","name":"System"},"description":"Services, tools and solutions to analyze data","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-08-28T19:29:57Z","uuid":"7f21741e-b8b9-485e-b34c-daf6c7670007","createdDate":"2013-08-28T19:29:57Z"}}


# Group CustomFieldDefinition

## optional_title [/public/customFieldDefinition/$id]

### Retrieve a Custom Field Definition [GET]


+ Request
    + Headers


    + Body

            {"id":1}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":[{"fieldType":"TEXT","editedDate":"2014-05-12T13:19:25Z","class":"marketplace.TextCustomFieldDefinition","label":"test","section":"typeProperties","isPermanent":false,"id":1,"createdBy":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"isRequired":true,"editedBy":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"description":"Testing custom field definitions in the store.","name":"Test","uuid":"0e88c913-7762-4eab-a5b3-450c5605e8d8","types":[{"id":2,"title":"App Component","uuid":"ea58bfa0-aba0-4573-b2ea-1bc9a6b1e863"},{"id":1,"title":"Web App","uuid":"40b60569-8d13-4b8c-a3b1-ec4d5432670d"}],"allTypes":false,"createdDate":"2014-05-12T13:19:25Z","tooltip":"Test Custom Field Definition"}]}


## optional_title [/public/customFieldDefinition]

### Retrieve all Custom Field Definitions [GET]


+ Request
    + Headers


    + Body

            {"max": 5, "offset":3,"order":"asc","sort":"title"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":[{"fieldType":"TEXT","editedDate":"2014-05-12T13:19:25Z","class":"marketplace.TextCustomFieldDefinition","label":"test","section":"typeProperties","isPermanent":false,"id":1,"createdBy":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"isRequired":true,"editedBy":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"description":"Testing custom field definitions in the store.","name":"Test","uuid":"0e88c913-7762-4eab-a5b3-450c5605e8d8","types":[{"id":2,"title":"App Component","uuid":"ea58bfa0-aba0-4573-b2ea-1bc9a6b1e863"},{"id":1,"title":"Web App","uuid":"40b60569-8d13-4b8c-a3b1-ec4d5432670d"}],"allTypes":false,"createdDate":"2014-05-12T13:19:25Z","tooltip":"Test Custom Field Definition"}]}


# Group Relationship

## optional_title [/relationship/getListings]

### Returns the list of service item descriptors used for setting listing's required items [GET]


+ Request
    + Headers


    + Body

            {"query":"","offset":0,"max":200,"page":1,"sort":"title","dir":"asc"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"totalCount":28,"msg":"Showing 28 listings.","data":[{"id":7,"title":"A Web App","imageURL":"http://icons.iconarchive.com/icons/capital18/capital-suite/128/Misc-Globe-icon.png","versionName":null},{"id":29,"title":"AAAABBBCCCC","imageURL":"http://","versionName":"1"},{"id":22,"title":"Another IE 7 Test","imageURL":"/marketplace/images/get/null","versionName":null},{"id":3,"title":"Component with requirements","imageURL":"https://www.owfgoss.org/jira/s/en_US-f3eu57-1988229788/6097/12/_/jira-logo-scaled.png","versionName":"1"},{"id":5,"title":"Component with requirements","imageURL":"https://www.owfgoss.org/jira/s/en_US-f3eu57-1988229788/6097/12/_/jira-logo-scaled.png","versionName":"1"},{"id":1,"title":"Group Test App","imageURL":"/marketplace/images/get/null","versionName":null},{"id":8,"title":"Ozone App","imageURL":"/marketplace/images/get/null","versionName":null},{"id":25,"title":"Push Test","imageURL":"/marketplace/images/get/null","versionName":null},{"id":2,"title":"Required Component","imageURL":"https://www.owfgoss.org/jira/s/en_US-f3eu57-1988229788/6097/12/_/jira-logo-scaled.png","versionName":"1"},{"id":10,"title":"Restore Test App","imageURL":"/marketplace/images/get/null","versionName":null},{"id":13,"title":"Restore Test App 2","imageURL":"/marketplace/images/get/null","versionName":null},{"id":15,"title":"Restore Test App 3","imageURL":"/marketplace/images/get/null","versionName":null},{"id":21,"title":"sdfsdf","imageURL":"/marketplace/images/get/null","versionName":null},{"id":27,"title":"Test","imageURL":"/marketplace/images/get/null","versionName":null},{"id":28,"title":"Test 2","imageURL":"/marketplace/images/get/null","versionName":null},{"id":24,"title":"Test Scorecard","imageURL":"http://www.cnn.com","versionName":null},{"id":4,"title":"Testing empty description and version","imageURL":"https://www.owfgoss.org/jira/s/en_US-f3eu57-1988229788/6097/12/_/jira-logo-scaled.png","versionName":null},{"id":6,"title":"Testing empty description and version","imageURL":"https://www.owfgoss.org/jira/s/en_US-f3eu57-1988229788/6097/12/_/jira-logo-scaled.png","versionName":"null"},{"id":11,"title":"Testing empty description and version","imageURL":"https://www.owfgoss.org/jira/s/en_US-f3eu57-1988229788/6097/12/_/jira-logo-scaled.png","versionName":"null"},{"id":16,"title":"Testing empty description and version","imageURL":"https://www.owfgoss.org/jira/s/en_US-f3eu57-1988229788/6097/12/_/jira-logo-scaled.png","versionName":"null"},{"id":19,"title":"Testing empty description and version","imageURL":"https://www.owfgoss.org/jira/s/en_US-f3eu57-1988229788/6097/12/_/jira-logo-scaled.png","versionName":"null"},{"id":9,"title":"Training Test App Component","imageURL":"http://officeimg.vo.msecnd.net/en-us/images/MH900234625.jpg","versionName":null},{"id":12,"title":"Training Test App Component","imageURL":"http://officeimg.vo.msecnd.net/en-us/images/MH900234625.jpg","versionName":"null"},{"id":14,"title":"Training Test App Component","imageURL":"http://officeimg.vo.msecnd.net/en-us/images/MH900234625.jpg","versionName":"null"},{"id":17,"title":"Training Test App Component","imageURL":"http://officeimg.vo.msecnd.net/en-us/images/MH900234625.jpg","versionName":"null"},{"id":20,"title":"Training Test App Component","imageURL":"http://officeimg.vo.msecnd.net/en-us/images/MH900234625.jpg","versionName":"null"},{"id":23,"title":"Training Test App Component","imageURL":"http://officeimg.vo.msecnd.net/en-us/images/MH900234625.jpg","versionName":"null"},{"id":26,"title":"Training Test App Component","imageURL":"http://officeimg.vo.msecnd.net/en-us/images/MH900234625.jpg","versionName":"null"}]}


## optional_title [/relationship/getRelatedItems/$id]

### Returns the list of service items required by the specified service item [GET]


+ Request
    + Headers


    + Body

            {"id":22}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"totalCount":1,"data":[{"id":21,"title":"Approve Test","imageURL":"http://","versionName":null}]}


## optional_title [/relationship/getOWFRequiredItems/$id]

### List of service items (approved, enabled, OWF-compatible and published) that are required listings for the given service item [GET]


+ Request
    + Headers


    + Body

            {"id":1}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":[{"imageLargeUrl":"http://","totalComments":0,"launchUrl":"http://","state":{"id":1,"title":"Active","uuid":"b0c62bd4-9cda-4767-915d-e805cb2456a1"},"totalRate5":0,"agencyIcon":"/marketplace/themes/common/images/agency/agencyDefault.png","totalRate4":0,"requirements":null,"ozoneAware":true,"id":1,"author":"testAdmin1","organization":"Test Admin Organization","avgRate":0,"lastActivity":{"activityDate":"2013-08-14T13:12:38Z"},"title":"Sample Required Item","releaseDate":null,"recommendedLayouts":[],"totalRate2":0,"dependencies":null,"totalRate3":0,"description":null,"approvalStatus":"Approved","totalVotes":0,"docUrl":null,"totalRate1":0,"types":{"id":2,"title":"App Component","description":"app component","hasLaunchUrl":true,"image":{"id":null,"imageSize":null,"contentType":null,"url":"null/themes/common/images/default_types_widget_icon.png"},"uuid":"f6623a5a-00a0-4b89-992a-c61c79169bed","hasIcons":true,"ozoneAware":true,"isPermanent":true},"screenshots":[{"id":85,"largeImageUrl":"https://localhost/1.png","smallImageUrl":"https://localhost/1.png","ordinal":1}],"techPoc":"testAdmin1","customFields":[],"isPublished":true,"agency":"optional_title","class":"marketplace.ServiceItem","versionName":null,"approvedDate":"2013-08-13T17:20:32Z","isEnabled":true,"requires":[],"owfProperties":{"id":1,"visibleInLaunch":true,"universalName":null,"height":650,"intents":[],"singleton":false,"background":false,"mobileReady":false,"owfWidgetType":"standard","width":1050,"stackContext":"","stackDescriptor":"","descriptorUrl":null},"installUrl":null,"isOutside":true,"intents":[],"owner":{"id":7,"username":"testAdmin1","name":"Test Admin 1"},"categories":[],"uuid":"100ed489-3bcc-4eab-8168-e01a55912d92","imageSmallUrl":"http://"}]}


## optional_title [/public/serviceItem/getOWFRequiredItems/$id]

### Returns the required items used in OWF for a service item [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=UTF-8

    + Body

            {"total":1,"data":[{"imageLargeUrl":"http://","totalComments":0,"launchUrl":"http://","state":{"id":1,"title":"Active","uuid":"b0c62bd4-9cda-4767-915d-e805cb2456a1"},"totalRate5":0,"agencyIcon":"/marketplace/themes/common/images/agency/agencyDefault.png","totalRate4":0,"requirements":null,"ozoneAware":true,"id":1,"author":"testAdmin1","organization":"Test Admin Organization","avgRate":0,"lastActivity":{"activityDate":"2013-08-14T13:12:38Z"},"title":"Sample Required Item","releaseDate":null,"recommendedLayouts":[],"totalRate2":0,"dependencies":null,"totalRate3":0,"description":null,"approvalStatus":"Approved","totalVotes":0,"docUrl":null,"totalRate1":0,"types":{"id":2,"title":"App Component","description":"app component","hasLaunchUrl":true,"image":{"id":null,"imageSize":null,"contentType":null,"url":"null/themes/common/images/default_types_widget_icon.png"},"uuid":"f6623a5a-00a0-4b89-992a-c61c79169bed","hasIcons":true,"ozoneAware":true,"isPermanent":true},"screenshots":[{"id":85,"largeImageUrl":"https://localhost/1.png","smallImageUrl":"https://localhost/1.png","ordinal":1}],"techPoc":"testAdmin1","customFields":[],"isPublished":true,"agency":"optional_title","class":"marketplace.ServiceItem","versionName":null,"approvedDate":"2013-08-13T17:20:32Z","isEnabled":true,"requires":[],"owfProperties":{"id":1,"visibleInLaunch":true,"universalName":null,"height":650,"intents":[],"singleton":false,"background":false,"mobileReady":false,"owfWidgetType":"standard","width":1050,"stackContext":"","stackDescriptor":"","descriptorUrl":null},"installUrl":null,"isOutside":true,"intents":[],"owner":{"id":7,"username":"testAdmin1","name":"Test Admin 1"},"categories":[],"uuid":"100ed489-3bcc-4eab-8168-e01a55912d92","imageSmallUrl":"http://"}]}


# Group Profile

## optional_title [/profile/editBio]

### Edits bio of the specified user. Returns indicaton of success and specified bio text [POST]


+ Request
    + Headers


    + Body

            {"username":"testAdmin1","bio":"blah"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"bio":"blah"}


## optional_title [/profile/search]

### Searches for and returns user profile records [GET]


+ Request
    + Headers


    + Body

            {"query":"testAdmin1","page":1,"start":0,"limit":25,"sort":"username","dir":"asc"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"totalCount":1,"records":[{"id":4,"display":"Stephanie Schneider Admin","username":"StephanieSAdmin","fmtdisplay":"Stephanie Schneider Admin","fmtusername":"<b>StephanieSAdmin</b>"}]}


## optional_title [/public/profile/$id]

### Returns JSON for the specified Profile object [GET]


+ Request
    + Headers


    + Body

            {"id":2}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":{"id":2,"username":"testAdmin1","bio":"balh","createdBy":{"id":1,"username":"System","name":"System"},"email":"testadmin1@nowhere.com","editedBy":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"class":"marketplace.Profile","editedDate":"2013-08-14T19:05:54Z","uuid":"3775c840-03c1-48c0-b01f-f195ce05a455","displayName":"Test Administrator 1","createdDate":"2013-05-28T17:52:27Z"}}


## optional_title [/public/profile]

### Returns the list of Profile objects [GET]


+ Request
    + Headers


    + Body

            {"max": 5, "offset":3,"order":"asc","sort":"username"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":2,"data":[{"id":1,"username":"System","bio":"","createdBy":{"id":null,"username":null,"name":null},"email":"","editedBy":{"id":null,"username":null,"name":null},"class":"marketplace.Profile","editedDate":"2013-05-28T16:39:04Z","uuid":"dfe4460c-0359-4f51-b20c-298896703d97","displayName":"System","createdDate":"2013-05-28T16:39:04Z"},{"id":2,"username":"testAdmin1","bio":"balh","createdBy":{"id":1,"username":"System","name":"System"},"email":"testadmin1@nowhere.com","editedBy":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"class":"marketplace.Profile","editedDate":"2013-08-14T19:05:54Z","uuid":"3775c840-03c1-48c0-b01f-f195ce05a455","displayName":"Test Administrator 1","createdDate":"2013-05-28T17:52:27Z"}]}


# Group Search

## optional_title [/public/search]

### Performs listing search and returns results as JSON list [GET]


String queryString = the string to query to find in listing text;
String controller = suggested controller;
String sort = name of the field to sort on which to sort results;
String order = the order of sorted results, "asc" or "desc" for ascending and descending, respectively;
int offset = the nth item of a return set at which to start;
int max = the max number of items to return;
boolean author = [true | false] to search the author;
boolean description = [true | false] to search the description;
boolean newsearch = [true | false];
boolean title = [true | false] to search the description;
String accessType = access role of the search user (e.g., Administrator | User | External);
String username = name of the user performing the search;
String[] categoryIDs = a list of category IDs;
String[] typeIDs = list of type IDs;
String[] stateIDs = a list of state IDs;
String[] statuses = a list of statuses;
boolean outside_only = [true | false] for outside only results;
String client = the client type (example: amp for an affiliated marketplace);
String callback = name of the callback to invoke on the returned data;

+ Request
    + Headers


    + Body

            {"queryString":"sample string","controller":"serviceItem","sort":"avgRate","order":"desc","offset":0,"max":6,"author":true,",description":true,"newsearch":new,"title":true,"accessType":Administrator,"username":testAdmin1,"categoryIDs":null,"typeIDs":null,"stateIDs":null,"statuses":null,"outside_only":true,"client":amp}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body


## optional_title [/search/getAffiliated/$id]

### Performs server-side search of the affiliated marketplace given AMP id and search parameters [GET]


int id = the id of the affiliated marketplace to search;
String queryString = the string to query to find in listing text;
String controller = suggested controller;
String sort = name of the field to sort on which to sort results;
String order = the order of sorted results, "asc" or "desc" for ascending and descending, respectively;
int offset = the nth item of a return set at which to start;
int max = the max number of items to return;
boolean author = [true | false] to search the author;
boolean description = [true | false] to search the description;
boolean newsearch = [true | false];
boolean title = [true | false] to search the description;
String accessType = access role of the search user (e.g., Administrator | User | External);
String username = name of the user performing the search;
String[] categoryIDs = a list of category IDs;
String[] typeIDs = list of type IDs;
String[] stateIDs = a list of state IDs;
String[] statuses = a list of statuses;
boolean outside_only = [true | false] for outside only results;
String client = the client type (example: amp for an affiliated marketplace);
String callback = name of the callback to invoke on the returned data;

+ Request
    + Headers


    + Body

            {"queryString":"sample string","controller":"serviceItem","sort":"avgRate","order":"desc","offset":0,"max":6,"author":true,",description":true,"newsearch":new,"title":true,"accessType":Administrator,"username":testAdmin1,"categoryIDs":null,"typeIDs":null,"stateIDs":null,"statuses":null,"outside_only":true,"client":amp}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body




# Group Theme

## optional_title [/themes]

### Returns the list of available themes [GET]


+ Request
    + Headers


    + Body

            {"page":1,"start":0,"limit":25,"sort":[{"property":"name","direction":"ASC"}]}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            [{"author":"Carbon Team","base_url":"themes/carbon.theme/","class":"ozone.marketplace.domain.ThemeDefinition","contact_email":"ozone-developers@googlegroups.com","contrast":"standard","created_date":"03/13/2013","css":"themes/carbon.theme/css/carbon.css","description":"Carbon Theme","display_name":"Carbon","font_size":12,"modified_date":"03/13/2013","name":"carbon","screenshots":[{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Landing Page","url":"themes/carbon.theme/images/preview/carbon-theme-6-shoppe.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Search Listings, Grid View","url":"themes/carbon.theme/images/preview/carbon-theme-3-search.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Search Listings, List View","url":"themes/carbon.theme/images/preview/carbon-theme-2-search.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Listing Details - Description","url":"themes/carbon.theme/images/preview/carbon-theme-5-listing.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Listing Details - Specifications","url":"themes/carbon.theme/images/preview/carbon-theme-1-listing.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Create a New Listing","url":"themes/carbon.theme/images/preview/carbon-theme-4-create.png"}],"thumb":"themes/carbon.theme/images/preview/carbon-theme-6-shoppe.png"},{"author":"Cobalt Team","base_url":"themes/cobalt.theme/","class":"ozone.marketplace.domain.ThemeDefinition","contact_email":"ozone-developers@googlegroups.com","contrast":"standard","created_date":"03/04/2013","css":"themes/cobalt.theme/css/cobalt.css","description":"Cobalt Theme","display_name":"Cobalt","font_size":12,"modified_date":"03/04/2013","name":"cobalt","screenshots":[{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Landing Page","url":"themes/cobalt.theme/images/preview/cobalt-theme-6-shoppe.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Search Listings, Grid View","url":"themes/cobalt.theme/images/preview/cobalt-theme-3-search.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Search Listings, List View","url":"themes/cobalt.theme/images/preview/cobalt-theme-2-search.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Listing Details - Description","url":"themes/cobalt.theme/images/preview/cobalt-theme-5-listing.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Listing Details - Specifications","url":"themes/cobalt.theme/images/preview/cobalt-theme-1-listing.png"}],"thumb":"themes/cobalt.theme/images/preview/cobalt-theme-6-shoppe.png"},{"author":"Gold Team","base_url":"themes/gold.theme/","class":"ozone.marketplace.domain.ThemeDefinition","contact_email":"ozone-developers@googlegroups.com","contrast":"standard","created_date":"09/01/12","css":"themes/gold.theme/css/gold.css","description":"This is the Gold theme ","display_name":"Gold","font_size":12,"modified_date":"09/14/2012","name":"gold","screenshots":[{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Landing Page","url":"themes/gold.theme/images/preview/gold-theme-6-shoppe.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Search Listings, Grid View","url":"themes/gold.theme/images/preview/gold-theme-3-search.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Search Listings, List View","url":"themes/gold.theme/images/preview/gold-theme-2-search.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Listing Details - Description","url":"themes/gold.theme/images/preview/gold-theme-5-listing.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Listing Details - Scorecard","url":"themes/gold.theme/images/preview/gold-theme-1-listing.png"},{"class":"ozone.marketplace.domain.ThemeScreenshotDefinition","description":"Create a New Listing","url":"themes/gold.theme/images/preview/gold-theme-4-create.png"}],"thumb":"themes/gold.theme/images/preview/gold-theme-6-shoppe.png"}]


## optional_title [/theme/selectTheme]

### Sets the current theme [POST]


+ Request
    + Headers


    + Body

            {"theme":"carbon"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true}


# Group ScoreCardItem

## optional_title [/scoreCardItem/getScoreCardData]

### Returns the list of score card items representing score card questions [GET]


+ Request
    + Headers


    + Body

            {"page":1,"start":0,"limit":25}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"ScoreCardList":[{"class":"marketplace.ScoreCardItem","id":1,"createdBy":{"class":"Profile","id":1},"createdDate":"2014-05-08T20:01:55Z","description":"In order to satisfy this criterion, the application must be supported with Tier 1 support so that users can access help for any arising issues.","editedBy":{"class":"Profile","id":1},"editedDate":"2014-05-08T20:01:55Z","image":"EMS","isStandardQuestion":true,"question":"Is Enterprise Management System (EMS) part of the support structure?","showOnListing":false},{"class":"marketplace.ScoreCardItem","id":2,"createdBy":{"class":"Profile","id":1},"createdDate":"2014-05-08T20:01:55Z","description":"In order to satisfy this criterion, the application must be running within the cloud structure. If an application is made up of multiple parts, all parts must be running within the cloud.","editedBy":{"class":"Profile","id":1},"editedDate":"2014-05-08T20:01:55Z","image":"CloudHost","isStandardQuestion":true,"question":"Is the application hosted within the infrastructure of the cloud?","showOnListing":false},{"class":"marketplace.ScoreCardItem","id":3,"createdBy":{"class":"Profile","id":1},"createdDate":"2014-05-08T20:01:55Z","description":"In order to satisfy this criterion, the application must be able to dynamically handle how many users are trying to access it. For instance, if a low number of users are accessing the App Component a small number of resources are used; if a large number of users are accessing the App Component, the App Component scales to take advantage of additional resources in the cloud.","editedBy":{"class":"Profile","id":1},"editedDate":"2014-05-08T20:01:55Z","image":"Scale","isStandardQuestion":true,"question":"Does the application elastically scale?","showOnListing":false},{"class":"marketplace.ScoreCardItem","id":4,"createdBy":{"class":"Profile","id":1},"createdDate":"2014-05-08T20:01:55Z","description":"In order to satisfy this criterion, the system should operate without constraining the user to interact with it.","editedBy":{"class":"Profile","id":1},"editedDate":"2014-05-08T20:01:55Z","image":"LicenseFree","isStandardQuestion":true,"question":"Does this system operate without license constraints?","showOnListing":false},{"class":"marketplace.ScoreCardItem","id":5,"createdBy":{"class":"Profile","id":1},"createdDate":"2014-05-08T20:01:55Z","description":"In order to satisfy this criterion, the application's data must be within cloud storage. If an application utilizes multiple data resources, all parts must utilize cloud storage.","editedBy":{"class":"Profile","id":1},"editedDate":"2014-05-08T20:01:55Z","image":"CloudStorage","isStandardQuestion":true,"question":"Is the application data utilizing cloud storage?","showOnListing":false},{"class":"marketplace.ScoreCardItem","id":6,"createdBy":{"class":"Profile","id":1},"createdDate":"2014-05-08T20:01:55Z","description":"In order to satisfy this criterion, the application must be accessible via an URL/URI that can be launched by a web browser.","editedBy":{"class":"Profile","id":1},"editedDate":"2014-05-08T20:01:55Z","image":"Browser","isStandardQuestion":true,"question":"Is the application accessible through a web browser?","showOnListing":false}]}


## optional_title [/scoreCardItem/]

### Saves the score card item passed in the message parameters [POST]


+ Request
    + Headers


    + Body

            {"question":"Question 2","description":"Description 2","image":"","weight":0,"showOnListing":true}

+ Response 200
    + Headers


    + Body


## optional_title [/scoreCardItem/$id]

### Saves the score card item passed in the message parameters [PUT]


+ Request
    + Headers


    + Body

            {"id":7,"createdBy":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"editedBy":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"description":"Description","editedDate":"2014-06-11T00:29:57Z","image":"","showOnListing":true,"question":"Question","createdDate":"2014-06-11T00:29:57Z","weight":0}

+ Response 200
    + Headers


    + Body


## optional_title [/scoreCardItem/$id]

### Delete given score card item [DELETE]


+ Request
    + Headers


    + Body


+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body
            {"success":true}

# Group RejectionJustification

## optional_title [/rejectionJustification/getListAsExt]

### Returns the list of rejection justifications [GET]


+ Request
    + Headers


    + Body

            {"page":1,"start":0,"limit":25}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"totalCount":6,"data":[{"id":3,"title":"Data Content","description":"Data Content"},{"id":5,"title":"Lack of Documentation","description":"Lack of Documentation"},{"id":4,"title":"Not Operational","description":"Not Operational"},{"id":6,"title":"Rejection Justification","description":"Description"},{"id":2,"title":"Security Guideline","description":"Security Guideline"},{"id":1,"title":"Sustainment","description":"Sustainment"}]}


## optional_title [/public/rejectionJustification/$id]

### Returns the specified RejectionJustification object [GET]


+ Request
    + Headers


    + Body

            {"id":1}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":{"id":1,"title":"Sustainment","createdBy":{"id":1,"username":"System","name":"System"},"description":"Sustainment","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-08-29T17:40:23Z","createdDate":"2013-08-29T17:40:23Z"}}


## optional_title [/public/rejectionJustification]

### Returns all RejectionJustification Objects [GET]


+ Request
    + Headers


    + Body

            {"max":2,"order":"asc","offset":0}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":5,"data":[{"id":3,"title":"Data Content","createdBy":{"id":1,"username":"System","name":"System"},"description":"Data Content","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-05-28T16:39:05Z","createdDate":"2013-05-28T16:39:05Z"},{"id":5,"title":"Lack of Documentation","createdBy":{"id":1,"username":"System","name":"System"},"description":"Lack of Documentation","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-05-28T16:39:05Z","createdDate":"2013-05-28T16:39:05Z"}]}


# Group Tag

## optional_title [/api/tag{?max,offset}]

### Returns all of the Tag objects in the system [GET]
+ Parameters
    + max (optional, number) ... The maximum number of Tags to return
    + offset (optional, number) ... The offset into the full list of Tags to start at

+ Response 200
    + Headers

    Content-Type:application/json;charset=utf-8

    + Body
    [
        {
            "id": 1,
            "title": "MAP",
            "itemCount": 2
        },
        {
            "id": 2,
            "title": "BOOK",
            "itemCount": 5
        }
    ]


## optional_title [/api/tag/search]

### Returns the Tags by title matching the title param [GET]

+ Parameters
    + title (string) ... The name of the tag to search on, for example "MA" which would match "MAP" or "MATH"

+ Response 200
    + Headers

    Content-Type:application/json;charset=utf-8

    + Body
    [
        {
            "id": 1,
            "title": "MAP",
            "itemCount": 2
        }
    ]


## optional_title [/api/tag/{tagId}]

### Removes the Tag from all service items and deletes the tag if executed by admin or owner [DELETE]
+ Response 204


# Group Text

## optional_title [/public/text/$id]

### Returns the specified Text object [GET]


+ Request
    + Headers


    + Body

            {"id":1}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":[{"id":1,"readOnly":false,"createdBy":{"id":1,"username":"System","name":"System"},"editedBy":{"id":1,"username":"System","name":"System"},"name":"About","value":"The Apps Mall allows visitors to discover and explore business, mission, and convenience applications and enables user-configurable visualizations of available content.","editedDate":"2013-05-28T16:39:06Z","createdDate":"2013-05-28T16:39:06Z"}]}


## optional_title [/public/text]

### Returns all Text Objects [GET]


+ Request
    + Headers


    + Body

            {"max":5,"offset":0,"order":"asc"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":[{"id":1,"readOnly":false,"createdBy":{"id":2,"username":"System","name":"System"},"editedBy":{"id":2,"username":"System","name":"System"},"name":"About","value":"The Apps Mall allows visitors to discover and explore business, mission, and convenience applications and enables user-configurable visualizations of available content.","editedDate":"2013-08-08T18:34:58Z","createdDate":"2013-08-08T18:34:58Z"}]}


# Group ItemComment

## optional_title [/itemComment/getUserComments]

### Returns a JSON structure containing the user's comments for all ServiceItems [GET]


+ Request
    + Headers


    + Body

            {"start":0,"limit":20,"page":1,"sort":"date","dir":"desc"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"totalCount":1,"rows":[{"id":0,"itemId":0,"date":"08/16/2013 12:07 PM EDT","userRate":null,"name":"Test","comment":"blah"}]}


## optional_title [/itemComment/saveItemComment]

### Creates or updates a feedback item for the specified listing. [POST]


+ Request
    + Headers


    + Body

            {"newUserRating":0,"currUserRating":0,"commentTextInput":"comment","serviceItemId":7}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true}


## optional_title [/itemComment/edit]

### Edits the feedback item for the specified listing. [POST]


+ Request
    + Headers


    + Body

            {"newUserRating":3, "currUserRating":3, "commentTextInput":"new comment","serviceItemId":1,"id":1}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"data":{"id":1,"userId":1,"username":"testAdmin1","displayName":"Test Administrator 1","text":"new comment","date":"09/09/2013 10:40 AM EDT","userRate":3,"serviceItemRateStats":{"avgRate":3.5,"totalRate2":0,"totalRate3":1,"totalRate1":0,"totalRate5":0,"totalRate4":1}}}


## optional_title [/itemComment/deleteItemComment]

### Deletes the specified comment. [POST]


int itemCommentId = comment ID;

+ Request
    + Headers


    + Body

            {"itemCommentId":2}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"result":"success"}


## optional_title [/itemComment/commentsByServiceItem/$id]

### Lists comments for the given service item. [GET]


int id = service item ID;
String author = filter by the author's username;
int limit = maximum number of objects to return;
int start = offset in to the result list (for pagination);
String sort = field on which to sort the result;
String dir = 'asc' or 'desc' to control the sort order;

+ Request
    + Headers


    + Body

            {"id":2,"author":"testAdmin1"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"totalCount":1,"rows":[{"id":1,"userId":100001,"username":"testAdmin1","displayName":"Test Administrator 1","text":"Unter den Blinden ist der Einäugige König.","date":"09/06/2013 09:58 AM EDT","userRate":5,"serviceItemRateStats":{"avgRate":5,"totalRate2":0,"totalRate3":0,"totalRate1":0,"totalRate5":1,"totalRate4":0}}]}


# Group Images

## optional_title [/images/get/$id]

### Returns images hosted by the store by id. [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:image/<image type>

    + Body




## optional_title [/public/images/get/$id]

### Returns images hosted by the store by id. [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:image/<image type>

    + Body




# Group ServiceItem

## optional_title [/public/serviceItem/$id]

### Returns JSON representation of a ServiceItem objects given its id. [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":{"imageLargeUrl":"http://sample/icon/file.png","totalComments":0,"launchUrl":"http://sample/icon/file.png","state":{"id":1,"title":"Active","uuid":"b0c62bd4-9cda-4767-915d-e805cb2456a1"},"totalRate5":0,"agencyIcon":"/marketplace/themes/common/images/agency/agencyDefault.png","totalRate4":0,"requirements":null,"ozoneAware":true,"id":6,"author":"testAdmin1","organization":"Test Admin Organization","avgRate":0,"lastActivity":{"activityDate":"2013-08-15T20:44:03Z"},"title":"Sample Component","releaseDate":null,"recommendedLayouts":[],"totalRate2":0,"dependencies":null,"totalRate3":0,"description":"Sample Description","approvalStatus":"In Progress","totalVotes":0,"docUrl":null,"totalRate1":0,"types":{"id":2,"title":"App Component","description":"app component","hasLaunchUrl":true,"image":{"id":null,"imageSize":null,"contentType":null,"url":"null"},"uuid":"f6623a5a-00a0-4b89-992a-c61c79169bed","hasIcons":true,"ozoneAware":true,"isPermanent":true},"screenshots":[{"id":85,"largeImageUrl":"https://localhost/1.png","smallImageUrl":"https://localhost/1.png","ordinal":1}],"techPoc":"testAdmin1","customFields":[{"customFieldDefinitionUuid":"caceace3-cc8b-4171-99f8-1884c2c31246","id":6,"fieldType":"DROP_DOWN","fieldValue":{},"name":"domain","customFieldDefinitionId":1,"value":"","label":"Sample"}],"isPublished":true,"agency":"Sample Company","class":"marketplace.ServiceItem","versionName":"1","approvedDate":null,"isEnabled":true,"owfProperties":{"id":5,"visibleInLaunch":true,"universalName":null,"height":650,"intents":[],"singleton":false,"background":false,"mobileReady":false,"owfWidgetType":"standard","width":1050,"stackContext":"","stackDescriptor":"","descriptorUrl":null},"installUrl":null,"isOutside":null,"intents":[],"owner":{"id":7,"username":"testAdmin1","name":"Test Admin a"},"categories":[],"uuid":"43b8d010-1cd4-41a4-a253-bcdb3c4f952d","imageSmallUrl":"http://sample/icon/file.png"}}


## optional_title [/public/serviceItem/]

### Returns JSON list representing all ServiceItem object in the system matching the search criteria. [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":[{"imageLargeUrl":"http://sample/icon/file.png","totalComments":0,"launchUrl":"http://sample/icon/file.png","state":{"id":1,"title":"Active","uuid":"b0c62bd4-9cda-4767-915d-e805cb2456a1"},"totalRate5":0,"agencyIcon":"/marketplace/themes/common/images/agency/agencyDefault.png","totalRate4":0,"requirements":null,"ozoneAware":true,"id":6,"author":"testAdmin1","organization":"Test Admin Organization","avgRate":0,"lastActivity":{"activityDate":"2013-08-15T20:44:03Z"},"title":"Sample Component","releaseDate":null,"recommendedLayouts":[],"totalRate2":0,"dependencies":null,"totalRate3":0,"description":"Sample Description","approvalStatus":"In Progress","totalVotes":0,"docUrl":null,"totalRate1":0,"types":{"id":2,"title":"App Component","description":"app component","hasLaunchUrl":true,"image":{"id":null,"imageSize":null,"contentType":null,"url":"null"},"uuid":"f6623a5a-00a0-4b89-992a-c61c79169bed","hasIcons":true,"ozoneAware":true,"isPermanent":true},"screenshots":[{"id":85,"largeImageUrl":"https://localhost/1.png","smallImageUrl":"https://localhost/1.png","ordinal":1}],"techPoc":"testAdmin1","customFields":[{"customFieldDefinitionUuid":"caceace3-cc8b-4171-99f8-1884c2c31246","id":6,"fieldType":"DROP_DOWN","fieldValue":{},"name":"domain","customFieldDefinitionId":1,"value":"","label":"Sample"}],"isPublished":true,"agency":"Sample Company","class":"marketplace.ServiceItem","versionName":"1","approvedDate":null,"isEnabled":true,"owfProperties":{"id":5,"visibleInLaunch":true,"universalName":null,"height":650,"intents":[],"singleton":false,"background":false,"mobileReady":false,"owfWidgetType":"standard","width":1050,"stackContext":"","stackDescriptor":"","descriptorUrl":null},"installUrl":null,"isOutside":null,"intents":[],"owner":{"id":7,"username":"testAdmin1","name":"Test Admin a"},"categories":[],"uuid":"43b8d010-1cd4-41a4-a253-bcdb3c4f952d","imageSmallUrl":"http://sample/icon/file.png"}]}


## optional_title [/public/serviceItem/getServiceItemsAsJSON]

### Returns JSON list representing all ServiceItem object in the system matching the search criteria. [GET]

URL Params:
[max] maximum number of items to return
[offset] the nth item of a return set at which to start[order] order of a sorted set: 'asc' for ascending, 'desc' for descending
[sort] the service item field by which to sort
(attribute)  The name and value of service item attribute by which to filter the results (e.g., title='Sample Title')

+ Request
    + Headers


    + Body

            {"max": 5, "offset":3,"order":"asc","sort":"title"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":[{"imageLargeUrl":"http://sample/icon/file.png","totalComments":0,"scoreCard.score":0,"launchUrl":"http://sample/icon/file.png","state":{"id":1,"title":"Active","uuid":"b0c62bd4-9cda-4767-915d-e805cb2456a1"},"totalRate5":0,"agencyIcon":"/marketplace/themes/common/images/agency/agencyDefault.png","totalRate4":0,"requirements":null,"ozoneAware":true,"id":6,"author":"testAdmin1","organization":"Test Admin Organization","avgRate":0,"lastActivity":{"activityDate":"2013-08-15T20:44:03Z"},"title":"Sample Component","releaseDate":null,"recommendedLayouts":[],"totalRate2":0,"dependencies":null,"totalRate3":0,"description":"Sample Description","approvalStatus":"In Progress","totalVotes":0,"docUrl":null,"totalRate1":0,"types":{"id":2,"title":"App Component","description":"app component","hasLaunchUrl":true,"image":{"id":null,"imageSize":null,"contentType":null,"url":"null"},"uuid":"f6623a5a-00a0-4b89-992a-c61c79169bed","hasIcons":true,"ozoneAware":true,"isPermanent":true},"screenshots":[{"id":85,"largeImageUrl":"https://localhost/1.png","smallImageUrl":"https://localhost/1.png","ordinal":1}],"techPoc":"testAdmin1","customFields":[{"customFieldDefinitionUuid":"caceace3-cc8b-4171-99f8-1884c2c31246","id":6,"fieldType":"DROP_DOWN","fieldValue":{},"name":"domain","customFieldDefinitionId":1,"value":"","label":"Sample"}],"isPublished":true,"agency":"Sample Company","class":"marketplace.ServiceItem","versionName":"1","approvedDate":null,"isEnabled":true,"owfProperties":{"id":5,"visibleInLaunch":true,"universalName":null,"height":650,"intents":[],"singleton":false,"background":false,"mobileReady":false,"owfWidgetType":"standard","width":1050,"stackContext":"","stackDescriptor":"","descriptorUrl":null},"installUrl":null,"isOutside":null,"intents":[],"owner":{"id":7,"username":"testAdmin1","name":"Test Admin a"},"categories":[],"uuid":"43b8d010-1cd4-41a4-a253-bcdb3c4f952d","imageSmallUrl":"http://sample/icon/file.png"}]}


## optional_title [/public/serviceItem/getOwfCompatibleItems]

### Returns JSON list representing all OWF compatible ServiceItem object in the system matching the search criteria. [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=UTF-8

    + Body

            {"total":1,"data":[{"imageLargeUrl":"http://sample/icon/file.png","totalComments":0,"launchUrl":"http://sample/icon/file.png","state":{"id":1,"title":"Active","uuid":"b0c62bd4-9cda-4767-915d-e805cb2456a1"},"totalRate5":0,"agencyIcon":"/marketplace/themes/common/images/agency/agencyDefault.png","totalRate4":0,"requirements":null,"ozoneAware":true,"id":6,"author":"testAdmin1","organization":"Test Admin Organization","avgRate":0,"lastActivity":{"activityDate":"2013-08-15T20:47:39Z"},"title":"Sample Component","releaseDate":null,"recommendedLayouts":[],"totalRate2":0,"dependencies":null,"totalRate3":0,"description":"Sample Description","approvalStatus":"Approved","totalVotes":0,"docUrl":null,"totalRate1":0,"types":{"id":2,"title":"App Component","description":"app component","hasLaunchUrl":true,"image":{"id":null,"imageSize":null,"contentType":null,"url":"null/themes/common/images/default_types_widget_icon.png"},"uuid":"f6623a5a-00a0-4b89-992a-c61c79169bed","hasIcons":true,"ozoneAware":true,"isPermanent":true},"screenshots":[{"id":85,"largeImageUrl":"https://localhost/1.png","smallImageUrl":"https://localhost/1.png","ordinal":1}],"techPoc":"testAdmin1","customFields":[{"customFieldDefinitionUuid":"caceace3-cc8b-4171-99f8-1884c2c31246","id":6,"fieldType":"DROP_DOWN","fieldValue":{},"name":"domain","customFieldDefinitionId":1,"value":"","label":"SampleLabel"}],"isPublished":true,"agency":"Sample Company","class":"marketplace.ServiceItem","versionName":"1","approvedDate":"2013-08-15T20:47:39Z","isEnabled":true,"owfProperties":{"id":5,"visibleInLaunch":true,"universalName":null,"height":650,"intents":[],"singleton":false,"background":false,"mobileReady":false,"owfWidgetType":"standard","width":1050,"stackContext":"","stackDescriptor":"","descriptorUrl":null},"installUrl":null,"isOutside":true,"intents":[],"owner":{"id":7,"username":"testAdmin1","name":"Test Admin 1"},"categories":[],"uuid":"43b8d010-1cd4-41a4-a253-bcdb3c4f952d","imageSmallUrl":"http://sample/icon/file.png"}]}


## optional_title [/serviceItem/$authorId/types]

### Returns the types of service items held by a user [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=UTF-8

    + Body

            {"success":true,"results":[{"type":"App Component","id":2,"total":2,"imageUrl":"/marketplace/themes/common/images/default_types_widget_icon.png"},{"type":"Sample Type","id":4,"total":1,"imageUrl":"/marketplace/themes/common/images/default_serviceitem_icon.png"}]}


## optional_title [/serviceItem/byAuthorandType/$authorId/$typeId]

### Returns JSON list representing ServiceItem objects matching the search criteria [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"totalCount":2,"records":[{"id":1,"title":"aaa","avgRate":0,"approved":true,"createdDate":"08/13/2013 01:20 PM EDT","dependencies":null,"description":null,"organization":"Test Admin Organization","requirements":null,"categories":"","state":"Active","approvalStatus":"Approved","isHidden":false},{"id":6,"title":"Sample Component","avgRate":0,"approved":true,"createdDate":"08/15/2013 04:44 PM EDT","dependencies":null,"description":"Sample Description","organization":"Test Admin Organization","requirements":null,"categories":"","state":"Active","approvalStatus":"Approved","isHidden":false}]}


## optional_title [/serviceItem/getActiveListings]

### Returns current user's approved listings. [GET]


int limit = maximum number of objects to return;
int start = offset in to the result list (for pagination);
String sort = field on which to sort the result;
String dir = 'asc' or 'desc' to control the sort order;

+ Request
    + Headers


    + Body



            {"limit":1,"start":0}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"totalCount":8,"data":[{"id":9,"title":"Test App Component","state":"Active","types":"App Component","lastActivity":"Modified on 08/30/2013 08:37 AM EDT","isHidden":0}]}


## optional_title [/serviceItem/getInactiveListings]

### Returns current user's listings in "in progress", "pending" or "rejected" status. [GET]


int limit = maximum number of objects to return;
int start = offset in to the result list (for pagination);
String sort = field on which to sort the result;
String dir = 'asc' or 'desc' to control the sort order;

+ Request
    + Headers


    + Body

            {"limit":1,"start":0}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"totalCount":1,"data":[{"id":20,"title":"Inactive App Component","approvalStatus":"In Progress","types":"App Component","lastActivity":"Created on 09/05/2013 11:37 AM EDT","isHidden":0}]}


## optional_title [/api/serviceItem/activity]

### Returns activies for all listings in Store. (Admin only) [GET]


int max = maximum number of objects to return;
int offset = offset in to the result list (for pagination);

+ Request
    + Body

            {"max":24,"offset":0}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":46,"data":[{"author":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"details":null,"activityDate":"2014-01-27T20:57:33Z","action":{"description":"Modified","name":"MODIFIED"},"changeDetails":[{"newValue":"[]","oldValue":"[]","fieldName":"intents"},{"newValue":"dependencies go here...","oldValue":null,"fieldName":"dependencies"},{"newValue":"http://example.com/image/large.jpg","oldValue":"http://","fieldName":"imageLargeUrl"},{"newValue":"http://example.com/image/small.jpg","oldValue":"http://","fieldName":"imageSmallUrl"},{"newValue":"http://example.com/launch","oldValue":"http://","fieldName":"launchUrl"},{"newValue":"requirements go here...","oldValue":null,"fieldName":"requirements"},{"newValue":"[testAdmin1]","oldValue":null,"fieldName":"techPocs"}],"serviceItem":{"id":15,"title":"Example listing"}},{"author":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"details":null,"activityDate":"2014-01-27T20:56:59Z","action":{"description":"Approved","name":"APPROVED"},"changeDetails":[],"serviceItem":{"id":15,"title":"Example listing"}},{"author":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"details":null,"activityDate":"2014-01-27T20:56:58Z","action":{"description":"Enabled","name":"ENABLED"},"changeDetails":[],"serviceItem":{"id":15,"title":"Example listing"}},{"author":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"details":null,"activityDate":"2014-01-27T20:56:56Z","action":{"description":"Submitted","name":"SUBMITTED"},"changeDetails":[],"serviceItem":{"id":15,"title":"Example listing"}},{"author":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"details":null,"activityDate":"2014-01-27T20:56:54Z","action":{"description":"Disabled","name":"DISABLED"},"changeDetails":[],"serviceItem":{"id":15,"title":"Example listing"}},{"author":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"details":null,"activityDate":"2014-01-27T20:56:53Z","action":{"description":"Inside","name":"INSIDE"},"changeDetails":[],"serviceItem":{"id":15,"title":"Example listing"}},{"author":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"details":null,"activityDate":"2014-01-27T20:56:48Z","action":{"description":"Rejected","name":"REJECTED"},"changeDetails":[],"serviceItem":{"id":15,"title":"Example listing"}},{"author":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"details":null,"activityDate":"2014-01-27T20:56:30Z","action":{"description":"Outside","name":"OUTSIDE"},"changeDetails":[],"serviceItem":{"id":15,"title":"Example listing"}},{"author":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"details":null,"activityDate":"2014-01-27T20:56:28Z","action":{"description":"Submitted","name":"SUBMITTED"},"changeDetails":[],"serviceItem":{"id":15,"title":"Example listing"}},{"author":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"details":null,"activityDate":"2014-01-27T20:56:24Z","action":{"description":"Created","name":"CREATED"},"changeDetails":[],"serviceItem":{"id":15,"title":"Example listing"}}]}


# Group ExtServiceItem

## optional_title [/public/extServiceItem/disable/$id]

### Disables service items if external service item creation is enabled on the server. [GET/POST]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:text/html;charset=utf-8

    + Body




## optional_title [/public/extServiceItem/enable/$id]

### Enables service items if external service item creation is enabled on the server. [GET/POST]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:text/html;charset=utf-8

    + Body




## optional_title [/public/extServiceItem]

### Returns JSON list representing all ServiceItem object in the system matching the search criteria. [GET]

URL Params:
[max] maximum number of items to return
[offset] the nth item of a return set at which to start[order] order of a sorted set: 'asc' for ascending, 'desc' for descending
[sort] the service item field by which to sort
(attribute)  The name and value of service item attribute by which to filter the results (e.g., title='Sample Title')

+ Request
    + Headers


    + Body

            {"max": 5, "offset":3,"order":"asc","sort":"title"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":[{"imageLargeUrl":"http://sample/icon/file.png","totalComments":0,"launchUrl":"http://sample/icon/file.png","state":{"id":1,"title":"Active","uuid":"b0c62bd4-9cda-4767-915d-e805cb2456a1"},"totalRate5":0,"agencyIcon":"/marketplace/themes/common/images/agency/agencyDefault.png","totalRate4":0,"screenshot2Url":null,"requirements":null,"ozoneAware":true,"id":6,"author":"testAdmin1","organization":"Test Admin Organization","avgRate":0,"lastActivity":{"activityDate":"2013-08-15T20:44:03Z"},"title":"Sample Component","releaseDate":null,"recommendedLayouts":[],"totalRate2":0,"dependencies":null,"totalRate3":0,"description":"Sample Description","approvalStatus":"In Progress","totalVotes":0,"docUrl":null,"totalRate1":0,"types":{"id":2,"title":"App Component","description":"app component","hasLaunchUrl":true,"image":{"id":null,"imageSize":null,"contentType":null,"url":"null"},"uuid":"f6623a5a-00a0-4b89-992a-c61c79169bed","hasIcons":true,"ozoneAware":true,"isPermanent":true},"screenshot1Url":null,"techPoc":"testAdmin1","customFields":[{"customFieldDefinitionUuid":"caceace3-cc8b-4171-99f8-1884c2c31246","id":6,"fieldType":"DROP_DOWN","fieldValue":{},"name":"domain","customFieldDefinitionId":1,"value":"","label":"Sample"}],"isPublished":true,"agency":"Sample Company","class":"marketplace.ServiceItem","versionName":"1","approvedDate":null,"isEnabled":true,"owfProperties":{"id":5,"visibleInLaunch":true,"universalName":null,"height":650,"intents":[],"singleton":false,"background":false,"mobileReady":false,"owfWidgetType":"standard","width":1050,"stackContext":"","stackDescriptor":"","descriptorUrl":null},"installUrl":null,"isOutside":null,"intents":[],"owner":{"id":7,"username":"testAdmin1","name":"Test Admin a"},"categories":[],"uuid":"43b8d010-1cd4-41a4-a253-bcdb3c4f952d","imageSmallUrl":"http://sample/icon/file.png"}]}


### Allows users with the external admin role to create service items by passing in the JSON represenation of an item. [POST]


+ Request
    + Headers


    + Body

            {"total":1,"serviceItem":{"imageLargeUrl":"http://example.host/Notes3.png","totalComments":0,"launchUrl":"http://example.host/Notes3.png","state":{"id":19,"title":"Active","uuid":"505cb2f2-9950-497e-9926-e577251439af"},"totalRate5":0,"totalRate4":0,"screenshot2Url":null,"requirements":null,"externalId":"99999","ozoneAware":true,"author":"testAdmin1","systemUri":"external:system","organization":null,"avgRate":0,"lastActivity":{"activityDate":"2012-08-21T12:22:29Z"},"title":"Sample External Service Item","releaseDate":"2010-10-27T04:00:00Z","recommendedLayouts":["Desktop","Tabbed","Portal","Accordion"],"totalRate2":0,"externalEditUrl":"http://example.host","dependencies":null,"totalRate3":0,"description":"An example description.","totalVotes":0,"totalRate1":0,"docUrl":null,"types":{"id":7,"title":"App Component","description":"app component","hasLaunchUrl":true,"image":{"id":null,"imageSize":null,"contentType":null,"url":"null/themes/common/images/default_types_widget_icon.png"},"uuid":"382b61e1-094c-460f-9f1e-c8a5b033e9dc","hasIcons":true,"ozoneAware":true,"isPermanent":true},"screenshot1Url":null,"techPoc":"tsmith","externalViewUrl":"http://example.hot","customFields":[],"isPublished":true,"class":"marketplace.ExtServiceItem","versionName":"2","approvedDate":"2012-08-21T12:22:29Z","isEnabled":true,"owfProperties":{"id":13,"visibleInLaunch":true,"singleton":true,"background":true},"installUrl":null,"owner":{"id":15,"username":"testAdmin1","name":"Test Admin 1"},"categories":[],"imageSmallUrl":"http://www.freeiconsweb.com/Icons-show/IconsExtra/Notes3.png"}}

+ Response 201
    + Headers

            Content-Type:application/json;charset=UTF-8

    + Body

            {"id":8}


## optional_title [/public/extServiceItem/$id]

### Returns an externally created service item by id. [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=UTF-8

    + Body

            {"total":1,"data":{"totalRate5":0,"totalRate4":0,"ozoneAware":true,"avgRate":0,"totalRate2":0,"recommendedLayouts":["Desktop","Accordion","Tabbed","Portal"],"totalRate3":0,"description":"An example description.","approvalStatus":"Pending","totalRate1":0,"techPoc":"tsmith","class":"marketplace.ExtServiceItem","versionName":"2","isEnabled":true,"installUrl":null,"isOutside":null,"owner":{"id":7,"username":"testAdmin1","name":"Test Admin 1"},"uuid":"afa834b0-5c08-4274-85d4-3d516402fd8f","imageLargeUrl":"http://www.freeiconsweb.com/Icons-show/IconsExtra/Notes3.png","totalComments":0,"launchUrl":"http://example.org/http://www.freeiconsweb.com/Icons-show/IconsExtra/Notes3.png","state":null,"screenshot2Url":null,"agencyIcon":null,"requirements":null,"externalId":"99999","id":8,"author":"testAdmin1","title":"Sample External Service Item","lastActivity":{"activityDate":"2013-08-16T18:57:02Z"},"organization":null,"systemUri":"external:system","releaseDate":"2010-10-27T04:00:00Z","dependencies":null,"externalEditUrl":"http://www.dumbo15.com/edit","totalVotes":0,"docUrl":null,"types":{"id":2,"title":"App Component","description":"app component","hasLaunchUrl":true,"image":{"id":null,"imageSize":null,"contentType":null,"url":"null/themes/common/images/default_types_widget_icon.png"},"uuid":"f6623a5a-00a0-4b89-992a-c61c79169bed","hasIcons":true,"ozoneAware":true,"isPermanent":true},"screenshot1Url":null,"externalViewUrl":"http://www.dumbo15.com","customFields":[],"isPublished":null,"agency":null,"approvedDate":null,"owfProperties":{"id":7,"visibleInLaunch":true,"universalName":null,"height":650,"intents":[],"singleton":true,"background":true,"mobileReady":false,"owfWidgetType":"standard","width":1050,"stackContext":null,"stackDescriptor":null,"descriptorUrl":null},"intents":[],"categories":[],"imageSmallUrl":"http://www.freeiconsweb.com/Icons-show/IconsExtra/Notes3.png"}}


### Update an externally created service item by id. [PUT]


+ Request
    + Headers


    + Body

            {"serviceItem":{"techPoc":"maytag man","externalViewUrl":"http://www.example.sample.com"}}

+ Response 200
    + Headers

            Content-Type:text/html;charset=utf-8

    + Body

            "Service Item successfully updated"


# Group ExtProfile

## optional_title [/public/extProfile]

### Returns externally created profiles [GET]


+ Request
    + Headers


    + Body

            {"max": 5, "offset":3,"order":"asc","sort":"title"}

+ Response 200
    + Headers

            Content-Type:text/html;charset=utf-8

    + Body

            {"total":1,"data":{"externalViewUrl":"http://sample.url","editedDate":"2013-08-16T21:04:55Z","class":"marketplace.ExtProfile","externalId":"testId1234","id":3,"username":"sprofile","systemUri":"external:system","bio":"Wrote a framework","createdBy":{"id":1,"username":"externAdmin1","name":"Test Admin 1"},"email":"sample@address.com","externalEditUrl":"http://sample.url","editedBy":{"id":1,"username":"externAdmin1","name":"Test Admin 1"},"uuid":"d668155a-4b0b-4d3a-9a8f-206347679812","displayName":"Sample Profile","createdDate":"2013-08-16T21:04:55Z"}}


### Creates an external profile; requires the external admin role for the user associated with this request [POST]


+ Request
    + Headers


    + Body

            {"profile":{"username":"sprofile","displayName":"Sample Profile","email":"sample@address.com","bio":"Wrote a framework.","systemUri":"external:system","externalId":"testId1234","externalViewUrl":"http://sample.url","externalEditUrl":"http://sample.url"}}

+ Response 201
    + Headers

            Content-Type:text/html;charset=utf-8

    + Body

            {"id":3}


## optional_title [/public/extProfile/$id]

### Returns a specific externally created profile. [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:text/html;charset=utf-8

    + Body

            {"total":1,"data":{"externalViewUrl":"http://sample.url","editedDate":"2013-08-16T21:04:55Z","class":"marketplace.ExtProfile","externalId":"testId1234","id":3,"username":"sprofile","systemUri":"external:system","bio":"Wrote a framework","createdBy":{"id":1,"username":"externAdmin1","name":"Test Admin 1"},"email":"sample@address.com","externalEditUrl":"http://sample.url","editedBy":{"id":1,"username":"externAdmin1","name":"Test Admin 1"},"uuid":"d668155a-4b0b-4d3a-9a8f-206347679812","displayName":"Sample Profile","createdDate":"2013-08-16T21:04:55Z"}}


### Updates an external profile; requires the external admin role for the user associated with this request [PUT]


+ Request
    + Headers


    + Body

            {"profile":{"username":"sprofile","displayName":"Sample Profile","email":"sample@address.com","bio":"Wrote a framework. Updated their bio.","systemUri":"external:system","externalId":"testId1234","externalViewUrl":"http://sample.url","externalEditUrl":"http://sample.url"}}

+ Response 200
    + Headers

            Content-Type:text/html;charset=utf-8

    + Body

            "Profile successfully updated"


# Group ExternalAccess

## optional_title [/public/outsideSearch]

### Returns service items which are set to outside. [GET]


String client = "amp" for JSON array of abbreviated affiliated marketplace service item objects, else JSON array of service item objects;

+ Request
    + Headers


    + Body

            {"max":1}

+ Response 200
    + Headers

            Content-Type:application/json;charset=UTF-8

    + Body

            {"total":10,"data":[{"imageLargeUrl":"http://","totalComments":1,"launchUrl":"http://","validLaunchUrl":false,"state":{"id":1,"title":"Active","uuid":"e78bf52f-7027-471f-a991-248963f21775"},"totalRate5":1,"agencyIcon":"/marketplace/themes/common/images/agency/default_store_icon.png","totalRate4":0,"requirements":null,"ozoneAware":true,"id":6,"organization":"DEFAULT_STORE_NAME","avgRate":5,"lastActivity":{"activityDate":"2013-08-29T21:30:28Z"},"title":"fdsdfsdfsdf","releaseDate":null,"recommendedLayouts":[],"totalRate2":0,"dependencies":null,"totalRate3":0,"description":null,"approvalStatus":"Approved","totalRate1":0,"totalVotes":1,"techPocs":["testAdmin1"],"types":{"id":8,"title":"App Component","description":"app component","hasLaunchUrl":true,"image":{"id":null,"imageSize":null,"contentType":null,"url":"null/themes/common/images/default_types_widget_icon.png"},"uuid":"e01cf8d1-3ed2-461a-bb51-59f4e9b51adf","hasIcons":true,"ozoneAware":true,"isPermanent":true},"screenshots":[{"id":85,"largeImageUrl":"https://localhost/1.png","smallImageUrl":"https://localhost/1.png","ordinal":1}],"docUrls":[],"customFields":[{"customFieldDefinitionUuid":"2c82dbf9-ed3a-46a7-ab17-2c1a6c05a1f1","id":9,"fieldType":"CHECK_BOX","name":"AccessibleFlag","customFieldDefinitionId":14,"value":true,"label":"Accessible"},{"customFieldDefinitionUuid":"fcbd594e-a34c-4cba-9f90-197e51ba834a","id":10,"fieldType":"DROP_DOWN","fieldValue":{},"name":"color","customFieldDefinitionId":5,"value":"","label":"Color"}],"isPublished":true,"agency":"","class":"marketplace.ServiceItem","versionName":null,"approvedDate":"2013-08-29T21:30:09Z","isEnabled":true,"owfProperties":{"id":6,"visibleInLaunch":true,"universalName":null,"height":650,"intents":[],"singleton":false,"background":false,"mobileReady":false,"owfWidgetType":"standard","width":1050,"stackContext":null,"stackDescriptor":null,"descriptorUrl":null},"installUrl":null,"isOutside":true,"intents":[],"categories":[],"uuid":"dce93223-c6bc-4d9f-b1bb-37f08b6306dd","owners":[{"id":100001,"username":"testAdmin1","name":"Test Administrator 1"}],"imageSmallUrl":"http://"}]}


# Group Export

## optional_title [/public/exportAll]

### Exports everything. Admin only. [GET]


boolean states = only export states, can be combined with other boolean flags (presence means "true", absence means "false");
boolean types = only export types, can be combined with other boolean flags (presence means "true", absence means "false");
boolean categories = only export categories, can be combined with other boolean flags (presence means "true", absence means "false");
boolean profiles = only export profiles, can be combined with other boolean flags (presence means "true", absence means "false");
boolean customFieldDefs = only export custom field definitions, can be combined with other boolean flags (presence means "true", absence means "false");
boolean serviceItems = only export service items, can be combined with other boolean flags (presence means "true", absence means "false");

+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=UTF-8

    + Body

            {"serviceItems":[{"editedDate":"2014-05-08T20:16:09Z","totalRate5":0,"totalRate4":0,"ozoneAware":true,"contacts":[],"avgRate":0,"totalRate2":0,"recommendedLayouts":[],"totalRate3":0,"description":null,"screenshots":[],"approvalStatus":"In Progress","totalRate1":0,"techPocs":["Test Administrator 1"],"itemComments":[],"createdDate":"2014-05-08T20:16:08Z","class":"marketplace.ServiceItem","versionName":null,"satisfiedScoreCardItems":[],"isEnabled":true,"installUrl":null,"isOutside":null,"createdBy":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"uuid":"d4ae2001-6593-441d-a5e4-b9788ebdbce7","owners":[{"id":2,"username":"testAdmin1","name":"Test Administrator 1"}],"imageLargeUrl":"http://icons.iconarchive.com/icons/iconshock/futurama/256/bender-icon.png","opensInNewBrowserTab":false,"totalComments":0,"launchUrl":"http://icons.iconarchive.com/icons/iconshock/futurama/256/bender-icon.png","validLaunchUrl":true,"state":{"id":1,"title":"Active","uuid":"070fd0a6-5d2a-4e0b-98cb-ab8e0076fb9e"},"requirements":null,"id":1,"title":"Test Listing","lastActivity":{"activityDate":"2014-05-08T20:16:09Z"},"organization":"NC","releaseDate":null,"dependencies":null,"editedBy":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"totalVotes":0,"types":{"id":2,"title":"App Component","description":"app component","hasLaunchUrl":true,"uuid":"ea58bfa0-aba0-4573-b2ea-1bc9a6b1e863","hasIcons":true,"ozoneAware":true,"isPermanent":true},"relationships":[{"relationshipType":"REQUIRE","relatedItems":[]}],"docUrls":[],"tags":[],"customFields":[],"isPublished":true,"agency":null,"approvedDate":null,"owfProperties":{"id":1,"visibleInLaunch":true,"universalName":null,"height":650,"intents":[],"singleton":false,"background":false,"mobileReady":false,"owfWidgetType":"standard","width":1050,"stackContext":null,"stackDescriptor":null,"descriptorUrl":null},"intents":[],"categories":[],"imageSmallUrl":"http://icons.iconarchive.com/icons/iconshock/futurama/256/bender-icon.png"}],"states":[{"id":1,"title":"Active","createdBy":{"id":1,"username":"System","name":"System"},"description":"Active description","isPublished":true,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2014-05-08T20:01:54Z","uuid":"070fd0a6-5d2a-4e0b-98cb-ab8e0076fb9e","createdDate":"2014-05-08T20:01:54Z"},{"id":2,"title":"Beta","createdBy":{"id":1,"username":"System","name":"System"},"description":"Beta description","isPublished":false,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2014-05-08T20:01:54Z","uuid":"bd510c71-39fb-4fcb-b449-1cb7f3e05410","createdDate":"2014-05-08T20:01:54Z"},{"id":3,"title":"Deprecated","createdBy":{"id":1,"username":"System","name":"System"},"description":"Deprecated description","isPublished":false,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2014-05-08T20:01:54Z","uuid":"de977642-4fb0-4cec-a34e-353e7cd3a34a","createdDate":"2014-05-08T20:01:54Z"},{"id":4,"title":"Planned","createdBy":{"id":1,"username":"System","name":"System"},"description":"Planned description","isPublished":false,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2014-05-08T20:01:54Z","uuid":"644b037b-f877-480a-9139-3e98e0b88f60","createdDate":"2014-05-08T20:01:54Z"},{"id":5,"title":"Retired","createdBy":{"id":1,"username":"System","name":"System"},"description":"Retired description","isPublished":false,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2014-05-08T20:01:54Z","uuid":"50491ef9-b029-4cd6-9320-495f40dc37fb","createdDate":"2014-05-08T20:01:54Z"}],"categories":[{"id":1,"title":"Category A","description":"Example Category A","editedDate":"2014-05-12T13:21:21Z","uuid":"8e1f7abe-ea9b-4367-a709-b881ae933246","createdDate":"2014-05-12T13:21:21Z"},{"id":2,"title":"Category B","description":"Example Category B","editedDate":"2014-05-12T13:21:21Z","uuid":"59e28f93-8fd4-4bb2-8920-bf5992b24136","createdDate":"2014-05-12T13:21:21Z"},{"id":3,"title":"Category C","description":"Example Category C","editedDate":"2014-05-12T13:21:21Z","uuid":"3595d93d-e022-4871-b0c1-0b832a8fbb10","createdDate":"2014-05-12T13:21:21Z"},{"id":4,"title":"Geospatial","description":"Analytics based on geographic data","editedDate":"2014-05-12T13:21:21Z","uuid":"6801a5dc-93c3-4104-9d98-a0ec5ef771aa","createdDate":"2014-05-12T13:21:21Z"},{"id":5,"title":"Query","description":"Data set retrieval","editedDate":"2014-05-12T13:21:21Z","uuid":"1fa02cc4-5b7e-432f-8d57-4185137079f2","createdDate":"2014-05-12T13:21:21Z"},{"id":6,"title":"Reporting","description":"Data set summarization","editedDate":"2014-05-12T13:21:21Z","uuid":"c68fecb8-97ab-425e-8b36-becd51514a7f","createdDate":"2014-05-12T13:21:21Z"},{"id":7,"title":"Temporal","description":"Amaltics based on temporal data","editedDate":"2014-05-12T13:21:21Z","uuid":"adf62780-e6b5-4153-843c-d908d4839146","createdDate":"2014-05-12T13:21:21Z"}],"profiles":[{"id":1,"username":"System","animationsEnabled":null,"bio":"","email":"","theme":null,"class":"marketplace.Profile","editedDate":"2014-05-08T20:01:54Z","uuid":"51eabed0-6256-4de9-aa24-d970af497f10","displayName":"System","createdDate":"2014-05-08T20:01:54Z"},{"theme":"gold","class":"marketplace.Profile","editedDate":"2014-05-08T20:05:36Z","id":2,"username":"testAdmin1","animationsEnabled":false,"bio":"","createdBy":{"id":1,"username":"System","name":"System"},"email":"testadmin1@nowhere.com","editedBy":{"id":1,"username":"System","name":"System"},"uuid":"b0fd13d1-aa04-49b3-b85d-7bb568c778e9","displayName":"Test Administrator 1","createdDate":"2014-05-08T20:05:36Z"}],"types":[{"id":2,"title":"App Component","createdBy":{"id":1,"username":"System","name":"System"},"description":"app component","editedBy":{"id":1,"username":"System","name":"System"},"hasLaunchUrl":true,"editedDate":"2014-05-08T20:01:54Z","uuid":"ea58bfa0-aba0-4573-b2ea-1bc9a6b1e863","hasIcons":true,"createdDate":"2014-05-08T20:01:54Z","ozoneAware":true,"isPermanent":true},{"id":3,"title":"OZONE App","createdBy":{"id":1,"username":"System","name":"System"},"description":"OZONE app","editedBy":{"id":1,"username":"System","name":"System"},"hasLaunchUrl":false,"editedDate":"2014-05-08T20:01:54Z","uuid":"c0313bba-64c1-445f-b6c6-5d62a95ea614","hasIcons":false,"createdDate":"2014-05-08T20:01:54Z","ozoneAware":true,"isPermanent":true},{"id":1,"title":"Web App","createdBy":{"id":1,"username":"System","name":"System"},"description":"web app","editedBy":{"id":1,"username":"System","name":"System"},"hasLaunchUrl":true,"editedDate":"2014-05-08T20:01:54Z","uuid":"40b60569-8d13-4b8c-a3b1-ec4d5432670d","hasIcons":false,"createdDate":"2014-05-08T20:01:54Z","ozoneAware":false,"isPermanent":false}],"relationships":[{"serviceItem":{"title":"Test Listing","uuid":"d4ae2001-6593-441d-a5e4-b9788ebdbce7"},"requires":[]}],"customFieldDefs":[{"fieldType":"TEXT","editedDate":"2014-05-12T13:19:25Z","class":"marketplace.TextCustomFieldDefinition","label":"test","section":"typeProperties","isPermanent":false,"id":1,"createdBy":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"isRequired":true,"editedBy":{"id":2,"username":"testAdmin1","name":"Test Administrator 1"},"description":"Testing custom field definitions in the store.","name":"Test","uuid":"0e88c913-7762-4eab-a5b3-450c5605e8d8","types":[{"id":2,"title":"App Component","uuid":"ea58bfa0-aba0-4573-b2ea-1bc9a6b1e863"},{"id":1,"title":"Web App","uuid":"40b60569-8d13-4b8c-a3b1-ec4d5432670d"}],"allTypes":false,"createdDate":"2014-05-12T13:19:25Z","tooltip":"Test Custom Field Definition"}]}


# Group Types

## optional_title [/types/imageDelete/$id]

### Removes the image associated with a type specified by id and associates that type with a default icon. [GET/POST]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"null":{"image":{"id":null,"imageSize":null,"contentType":null,"url":"/marketplace/themes/common/images/default_serviceitem_icon.png"},"editedDate":"2013-08-16T15:34:21Z","hasIcons":false,"ozoneAware":false,"isPermanent":false,"id":4,"title":"Sample Type","createdBy":{"id":7,"username":"testAdmin1","name":"Test Admin 1"},"description":null,"editedBy":{"id":7,"username":"testAdmina","name":"Test Admin 1"},"hasLaunchUrl":true,"uuid":"c8690223-3f96-442d-96c7-f099d09d0659","createdDate":"2013-08-13T17:26:07Z"}}


## optional_title [/public/types/$id]

### Returns JSON representation of a Types objects given its id. [GET]


+ Request
    + Headers


    + Body

            {"id":8}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":{"id":8,"title":"App Component","createdBy":{"id":1,"username":"System","name":"System"},"description":"app component","editedBy":{"id":1,"username":"System","name":"System"},"hasLaunchUrl":true,"editedDate":"2013-08-29T17:40:23Z","image":{"id":null,"imageSize":null,"contentType":null,"url":"/marketplace/themes/common/images/default_types_widget_icon.png"},"uuid":"e01cf8d1-3ed2-461a-bb51-59f4e9b51adf","hasIcons":true,"createdDate":"2013-08-29T17:40:23Z","ozoneAware":true,"isPermanent":true}}


## optional_title [/public/types]

### Returns JSON list representing all types in the system. [GET]


+ Request
    + Headers


    + Body

            {"max": 5, "offset":3,"order":"asc","sort":"title"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":9,"data":[{"id":8,"title":"App Component","createdBy":{"id":1,"username":"System","name":"System"},"description":"app component","editedBy":{"id":1,"username":"System","name":"System"},"hasLaunchUrl":true,"editedDate":"2013-08-29T17:40:23Z","image":{"id":null,"imageSize":null,"contentType":null,"url":"/marketplace/themes/common/images/default_types_widget_icon.png"},"uuid":"e01cf8d1-3ed2-461a-bb51-59f4e9b51adf","hasIcons":true,"createdDate":"2013-08-29T17:40:23Z","ozoneAware":true,"isPermanent":true},{"id":3,"title":"Desktop Apps","createdBy":{"id":1,"username":"System","name":"System"},"description":"desktop apps","editedBy":{"id":100001,"username":"testAdmin1","name":"Test Administrator 1"},"hasLaunchUrl":true,"editedDate":"2013-09-04T14:32:24Z","image":{"id":null,"imageSize":null,"contentType":null,"url":"/marketplace/themes/common/images/default_types_desktop_apps_icon.png"},"uuid":"df0f2320-d678-4135-a552-932f46c393b5","hasIcons":false,"createdDate":"2013-08-29T17:40:23Z","ozoneAware":false,"isPermanent":false},{"id":5,"title":"Microformats","createdBy":{"id":1,"username":"System","name":"System"},"description":"microformats","editedBy":{"id":1,"username":"System","name":"System"},"hasLaunchUrl":false,"editedDate":"2013-08-29T17:40:23Z","image":{"id":null,"imageSize":null,"contentType":null,"url":"/marketplace/themes/common/images/default_types_microformats_icon.png"},"uuid":"7f979762-8de3-4ea0-aeb8-364ee02521b3","hasIcons":false,"createdDate":"2013-08-29T17:40:23Z","ozoneAware":false,"isPermanent":false},{"id":7,"title":"OZONE App","createdBy":{"id":1,"username":"System","name":"System"},"description":"OZONE app","editedBy":{"id":1,"username":"System","name":"System"},"hasLaunchUrl":false,"editedDate":"2013-08-29T17:40:23Z","image":{"id":null,"imageSize":null,"contentType":null,"url":"/marketplace/themes/common/images/default_types_stack_icon.png"},"uuid":"a18b060d-99cd-4be4-8ae1-bc344e9bd078","hasIcons":false,"createdDate":"2013-08-29T17:40:23Z","ozoneAware":true,"isPermanent":true},{"id":6,"title":"Plugin","createdBy":{"id":1,"username":"System","name":"System"},"description":"plugins","editedBy":{"id":100001,"username":"testAdmin1","name":"Test Administrator 1"},"hasLaunchUrl":false,"editedDate":"2013-09-04T14:32:24Z","image":{"id":null,"imageSize":null,"contentType":null,"url":"/marketplace/themes/common/images/default_types_plugin_icon.png"},"uuid":"d1765da4-816b-42c6-a9b2-ef9b15732f1e","hasIcons":false,"createdDate":"2013-08-29T17:40:23Z","ozoneAware":false,"isPermanent":false},{"id":1,"title":"Service:REST","createdBy":{"id":1,"username":"System","name":"System"},"description":"services","editedBy":{"id":1,"username":"System","name":"System"},"hasLaunchUrl":true,"editedDate":"2013-08-29T17:40:23Z","image":{"id":null,"imageSize":null,"contentType":null,"url":"/marketplace/themes/common/images/default_types_service_rest_icon.png"},"uuid":"96a63087-b281-4804-8a49-dec4f8a1098f","hasIcons":false,"createdDate":"2013-08-29T17:40:23Z","ozoneAware":false,"isPermanent":false},{"id":4,"title":"Service:SOAP","createdBy":{"id":1,"username":"System","name":"System"},"description":"services","editedBy":{"id":1,"username":"System","name":"System"},"hasLaunchUrl":true,"editedDate":"2013-08-29T17:40:23Z","image":{"id":null,"imageSize":null,"contentType":null,"url":"/marketplace/themes/common/images/default_types_service_soap_icon.png"},"uuid":"97e615ef-584b-4294-9ac9-b8bffa3eeb15","hasIcons":false,"createdDate":"2013-08-29T17:40:23Z","ozoneAware":false,"isPermanent":false},{"id":2,"title":"Web App","createdBy":{"id":1,"username":"System","name":"System"},"description":"web app","editedBy":{"id":1,"username":"System","name":"System"},"hasLaunchUrl":true,"editedDate":"2013-08-29T17:40:23Z","image":{"id":null,"imageSize":null,"contentType":null,"url":"/marketplace/themes/common/images/default_types_web_apps_icon.png"},"uuid":"c7ed3763-53a6-4483-9caf-0d82cf179c34","hasIcons":false,"createdDate":"2013-08-29T17:40:23Z","ozoneAware":false,"isPermanent":false},{"id":9,"title":"Widget","createdBy":{"id":1,"username":"System","name":"System"},"description":"widgets","editedBy":{"id":1,"username":"System","name":"System"},"hasLaunchUrl":true,"editedDate":"2013-08-29T17:40:23Z","image":{"id":null,"imageSize":null,"contentType":null,"url":"/marketplace/themes/common/images/default_serviceitem_icon.png"},"uuid":"34802a88-a65a-4a3a-a838-cc6ad20118be","hasIcons":true,"createdDate":"2013-08-29T17:40:23Z","ozoneAware":true,"isPermanent":false}]}


## optional_title [/public/owfWidgetTypes/$id]

### Retrieve the OWF widget type by id. [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":{"id":1,"title":"standard","createdBy":{"id":1,"username":"System","name":"System"},"description":"Widget will appear in OWF's Launch Menu","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-08-28T04:00:00Z","uuid":"8ff6babb-6f21-4118-88e3-549d642c728c","createdDate":"2013-08-28T04:00:00Z"}}


## optional_title [/public/owfWidgetTypes]

### Retrieve all OWF widget types. [GET]


+ Request
    + Headers


    + Body

            {"max": 5, "offset":3,"order":"asc","sort":"title"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":4,"data":[{"id":4,"title":"administration","createdBy":{"id":1,"username":"System","name":"System"},"description":"Widget will appear under the Administration button on OWF's toolbar","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-08-28T04:00:00Z","uuid":"755db290-3525-41ba-8f76-3df61b0907b7","createdDate":"2013-08-28T04:00:00Z"},{"id":3,"title":"marketplace","createdBy":{"id":1,"username":"System","name":"System"},"description":"Widget will appear under the Marketplace button on OWF's toolbar","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-08-28T04:00:00Z","uuid":"649dbcc1-26d4-4053-83c7-1fdb5a39ca15","createdDate":"2013-08-28T04:00:00Z"},{"id":2,"title":"metric","createdBy":{"id":1,"username":"System","name":"System"},"description":"Widget will appear under the Metric button on OWF's toolbar","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-08-28T04:00:00Z","uuid":"a2139cf5-2ce5-4e15-a1c3-520bb650eb13","createdDate":"2013-08-28T04:00:00Z"},{"id":1,"title":"standard","createdBy":{"id":1,"username":"System","name":"System"},"description":"Widget will appear in OWF's Launch Menu","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-08-28T04:00:00Z","uuid":"8ff6babb-6f21-4118-88e3-549d642c728c","createdDate":"2013-08-28T04:00:00Z"}]}


# Group State

## optional_title [/public/state/$id]

### Returns JSON representation of a State objects given its id. [GET]


+ Request
    + Headers


    + Body

            {"id":1}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":5,"data":[{"id":1,"title":"Active","createdBy":{"id":1,"username":"System","name":"System"},"description":"Active description","isPublished":true,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-05-28T16:39:04Z","uuid":"5b8f6fef-0eca-46c5-a451-ba6a7573f089","createdDate":"2013-05-28T16:39:04Z"},{"id":2,"title":"Beta","createdBy":{"id":1,"username":"System","name":"System"},"description":"Beta description","isPublished":false,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-05-28T16:39:04Z","uuid":"bfe06849-0815-4f4a-82a6-2dcc15688f5d","createdDate":"2013-05-28T16:39:04Z"},{"id":3,"title":"Deprecated","createdBy":{"id":1,"username":"System","name":"System"},"description":"Deprecated description","isPublished":false,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-05-28T16:39:05Z","uuid":"8fd4105a-8d24-420d-b677-5c0c827506e0","createdDate":"2013-05-28T16:39:05Z"},{"id":4,"title":"Planned","createdBy":{"id":1,"username":"System","name":"System"},"description":"Planned description","isPublished":false,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-05-28T16:39:05Z","uuid":"d7ef52b1-a0e1-409f-8ad2-a2ea4cedeb8e","createdDate":"2013-05-28T16:39:05Z"},{"id":5,"title":"Retired","createdBy":{"id":1,"username":"System","name":"System"},"description":"Retired description","isPublished":false,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-05-28T16:39:05Z","uuid":"cd3e2105-1d21-43fd-80e4-0e07278ebfa1","createdDate":"2013-05-28T16:39:05Z"}]}


## optional_title [/public/state]

### Returns JSON list representing all states in the system. [GET]


+ Request
    + Headers


    + Body

            {"max": 5, "offset":3,"order":"asc","sort":"title"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":5,"data":[{"id":1,"title":"Active","createdBy":{"id":1,"username":"System","name":"System"},"description":"Active description","isPublished":true,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-05-28T16:39:04Z","uuid":"5b8f6fef-0eca-46c5-a451-ba6a7573f089","createdDate":"2013-05-28T16:39:04Z"},{"id":2,"title":"Beta","createdBy":{"id":1,"username":"System","name":"System"},"description":"Beta description","isPublished":false,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-05-28T16:39:04Z","uuid":"bfe06849-0815-4f4a-82a6-2dcc15688f5d","createdDate":"2013-05-28T16:39:04Z"},{"id":3,"title":"Deprecated","createdBy":{"id":1,"username":"System","name":"System"},"description":"Deprecated description","isPublished":false,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-05-28T16:39:05Z","uuid":"8fd4105a-8d24-420d-b677-5c0c827506e0","createdDate":"2013-05-28T16:39:05Z"},{"id":4,"title":"Planned","createdBy":{"id":1,"username":"System","name":"System"},"description":"Planned description","isPublished":false,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-05-28T16:39:05Z","uuid":"d7ef52b1-a0e1-409f-8ad2-a2ea4cedeb8e","createdDate":"2013-05-28T16:39:05Z"},{"id":5,"title":"Retired","createdBy":{"id":1,"username":"System","name":"System"},"description":"Retired description","isPublished":false,"editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-05-28T16:39:05Z","uuid":"cd3e2105-1d21-43fd-80e4-0e07278ebfa1","createdDate":"2013-05-28T16:39:05Z"}]}


# Group AffiliatedMarketplace

## optional_title [/affiliatedMarketplace/listAsJSON]

### List of Affiliated Marketplaces as JSON [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"totalCount":1,"data":[{"id":1,"icon":{"id":1,"imageSize":1956,"contentType":"image/png","url":"/marketplace/images/get/1"},"name":"QA01","active":1,"timeout":30000,"serverUrl":"https://amlqa01.goss.owfgoss.org/marketplace"}]}


## optional_title [/affiliatedMarketplace/create]

### CREATE an Affiliate Marketplace Instance [GET]

This method accepts x-www-form-urlencoded form data with the following values:
[id] identifier
[name] affiliated server name
[serverUrl] https://example.server/marketplace
[timeout] Timeout in milliseconds
[active] Active status [on|off]

+ Request
    + Headers


    + Body

            {"id":1, "name":"Sample Store","serverUrl":"https://my.store.url","timeout":30000,"active":"on"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"totalCount":1,"data":[{"id":null,"icon":{"id":null,"imageSize":null,"contentType":"null","url":"/marketplace/images/get/null"},"name":"QQ","active":1,"timeout":null,"serverUrl":"http://test"}]}


## optional_title [/affiliatedMarketplace/show/$id]

### SHOW an Affiliate Marketplace Instance [GET/POST]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"success":true,"totalCount":1,"params":{"id":"2","action":"show","controller":"affiliatedMarketplace"},"data":[{"id":2,"icon":{"id":null,"imageSize":null,"contentType":"null","url":"/marketplace/images/get/null"},"name":"QA 02","active":1,"timeout":30000,"serverUrl":"https://example.location/marketplace"}]}


## optional_title [/affiliatedMarketplace/save]

### SAVE an Affiliate Marketplace Instance [POST]

This method accepts x-www-form-urlencoded form data with the following values:
[id] identifier; if missing/empty, this creates a new affiliated marketplace
[name] affiliated server name
[serverUrl] https://example.server/marketplace
[timeout] Timeout in milliseconds
[active] Active status [on|off]

+ Request
    + Headers


    + Body

            {"id":1, "name":"Sample Store","serverUrl":"https://my.store.url","timeout":30000,"active":"on"}

+ Response 200
    + Headers

            Content-Type:text/html;charset=utf-8

    + Body

            {"success":true,"totalCount":1,"data":[{"id":1,"icon":{"id":null,"imageSize":null,"contentType":"null","url":"/marketplace/images/get/null"},"name":"QA 02","active":1,"timeout":30000,"serverUrl":"https://example.location/marketplace"}]}


## optional_title [/affiliatedMarketplace/delete]

### DELETE an Affiliate Marketplace Instance [POST]

This method accepts x-www-form-urlencoded form data with the following values:
[id] identifier

+ Request
    + Headers


    + Body

            {"id":1}

+ Response
    + Headers

            Content-Type:

    + Body

            {"result":"success","success":true}


## optional_title [/affiliatedMarketplace/getMarketplaceIconImage/$id]

### Returns Marketplace Icon Image [GET]

Params:
isDefault [true|false]
retrieveImage [true|false]  If true, an image is returned.  Otherwise, JSON describing the image is returned

+ Request
    + Headers


    + Body

            {"isDefault":"true","retrieveImage":"true"}

+ Response 200
    + Headers

            Content-Type:[application/json;charset=utf-8 | image/<image type>]

    + Body

            {"result":"success","success":true,"totalCount":1,"data":{"url":"/marketplace/images/get/null","contentType":null,"imageSize":null,"imageId":null}}


# Group FranchiseListing

## optional_title [/franchiseListing]

### This is the default action and the"show" view is rendered [GET]


+ Request
    + Headers


    + Body



+ Response
    + Headers

            Content-Type:text/html

    + Body




# Group FranchiseReporting

## optional_title [/franchiseReporting]

### This is the default action and the"show" view is rendered [GET]


+ Request
    + Headers


    + Body



+ Response
    + Headers

            Content-Type:text/html

    + Body




## optional_title [/franchiseReporting/getStoreAttributes]

### Returns the information about this instance of marketplace/franchise store [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":4,"data":{"IS_FRANCHISE_STORE":true,"STORE_AGENCY":"QA Oh Too","LISTING_DOMAIN_VALUES":"DOMAIN1, DOMAIN2, DOMAIN3"}}


## optional_title [/public/store/attributes]

### Returns the information about this instance of marketplace/franchise store [GET]


+ Request
    + Headers


    + Body



+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":4,"data":{"IS_FRANCHISE_STORE":true,"STORE_AGENCY":"QA Oh Too","LISTING_DOMAIN_VALUES":"DOMAIN1, DOMAIN2, DOMAIN3"}}


# Group ApplicationConfiguration

## optional_title [/applicationConfiguration/configs?groupName={name}]

### All configuration objects returned as a JSON array. [GET]

URL Params:
[groupName] Name of a group of config objects (optional)

+ Request
    + Headers


    + Body

            {"groupName":"Branding"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            [{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":23,"id":23,"code":"store.insideOutside.behavior","description":null,"groupName":"ADDITIONAL_CONFIGURATION","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":1,"title":"","type":"String","value":"ADMIN_SELECTED"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":24,"id":24,"code":"store.enable.ext.serviceitem","description":null,"groupName":"ADDITIONAL_CONFIGURATION","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":2,"title":"","type":"Boolean","value":"false"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":25,"id":25,"code":"store.allow.owner.to.edit.approved.listing","description":null,"groupName":"ADDITIONAL_CONFIGURATION","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":3,"title":"","type":"Boolean","value":"true"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":26,"id":26,"code":"store.amp.search.result.size","description":null,"groupName":"ADDITIONAL_CONFIGURATION","help":null,"mutable":true,"subGroupName":"Affiliated Search","subGroupOrder":1,"title":"","type":"Integer","value":"6"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":27,"id":27,"code":"store.amp.search.default.timeout","description":null,"groupName":"ADDITIONAL_CONFIGURATION","help":null,"mutable":true,"subGroupName":"Affiliated Search","subGroupOrder":2,"title":"","type":"Integer","value":"30000"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":28,"id":28,"code":"store.image.allow.upload","description":null,"groupName":"ADDITIONAL_CONFIGURATION","help":null,"mutable":true,"subGroupName":"Image Configurations","subGroupOrder":1,"title":"","type":"Boolean","value":"true"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":29,"id":29,"code":"store.type.image.max.size","description":null,"groupName":"ADDITIONAL_CONFIGURATION","help":null,"mutable":true,"subGroupName":"Image Configurations","subGroupOrder":2,"title":"","type":"Integer","value":"1048576"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":30,"id":30,"code":"store.amp.image.max.size","description":null,"groupName":"ADDITIONAL_CONFIGURATION","help":null,"mutable":true,"subGroupName":"Image Configurations","subGroupOrder":3,"title":"","type":"Integer","value":"1048576"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":31,"id":31,"code":"store.owf.sync.urls","description":null,"groupName":"ADDITIONAL_CONFIGURATION","help":null,"mutable":true,"subGroupName":"Owf Sync","subGroupOrder":1,"title":"","type":"List","value":"https://example.owf.url/owf"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":32,"id":32,"code":"store.owf.sync.sendIntentsCF","description":null,"groupName":"ADDITIONAL_CONFIGURATION","help":null,"mutable":true,"subGroupName":"Owf Sync","subGroupOrder":2,"title":"","type":"String","value":null},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":33,"id":33,"code":"store.owf.sync.receiveIntentsCF","description":null,"groupName":"ADDITIONAL_CONFIGURATION","help":null,"mutable":true,"subGroupName":"Owf Sync","subGroupOrder":3,"title":"","type":"String","value":null},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":34,"id":34,"code":"store.contact.email","description":null,"groupName":"ADDITIONAL_CONFIGURATION","help":null,"mutable":true,"subGroupName":"Store Contact Information","subGroupOrder":1,"title":"","type":"String","value":null},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":45,"id":45,"code":"store.enable.cef.logging","description":null,"groupName":"AUDITING","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":1,"title":"","type":"Boolean","value":"true"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":46,"id":46,"code":"store.enable.cef.object.access.logging","description":null,"groupName":"AUDITING","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":2,"title":"","type":"Boolean","value":"false"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":47,"id":47,"code":"store.enable.cef.log.sweep","description":null,"groupName":"AUDITING","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":3,"title":"","type":"Boolean","value":"true"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":48,"id":48,"code":"store.cef.log.location","description":null,"groupName":"AUDITING","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":4,"title":"","type":"String","value":"/usr/share/tomcat6"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":49,"id":49,"code":"store.cef.sweep.log.location","description":null,"groupName":"AUDITING","help":null,"mutable":false,"subGroupName":null,"subGroupOrder":5,"title":"","type":"String","value":"/var/log/cef"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":50,"id":50,"code":"store.security.level","description":null,"groupName":"AUDITING","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":6,"title":"","type":"String","value":"Example Level"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":1,"id":1,"code":"store.is.franchise","description":null,"groupName":"BRANDING","help":null,"mutable":false,"subGroupName":null,"subGroupOrder":1,"title":"","type":"Boolean","value":"true"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":2,"id":2,"code":"store.name","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":2,"title":"","type":"String","value":"optional_title"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":3,"id":3,"code":"store.logo","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":3,"title":"","type":"Image","value":"../themes/cobalt.theme/images/Mp_logo.png"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":4,"id":4,"code":"store.icon","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":4,"title":"","type":"Image","value":"/marketplace/themes/common/images/organization/organizationDefault.png"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":5,"id":5,"code":"store.themes","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":5,"title":"","type":"Map","value":"ORG-1::/marketplace/themes/common/images/example_image1.png,ORG-2::/marketplace/themes/common/images/example_image2.png"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":6,"id":6,"code":"free.warning.content","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":6,"title":"","type":"String","value":"Warning: This is an example warning!"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":7,"id":7,"code":"about.box.content","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"About Information","subGroupOrder":1,"title":"","type":"String","value":"The Apps Mall allows visitors to discover and explore business, mission, and convenience applications and enables user-configurable visualizations of available content."},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":8,"id":8,"code":"about.box.image","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"About Information","subGroupOrder":2,"title":"","type":"String","value":"/marketplace/themes/gold.theme/images/Mp_logo.png"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":9,"id":9,"code":"access.alert.enable","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Access Alert Information","subGroupOrder":1,"title":"","type":"Boolean","value":"true"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":10,"id":10,"code":"access.alert.content","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Access Alert Information","subGroupOrder":2,"title":"","type":"String","value":"You are accessing a restricted system."},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":11,"id":11,"code":"store.optional_titleter.featured.title","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Footer Information","subGroupOrder":1,"title":"","type":"String","value":"Apps Mall"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":12,"id":12,"code":"store.optional_titleter.featured.content","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Footer Information","subGroupOrder":2,"title":"","type":"String","value":"The Apps Mall allows visitors to discover and explore business, mission, and convenience applications and enables user-configurable visualizations of available content."},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":13,"id":13,"code":"store.open.search.title.message","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Open Search","subGroupOrder":1,"title":"","type":"String","value":"Marketplace Search"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":14,"id":14,"code":"store.open.search.description.message","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Open Search","subGroupOrder":2,"title":"","type":"String","value":"Marketplace Search Description"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":15,"id":15,"code":"store.open.search.fav.icon","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Open Search","subGroupOrder":3,"title":"","type":"Image","value":"../themes/gold.theme/images/favicon.ico"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":16,"id":16,"code":"store.open.search.site.icon","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Open Search","subGroupOrder":4,"title":"","type":"Image","value":"../images/themes/default/market_64x64.png"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":17,"id":17,"code":"store.custom.header.url","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Custom Header and Footer","subGroupOrder":1,"title":"","type":"String","value":"/marketplace/banner"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":18,"id":18,"code":"store.custom.header.height","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Custom Header and Footer","subGroupOrder":2,"title":"","type":"Integer","value":"17"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":19,"id":19,"code":"store.custom.optional_titleter.url","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Custom Header and Footer","subGroupOrder":3,"title":"","type":"String","value":"/marketplace/banner"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":20,"id":20,"code":"store.custom.optional_titleter.height","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Custom Header and Footer","subGroupOrder":4,"title":"","type":"Integer","value":"17"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":21,"id":21,"code":"store.custom.css","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Custom Header and Footer","subGroupOrder":5,"title":"","type":"String","value":"/marketplace/banner/css"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":22,"id":22,"code":"store.custom.js","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Custom Header and Footer","subGroupOrder":6,"title":"","type":"String","value":""},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":38,"id":38,"code":"store.valid.domains","description":null,"groupName":"FRANCHISE_AFFILIATION","help":null,"mutable":false,"subGroupName":null,"subGroupOrder":1,"title":"","type":"String","value":"SAMPLE1, SAMPLE2"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":39,"id":39,"code":"store.domains","description":null,"groupName":"FRANCHISE_AFFILIATION","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":2,"title":"","type":"String","value":null},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":36,"id":36,"code":"store.default.theme","description":null,"groupName":"HIDDEN","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":1,"title":"","type":"String","value":"gold"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":37,"id":37,"code":"store.job.disable.accounts.interval","description":null,"groupName":"HIDDEN","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":2,"title":"","type":"Integer","value":"1440"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":35,"id":35,"code":"store.enable.scoreCard","description":null,"groupName":"SCORECARD","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":1,"title":"","type":"Boolean","value":"true"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":40,"id":40,"code":"store.session.control.enabled","description":null,"groupName":"USER_ACCOUNT_SETTINGS","help":null,"mutable":true,"subGroupName":"Session Control","subGroupOrder":1,"title":"","type":"Boolean","value":"false"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":41,"id":41,"code":"store.session.control.max.concurrent","description":null,"groupName":"USER_ACCOUNT_SETTINGS","help":null,"mutable":true,"subGroupName":"Session Control","subGroupOrder":2,"title":"","type":"Integer","value":"1"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":42,"id":42,"code":"store.disable.inactive.accounts","description":null,"groupName":"USER_ACCOUNT_SETTINGS","help":null,"mutable":true,"subGroupName":"Inactive Accounts","subGroupOrder":1,"title":"","type":"Boolean","value":"true"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":43,"id":43,"code":"store.inactivity.threshold","description":null,"groupName":"USER_ACCOUNT_SETTINGS","help":null,"mutable":true,"subGroupName":"Inactive Accounts","subGroupOrder":2,"title":"","type":"Integer","value":"90"},{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":44,"id":44,"code":"store.job.disable.accounts.start.time","description":null,"groupName":"USER_ACCOUNT_SETTINGS","help":null,"mutable":true,"subGroupName":"Inactive Accounts","subGroupOrder":3,"title":"","type":"String","value":"23:59:59"}]


## optional_title [/applicationConfiguration/get/$id]

### This is invoked when a GET to /applicationConfiguration/get/{id} [GET]


+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":1,"id":1,"code":"store.is.franchise","description":null,"groupName":"BRANDING","help":null,"mutable":false,"subGroupName":null,"subGroupOrder":1,"title":"","type":"Boolean","value":"true"}


## optional_title [/applicationConfiguration/configs/validate?groupName={name}]

### Returns Invalid Configs via JSON [GET]


+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8


## optional_title [/applicationConfiguration/configs/$id]

### The value is parsed from the json string and the applicationConfiguration object is updated and saved [PUT]
 *
                If the config item is toggling franchise store, the value is
                moving to true, then do isValidStoreSetting checks

+ Request
    + Body

            {"id":9,"code":"access.alert.enable","value":"false","title":"Access Alert Switch","mutable":true,"description":"Toggles on and off an \"Access Alert\" window that requires user acknowledgement before allowing access to the system.","type":"Boolean","groupName":"BRANDING","subGroupName":"Access Alert Information","subGroupOrder":1,"help":"","errors":null,"saveSuccessful":null,"priorValue":"true"}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":9,"id":9,"code":"access.alert.enable","description":null,"groupName":"BRANDING","help":null,"mutable":true,"subGroupName":"Access Alert Information","subGroupOrder":1,"title":"","type":"Boolean","value":"false"}


## optional_title [/applicationConfiguration/getFranchiseFlag]

### Get FranchiseFlag as JSON [GET]


+ Response 200
    + Headers

            Content-Type:application/json;charset=UTF-8

    + Body

            {"isFranchiseStore":true}


## optional_title [/applicationConfiguration/getApplicationConfiguration/$configKey]

### Get the application configuration as JSON for a particular ApplicationSetting.  Example: applicationConfiguration/getApplicationConfiguration/SCORE_CARD_ENABLED [GET]


+ Response 200
    + Headers

            Content-Type:application/json;charset=UTF-8

    + Body

            {"applicationConfiguration":{"class":"org.ozoneplatform.appconfig.server.domain.model.ApplicationConfiguration","id":35,"id":35,"code":"store.enable.scoreCard","description":null,"groupName":"SCORECARD","help":null,"mutable":true,"subGroupName":null,"subGroupOrder":1,"title":"","type":"Boolean","value":"true"}}

# Group Intent Action

## optional_title [/public/intentAction/$id]

### Retrieve an Intent Action [GET]


+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":{"id":1,"title":"share","createdBy":{"id":1,"username":"System","name":"System"},"description":"The share intent is designed to give applications the ability to offer a simple mechanism for sharing data from the current page.","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-10-14T04:00:00Z","uuid":"3d1fdbbf-2f2b-4622-9831-7eea29e98ad8","createdDate":"2013-10-14T04:00:00Z"}}


## optional_title [/public/intentAction]

### Retrieves all intent Actions. [GET]


+ Request
    + Body

            {"max": 4, "sort": "title", "order": "asc", "offset": 1}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":[{"id":2,"title":"edit","createdBy":{"id":1,"username":"System","name":"System"},"description":"The edit intent is designed to give applications the ability to offer a simple mechanism to edit data from the current page.","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-10-14T04:00:00Z","uuid":"444afe82-acf5-484b-b607-76e2454d38d9","createdDate":"2013-10-14T04:00:00Z"}]}

# Group Intent DataType

## optional_title [/public/intentDataType/$id]

### Retrieve an Intent DataType [GET]


+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":{"id":1,"title":"audio","createdBy":{"id":1,"username":"System","name":"System"},"description":"Audio file. SAMPLE DATA TYPE.","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-10-14T04:00:00Z","uuid":"e03cfa6b-c58b-4808-bdd1-31d06da8d364","createdDate":"2013-10-14T04:00:00Z"}}


## optional_title [/public/intentDataType]

### Retrieves all Intent DataTypes. [GET]


+ Request
    + Body

            {"max": 4, "sort": "title", "order": "asc", "offset": 1}

+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body

            {"total":1,"data":[{"id":1,"title":"audio","createdBy":{"id":1,"username":"System","name":"System"},"description":"Audio file. SAMPLE DATA TYPE.","editedBy":{"id":1,"username":"System","name":"System"},"editedDate":"2013-10-14T04:00:00Z","uuid":"e03cfa6b-c58b-4808-bdd1-31d06da8d364","createdDate":"2013-10-14T04:00:00Z"}]}

# Group Agency

## Agency [/api/agency]

### Retrieves all Agencies in the system [GET]


+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body
            [{ "id": 0, "createdBy": { "id": 3, "username": "testAdmin1", "name": "Test Admin 1" }, "editedBy": { "id": 3, "username": "testAdmin1", "name": "Test Admin 1" }, "iconUrl": "https:///", "title": "test company 14", "editedDate": "2013-11-14T17:32:15Z", "createdDate": "2013-11-14T17:32:15Z" }]

### Creates a new Agency. Only administrators may access this URL. [POST]


+ Request
    + Body

            {"title": "Agency Name", "https://localhost/icon.png"}

+ Response 201
    + Headers

            Content-Type:application/json;charset=utf-8
            Location: https://localhost:8443/marketplace/api/agency/0

    + Body
            { "id": 0, "createdBy": { "id": 3, "username": "testAdmin1", "name": "Test Admin 1" }, "editedBy": { "id": 3, "username": "testAdmin1", "name": "Test Admin 1" }, "iconUrl": "https:///", "title": "test agency 14", "editedDate": "2013-11-14T17:32:15Z", "createdDate": "2013-11-14T17:32:15Z" }


## Agency [/api/agency/{id}]

### Retrieves a specific Agency by id [GET]


+ Response 200
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body
            { "id": 0, "createdBy": { "id": 3, "username": "testAdmin1", "name": "Test Admin 1" }, "editedBy": { "id": 3, "username": "testAdmin1", "name": "Test Admin 1" }, "iconUrl": "https:///", "title": "test agency 14", "editedDate": "2013-11-14T17:32:15Z", "createdDate": "2013-11-14T17:32:15Z" }

### The id in the request body and the id in the URL must match. Only administrators may access this URL. Updates an agency [PUT]


+ Request
    + Body

            {id: 1, "title": "Agency Name", "https://localhost/icon.png"}

+ Response 201
    + Headers

            Content-Type:application/json;charset=utf-8

    + Body
            { "id": 0, "createdBy": { "id": 3, "username": "testAdmin1", "name": "Test Admin 1" }, "editedBy": { "id": 3, "username": "testAdmin1", "name": "Test Admin 1" }, "iconUrl": "https:///", "title": "test agency 14", "editedDate": "2013-11-14T17:32:15Z", "createdDate": "2013-11-14T17:32:15Z" }

### Delete an Agency from the system. Only administrators may access this URL. Delete an Agency from the system [DELETE]

+ Response 204

# Group ServiceItem REST

The JSON representation of a ServiceItem has many properties as shown in the GET Model.

The Following properties are read-only.  For PUT and POST requests these values do not need to be included
- totalRate5
- totalRate4
- totalRate3
- totalRate2
- totalRate1
- avgRate
- createdDate
- satifiedScoreCardItems
- createdBy
- uuid
- totalComments
- validLaunchUrl
- lastActivity
- editedBy
- totalVotes
- isPublished
- intents (The intents property on owfProperties is not read-only)
- approvedDate
- ozoneAware

The following properties are references, meaning that for PUT and POST requests, only the
id property of the subobject is considered, and that id is used to reference an existing
object
- owners
- state
- types
- customFields[\*].customFieldDefinition
- agency
- categories
- owfProperties.intents[\*].intentDataType
- owfProperties.intents[\*].intentAction
- relationships[\*].relatedItems
- contacts[\*].type

The following properties are deprecated and only exist for backwards compatibility.
Most of them give information that is already available elsewhere in the JSON
- ozoneAware (available on the type)
- validLaunchUrl
- customFields[\*].customFieldDefinitionUuid (available on the CustomFieldDefinition)
- customFields[\*].fieldType (available on the CustomFieldDefinition)
- customFields[\*].name (available on the CustomFieldDefinition)
- customFields[\*].customFieldDefinitionId (available on the CustomFieldDefinition)
- customFields[\*].label (available on the CustomFieldDefinition)
- intents (available on owfProperties)

Modifying the following properties will have additional effects as specified
- approvalStatus - Must be "In Progress" for a POST;
    may be changed from "In Progress" to "Pending" by a user, and from "Pending" to "Approved"
    by an administrator. This will put the listing into the appropriate state in the system,
    and will create the appropriate ServiceItemActivity
- isEnabled - When changed, creates an extra ServiceItemActivity.
- isOutside - When changed, creates an extra ServiceItemActivity. May start as null, but once
    set to true or false, can never be null again.  Must be true or false for approval to succeed
- relationships - When relatedItems are added or removed, additional ServiceItemActivities are
    generated on both this ServiceItem and the related one

The following enum-based properties have the following valid values
- recommendedLayouts must be a list containing zero or more of the following strings: "DESKTOP", "ACCORDION", "TABBED", and "PORTAL"
- relationships.relationshipType must be "REQUIRE"

#### Custom Fields
Custom Fields give much more information on a GET than is necessary when doing a PUT or POST. For a PUT or POST of a customField, the following fields are necessary
- customFieldDefinition - must be an object containing an id property with the id of the CustomFieldDefinition
- class - The server-side class name of the custom field.  In future versions this might not be required
- value or fieldValueList - For drop down custom fields, if the field is a single-value field, pass in the string value of the selection using the 'value' property.  If it is a mult-value drop down, pass in a JSON list containing all of the selected string values using the fieldValueList property.  For all other customField types, the 'value' property should be used. It should be a string for text fields and a boolean for check box fields

## ServiceItem [/api/serviceItem/{id}]

+ Parameters
    + id (number) ... The id of the ServiceItem to retrieve

+ Model (application/json)

    The JSON model of a ServiceItem

    + Body
            {
                "editedDate": "2014-02-19T17:09:05Z",
                "totalRate5": 0,
                "totalRate4": 0,
                "ozoneAware": true,
                "contacts": [{
                    "id": 0,
                    "organization": null,
                    "email": "test@example.org",
                    "name": "Test Contact",
                    "unsecurePhone": "555-555-5555",
                    "securePhone": "555-5555",
                    "type": {
                        "id": 0,
                        "title": "Sponsor",
                        "required": false
                    }
                }],
                "avgRate": 0,
                "totalRate2": 0,
                "recommendedLayouts": ["DESKTOP"],
                "totalRate3": 0,
                "description": "Test Description",
                "screenshots": [{
                    "id": 69,
                    "largeImageUrl": "https://www.owfgoss.org/demodata/Favorites.png",
                    "ordinal": 0,
                    "smallImageUrl": "https://www.owfgoss.org/demodata/Favorites.png"
                }],
                "approvalStatus": "Approved",
                "totalRate1": 0,
                "techPocs": ["tpou"],
                "createdDate": "2014-02-18T22:16:55Z",
                "class": "marketplace.ServiceItem",
                "satisfiedScoreCardItems": [],
                "versionName": "3",
                "isEnabled": true,
                "installUrl": "https://www.owfgoss.org/demodata/Favorites.png",
                "isOutside": false,
                "createdBy": {
                    "id": 2,
                    "username": "testAdmin1",
                    "name": "Test Administrator 1"
                },
                "uuid": "e4fc6bf0-96f9-47b5-a913-f98120a9481e",
                "owners": [{
                    "id": 26,
                    "username": "tpou",
                    "name": "Tania Pou"
                }],
                "imageLargeUrl": "https://www.owfgoss.org/demodata/Favorites.png",
                "opensInNewBrowserTab": false,
                "totalComments": 0,\
                "launchUrl": "https://www.owfgoss.org/demodata/Favorites.png",
                "validLaunchUrl": true,
                "state": {
                    "id": 1,
                    "title": "Active",
                    "uuid": "c0fdb8b1-cabf-4c89-b1f8-17402f27e2ba"
                },
                "requirements": null,
                "id": 5,
                "title": "Book Lock",
                "lastActivity": {
                    "activityDate": "2014-02-19T17:09:05Z"
                },
                "organization": "Org 1",
                "releaseDate": "2012-06-09",
                "dependencies": null,
                "editedBy": {
                    "id": 2,
                    "username": "testAdmin1",
                    "name": "Test Administrator 1"
                },
                "totalVotes": 0,
                "types": {
                    "id": 2,
                    "title": "App Component",
                    "description": "app component",
                    "hasLaunchUrl": true,
                    "hasIcons": true,
                    "uuid": "9d541c50-60a7-4d5b-bf7f-7b63d330bfb1",
                    "ozoneAware": true,
                    "isPermanent": true
                },
                "relationships": [{
                    "relationshipType": "REQUIRE",
                    "relatedItems": [{
                        "id": 3,
                        "title": "Collaboration Buddy",
                        "imageSmallUrl": "https://www.owfgoss.org/demodata/chat-icon.png"
                    }]
                }],
                "docUrls": [{
                    "name": "https://www.owfgoss.org/demodata/Favorites.png",
                    "url": "https://www.owfgoss.org/demodata/Favorites.png"
                }],
                "customFields": [{
                    "id": 135,
                    "customFieldDefinitionUuid": "b5deca05-3ea2-4246-9ee0-3f3707e977da",
                    "fieldType": "CHECK_BOX",
                    "name": "AccessibleFlag",
                    "value": false,
                    "customFieldDefinitionId": 2,
                    "class": "marketplace.CheckBoxCustomField",
                    "label": "Accessible",
                    "customFieldDefinition": {
                        "id": 2,
                        "fieldType": "Checkbox",
                        "name": "AccessibleFlag",
                        "label": "Accessible",
                        "uuid": "b5deca05-3ea2-4246-9ee0-3f3707e977da",
                        "section": "typeProperties"
                    },
                    "section": {
                        "enumType": "ozone.marketplace.enums.CustomFieldSection",
                        "name": "typeProperties"
                    }
                }, {
                    "id": 137,
                    "customFieldDefinitionUuid": "f666d1fa-3309-4bcf-bf88-9e9ab975e394",
                    "fieldValue": {
                        "id": 19,
                        "value": "John Backus",
                        "isEnabled": 1
                    },
                    "fieldType": "DROP_DOWN",
                    "name": "Computer Scientist",
                    "value": "John Backus",
                    "customFieldDefinitionId": 6,
                    "class": "marketplace.DropDownCustomField",
                    "label": "Computer scientists",
                    "customFieldDefinition": {
                        "id": 6,
                        "fieldType": "Drop Down",
                        "name": "Computer Scientist",
                        "label": "Computer scientists",
                        "uuid": "f666d1fa-3309-4bcf-bf88-9e9ab975e394",
                        "section": "typeProperties"
                    },
                    "section": {
                        "enumType": "ozone.marketplace.enums.CustomFieldSection",
                        "name": "typeProperties"
                    }
                }, {
                    "id": 138,
                    "customFieldDefinitionUuid": "cf66488e-58ab-49d9-bda8-57f946a6f799",
                    "fieldType": "TEXT",
                    "name": "Design",
                    "value": "a",
                    "customFieldDefinitionId": 8,
                    "class": "marketplace.TextCustomField",
                    "label": "Design Approved by",
                    "customFieldDefinition": {
                        "id": 8,
                        "fieldType": "Text",
                        "name": "Design",
                        "label": "Design Approved by",
                        "uuid": "cf66488e-58ab-49d9-bda8-57f946a6f799",
                        "section": "typeProperties"
                    },
                    "section": {
                        "enumType": "ozone.marketplace.enums.CustomFieldSection",
                        "name": "typeProperties"
                    }
                }],
                "isPublished": true,
                "agency": {
                    "id": 1,
                    "title": "NSA",
                    "iconUrl": "https://www.owfgoss.org/demodata/agency1.png"
                },
                "approvedDate": "2014-02-18T22:16:55Z",
                "owfProperties": {
                    "universalName": null,
                    "singleton": false,
                    "width": 1050,
                    "stackContext": null,
                    "visibleInLaunch": true,
                    "id": 88,
                    "intents": [{
                        "id": 13,
                        "dataType": {
                            "id": 1,
                            "title": "audio",
                            "uuid": "ec31f696-7a20-423d-b36c-e85d6f0ae55c"
                        },
                        "send": true,
                        "action": {
                            "id": 2,
                            "title": "edit",
                            "uuid": "7620ad4c-3103-4c0c-9e50-c6772c015655"
                        },
                        "receive": false
                    }],
                    "height": 650,
                    "owfWidgetType": "standard",
                    "background": false,
                    "mobileReady":false,
                    "descriptorUrl": null,
                    "stackDescriptor": null
                },
                "intents": [{
                    "id": 13,
                    "dataType": {
                        "id": 1,
                        "title": "audio",
                        "uuid": "ec31f696-7a20-423d-b36c-e85d6f0ae55c"
                    },
                    "send": true,
                    "action": {
                        "id": 2,
                        "title": "edit",
                        "uuid": "7620ad4c-3103-4c0c-9e50-c6772c015655"
                    },
                    "receive": false
                }],
                "categories": [{
                    "id": 1,
                    "title": "Analytics and Analysis",
                    "uuid": "a7a63e35-a811-46d2-8769-f9409841150f"
                }, {
                    "id": 14,
                    "title": "Geospatial Solutions",
                    "uuid": "2156a18a-b99c-4271-8f0b-2542de887ff8"
                }, {
                    "id": 19,
                    "title": "Search",
                    "uuid": "84ff9f28-28da-485f-b55e-875d52c194fd"
                }],
                "imageSmallUrl": "https://www.owfgoss.org/demodata/Favorites.png"
            }

### Retrieves a single ServiceItem by ID [GET]
+ Response 200
    [ServiceItem][]

### Updates an existing ServiceItem by ID. The ID in the URL must match the ID in the JSON [PUT]
+ Request
    [ServiceItem][]
+ Response 200
    [ServiceItem][]

### Delete the ServiceItem [DELETE]
+ Response 204


## ServiceItems [/api/serviceItem{?max,offset}]

+ Model (application/json)
    + Body
        {
            "total": 118,
            "data": [{
                "editedDate": "2014-02-19T17:09:05Z",
                "totalRate5": 0,
                "totalRate4": 0,
                "ozoneAware": true,
                "contacts": [{
                    "id": 0,
                    "organization": null,
                    "email": "test@example.org",
                    "name": "Test Contact",
                    "unsecurePhone": "555-555-5555",
                    "securePhone": "555-5555",
                    "type": {
                        "id": 0,
                        "title": "Sponsor",
                        "required": false
                    }
                }],
                "avgRate": 0,
                "totalRate2": 0,
                "recommendedLayouts": ["DESKTOP"],
                "totalRate3": 0,
                "description": "Test Description",
                "screenshots": [{
                    "id": 69,
                    "largeImageUrl": "https://www.owfgoss.org/demodata/Favorites.png",
                    "ordinal": 0,
                    "smallImageUrl": "https://www.owfgoss.org/demodata/Favorites.png"
                }],
                "approvalStatus": "Approved",
                "totalRate1": 0,
                "techPocs": ["tpou"],
                "createdDate": "2014-02-18T22:16:55Z",
                "class": "marketplace.ServiceItem",
                "satisfiedScoreCardItems": [],
                "versionName": "3",
                "isEnabled": true,
                "installUrl": "https://www.owfgoss.org/demodata/Favorites.png",
                "isOutside": false,
                "createdBy": {
                    "id": 2,
                    "username": "testAdmin1",
                    "name": "Test Administrator 1"
                },
                "uuid": "e4fc6bf0-96f9-47b5-a913-f98120a9481e",
                "owners": [{
                    "id": 26,
                    "username": "tpou",
                    "name": "Tania Pou"
                }],
                "imageLargeUrl": "https://www.owfgoss.org/demodata/Favorites.png",
                "opensInNewBrowserTab": false,
                "totalComments": 0,
                "launchUrl": "https://www.owfgoss.org/demodata/Favorites.png",
                "validLaunchUrl": true,
                "state": {
                    "id": 1,
                    "title": "Active",
                    "uuid": "c0fdb8b1-cabf-4c89-b1f8-17402f27e2ba"
                },
                "requirements": null,
                "id": 5,
                "title": "Book Lock",
                "lastActivity": {
                    "activityDate": "2014-02-19T17:09:05Z"
                },
                "organization": "Org 1",
                "releaseDate": "2012-06-09",
                "dependencies": null,
                "editedBy": {
                    "id": 2,
                    "username": "testAdmin1",
                    "name": "Test Administrator 1"
                },
                "totalVotes": 0,
                "types": {
                    "id": 2,
                    "title": "App Component",
                    "description": "app component",
                    "hasLaunchUrl": true,
                    "hasIcons": true,
                    "uuid": "9d541c50-60a7-4d5b-bf7f-7b63d330bfb1",
                    "ozoneAware": true,
                    "isPermanent": true
                },
                "relationships": [{
                    "relationshipType": "REQUIRE",
                    "relatedItems": [{
                        "id": 3,
                        "title": "Collaboration Buddy",
                        "imageSmallUrl": "https://www.owfgoss.org/demodata/chat-icon.png"
                    }]
                }],
                "docUrls": [{
                    "name": "https://www.owfgoss.org/demodata/Favorites.png",
                    "url": "https://www.owfgoss.org/demodata/Favorites.png"
                }],
                "customFields": [{
                    "id": 135,
                    "customFieldDefinitionUuid": "b5deca05-3ea2-4246-9ee0-3f3707e977da",
                    "fieldType": "CHECK_BOX",
                    "name": "AccessibleFlag",
                    "value": false,
                    "customFieldDefinitionId": 2,
                    "class": "marketplace.CheckBoxCustomField",
                    "label": "Accessible",
                    "customFieldDefinition": {
                        "id": 2,
                        "fieldType": "Checkbox",
                        "name": "AccessibleFlag",
                        "label": "Accessible",
                        "uuid": "b5deca05-3ea2-4246-9ee0-3f3707e977da",
                        "section": "typeProperties"
                    },
                    "section": {
                        "enumType": "ozone.marketplace.enums.CustomFieldSection",
                        "name": "typeProperties"
                    }
                }, {
                    "id": 137,
                    "customFieldDefinitionUuid": "f666d1fa-3309-4bcf-bf88-9e9ab975e394",
                    "fieldValue": {
                        "id": 19,
                        "value": "John Backus",
                        "isEnabled": 1
                    },
                    "fieldType": "DROP_DOWN",
                    "name": "Computer Scientist",
                    "value": "John Backus",
                    "customFieldDefinitionId": 6,
                    "class": "marketplace.DropDownCustomField",
                    "label": "Computer scientists",
                    "customFieldDefinition": {
                        "id": 6,
                        "fieldType": "Drop Down",
                        "name": "Computer Scientist",
                        "label": "Computer scientists",
                        "uuid": "f666d1fa-3309-4bcf-bf88-9e9ab975e394",
                        "section": "typeProperties"
                    },
                    "section": {
                        "enumType": "ozone.marketplace.enums.CustomFieldSection",
                        "name": "typeProperties"
                    }
                }, {
                    "id": 138,
                    "customFieldDefinitionUuid": "cf66488e-58ab-49d9-bda8-57f946a6f799",
                    "fieldType": "TEXT",
                    "name": "Design",
                    "value": "a",
                    "customFieldDefinitionId": 8,
                    "class": "marketplace.TextCustomField",
                    "label": "Design Approved by",
                    "customFieldDefinition": {
                        "id": 8,
                        "fieldType": "Text",
                        "name": "Design",
                        "label": "Design Approved by",
                        "uuid": "cf66488e-58ab-49d9-bda8-57f946a6f799",
                        "section": "typeProperties"
                    },
                    "section": {
                        "enumType": "ozone.marketplace.enums.CustomFieldSection",
                        "name": "typeProperties"
                    }
                }],
                "isPublished": true,
                "agency": {
                    "id": 1,
                    "title": "NSA",
                    "iconUrl": "https://www.owfgoss.org/demodata/agency1.png"
                },
                "approvedDate": "2014-02-18T22:16:55Z",
                "owfProperties": {
                    "universalName": null,
                    "singleton": false,
                    "width": 1050,
                    "stackContext": null,
                    "visibleInLaunch": true,
                    "id": 88,
                    "intents": [{
                        "id": 13,
                        "dataType": {
                            "id": 1,
                            "title": "audio",
                            "uuid": "ec31f696-7a20-423d-b36c-e85d6f0ae55c"
                        },
                        "send": true,
                        "action": {
                            "id": 2,
                            "title": "edit",
                            "uuid": "7620ad4c-3103-4c0c-9e50-c6772c015655"
                        },
                        "receive": false
                    }],
                    "height": 650,
                    "owfWidgetType": "standard",
                    "background": false,
                    "mobileReady":false,
                    "descriptorUrl": null,
                    "stackDescriptor": null
                },
                "intents": [{
                    "id": 13,
                    "dataType": {
                        "id": 1,
                        "title": "audio",
                        "uuid": "ec31f696-7a20-423d-b36c-e85d6f0ae55c"
                    },
                    "send": true,
                    "action": {
                        "id": 2,
                        "title": "edit",
                        "uuid": "7620ad4c-3103-4c0c-9e50-c6772c015655"
                    },
                    "receive": false
                }],
                "categories": [{
                    "id": 1,
                    "title": "Analytics and Analysis",
                    "uuid": "a7a63e35-a811-46d2-8769-f9409841150f"
                }, {
                    "id": 14,
                    "title": "Geospatial Solutions",
                    "uuid": "2156a18a-b99c-4271-8f0b-2542de887ff8"
                }, {
                    "id": 19,
                    "title": "Search",
                    "uuid": "84ff9f28-28da-485f-b55e-875d52c194fd"
                }],
                "imageSmallUrl": "https://www.owfgoss.org/demodata/Favorites.png"
            }]
        }

### Retrieve all ServiceItems, with optional paging [GET]
+ Parameters
    + max (optional, number) ... The maximum number of ServiceItems to return
    + offset (optional, number) ... The offset into the full list of ServiceItems to start at

+ Response 200
    [ServiceItems][]

### Create a new ServiceItem [POST]
+ Request
    [ServiceItem][]
+ Response 201
    [ServiceItem][]

## ServiceItemActivity [/api/serviceItem/{serviceItemId}/activity{?offset,max}]

+ Parameters
    + serviceItemId (number) ... The ID of the serviceItem whose activities are being retrieved
    + max (optional, number) ... The maximum number of ServiceItemsActivities to return
    + offset (optional, number) ... The offset into the full list of ServiceItemsActivities to start at

+ Model (application/json)
    + Body
        {
            "total": 10,
            "data": {
                "author": {
                    "id": 2,
                    "username": "testAdmin1",
                    "name": "Test Administrator 1"
                },
                "activityDate": "2014-01-17T15:48:24Z",
                "action": {
                    "description": "Outside",
                    "name": "OUTSIDE"
                },
                "changeDetails": [],
                "serviceItem": {
                    "id": 2,
                    "title": "Agency Test User 2",
                    "imageSmallUrl": "http://localhost/icons/Comics.png"
                }
            }

### Retrieve all ServiceItemActivities for a ServiceItem [GET]
+ Response 200
    [ServiceItemActivity][]

## ServiceItemActivityGlobal [/api/serviceItem/activity{?offset,max}]

+ Parameters
    + max (optional, number) ... The maximum number of ServiceItemsActivities to return
    + offset (optional, number) ... The offset into the full list of ServiceItemsActivities to start at

### Retrieve all ServiceItemActivities [GET]
+ Response 200
    [ServiceItemActivity][]

## RejectionListing [/api/serviceItem/{serviceItemId}/rejectionListing]

+ Parameters
    + serviceItemId (number) ... The ID of the serviceItem being rejected

+ Model
    + Body
        {
            "id": 2,
            "author": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "createdBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "editedBy": {
                "id": 2,
                "username": "testAdmin1",
                "name": "Test Administrator 1"
            },
            "description": "This listing is terrible",
            "editedDate": "2014-03-09T04:33:05Z",
            "justification": {
                "id": 3,
                "title": "Data Content",
                "description": "Data Content"
            },
            "createdDate": "2014-03-09T04:33:05Z"
        }

### Reject a ServiceItem. This create a RejectionListing and sets the ServiceItem's approvalStatus to Rejected. [POST]
+ Request
    [RejectionListing][]

+ Response 201
    [RejectionListing][]

### Get rejection justification for a ServiceItem. [GET]

+ Response 201
    [RejectionListing][]

## RequiredListings [/api/serviceItem/{serviceItemId}/requiredServiceItems]
+ Parameters
    + serviceItemId (number) ... The ID of the serviceItem whose requirements are being fetched

+ Model (application/json)
    A simple list of ServiceItems that are required by this ServiceItem
    + Body
    [{
        "editedDate": "2014-02-19T17:09:05Z",
        "totalRate5": 0,
        "totalRate4": 0,
        "ozoneAware": true,
        "contacts": [{
            "id": 0,
            "organization": null,
            "email": "test@example.org",
            "name": "Test Contact",
            "unsecurePhone": "555-555-5555",
            "securePhone": "555-5555",
            "type": {
                "id": 0,
                "title": "Sponsor",
                "required": false
            }
        }],
        "avgRate": 0,
        "totalRate2": 0,
        "recommendedLayouts": ["DESKTOP"],
        "totalRate3": 0,
        "description": "Test Description",
        "screenshots": [{
            "id": 69,
            "largeImageUrl": "https://www.owfgoss.org/demodata/Favorites.png",
            "ordinal": 0,
            "smallImageUrl": "https://www.owfgoss.org/demodata/Favorites.png"
        }],
        "approvalStatus": "Approved",
        "totalRate1": 0,
        "techPocs": ["tpou"],
        "createdDate": "2014-02-18T22:16:55Z",
        "class": "marketplace.ServiceItem",
        "satisfiedScoreCardItems": [],
        "versionName": "3",
        "isEnabled": true,
        "installUrl": "https://www.owfgoss.org/demodata/Favorites.png",
        "isOutside": false,
        "createdBy": {
            "id": 2,
            "username": "testAdmin1",
            "name": "Test Administrator 1"
        },
        "uuid": "e4fc6bf0-96f9-47b5-a913-f98120a9481e",
        "owners": [{
            "id": 26,
            "username": "tpou",
            "name": "Tania Pou"
        }],
        "imageLargeUrl": "https://www.owfgoss.org/demodata/Favorites.png",
        "opensInNewBrowserTab": false,
        "totalComments": 0,
        "launchUrl": "https://www.owfgoss.org/demodata/Favorites.png",
        "validLaunchUrl": true,
        "state": {
            "id": 1,
            "title": "Active",
            "uuid": "c0fdb8b1-cabf-4c89-b1f8-17402f27e2ba"
        },
        "requirements": null,
        "id": 5,
        "title": "Book Lock",
        "lastActivity": {
            "activityDate": "2014-02-19T17:09:05Z"
        },
        "organization": "Org 1",
        "releaseDate": "2012-06-09",
        "dependencies": null,
        "editedBy": {
            "id": 2,
            "username": "testAdmin1",
            "name": "Test Administrator 1"
        },
        "totalVotes": 0,
        "types": {
            "id": 2,
            "title": "App Component",
            "description": "app component",
            "hasLaunchUrl": true,
            "hasIcons": true,
            "uuid": "9d541c50-60a7-4d5b-bf7f-7b63d330bfb1",
            "ozoneAware": true,
            "isPermanent": true
        },
        "relationships": [{
            "relationshipType": "REQUIRE",
            "relatedItems": [{
                "id": 3,
                "title": "Collaboration Buddy",
                "imageSmallUrl": "https://www.owfgoss.org/demodata/chat-icon.png"
            }]
        }],
        "docUrls": [{
            "name": "https://www.owfgoss.org/demodata/Favorites.png",
            "url": "https://www.owfgoss.org/demodata/Favorites.png"
        }],
        "customFields": [{
            "id": 135,
            "customFieldDefinitionUuid": "b5deca05-3ea2-4246-9ee0-3f3707e977da",
            "fieldType": "CHECK_BOX",
            "name": "AccessibleFlag",
            "value": false,
            "customFieldDefinitionId": 2,
            "class": "marketplace.CheckBoxCustomField",
            "label": "Accessible",
            "customFieldDefinition": {
                "id": 2,
                "fieldType": "Checkbox",
                "name": "AccessibleFlag",
                "label": "Accessible",
                "uuid": "b5deca05-3ea2-4246-9ee0-3f3707e977da",
                "section": "typeProperties"
            },
            "section": {
                "enumType": "ozone.marketplace.enums.CustomFieldSection",
                "name": "typeProperties"
            }
        }, {
            "id": 137,
            "customFieldDefinitionUuid": "f666d1fa-3309-4bcf-bf88-9e9ab975e394",
            "fieldValue": {
                "id": 19,
                "value": "John Backus",
                "isEnabled": 1
            },
            "fieldType": "DROP_DOWN",
            "name": "Computer Scientist",
            "value": "John Backus",
            "customFieldDefinitionId": 6,
            "class": "marketplace.DropDownCustomField",
            "label": "Computer scientists",
            "customFieldDefinition": {
                "id": 6,
                "fieldType": "Drop Down",
                "name": "Computer Scientist",
                "label": "Computer scientists",
                "uuid": "f666d1fa-3309-4bcf-bf88-9e9ab975e394",
                "section": "typeProperties"
            },
            "section": {
                "enumType": "ozone.marketplace.enums.CustomFieldSection",
                "name": "typeProperties"
            }
        }, {
            "id": 138,
            "customFieldDefinitionUuid": "cf66488e-58ab-49d9-bda8-57f946a6f799",
            "fieldType": "TEXT",
            "name": "Design",
            "value": "a",
            "customFieldDefinitionId": 8,
            "class": "marketplace.TextCustomField",
            "label": "Design Approved by",
            "customFieldDefinition": {
                "id": 8,
                "fieldType": "Text",
                "name": "Design",
                "label": "Design Approved by",
                "uuid": "cf66488e-58ab-49d9-bda8-57f946a6f799",
                "section": "typeProperties"
            },
            "section": {
                "enumType": "ozone.marketplace.enums.CustomFieldSection",
                "name": "typeProperties"
            }
        }],
        "isPublished": true,
        "agency": {
            "id": 1,
            "title": "NSA",
            "iconUrl": "https://www.owfgoss.org/demodata/agency1.png"
        },
        "approvedDate": "2014-02-18T22:16:55Z",
        "owfProperties": {
            "universalName": null,
            "singleton": false,
            "width": 1050,
            "stackContext": null,
            "visibleInLaunch": true,
            "id": 88,
            "intents": [{
                "id": 13,
                "dataType": {
                    "id": 1,
                    "title": "audio",
                    "uuid": "ec31f696-7a20-423d-b36c-e85d6f0ae55c"
                },
                "send": true,
                "action": {
                    "id": 2,
                    "title": "edit",
                    "uuid": "7620ad4c-3103-4c0c-9e50-c6772c015655"
                },
                "receive": false
            }],
            "height": 650,
            "owfWidgetType": "standard",
            "background": false,
            "mobileReady":false,
            "descriptorUrl": null,
            "stackDescriptor": null
        },
        "intents": [{
            "id": 13,
            "dataType": {
                "id": 1,
                "title": "audio",
                "uuid": "ec31f696-7a20-423d-b36c-e85d6f0ae55c"
            },
            "send": true,
            "action": {
                "id": 2,
                "title": "edit",
                "uuid": "7620ad4c-3103-4c0c-9e50-c6772c015655"
            },
            "receive": false
        }],
        "categories": [{
            "id": 1,
            "title": "Analytics and Analysis",
            "uuid": "a7a63e35-a811-46d2-8769-f9409841150f"
        }, {
            "id": 14,
            "title": "Geospatial Solutions",
            "uuid": "2156a18a-b99c-4271-8f0b-2542de887ff8"
        }, {
            "id": 19,
            "title": "Search",
            "uuid": "84ff9f28-28da-485f-b55e-875d52c194fd"
        }],
        "imageSmallUrl": "https://www.owfgoss.org/demodata/Favorites.png"
    }]

### Retrieve a list of all ServiceItems that are required by this ServiceItem. If the Referer header indicates that this call is from a foreign system, "Inside" ServiceItems will be filtered from the results.  This call supports JSONP [GET]
+ Response 200
    [RequiredListings][]


## RequiringListings [/api/serviceItem/{serviceItemId}/requiringServiceItems]
+ Parameters
    + serviceItemId (number) ... The ID of the serviceItem whose requiring listings are being fetched.

+ Model (application/json)
    A simple list of ServiceItems that directly require this ServiceItem
    + Body
    [{
        "editedDate": "2014-02-19T17:09:05Z",
        "totalRate5": 0,
        "totalRate4": 0,
        "ozoneAware": true,
        "contacts": [{
            "id": 0,
            "organization": null,
            "email": "test@example.org",
            "name": "Test Contact",
            "unsecurePhone": "555-555-5555",
            "securePhone": "555-5555",
            "type": {
                "id": 0,
                "title": "Sponsor",
                "required": false
            }
        }],
        "avgRate": 0,
        "totalRate2": 0,
        "recommendedLayouts": ["DESKTOP"],
        "totalRate3": 0,
        "description": "Test Description",
        "screenshots": [{
            "id": 69,
            "largeImageUrl": "https://www.owfgoss.org/demodata/Favorites.png",
            "ordinal": 0,
            "smallImageUrl": "https://www.owfgoss.org/demodata/Favorites.png"
        }],
        "approvalStatus": "Approved",
        "totalRate1": 0,
        "techPocs": ["tpou"],
        "createdDate": "2014-02-18T22:16:55Z",
        "class": "marketplace.ServiceItem",
        "satisfiedScoreCardItems": [],
        "versionName": "3",
        "isEnabled": true,
        "installUrl": "https://www.owfgoss.org/demodata/Favorites.png",
        "isOutside": false,
        "createdBy": {
            "id": 2,
            "username": "testAdmin1",
            "name": "Test Administrator 1"
        },
        "uuid": "e4fc6bf0-96f9-47b5-a913-f98120a9481e",
        "owners": [{
            "id": 26,
            "username": "tpou",
            "name": "Tania Pou"
        }],
        "imageLargeUrl": "https://www.owfgoss.org/demodata/Favorites.png",
        "opensInNewBrowserTab": false,
        "totalComments": 0,
        "launchUrl": "https://www.owfgoss.org/demodata/Favorites.png",
        "validLaunchUrl": true,
        "state": {
            "id": 1,
            "title": "Active",
            "uuid": "c0fdb8b1-cabf-4c89-b1f8-17402f27e2ba"
        },
        "requirements": null,
        "id": 5,
        "title": "Book Lock",
        "lastActivity": {
            "activityDate": "2014-02-19T17:09:05Z"
        },
        "organization": "Org 1",
        "releaseDate": "2012-06-09",
        "dependencies": null,
        "editedBy": {
            "id": 2,
            "username": "testAdmin1",
            "name": "Test Administrator 1"
        },
        "totalVotes": 0,
        "types": {
            "id": 2,
            "title": "App Component",
            "description": "app component",
            "hasLaunchUrl": true,
            "hasIcons": true,
            "uuid": "9d541c50-60a7-4d5b-bf7f-7b63d330bfb1",
            "ozoneAware": true,
            "isPermanent": true
        },
        "relationships": [{
            "relationshipType": "REQUIRE",
            "relatedItems": [{
                "id": 3,
                "title": "Collaboration Buddy",
                "imageSmallUrl": "https://www.owfgoss.org/demodata/chat-icon.png"
            }]
        }],
        "docUrls": [{
            "name": "https://www.owfgoss.org/demodata/Favorites.png",
            "url": "https://www.owfgoss.org/demodata/Favorites.png"
        }],
        "customFields": [{
            "id": 135,
            "customFieldDefinitionUuid": "b5deca05-3ea2-4246-9ee0-3f3707e977da",
            "fieldType": "CHECK_BOX",
            "name": "AccessibleFlag",
            "value": false,
            "customFieldDefinitionId": 2,
            "class": "marketplace.CheckBoxCustomField",
            "label": "Accessible",
            "customFieldDefinition": {
                "id": 2,
                "fieldType": "Checkbox",
                "name": "AccessibleFlag",
                "label": "Accessible",
                "uuid": "b5deca05-3ea2-4246-9ee0-3f3707e977da",
                "section": "typeProperties"
            },
            "section": {
                "enumType": "ozone.marketplace.enums.CustomFieldSection",
                "name": "typeProperties"
            }
        }, {
            "id": 137,
            "customFieldDefinitionUuid": "f666d1fa-3309-4bcf-bf88-9e9ab975e394",
            "fieldValue": {
                "id": 19,
                "value": "John Backus",
                "isEnabled": 1
            },
            "fieldType": "DROP_DOWN",
            "name": "Computer Scientist",
            "value": "John Backus",
            "customFieldDefinitionId": 6,
            "class": "marketplace.DropDownCustomField",
            "label": "Computer scientists",
            "customFieldDefinition": {
                "id": 6,
                "fieldType": "Drop Down",
                "name": "Computer Scientist",
                "label": "Computer scientists",
                "uuid": "f666d1fa-3309-4bcf-bf88-9e9ab975e394",
                "section": "typeProperties"
            },
            "section": {
                "enumType": "ozone.marketplace.enums.CustomFieldSection",
                "name": "typeProperties"
            }
        }, {
            "id": 138,
            "customFieldDefinitionUuid": "cf66488e-58ab-49d9-bda8-57f946a6f799",
            "fieldType": "TEXT",
            "name": "Design",
            "value": "a",
            "customFieldDefinitionId": 8,
            "class": "marketplace.TextCustomField",
            "label": "Design Approved by",
            "customFieldDefinition": {
                "id": 8,
                "fieldType": "Text",
                "name": "Design",
                "label": "Design Approved by",
                "uuid": "cf66488e-58ab-49d9-bda8-57f946a6f799",
                "section": "typeProperties"
            },
            "section": {
                "enumType": "ozone.marketplace.enums.CustomFieldSection",
                "name": "typeProperties"
            }
        }],
        "isPublished": true,
        "agency": {
            "id": 1,
            "title": "NSA",
            "iconUrl": "https://www.owfgoss.org/demodata/agency1.png"
        },
        "approvedDate": "2014-02-18T22:16:55Z",
        "owfProperties": {
            "universalName": null,
            "singleton": false,
            "width": 1050,
            "stackContext": null,
            "visibleInLaunch": true,
            "id": 88,
            "intents": [{
                "id": 13,
                "dataType": {
                    "id": 1,
                    "title": "audio",
                    "uuid": "ec31f696-7a20-423d-b36c-e85d6f0ae55c"
                },
                "send": true,
                "action": {
                    "id": 2,
                    "title": "edit",
                    "uuid": "7620ad4c-3103-4c0c-9e50-c6772c015655"
                },
                "receive": false
            }],
            "height": 650,
            "owfWidgetType": "standard",
            "background": false,
            "mobileReady":false,
            "descriptorUrl": null,
            "stackDescriptor": null
        },
        "intents": [{
            "id": 13,
            "dataType": {
                "id": 1,
                "title": "audio",
                "uuid": "ec31f696-7a20-423d-b36c-e85d6f0ae55c"
            },
            "send": true,
            "action": {
                "id": 2,
                "title": "edit",
                "uuid": "7620ad4c-3103-4c0c-9e50-c6772c015655"
            },
            "receive": false
        }],
        "categories": [{
            "id": 1,
            "title": "Analytics and Analysis",
            "uuid": "a7a63e35-a811-46d2-8769-f9409841150f"
        }, {
            "id": 14,
            "title": "Geospatial Solutions",
            "uuid": "2156a18a-b99c-4271-8f0b-2542de887ff8"
        }, {
            "id": 19,
            "title": "Search",
            "uuid": "84ff9f28-28da-485f-b55e-875d52c194fd"
        }],
        "imageSmallUrl": "https://www.owfgoss.org/demodata/Favorites.png"
    }]

### Retrieve a list of ServiceItems which directly require this ServiceItem.  Note that this is unlike the requiredServiceItems called, which checks indirect relationships, aka. transitive dependencies, as well. If the Referer header indicates that this call is from a foreign system, "Inside" ServiceItems will be filtered from the results.  This call supports JSONP [GET]
+ Response 200
    [RequiredListings][]

## optional_title [/api/serviceItem/{serviceItemId}/tag/]

### Retrieve tags for this ServiceItem [GET]

+ Response 200
    + Headers

    Content-Type:application/json;charset=utf-8

    + Body
    [
        {
            "id": 1,
            "createdBy": {
                "id": 2,
                "username": "testAdmin1"
            },
            "tag": {
                "id": 1,
                "title": "MAP"
            }
        }
    ]


## optional_title [/api/serviceItem/{serviceItemId}/tag]

### Create a tag or tags for this ServiceItem [POST]

+ Request
    + Body
        [{"serviceItemId":5,"title":"tag1","createdBy":{"id":2}}]

+ Response 201
    + Headers

    Content-Type:application/json;charset=utf-8

    + Body
    [
        {
            "id": 3,
            "createdBy": {
                "id": 2,
                "username": "testAdmin1"
            },
            "tag": {
                "id": 3,
                "title": "tag1"
            }
        }
    ]

## optional_title [/api/serviceItem/{serviceItemId}/tag/{tagId}]

### Delete a tag for this ServiceItem [DELETE]

+ Response 204



# Group Contact Type

## ContactType [/api/contactType/{id}]

+ Parameters
    + id (number) ... The id of the contact type

+ Model
    + Body
    {
        "id": 1,
        "title": "A Required Contact Type",
        "createdBy": {
            "id": 14,
            "username": "RobAdmin",
            "name": "Rob Szewczyk Admin"
        },
        "editedBy": {
            "id": 2,
            "username": "MikePAdmin",
            "name": "Mike Parizer Admin"
        },
        "editedDate": "2014-01-17T05:00:00Z",
        "required": false,
        "createdDate": "2014-01-14T05:00:00Z"
    }

### Update an existing contactType [PUT]
+ Request
    [ContactType][]
+ Response 200
    [ContactType][]

### Delete a Contact Type [DELETE]
+ Response 204

## ContactTypes [/api/contactType{?max,offset}]


+ Model
    + Body
        {
            "total": 4,
            "data": [{
                "id": 1,
                "title": "A Required Contact Type",
                "createdBy": {
                    "id": 14,
                    "username": "RobAdmin",
                    "name": "Rob Szewczyk Admin"
                },
                "editedBy": {
                    "id": 2,
                    "username": "MikePAdmin",
                    "name": "Mike Parizer Admin"
                },
                "editedDate": "2014-01-17T05:00:00Z",
                "required": false,
                "createdDate": "2014-01-14T05:00:00Z"
            }]
        }

### Create a ContactType - Admin only [POST]
+ Request
    [ContactType][]
+ Response 201
    [ContactType][]

### List ContactTypes [GET]
+ Parameters
    + max (optional, number) ... The maximum number of ContactTypes to return
    + offset (optional, number) ... The offset into the full list of ContactTypes to start at

+ Response 200
    [ContactTypes][]

# Group Profile REST

## Profiles [/api/profile{?offset,max}]

+ Model
    + Body
    {
        "total": 2,
        "data": [{
            "id": 1,
            "animationsEnabled": null,
            "username": "System",
            "bio": "",
            "email": "",
            "theme": null,
            "editedDate": "2014-02-21T17:15:40Z",
            "class": "marketplace.Profile",
            "uuid": "457ae498-7068-4495-8a4c-d105d3f2415a",
            "displayName": "System",
            "createdDate": "2014-02-21T17:15:40Z"
        }, {
            "theme": null,
            "class": "marketplace.Profile",
            "editedDate": "2014-02-21T17:16:12Z",
            "id": 2,
            "username": "testAdmin1",
            "animationsEnabled": false,
            "bio": "",
            "createdBy": {
                "id": 1,
                "username": "System",
                "name": "System"
            },
            "email": "testadmin1@nowhere.com",
            "editedBy": {
                "id": 1,
                "username": "System",
                "name": "System"
            },
            "uuid": "714b4169-8dc7-4eac-83d4-f54b559315a1",
            "displayName": "Test Administrator 1",
            "createdDate": "2014-02-21T17:16:12Z"
        }]
    }

### Retrieve a paged list of all profiles [GET]

+ Parameters
    + max (optional, number) ... The maximum number of ContactTypes to return
    + offset (optional, number) ... The offset into the full list of ContactTypes to start at

+ Response 200
    [Profiles][]

## Profile [/api/profile/{profileId}]

+ Parameters
    + profileId (string) ... The id of the profile being retrieved, or "self"

+ Model
    + Body
    {
        "theme": "gold",
        "class": "marketplace.Profile",
        "editedDate": "2014-02-21T17:16:12Z",
        "id": 2,
        "username": "testAdmin1",
        "animationsEnabled": false,
        "bio": "",
        "createdBy": {
            "id": 1,
            "username": "System",
            "name": "System"
        },
        "email": "testadmin1@nowhere.com",
        "editedBy": {
            "id": 1,
            "username": "System",
            "name": "System"
        },
        "uuid": "714b4169-8dc7-4eac-83d4-f54b559315a1",
        "displayName": "Test Administrator 1",
        "createdDate": "2014-02-21T17:16:12Z"
    }

### Retrieve a profile [GET]

+ Response 200
    [Profile][]

### Update a profile. Since most profile options are set via the security plugin, only "bio" is changeable via this call.  "animationsEnabled" and "theme" may be added in the future [PUT]

+ Request
    [Profile][]

+ Response 200
    [Profile][]

## ProfileServiceItems [/api/profile/{profileId}/serviceItem]

+ Parameters
    + profileId (string) ... The id of the profile being retrieved, or "self"

### Retrieve a list of ServiceItems for whom this Profile is an Owner [GET]

+ Response 200
    [ServiceItems][]

## ProfileComments [/api/profile/{profileId}/itemComment]

+ Parameters
    + profileId (string) ... The id of the profile being retrieved, or "self"

+ Model
    + Body
    [{
        "id": 1,
        "author": {
            "id": 2,
            "username": "testAdmin1",
            "displayName": "Test Administrator 1"
        },
        "text": "Test Comment",
        "rate": 3,
        "createdBy": {
            "id": 2,
            "username": "testAdmin1",
            "name": "Test Administrator 1"
        },
        "editedBy": {
            "id": 2,
            "username": "testAdmin1",
            "name": "Test Administrator 1"
        },
        "editedDate": "2014-02-21T17:23:40Z",
        "serviceItem": {
            "id": 1,
            "title": "test listing",
            "imageSmallUrl": "https://localhost/"
        },
        "createdDate": "2014-02-21T17:23:40Z"
    }]

### Retrieve all comments made by this user [GET]

+ Response 200
    [ProfileComments][]


## ProfileTags [/api/profile/{profileId}/tag]

+ Parameters
    + profileId (string) ... The id of the profile being retrieved, or "self"

+ Model (application/json)
    + Body
    [
        {
            "id": 1,
            "createdBy": {
                "id": 2,
                "username": "testAdmin1"
            },
            "tag": {
                "id": 1,
                "title": "MAP"
            }
        }
    ]

### Retrieve tags created by this user [GET]

+ Response 200
    [ProfileTags][]

## ProfileServiceItemActivities [/api/profile/{profileId}/activity{?offset,max}]

+ Parameters
    + profileId (string) ... The id of the profile being retrieved, or "self"
    + max (optional, number) ... The maximum number of ServiceItemActivities to return
    + offset (optional, number) ... The offset into the full list of ServiceItemActivities to start at

+ Model
    + Body
    {
        "total": 4,
        "data": [{
            "author": {
                "id": 3,
                "username": "testUser1",
                "name": "Test User 1"
            },
            "relatedItems": [{
                "id": 2,
                "title": "invalid icon"
            }],
            "activityDate": "2014-03-05T20:00:32Z",
            "action": {
                "description": "Added as a requirement",
                "name": "ADDRELATEDTOITEM"
            },
            "changeDetails": [],
            "serviceItem": {
                "id": 1,
                "title": "no icon",
                "imageSmallUrl": null
            }
        }]
    }

### Retrieve all ServiceItemActivities made by this user [GET]

+ Response 200
    [ProfileServiceItemActivities][]

## OwnProfileServiceItemActivities [/api/profile/{profileId}/serviceItem/activity{?offset,max}]

+ Parameters
    + profileId (string) ... The id of the profile being retrieved, or "self"
    + max (optional, number) ... The maximum number of ServiceItemActivities to return
    + offset (optional, number) ... The offset into the full list of ServiceItemActivities to start at

+ Model
    + Body
    {
        "total": 4,
        "data": [{
            "author": {
                "id": 3,
                "username": "testUser1",
                "name": "Test User 1"
            },
            "relatedItems": [{
                "id": 2,
                "title": "invalid icon"
            }],
            "activityDate": "2014-03-05T20:00:32Z",
            "action": {
                "description": "Added as a requirement",
                "name": "ADDRELATEDTOITEM"
            },
            "changeDetails": [],
            "serviceItem": {
                "id": 1,
                "title": "no icon",
                "imageSmallUrl": null
            }
        }]
    }

### Retrieve all ServiceItemActivities on ServiceItems owned by this user [GET]

+ Response 200
    [ProfileServiceItemActivities][]

