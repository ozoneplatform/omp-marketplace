# include the utils rb file which has extra functionality for the marketplace theme
dir = File.dirname(__FILE__)
require File.join(dir, 'lib', 'marketplace_utils.rb')

# register marketplace-common as a compass framework
Compass::Frameworks.register 'marketplace-common', dir
