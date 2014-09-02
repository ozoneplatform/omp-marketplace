$common_theme_path = "../../common" #location of marketplace's stylesheets and images that are common to mutiple themes
$image_path = "../images"  #path to the images directory in this theme

# sass_path: the directory your Sass files are in. THIS file should also be in the Sass folder
sass_path = File.dirname(__FILE__)

# css_path: the directory you want your CSS files to be.
# Generally this is a folder in the parent directory of your Sass files
css_path = File.join(sass_path, "..", "css")

# output_style: The output style for your compiled CSS
# nested, expanded, compact, compressed
# More information can be found here http://sass-lang.com/docs/yardoc/file.SASS_REFERENCE.html#output_style
output_style = :expanded

#We need to load the MARKETPLACE Common Stylesheets
load File.join(File.dirname(__FILE__), $common_theme_path)

#Add "local to theme" variable path
load File.join(File.dirname(__FILE__), File.join(sass_path, "variables"))

$vendor_path = "../../../vendor"
add_import_path File.join(File.dirname(__FILE__), $vendor_path + "/select2-3.4.5")
add_import_path File.join(File.dirname(__FILE__), $vendor_path + "/bootstrap-datepicker")
