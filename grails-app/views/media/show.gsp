<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="${session.marketplaceLayout}" />
  </head>

  <body>

	<div id="marketContentWrapper" class="widget-marketContentWrapper-wleftbar">
    	<div class="body">
          <div id="marketContent" style="padding-top: 2%; ">

          		<div id= "videoContent" style="padding-top: 2%; height:340px;">

          			<span id="videoTag" style="padding-left: 3%;">
                            <img src="${request.contextPath}/static/video/Introduction_to_OZONE/Introduction_to_OZONE_Tutorials.png" style="width:400px;height:225px;"/>
					</span>

					<span id= "contentInfo" style=" padding-left:2%; position:absolute;">
						<h1 id= "video-title">Introduction to OZONE</h1>
					</span>

          			<div class="video_shadow_large"></div>

          		</div>


          		<div class="border-top" style="top:25px;position:relative;"></div>


          		<div id= "mediaNavigation" style="padding-top:4%;">
		            <div id="media-tabs" class="media-tabs">
		                <button type="button" href="#all-tab"    class="tab-button all-button">All Tutorials</button>
		                <button type="button" href="#novice-tab" class="tab-button novice-button" style="width:150px;">Novice Videos</button>
		                <button type="button" href="#expert-tab" class="tab-button expert-button" style="width:150px;">Expert Videos</button>
		                <button type="button" href="#guides-tab" class="tab-button guides-button" style="width:100px;">Guides</button>
		                <button type="button" href="#slideshow-tab" class="tab-button slideshow-button" style="width:150px;">Slideshows</button>
		            </div>

		            <div id= "all-tab" class="container" style="padding-top:4%;">
		            	<g:render template="/media/media_list" var="fileMap" bean='${fileMap}' />
		            </div>

		            <div id= "novice-tab" class="container" style="padding-top:4%;">
		            	<g:render template="/media/media_list" var="fileMap" bean='${noviceMedia}' />
		            </div>

		            <div id= "expert-tab" class="container" style="padding-top:4%;">
		            	<g:render template="/media/media_list" var="fileMap" bean='${expertMedia}' />
		            </div>

		            <div id= "guides-tab" class="container" style="padding-top:4%;">
		            	<g:render template="/media/media_list" var="fileMap" bean='${guideMedia}' />
		            </div>

		            <div id= "slideshow-tab" class="container" style="padding-top:4%;">
		            	<g:render template="/media/media_list" var="fileMap" bean='${slideshowMedia}' />
		            </div>

          		</div>
	      </div>
	    </div>
    </div>

    <script type="text/javascript" >
		jQuery(document).ready(function() {
			 var $ = jQuery;

			$('.omp_user_search').hide();

			Marketplace.applyAutoEllipsis(".listing_short_title_text", 32);

			$('.tab-button:not(:first)').addClass('inactive');
			$('.container:not(:first)').hide();

			$('.tab-button:first').removeClass('inactive');
			$('.container:first').show();

			$('.tab-button').click(function(){
				var t = $(this).attr('href');

				if($(this).hasClass('inactive')){
					$('.tab-button').addClass('inactive');
				    $(this).removeClass('inactive');
				    $('.container').hide();
				    $(t).fadeIn('slow');
				}

			    return false;
			});

			$('#vidPlayer').hover(
						function(){$(this).attr('controls',true)},
					    function(){$(this).attr('controls',false)});

			$('#vidPlayer').on('play', function (e){ $(".media_length_big").hide();});
			$('#vidPlayer').on('pause',  function (e){ $(".media_length_big").show();});

			$('#vidPlayer').on('play', function (e){ $(".video_shadow_large").hide();});
			$('#vidPlayer').on('pause',  function (e){ $(".video_shadow_large").show();});

			$('.media_badge_container').click(function(){
				var mediaTitle = $(this).children('#nameData').val();
				var mediaDescription = $(this).children('#descData').val();
				var mediaLength = $(this).children('#lengthData').val();
				var mediaType = $(this).children('#typeData').val().toLowerCase();
				var mediaDisplayName = $(this).children('#displayNameData').val();

				if(mediaType == 'slideshow') {
					//launch pdf here
					window.open('../static/media/'+mediaTitle + '/' + mediaTitle + '.ppt');
				} else if(mediaType == 'guide') {
					//launch pdf here
					window.open('../static/media/'+mediaTitle + '/' + mediaTitle + '.pdf');
				} else {
				    $('#contentInfo').children('#video-title').text(mediaDisplayName);
				    $('#contentInfo').children('#video-description').text(mediaDescription);
				    $('.large_ribbon_time').text(mediaLength);
				    $('.large_ribbon_title').text(mediaDisplayName);

					$('#videoTag').html("<video id='vidPlayer'  poster=../static/media/" + mediaTitle + "/" + mediaTitle +"_Large.png width='400px' height='300px'>"+
							 "<source src="+ mediaTitle +"/"+ mediaTitle +".mp4 type='video/mp4'>"+
						  	 "<source src="+ mediaTitle +"/"+ mediaTitle +".webm type='video/webm'>"+
							 "<source src="+ mediaTitle +"/"+ mediaTitle +".ogv type='video/ogv'>"+
							 "<source src="+ mediaTitle +"/"+ mediaTitle +".ogg type='video/ogg'>"+

						  	 "<object type='application/x-shockwave-flash' data="+ mediaTitle +"/"+ mediaTitle +".swf width='400px' height='300px'>"+
								   "<param name='allowfullscreen' value='true'>"+
								   "<param name='allowscriptaccess' value='always'>"+
								   "<param name='flashvars' value="+ mediaTitle +"/"+ mediaTitle +".mp4>"+
						  	  	   "<param name='movie' value="+ mediaTitle +"/"+ mediaTitle +".swf>"+
						  	  	   "<img src=../static/media/" + mediaTitle + "/" + mediaTitle +"_Large.png  width='400px' height='300px'/>"+
							  "</object>"+
							  "<p style='position:absolute; top:300px; left:775px;'>Your browser does not support playing video<br/>" +
								"<a href="+ mediaTitle +"/"+ mediaTitle +".mp4 style='left:75px;position:relative;'>Download video here.</a>"+
							  "</p>"+
							"</video>");

                    // not IE7?
					if (!$('html').hasClass('ie7')) {
                        // load and play the video
						$('#videoContent video')[0].load();
						$('#videoContent video')[0].play();
					}

					$('#vidPlayer').hover(
							function(){$('#vidPlayer').attr('controls',true)},
						    function(){$('#vidPlayer').attr('controls',false)});


					$('#vidPlayer').on('play', function (e){ $(".media_length_big").hide();});
					$('#vidPlayer').on('pause',  function (e){ $(".media_length_big").show();});

					$('#vidPlayer').on('play', function (e){ $(".video_shadow_large").hide();});
					$('#vidPlayer').on('pause',  function (e){ $(".video_shadow_large").show();});

				}

			});
		});
	</script>
  </body>
</html>
