import marketplace.*
import ozone.utils.Utils

testDataConfig {
    sampleData {
        'marketplace.ServiceItem' {
            uuid = { Utils.generateUUID() }
            isHidden = 0
            launchUrl = "http://launch.url.gov"
            imageSmallUrl = "http://launch.url.gov/image.gif"
            imageMediumUrl = "http://launch.url.gov/image.gif"
            imageLargeUrl = "http://launch.url.gov/image.gif"
            totalVotes = 0
            avgRate = 0
            categories = {-> [new Category(title:"Category A"),new Category(title:"Category B")]}
        }
    		'marketplace.Profile' {
    		  	def i = 2
    			  username = {-> "testUser${i++}" }
    		}
    		'marketplace.CustomFieldDefinition' {
    			  allTypes = false
    		}
    		'marketplace.TextCustomFieldDefinition' {
    			  allTypes = false
    		}
    	  'marketplace.DropDownCustomFieldDefinition' {
          fieldValues = { -> [new FieldValue(displayText:"red")]}
    	  }
  	    'marketplace.FieldValue' {
  				isEnabled = 1
        }
    }
}

environments {
    production {
        testDataConfig {
            enabled = false
        }
    }
}
