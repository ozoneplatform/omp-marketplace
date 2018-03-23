var LISTSPACERVALUE=0;

function nothing() {
}

//Returns first selected item or null
function getSelectedOption(ctrl)
{
	var selected = null;
	for (i = 0; i < ctrl.length; i++)
  	{
  		if (ctrl[i].selected)
  			return ctrl.options[i];
  	}
  	return null;
}

function isAnOptionSelected(ctrl)
{
	var selected = getSelectedOption(ctrl);
	return ( (selected != null) && (selected.value != ""));
}

function buildSelectedList(source, delim)
{
 // Bulids a comma seperated list of selected items in a listbox
 // Get all items in the listbox i.e.  DisplayText1=Value1|DisplayText2=Value2

 var list = '';

 for( i = 0; i < source.length; i++ ) {
  if (source.options[i].selected) {
    if( list.length == 0 ) {
      list = source.options[i].value;
      }
    else {
      list = list + delim + source.options[i].value;
    }
  }
 }

 return list;
}
function buildSelectedTextList(source, delim)
{
 // Bulids a comma seperated list of selected items in a listbox
 // Get all items in the listbox i.e.  DisplayText1=Value1|DisplayText2=Value2

 var list = '';

 for( i = 0; i < source.length; i++ ) {
  if (source.options[i].selected) {
    if( list.length == 0 ) {
      list = source.options[i].text;
      }
    else {
      list = list + delim + source.options[i].text;
    }
  }
 }

 return list;
}


function clearList(listObj)
{
var idx;
for (idx=listObj.options.length-1; idx>=0; idx--)
   listObj.options[idx]=null;
}

// This function checks for the existence of data inside of a select box
function fieldContainsData(fieldObj)
{
   var idx = 0;
   var dataFound = false;

   for (;idx<fieldObj.options.length;idx++) {
       if (fieldObj.options[idx].value != "") {
          dataFound = true;
          break;
       }
   }

   return dataFound;
}

/**
 * selectItemByValue - give the value of a select option, find it and make it selected
 */
function selectItemByValue(listObj, value)
{
	var idx = 0;
	for (idx=0; idx<listObj.options.length; idx++)
	{
		if (listObj.options[idx].value == value)
		{
   			listObj.options[idx].selected=true;
   		}
	}
}

function selectAllList(listObj)
{
	var idx;
	for (idx=0; idx<listObj.options.length; idx++)
		listObj.options[idx].selected=true;
}

function deselectItem(listObj,idx)
{
   listObj.options[idx].selected=false;
}

function deselectAllList(listObj)
{
var idx;
for (idx=0; idx<listObj.options.length; idx++)
   listObj.options[idx].selected=false;
}


function addItem(text, value, target, title)
{
 isInList = false;

 // Skip adding blank text and common naming conventions
 if ((text.length == 0) || (text == "<Select>")) {
   isInList = true;
  }

 for(i=0; i<target.length && !isInList; i++)
 {
  if(target.options[i].text == text)
  {
   isInList = true;
   break;
  }
 }

 // If the item is not in the list, add it to the list.
 if(isInList != true)
 {
	 var opt = new Option(text, value);
	 if (title){
		 opt.title = title
	 }
	 target.options[target.length] = opt;
 }
 return;
}


function addItemSelected(text, value, target, title)
{
var idx;

addItem(text,value,target, title);

for (idx=0; idx<target.options.length; idx++) {
  if(target.options[idx].text == text)
   target.options[idx].selected=true;
  }
}

function checkListForItem(text, target)
{
   var idx;
   var inList = false;
   for (idx=0; idx < target.options.length; idx++)  {
      if(target.options[idx].text == text)  {
         inList = true;
      }
   }
   return inList;
}
function removeSelected(listObj)
{
	for (var i = listObj.options.length - 1; i >= 0; i--)
    {
    	if (listObj.options[i].selected == true)
   			removeItem(listObj, i);
	}
}

function removeItem(source, index)
{
	if (index >= 0)
	{
		source.options[index] = null;
		source.blur();
	}

	return;
}

var fontWidth=6;
var fontHeight=20;


function addSelectedValues(fromList, toList, selected)
{
for (idx=0; idx<fromList.length; idx++)
  {
  if (fromList.options[idx].selected)
    {
    if (selected)
    {
    	addItemSelected(fromList.options[idx].text,fromList.options[idx].value,toList,fromList.options[idx].title);
    }
    else
    {
    	addItem(fromList.options[idx].text,fromList.options[idx].value,toList,fromList.options[idx].title);
    }
    }
  }
}

function addStringBeforeText(str,textObj)
{
  if (str.length > 0)
    {
    if (textObj.value == "")
      {
      textObj.value += str;
      }
    else
      {
      textObj.value = str + '\n\n' + textObj.value
      }
    }

}

function countSelectedItems(textObj)
{
	var counter = 0;
	for (idx=0; idx<textObj.length; idx++)
  	{
  		if (textObj.options[idx].selected)
    	{
    		counter++;
    	}
  	}
  	return counter;
}
function appendStringToText(str,textObj)
{
  if (str.length > 0)
    {
    if (textObj.value == "")
      {
      textObj.value += str;
      }
    else
      {
      textObj.value += '\n\n' + str;
      }
    }

}

function appendSelectedItemToText(listObj,textObj)
{
  var selectedItem = listObj.options[listObj.selectedIndex].value;

  if (selectedItem != "")
    {
    appendStringToText(selectedItem,textObj);
    }

  listObj.selectedIndex = 0;
}



function listToText(listObj, textObj) {
  textObj.value = buildSelectedList(listObj, ",");
}


// Tokenize the string and add each item to list
function textToList(textObj, listObj, selectIt) {
var val;
var tokens;
var str;

val = textObj.value;
if (val.length > 0) {
  tokens = val.split(",");
  for (idx=0; idx<tokens.length; idx++)
    {
    str = tokens[idx];
    if (selectIt) {
      addItemSelected(str,str,listObj);
      }
    else  {
      addItem(str,str,listObj);
      }
    }
  }
}