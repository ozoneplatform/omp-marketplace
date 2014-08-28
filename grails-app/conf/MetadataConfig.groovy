/**
 * Contains values for reference metadata domain objects such as Types, States, and Categories.
 */
marketplace {
    metadata {
        profiles = [
                [username: 'System', displayName: 'System']
        ]

        types = [
                [title: 'Web App', imageFilename: 'default_types_web_apps_icon.png', description: 'web app',
                        ozoneAware: false, hasLaunchUrl: true, hasIcons: false, isPermanent: false],
                [title: 'App Component', imageFilename: 'default_types_widget_icon.png', description: 'app component',
                        ozoneAware: true, hasLaunchUrl: true, hasIcons: true, isPermanent: true],
				[title: 'OZONE App', imageFilename: 'default_types_stack_icon.png', description: 'OZONE app',
					ozoneAware: true, hasLaunchUrl: false, hasIcons: false, isPermanent: true]
        ]

        rejectionJustifications = [
                [title: 'Sustainment', description:'Sustainment'],
                [title: 'Security Guideline', description:'Security Guideline'],
                [title: 'Data Content', description:'Data Content'],
                [title: 'Not Operational', description:'Not Operational'],
                [title: 'Lack of Documentation', description:'Lack of Documentation']
        ]

        states = [
                [title: 'Active', description:'Active description', isPublished: true],
                [title: 'Beta', description:'Beta description', isPublished: false],
                [title: 'Deprecated', description:'Deprecated description', isPublished: false],
                [title: 'Planned', description:'Planned description', isPublished: false],
                [title: 'Retired', description:'Retired description', isPublished: false]
        ]

        owfWidgetTypes = [
                [title: 'standard', description: "Widget will appear in OWF's Launch Menu"],
                [title: 'metric', description: "Widget will appear under the Metric button on OWF's toolbar"],
                [title: 'marketplace', description: "Widget will appear under the Marketplace button on OWF's toolbar"],
                [title: 'administration', description: "Widget will appear under the Administration button on OWF's toolbar"],
        ]

        intentDataTypes = [
                [title: 'audio', description: 'Audio file. SAMPLE DATA TYPE.'],
                [title: 'image', description: 'Image file. SAMPLE DATA TYPE.'],
                [title: 'json', description: 'JSON document. SAMPLE DATA TYPE.'],
                [title: 'text', description: 'Textual information. SAMPLE DATA TYPE.'],
                [title: 'uri', description: 'URI - Uniform Resource Identifier. SAMPLE DATA TYPE.'],
                [title: 'video', description: 'Video file. SAMPLE DATA TYPE.'],
        ]

        intentActions = [
                [title: "share", description: "The share intent is designed to give applications the ability to offer a simple mechanism for sharing data from the current page."],
                [title: "edit", description: "The edit intent is designed to give applications the ability to offer a simple mechanism to edit data from the current page."],
                [title: "view", description: "The view intent is designed to give applications the ability to offer a simple mechanism to view data in their application."],
                [title: "pick", description: "The pick intent is designed to give services the ability to allow their users pick files from their service for use in a client application."],
                [title: "subscribe", description: "The subscribe intent is designed to give applications the ability to offer a simple mechanism for subscribing to data from the current page"],
                [title: "save", description: "The Save intent is designed to give applications the ability to offer a simple mechanism to save data in their application."],
        ]
    }
}

println("Loaded Franchise Store metadata config info.")
