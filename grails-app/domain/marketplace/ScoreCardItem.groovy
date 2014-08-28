package marketplace

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.codehaus.groovy.grails.web.json.JSONObject

@gorm.AuditStamp
class ScoreCardItem implements Serializable {


    String question

    String description

    String image

    Boolean showOnListing = false

    private static final int SMALL_FIELD_SIZE = 250

    static constraints = {
        question(blank: false, nullable: false, maxSize: SMALL_FIELD_SIZE, unique: false)
        description(blank: false, nullable: false, maxSize: 500, unique: false)
        image(nullable: true, maxSize: SMALL_FIELD_SIZE, unique: false)
        showOnListing(blank: true, nullable: true)
    }

    static mapping = {
        cache true
    }

    JSONObject asJSON() {
      new JSONObject([
              id: id,
              question: question,
              description: description,
              image: image,
              showOnListing: showOnListing
      ])
    }

    @Override
    int hashCode() {
        new HashCodeBuilder()
            .append(question)
            .append(description)
            .toHashCode()
    }

    @Override
    boolean equals(other) {
        if (other instanceof ScoreCardItem) {
            return new EqualsBuilder()
                .append(question, other.question)
                .append(description, other.description)
                .isEquals()
        }
        return false
    }

    @Override
    String toString() {
        return question + ": True"

    }
}
