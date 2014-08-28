package ozone.utils

import groovy.lang.Closure

/****
 * Works like:
 * 
 * class SomeClass {
 *   static something = {
 *       key1 "value1"              // calls methodMissing("key1", ["value1"])
 *       key2("value2")             // calls methodMissing("key2", ["value2"])
 *       key3 = "value3"            // calls propertyMissing("key3", "value3")
 *       key4 "foo", "bar", "baz"   // calls methodMissing("key4", ["foo","bar","baz"])
 *       key5 mapKeyFoo:mapValueFoo //calls methodMissing("key5", [mapKeyFoo:mapValueFoo]
 *   }
 * }
 *
 *
 * def closureValueMap = ClosureInterrogator.extractValuesFromClosure(new SomeClass().something)
 * 
 * assert "value1" == closureValueMap."key1"  // calls propertyMissing("key1")
 * assert "value2" == closureValueMap."key2"  // calls propertyMissing("key2")
 * assert "value3" == closureValueMap."key3"  // calls propertyMissing("key3")
 * assert ["foo", "bar", "baz"] == closureValueMap."key4"  // calls propertyMissing("key4")
 * assert new ArrayList<Map>([mapKeyFoo:mapValueFoo]) == closureValueMap."key5" //calls propertyMissing("key5")
 * 
 */
class ClosureInterrogator {
    private Map closureValueMap = [:]

    static Map extractValuesFromClosure(Closure closure) {
        def interrogator = new ClosureInterrogator(closure)
        return interrogator.closureValueMap
    }

    private ClosureInterrogator(Closure closure) {
        def oldResolveStrategy = closure.getResolveStrategy()
        def oldDelegate = closure.getDelegate()
        closure.delegate = this
        closure.resolveStrategy = Closure.DELEGATE_FIRST

        try {
            closure()
        } finally {        
            closure.setDelegate(oldDelegate)
            closure.setResolveStrategy(oldResolveStrategy)
        }
    }

    // property getter
    def propertyMissing(String name) {
        return closureValueMap[name]
    }

    // property setter
    def propertyMissing(String name, value) {
        closureValueMap[name] = value
    }

    def methodMissing(String name, args) {
		if(args.size() == 0){return}
		if (args.size() == 1) {
			if(args[0] instanceof Map){
				if(closureValueMap[name] == null){
					def entryArray = new ArrayList<Map>()
					entryArray.add(args[0])
					closureValueMap[name] = entryArray
				}else{
					((List<Map>)closureValueMap[name]).add(args[0])
				}
			}else{
            	closureValueMap[name] = args[0]
			}
        } else {
            closureValueMap[name] = args
        }
    }
}
