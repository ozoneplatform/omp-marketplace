package marketplace.grails.service


class DefaultMetadata {

    public static final List<Map> TYPES =
            [[title        : "Web App",
              imageFilename: "default_types_web_apps_icon.png",
              description  : "web app",
              ozoneAware   : false,
              hasLaunchUrl : true,
              hasIcons     : false,
              isPermanent  : false],

             [title        : "App Component",
              imageFilename: "default_types_widget_icon.png",
              description  : "app component",
              ozoneAware   : true,
              hasLaunchUrl : true,
              hasIcons     : true,
              isPermanent  : true],

             [title        : "OZONE App",
              imageFilename: "default_types_stack_icon.png",
              description  : "OZONE app",
              ozoneAware   : true,
              hasLaunchUrl : false,
              hasIcons     : false,
              isPermanent  : true]]

    public static final List<Map> STATES =
            [[title: 'Active', description: 'Active description', isPublished: true],
             [title: 'Beta', description: 'Beta description', isPublished: false],
             [title: 'Deprecated', description: 'Deprecated description', isPublished: false],
             [title: 'Planned', description: 'Planned description', isPublished: false],
             [title: 'Retired', description: 'Retired description', isPublished: false]]


}
