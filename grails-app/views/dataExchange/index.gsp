<%@ page import="marketplace.ImportTask" %>
<!DOCTYPE html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="${session.marketplaceLayout}" />
    <title>Import and Export listings</title>

    <script>
      jQuery(document).ready(function($) {
            //if the table is empty, hide it
        	if (jQuery('#data-exchange-log-table > tbody > tr').size() === 0) {
            	jQuery('#data-exchange-log-table').css('display', 'none');
        	}
        	else {
  	      	jQuery('#data-exchange-log-emptymsg').css('display', 'none');
    		}
      });

      function addImportLogRow(date, message)
      {
          jQuery('#data-exchange-log-table tbody').prepend("<tr><td>" + date + "</td><td>" + message + "</td></tr>")
          // Make sure the table is showing and the empty message is not.
          jQuery('#data-exchange-log-table').css('display', 'block');
          jQuery('#data-exchange-log-emptymsg').css('display', 'none');

      }
    </script>

</head>
<body>
    <div id="marketContentWrapper">
        <div class="bootstrap-active">
            <div class="body">
            <div id="marketContent">
                <h5 class="admin-home-title inline" ><i class="icon-home"></i><g:link controller="administration"><g:message code="admin.home"  encodeAs="HTML" /></g:link></h5>

                <h1 class="data-exchange-header">Data Exchange</h1>
                <div class="export-listings">
                  <h2 class="data-exchange-subheader">Export</h2>

                  <div class="data-exchange-row">

                    <div class="col-left">
                        <p class="subheader-description" style="margin-top:10px; margin-bottom: 0px;">Export listings from the Store</p>

                    </div>
                    <div class="col-right">
                      <button id="show-export-wizard"  class="btn">Export</button>
                    </div>

                  </div>
                </div>

                <div class="import-listings">
                  <h2 class="data-exchange-subheader">Import</h2><br />
                  <h3 class="import-subheader">Import from File</h3>
                  <p class="subheader-description">Import listings into the Store</p>
                  <div class="data-exchange-row">
                    <iframe id="hidden-upload-frame" name="hidden-upload-frame" style="display: none">
                    </iframe>
                    <g:form name="importForm" method="POST" action="importFromFile" enctype="multipart/form-data" target="hidden-upload-frame">
                      <div class="col-left">
                        <div class="fileupload fileupload-new" data-provides="fileupload">
                          <div class="uneditable-input">
                            <span class="fileupload-preview">Choose file</span>
                          </div>
                          <span class="btn btn-file">
                            <span class="fileupload-new">Browse</span>
                            <input type="file" name="file" />
                          </span>
                        </div>
                      </div>
                      <div class="col-right">
                        <button id="show-wizard" type="submit" class="btn">Import</button>
                      </div>
                    </g:form>
                  </div>
                  <div class="file-upload-warning"></div>
                </div>

                <div class="data-exchange-log">
                  <h3 class="import-subheader">Import Log</h3>
                  <p class="subheader-description">View import history</p>
                  <p id="data-exchange-log-emptymsg">No history</p>
                  <table id="data-exchange-log-table" class="table table-hover">
                    <thead>
                      <tr>
                        <th>Date</th>
                        <th>Result</th>
                      </tr>
                    </thead>
                    <tbody>
                      <g:each in="${importTaskHistory.runs}">
                        <tr>
                          <td><g:formatDate format="MM/dd/yyyy HH:mm" date="${it.runDate}" locale="${Locale.US}"/></td>
                          <td>${it.message.encodeAsHTML()}</td>
                        </tr>
                      </g:each>
                    </tbody>
                  </table>
                  <g:form name="clearLogForm" action="deleteAllResults">
                  	<input type="submit" value="Clear Log" class="btn">
                  </g:form>
                </div>
              </div>
            </div>
        </div>
    </div>

<script>
    (function ($, require) {

        $("#hidden-upload-frame").on('load', function() {
            // OP-3255: Create a separate requirejs context for the import wizard.
            // This sets up the correct paths for importWizard/app.js to load
            // the listed paths, since this load event handler
            // fires immediately when the page loads in Firefox 17.
            var importReq = require.config({
                paths: {
                    'jquery': "../vendor/jquery/js/jquery-1.10.2",
                    'bootstrap': '../vendor/bootstrap/bootstrap-2.3.2',
                    'handlebars': "../vendor/handlebars/handlebars-1.0.0",
                    'underscore': "../vendor/lodash.compat",
                    'backbone': "../vendor/backbone/backbone-1.0.0"
                },
                shim: {
                    'jquery': {
                        exports: '$'
                    },
                    'underscore': {
                        deps: [],
                        exports: "_"
                    },
                    'backbone': {
                        deps: ["jquery", "underscore"],
                        exports: "Backbone"
                    },
                    'bootstrap': {
                        deps: ['jquery'],
                        exports: 'bootstrap'
                    },
                    'handlebars': {
                        exports: 'Handlebars'
                    }
                }
            });

            importReq(['dataExchange/importWizard/app'], function(App) {
                App.initialize();
            })
        });

        $("#show-export-wizard").on('click', function() {
            require(['dataExchange/exportWizard/app'], function(App) {
                App.initialize();
            })
        });

    })(jQuery, require);
