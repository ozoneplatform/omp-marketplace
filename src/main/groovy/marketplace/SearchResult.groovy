package marketplace

import javax.annotation.Nonnull
import javax.annotation.Nullable

import static java.util.Collections.emptyList
import static java.util.Collections.emptyMap


class SearchResult<T> {

    final @Nonnull List<T> items

    final @Nonnull Integer total

    final @Nonnull Map params

    SearchResult(@Nullable List<T> items,
                 @Nullable Integer total,
                 @Nullable Map params) {
        this.items = items ?: emptyList()
        this.total = total ?: 0
        this.params = params ?: emptyMap()
    }

}
