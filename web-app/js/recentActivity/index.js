define([
    'views/LoadMoreListWithCountView',
    'views/changelog/ChangelogItemView'
],
function(LoadMoreListWithCount, ChangelogItemView) {

    return LoadMoreListWithCount.extend({
        ItemView: ChangelogItemView,
        itemViewOptions: function() {
            return {
                globalActivity: true,
                quickviewTab: this.quickviewTab
            };
        },
        className: 'changelog-pane recent-activity'
    });
});
