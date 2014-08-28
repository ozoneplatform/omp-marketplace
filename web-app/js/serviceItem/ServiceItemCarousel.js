function configureCarousels(){
    $('#landingPageRowSpan').fadeIn('slow');

    //Cache the selector because we will use this later as well
    var carouselDivs = $('.carousel');

    carouselDivs.find('ul').each(function(index) {
        $(this).bxSlider({
            pager: true,
            buildPager: function(slideIndex) {
                // return an empty link so just the background image for the page will show but it will be properly styled
                return '<a href=""></a>';
            },
            prevText: '',
            nextText: ''
        });
    });

    $("a.bx-prev:not(:first)").hide();
    $("a.bx-next:not(:first)").hide();
    $("a.bx-prev:first").show();
    $("a.bx-next:first").show();

    $("div.carousel-invisible").mouseover(function() {
        $(this).find("a.bx-prev").show();
        $(this).find("a.bx-next").show();
    }).mouseout(function() {
        $(this).find("a.bx-prev").hide();
        $(this).find("a.bx-next").hide();
    });
}
