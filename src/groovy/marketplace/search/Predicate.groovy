package marketplace.search

public interface Predicate {

    String getPredicateName()

    String getStringValue()

    def getValue()

    def getSearchClause()

    boolean isFilter()
}