</script>


<!-- Templates -->
<script id="wizard-template" type="text/x-handlebars-template">
    <div class="bootstrap-active modal-container">
        <div class="body">
        <div class="modal wizard-modal" aria-hidden="true">

            <div class="wizard-modal-header modal-header">
                <button type="button" class="wizard-close close" data-dismiss="modal" aria-hidden="true">×</button>
                <h1 id="wizard-header">{{ title }}</h1>
            </div>

            <div class="pull-left wizard-steps">
                <div class="wizard-nav-container">

                </div>
                <div class="wizard-progress-container">
                    <div class="wizard-progress progress">
                        <div class="bar"></div>
                    </div>
                </div>
            </div>

            <div class="wizard-cards">
                <div class="wizard-card-container">
                    <!-- one of these per card -->

                </div>

                <div class="wizard-modal-footer modal-footer">
                    <div class="wizard-buttons-container">
                    <div class="selection-count pull-left"></div>
                    <button class="btn prev-button pull-left">Back</button>

                    <button class="btn btn-primary next-button">Next</button>
                    <button class="btn cancel-button" data-dismiss="modal" aria-hidden="true">Cancel</button>
                </div>
              </div>
            </div>
        </div>

        <div class="modal-backdrop"></div>
    </div>
    </div>
</script>

<!--Nav template -->
<script id="nav-template" type="text/template">

</script>


<script id="card-template-export" type="text/x-handlebars-template">

  <h2 id="wizard-card-header-export">{{title}}</h2>
  <p id="wizard-card-body-export">{{instructions}}</p>

</script>


<script id="import-card-template" type="text/x-handlebars-template">
  <h2 id="wizard-card-header">{{title}}</h2>
  <p id="wizard-card-body">{{instructions}}</p>
</script>


<!-- Select Listings Template -->
<script id="select-listings-template" type="text/x-handlebars-template">
  <p>Note: Required listings are automatically included in the import. If a listing duplicates one that already exists in the Store, the Store will save the newer listing and delete the older one.</p>
    <label class="radio import-all">
    <input type="radio" name="selectListings" value="all" checked>
    Import all
  </label>
  <label class="radio import-selection">
    <input type="radio" name="selectListings" value="selection">
    Select individual listings to import
  </label>
</script>

<script id="select-listings-table" type="text/x-handlebars-template">
  <table id="select-listings" class="display dataTable table">
    <thead>
    <tr>
        <th><input title="Select All" type='checkbox' id='select-all-listings'></input></th>
        <th>Listing</th>
        <th></th>
        <th>Type</th>
        <th>State</th>
        <th>Categories</th>
        <th>Company</th>
        <th>IsOutside</th>
    </tr>
    </thead>
    <tbody>

    </tbody>
  </table>
</script>

