define([
    'BootstrapUtil',
    'jquery',
    'sinon'
], function(BootstrapUtil, $, sinon) {
    describe('BootstrapUtil', function() {
        describe('attachTooltip', function() {
            var title = 'Title';

            function createEl() {
                return $('<div>');
            }

            it('calls the tooltip plugin on a jquery element', function() {
                var el = createEl();

                var tooltipStub = el.tooltip = sinon.spy();

                BootstrapUtil.attachTooltip(el, {
                    title: title
                });

                expect(tooltipStub.calledWithMatch({ title: title })).to.be.ok();
            });

            it('adds the tooltip to a specified container', function() {
                var container = createEl(),
                    containerCls = 'container',
                    el = createEl();

                var tooltipStub = el.tooltip = sinon.spy();

                container.append(el).addClass(containerCls);
                $('body').append(container);

                BootstrapUtil.attachTooltip(el, {
                    title: title,
                    container: '.' + containerCls
                });

                //bootstrap-active class should be added to the container element
                expect(container.hasClass('bootstrap-active')).to.be.ok();

                expect(tooltipStub.calledWithMatch({
                    title: title,
                    container: '.' + containerCls
                })).to.be.ok();
            });
        });
    });
});
