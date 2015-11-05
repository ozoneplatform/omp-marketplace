import static org.apache.commons.lang.StringEscapeUtils.escapeSql
import marketplace.Category
import marketplace.ScoreCardItem
import ozone.utils.Utils

databaseChangeLog = {

	property([name:"showOnListing", value:"true", dbms:"postgresql"])
	property([name:"showOnListing", value:"1", dbms:"mysql, oracle, mssql, h2"])


    changeSet(author: 'marketplace', dbms: 'postgresql', id: 'defaultData-1',
                context: 'defaultData') {
        comment('Ensure that the category table has auto-incrementing ids')
        sql("""
            ALTER TABLE category ALTER COLUMN id SET DEFAULT nextval('hibernate_sequence');
        """)
    }
    // add trigger
    changeSet(author: 'marketplace', dbms: 'oracle', id: 'defaultData-1', context: 'defaultData') {
        comment('Create a trigger for category insert')
        sql(endDelimiter: "", splitStatements: false, sql: """
            create or replace trigger category_insert before insert on category
            for each row
            when (new.id is null)
            begin
            select hibernate_sequence.nextval into :new.id from dual;
            end;
            /
            """)
    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, oracle, postgresql, hsqldb, h2',
                id: 'defaultData-2', context: 'defaultData') {
        def defaultCategories = [
            new Category(
                title: "Category A",
                description: "Example Category A"
            ),
            new Category(
                title: "Category B",
                description: "Example Category B"
            ),
            new Category(
                title: "Category C",
                description: "Example Category C"
            ),
            new Category(
                title: "Geospatial",
                description: "Analytics based on geographic data"
            ),
            new Category(
                title: "Query",
                description: "Data set retrieval"
            ),
            new Category(
                title: "Reporting",
                description: "Data set summarization"
            ),
            new Category(
                title: "Temporal",
                description: "Amaltics based on temporal data"
            )
        ]

        defaultCategories.each { cat ->
            update(tableName: 'category') {
                column(name: 'description', value: cat.description)
				where("title = '${escapeSql(cat.title)}'")
            }
            //insert rows where the title does not already exist.
            //
            //NOTE: the FROM line doesn't actually do anything, it is
            //just there to keep liquibase/oracle from complaining about a
            //SELECT-WHERE without a FROM. application_configuration table is
            //used simply because we can guarantee that it will have at least one row
            sql("""
                INSERT INTO category (title, description, version, uuid, created_date, edited_date)
                    SELECT DISTINCT '${escapeSql(cat.title)}', '${escapeSql(cat.description)}', 0,
                        '${Utils.generateUUID()}', \${currentDateFunction},
                        \${currentDateFunction}
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM category
                        WHERE title = '${escapeSql(cat.title)}'
                    )
            """)
        }
    }

    changeSet(author: 'marketplace', dbms: 'oracle', id: 'defaultData-3', context: 'defaultData') {
        comment("Drop the trigger")
        sql(endDelimiter: "", splitStatements: false, sql: """
            drop trigger category_insert;
        """)
    }

    changeSet(author: 'marketplace', dbms: 'oracle', id: 'defaultData-4', context: 'defaultData') {
        comment('Create a trigger for score_card_item insert')
        sql(endDelimiter: "", splitStatements: false, sql: """
            create or replace trigger score_card_item_insert before insert on score_card_item
            for each row
            when (new.id is null)
            begin
            select hibernate_sequence.nextval into :new.id from dual;
            end;
            /
            """)
    }		
		
       // Insert standardScorcard Questions for Postgres
    changeSet(author: 'marketplace', dbms: 'mssql, mysql, oracle, postgresql, hsqldb, h2', id: 'defaultData-5', context: 'defaultData') {
        def createdDate = new Date()
        def standardQuestions = [
            new ScoreCardItem(
                question: "Is Enterprise Management System (EMS) part of the support structure?",
                description: "In order to satisfy this criterion, the application must be supported with Tier 1 support so that users can access help for any arising issues.",
                image: "/marketplace/themes/common/images/scorecard/ScorecardIcons_EMS_lrg.png",
                createdDate: createdDate,
                editedDate: createdDate,
                showOnListing: 1
            ),
            new ScoreCardItem(
                question: "Is the application hosted within the infrastructure of the cloud?",
                description: "In order to satisfy this criterion, the application must be running within the cloud structure. If an application is made up of multiple parts, all parts must be running within the cloud.",
                image: "/marketplace/themes/common/images/scorecard/ScorecardIcons_CloudHost_lrg.png",
                createdDate: createdDate,
                editedDate: createdDate,
                showOnListing: 1
            ),
            new ScoreCardItem(
                question: "Does the application elastically scale?",
                description: "In order to satisfy this criterion, the application must be able to dynamically handle how many users are trying to access it. For instance, if a low number of users are accessing the App Component a small number of resources are used; if a large number of users are accessing the App Component, the App Component scales to take advantage of additional resources in the cloud.",
                image: "/marketplace/themes/common/images/scorecard/ScorecardIcons_Scale_lrg.png",
                createdDate: createdDate,
                editedDate: createdDate,
                showOnListing: 1
            ),
            new ScoreCardItem(
                question: "Does this system operate without license constraints?",
                description: "In order to satisfy this criterion, the system should operate without constraining the user to interact with it.",
                image: "/marketplace/themes/common/images/scorecard/ScorecardIcons_LicenseFree_lrg.png",
                createdDate: createdDate,
                editedDate: createdDate,
                showOnListing: 1
            ),
            new ScoreCardItem(
                question: "Is the application data utilizing cloud storage?",
                description: "In order to satisfy this criterion, the application's data must be within cloud storage. If an application utilizes multiple data resources, all parts must utilize cloud storage.",
                image: "/marketplace/themes/common/images/scorecard/ScorecardIcons_CloudStorage_lrg.png",
                createdDate: createdDate,
                editedDate: createdDate,
                showOnListing: 1
            ),
            new ScoreCardItem(
                question: "Is the application accessible through a web browser?",
                description: "In order to satisfy this criterion, the application must be accessible via an URL/URI that can be launched by a web browser.",
                image: "/marketplace/themes/common/images/scorecard/ScorecardIcons_Browser_lrg.png",
                createdDate: createdDate,
                editedDate: createdDate,
                showOnListing: 1
            )
        ]
			standardQuestions.each { question ->
            //insert rows where the question does not already exist.
            //
            //NOTE: the FROM line doesn't actually do anything, it is
            //just there to keep liquibase/oracle from complaining about a
            //SELECT-WHERE without a FROM. application_configuration table is
            //used simply because we can guarantee that it will have at least one row
            sql("""
                INSERT INTO score_card_item (question, description, image, version, created_date, edited_date, show_on_listing)
                    SELECT DISTINCT '${escapeSql(question.question)}', '${escapeSql(question.description)}', '${escapeSql(question.image)}', 0,
                        \${currentDateFunction}, \${currentDateFunction}, \${showOnListing}
                    FROM application_configuration
                    WHERE NOT EXISTS (SELECT id FROM score_card_item
                        WHERE question = '${escapeSql(question.question)}'
                    )
            """)
        }
    }

    changeSet(author: 'marketplace', dbms: 'oracle', id: 'defaultData-6', context: 'defaultData') {
        comment("Drop the trigger")
        sql(endDelimiter: "", splitStatements: false, sql: """
            drop trigger score_card_item_insert;
        """)
		}
	
}