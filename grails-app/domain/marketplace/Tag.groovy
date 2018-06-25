package marketplace

import org.grails.web.json.JSONObject

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder


class Tag extends AuditStamped implements Serializable, Comparable<Tag>, ToJSON {

    String title

    static searchable = {
        root false
        title index: 'not_analyzed', excludeFromAll: false
        only = ['title']
    }

    static hasMany = [serviceItemTags: ServiceItemTag]

    static mapping = {
        title index: 'tag_title_idx'
    }

    static constraints = {
        title(blank: false, nullable:false, maxSize:16, validator: { val, obj ->
            def tag = Tag.findByTitleIlike(val)
            return (!tag || obj.id == tag.id) ? true : ['unique']
        })
    }

    final static bindableProperties = ['title']
    final static modifiableReferenceProperties = []

    @Override
    int hashCode() {
        new HashCodeBuilder()
            .append(title)
            .toHashCode()
    }

    @Override
    boolean equals(other) {
        if (other instanceof Tag) {
            return new EqualsBuilder()
                .append(title, other.title)
                .isEquals()
        }
        return false
    }

    @Override
    JSONObject asJSON() {
        marshall([id       : id,
                  title    : title,
                  //Note this will cause a transient object exception if a new ServiceItemTag was added in the same transaction
                  itemCount: serviceItemTags?.size() ?: 0])
    }

    String prettyPrint() {
        toString()
    }

    @Override
    String toString() {
        "title: $title"
    }


    static boolean findDuplicates(def obj) {
        obj?.title && Tag.findByTitle(obj.title)
    }


    int compareTo(Tag other) {
         this.title.toLowerCase() <=> other.title.toLowerCase()
    }

    void trim(){
        if(title=~'\\s+'){
            title = title?.replaceAll('\\s+', ' ').trim()
        }
    }

     /**
     * This method is used by the import logic to create a Domain object.
     */
    void bindFromJSON(JSONObject obj) {
        this.with {
            title = obj.title
        }

    }
}
