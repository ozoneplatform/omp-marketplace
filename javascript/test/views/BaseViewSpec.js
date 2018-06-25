define(['views/BaseView', 'jquery', 'sinon'], function(BaseView, $, sinon) {

    describe('Base view', function() {
        var view;

        beforeEach(function () {
            view = new BaseView({
                foo: 'foo'
            });
        });

        afterEach(function () {
            view.remove();
        });

        it('adds itself to jQuery data cache.', function() {
            expect(view.$el.data('view')).to.be(view);
        });

        it('merges all options to the instance.', function() {
            expect(view.foo).to.be('foo');
        });

    });

});