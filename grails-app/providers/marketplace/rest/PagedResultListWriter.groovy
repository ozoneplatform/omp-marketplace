package marketplace.rest

import grails.orm.PagedResultList

import javax.ws.rs.Produces
import javax.ws.rs.ext.Provider

@Provider
@Produces(['text/x-json', 'application/json'])
class PagedResultListWriter extends AbstractMessageBodyWriter<PagedResultList> {
    PagedResultListWriter() {
        super(PagedResultList.class)
    }

    @Override
    protected Map toBodyMap(PagedResultList list) {
        [ total: list.totalCount, data: list ]
    }
}
