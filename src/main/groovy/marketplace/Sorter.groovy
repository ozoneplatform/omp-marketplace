package marketplace

import marketplace.Constants.SortDirection

/**
 * A Comparator to facilitate transportation and usage of sorting parameters
 */
class Sorter<T> implements Comparator<T> {
    final SortDirection direction
    final String sortField

    public Sorter(SortDirection direction, String sortField) {
        this.direction = direction
        this.sortField = sortField
    }

    @Override
    int compare(T a, T b) {
        a[sortField].compareTo(b[sortField]) *
            (direction == SortDirection.ASC ? 1 : -1)
    }
}
