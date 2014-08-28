import static org.apache.commons.lang.StringEscapeUtils.escapeSql
import marketplace.Category
import marketplace.ScoreCardItem
import ozone.utils.Utils

databaseChangeLog = {
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
        def createdDate = new Date()
        def defaultCategories = [
            new Category(
                title: "Category A",
                description: "Example Category A",
                createdDate: createdDate,
                editedDate: createdDate
            ),
            new Category(
                title: "Category B",
                description: "Example Category B",
                createdDate: createdDate,
                editedDate: createdDate
            ),
            new Category(
                title: "Category C",
                description: "Example Category C",
                createdDate: createdDate,
                editedDate: createdDate
            ),
            new Category(
                title: "Geospatial",
                description: "Analytics based on geographic data",
                createdDate: createdDate,
                editedDate: createdDate
            ),
            new Category(
                title: "Query",
                description: "Data set retrieval",
                createdDate: createdDate,
                editedDate: createdDate
            ),
            new Category(
                title: "Reporting",
                description: "Data set summarization",
                createdDate: createdDate,
                editedDate: createdDate
            ),
            new Category(
                title: "Temporal",
                description: "Amaltics based on temporal data",
                createdDate: createdDate,
                editedDate: createdDate
            )
        ]

        defaultCategories.each { cat ->
            insert(tableName: 'category') {
                column name: 'title', value: "${escapeSql(cat.title)}"
                column name: 'description', value: "${escapeSql(cat.description)}"
                column name: 'uuid', value: "${Utils.generateUUID()}"
                column name: 'version', valueNumeric: 0
                column name: 'created_date', value: "${escapeSql(cat.createdDate.format('yyyy-MM-dd HH:mm:ss'))}"
                column name: 'edited_date', value: "${escapeSql(cat.editedDate.format('yyyy-MM-dd HH:mm:ss'))}"
            }
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

        // Insert default categories
    changeSet(author: 'marketplace', dbms: 'mssql, mysql, oracle, postgresql, hsqldb, h2', id: 'defaultData-5', context: 'defaultData') {
        def createdDate = new Date()
        def standardQuestions = [
            new ScoreCardItem(
                question: "Is Enterprise Management System (EMS) part of the support structure?",
                description: "In order to satisfy this criterion, the application must be supported with Tier 1 support so that users can access help for any arising issues.",
                image: "/marketplace/themes/common/images/scorecard/ScorecardIcons_EMS_lrg.png",
                createdDate: createdDate,
                editedDate: createdDate,
                showOnListing: true
            ),
            new ScoreCardItem(
                question: "Is the application hosted within the infrastructure of the cloud?",
                description: "In order to satisfy this criterion, the application must be running within the cloud structure. If an application is made up of multiple parts, all parts must be running within the cloud.",
                image: "/marketplace/themes/common/images/scorecard/ScorecardIcons_CloudHost_lrg.png",
                createdDate: createdDate,
                editedDate: createdDate,
                showOnListing: true
            ),
            new ScoreCardItem(
                question: "Does the application elastically scale?",
                description: "In order to satisfy this criterion, the application must be able to dynamically handle how many users are trying to access it. For instance, if a low number of users are accessing the App Component a small number of resources are used; if a large number of users are accessing the App Component, the App Component scales to take advantage of additional resources in the cloud.",
                image: "/marketplace/themes/common/images/scorecard/ScorecardIcons_Scale_lrg.png",
                createdDate: createdDate,
                editedDate: createdDate,
                showOnListing: true
            ),
            new ScoreCardItem(
                question: "Does this system operate without license constraints?",
                description: "In order to satisfy this criterion, the system should operate without constraining the user to interact with it.",
                image: "/marketplace/themes/common/images/scorecard/ScorecardIcons_LicenseFree_lrg.png",
                createdDate: createdDate,
                editedDate: createdDate,
                showOnListing: true
            ),
            new ScoreCardItem(
                question: "Is the application data utilizing cloud storage?",
                description: "In order to satisfy this criterion, the application's data must be within cloud storage. If an application utilizes multiple data resources, all parts must utilize cloud storage.",
                image: "/marketplace/themes/common/images/scorecard/ScorecardIcons_CloudStorage_lrg.png",
                createdDate: createdDate,
                editedDate: createdDate,
                showOnListing: true
            ),
            new ScoreCardItem(
                question: "Is the application accessible through a web browser?",
                description: "In order to satisfy this criterion, the application must be accessible via an URL/URI that can be launched by a web browser.",
                image: "/marketplace/themes/common/images/scorecard/ScorecardIcons_Browser_lrg.png",
                createdDate: createdDate,
                editedDate: createdDate,
                showOnListing: true
            )
        ]

        standardQuestions.each { question ->
            insert(tableName: 'score_card_item') {
                column name: 'question', value: "${escapeSql(question.question)}"
                column name: 'description', value: "${escapeSql(question.description)}"
                column name: 'image', value: "${escapeSql(question.image)}"
                column name: 'version', valueNumeric: 0
                column name: 'created_date', value: "${escapeSql(question.createdDate.format('yyyy-MM-dd HH:mm:ss'))}"
                column name: 'edited_date', value: "${escapeSql(question.editedDate.format('yyyy-MM-dd HH:mm:ss'))}"
                column name: 'show_on_listing', valueBoolean: question.showOnListing
            }
        }
    }

    changeSet(author: 'marketplace', dbms: 'oracle', id: 'defaultData-6', context: 'defaultData') {
        comment("Drop the trigger")
        sql(endDelimiter: "", splitStatements: false, sql: """
            drop trigger score_card_item_insert;
        """)
    }
}
