package marketplace

class ObserveTagLib {

    def observe = { attrs ->
        if (!attrs.noScript) {
            out << '<script type="text/javascript">'
        }

        if (attrs.element && attrs.element instanceof String) {
            printObserve("\$('${attrs.element}')", attrs.event, attrs.function, out)
        }

        if (attrs.element && attrs.element instanceof List) {
            attrs.element.each {
                it -> printObserve("\$('${it}')", attrs.event, attrs.function, out)
            }
        }

        if (attrs.classes && attrs.classes instanceof String) {
            printObserveClass(attrs.classes, attrs.event, attrs.function, out)
        }

        if (attrs.classes && attrs.classes instanceof List) {
            attrs.classes.each {
                it -> printObserveClass(it, attrs.event, attrs.function, out)
            }
        }

        if (!attrs.noScript) {
            out << '</script>'
        }
    }

    def printObserveClass(className, event, function, out) {
        out << "var classes = \$\$('.' + '${className}');"
        out << "for(i = 0; i < classes.length; i++) {"
        printObserve("classes[i]", event, function, out)
        out << "}"
    }

    def printObserve(element, event, function, out) {
        if (event && event instanceof String) {
            out << "${element}.observe('${event}', ${function});"
        }

        if (event && event instanceof List) {
            attrs.event.each { it ->
                out << "${element}.observe('${it}', ${function});"
            }
        }
    }

}
