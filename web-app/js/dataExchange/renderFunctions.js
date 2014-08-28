define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars'

], function($, _, Backbone, Handlebars){



// For rows
Handlebars.registerHelper ('displayArray', function(array) {

    return  _.map(array,
                function(val) {return val.trim()}).join(', ');
});

// Adds spaces after commas in the text so it will wrap
// Input is comma-delimited string of values
Handlebars.registerHelper ('displayInt', function(string) {

   return  _.map(string.split(','),
                function(val) {return val.trim()}).join(', ');

});

// Renders an icon for duplicate listings
Handlebars.registerHelper('showDupIcon', function(duplicate){
    return (duplicate) ? 'duplicate' : '';
});

// Used in NavItemView
Handlebars.registerHelper ('activate', function(active) {
    return (active) ? 'active' : '';
});

Handlebars.registerHelper ('setVisited', function(visited) {
    return (visited) ? 'already-visited' : '';
});

Handlebars.registerHelper ('enable', function(visited, active) {

    return (!visited) ? 'disabled' : '';
});

// in resolveDifferences
Handlebars.registerHelper ('fieldHeader', function(differenceType) {
    if(differenceType === 'type') return 'Type';
    if(differenceType === 'category')  return 'Category';
    if(differenceType === 'custom field') return 'Field';
    if(differenceType === 'agency') return 'Agency';
});

Handlebars.registerHelper ('actionHeader', function(differenceType) {

    return 'Action';
});

    Handlebars.registerHelper ('getTitleMarkup', function(sourceItemTitle) {
        return (sourceItemTitle === 'no agency') ? 'class=noAgencyTitle' : '';
    })
});