<script id="select-listings-table-row" type="text/x-handlebars-template">
  <td><input type='checkbox' value={{listingId}} class="listing-checkbox" /></td>
  <td>{{listingName}}</td>
  <td class="{{showDupIcon duplicate}}" {{#if duplicate}} rel="tooltip" title="{{duplicateTooltip}}" {{/if}} ></td>
  <td>{{listingType}}</td>
  <td>{{listingState}}</td>
  <td>{{displayArray listingCategories}}</td>
  <td>{{listingAgency}}</td>
  <td>{{isOutside}}</td>
</script>

<script id="import-options-template" type="text/x-handlebars-template">
  <div class="row import-options-row">
    <div class="import-options-left">
      <label class="import-options-label">Reviews</label>
    </div>

    <div class="import-options-right ratings-col">
      <label class="radio" data-toggle="tooltip" title="{{ratingTrueTooltip}}">
      <input type="radio" name="{{ratingName}}" value="true" checked>
        Yes
      </label>
      <label class="radio" data-toggle="tooltip" title="{{ratingFalseTooltip}}">
      <input type="radio" name="{{ratingName}}" value="false">
        No
      </label>
    </div>
  </div>

  <div class="row import-options-row">
    <div class="import-options-left">
      <label class="import-options-label">Profiles</label>
    </div>
    <div class="import-options-right profiles-col">
      <label class="radio" data-toggle="tooltip" title="{{userProfileTrueTooltip}}">
        <input type="radio" name="{{profileName}}" value={{#if data.exportAll}}"true" checked{{else}}"false"{{/if}}>
        All Store Profiles
      </label>
      <label class="radio" data-toggle="tooltip" title="{{userProfileFalseTooltip}}">
        <input type="radio" name="{{profileName}}" value={{#if data.exportAll}}"false"{{else}}"true" checked{{/if}}>
        Listing Owners & Reviewers
      </label>
    </div>
  </div>

</script>

<script id="export-review" type="text/x-handlebars-template">
  <p class="review-message">{{message}}</p>

  {{#if listings}}
    <ul class="review-list">
    {{#each listings}}
      <li>
        {{title}}
        {{#if isRequiredListing}}
          <img src="../images/icons/RequiredListings.png" class="required-listing-img" alt="Required Listing" data-toggle="tooltip" title="This listing is required by another listing">
        {{/if}}
      </li>
    {{/each}}
    </ul>
    <br />
  {{/if}}
</script>

<script id="import-review" type="text/x-handlebars-template">
  <p class="review-message">{{message}}</p>

  {{#if listings}}
    <p class="review-toggle">{{listings.length}} Listings</p>
    <div class="review-section">
      <ul class="review-list">
      {{#each listings}}
        <li>
          {{title}}
          {{#if isRequiredListing}}
            <img src="../images/icons/RequiredListings.png" class="required-listing-img" alt="Required Listing" data-toggle="tooltip" title="This listing is required by another listing">
          {{/if}}
        </li>
      {{/each}}
      </ul>
      <br />
    </div>
  {{/if}}

  {{#if types}}
  <p class="review-toggle">{{types.length}} New Types</p>
    <div class="review-section">
      <ul class="review-list">
      {{#each types}}
        <li>
          {{title}}
        </li>
      {{/each}}
      </ul>
      <br />
    </div>
  {{/if}}

  {{#if categories}}
    <p class="review-toggle">{{categories.length}} New Categories</p>
    <div class="review-section">
      <ul class="review-list">
      {{#each categories}}
        <li>
          {{title}}
        </li>
      {{/each}}
      </ul>
      <br />
    </div>
  {{/if}}

  {{#if customFields}}
    <p class="review-toggle">{{customFields.length}} New Custom Fields</p>
    <div class="review-section">
      <ul class="review-list">
      {{#each customFields}}
        <li>
          {{name}}
        </li>
      {{/each}}
      </ul>
      <br />
    </div>
  {{/if}}

  {{#if agencies}}
    <p class="review-toggle">{{agencies.length}} New Companies</p>
    <div class="review-section">
      <ul class="review-list">
      {{#each agencies}}
        <li>
          {{name}}
        </li>
      {{/each}}
      </ul>
      <br />
    </div>
  {{/if}}

  {{#if contactTypes}}
    <p class="review-toggle">{{contactTypes.length}} New Contact Types</p>
    <div class="review-section">
      <ul class="review-list">
      {{#each contactTypes}}
        <li>
          {{title}}
        </li>
      {{/each}}
      </ul>
      <br />
    </div>
  {{/if}}
</script>

<script id="import-complete" type="text/x-handlebars-template">
  <p>{{message}}</p>

  {{#if completeData}}
    {{#each completeData}}
      <p class="complete-toggle">{{total}} {{name}}</p>
      <div class="complete-section">
        <ul class="complete-list">
          <li>{{created}} Created</li>
          <li>{{failed}} Failed</li>
          <li>{{updated}} Updated</li>
          <li>{{notUpdated}} Not Updated</li>
        </ul>
        <br />
      </div>
    {{/each}}
  {{/if}}
</script>

<script id="resolve-differences-template" type="text/x-handlebars-template">
  <p>{{instructions}}</p>

  <table id="resolve-differences" class="table">
    <thead>
      <tr>
        <th>{{fieldHeader differenceType}}</th>
        <th>{{actionHeader differenceType}}</th>
      </tr>
    </thead>
    <tbody>

    </tbody>
  </table>
</script>

<script id="no-resolving-template" type="text/x-handlebars-template">
  <p>{{message}}</p>
</script>

<script id="resolve-differences-row" type="text/x-handlebars-template">
  <td {{getTitleMarkup sourceItemTitle}}>
    {{sourceItemTitle}}
  </td>
  <td>
  <select class="selectpicker resolve-select" name="{{sourceItemId}}" data-container=".modal">
    {{#each defaultChoices}}
      <option class="default-choice" value={{choiceValue}}>{{choiceMessage}}</option>
    {{/each}}

    {{#each targetItems}}
      <option value="{{id}}">
        {{#if title}}
          {{title}}
        {{else}}
          {{name}}
        {{/if}}
      </option>
    {{/each}}
  </select>
  </td>
</script>

<script id="filter-options" type="text/x-handlebars-template">
    {{#each config}}
    <dl>
        <dt>{{description}}</dt>
        {{#each options}}
            <dd><label class="checkbox"><input type="checkbox" name="{{name}}" id="{{name}}_checkbox"> {{displayText}} </input></label></dd>
        {{/each}}
    </dl>
    {{/each}}
</script>

<!-- End Templates -->

</body>
</html>
