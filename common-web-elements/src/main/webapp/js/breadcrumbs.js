/*Path Breadcrumbs javascript
Version 0.1 beta
Created by Ted David
Created 5 September 2009
Copyright 2009, Yed David and UltiMate Music.
Bugs? email ted.david@verizon.net
*/

var cookieName = 'SessionPath';
var current_page;					// this holds the URL of the current page
var current_label;					// this holds the title of the current page
var seg_URLs = new Array();		// this holds the full set of navigated paths URLs
var seg_labels = new Array();		// this holds the labels to be used in the bread
var breadcrumbID = 'breadcrumbs'; // this is the id of the target element where breadcrumbs will be written for any page
var cookieValue;
var doc;
var tU = '<URL>'
var tUL = '</URL><LABEL>'
var tL = '</LABEL>'
// function initCookie() is called by <body onload="getBreadcrumb(document, document.location, document.title)" etc.

function getBreadcrumb(D, U, L) // call this in 'body onload' for the default page on the website to initialize the session cookie
{
	if(testSessionCookie() == false) // browser doesn't accept cookies, so no breadcrumbs
	{
		return;
	}
	doc = D;
	current_page = U;
	current_label = L;
	if(!cookieExists(cookieName))
	{
		cookieValue = tU + current_page + tUL + current_label + tL;
		writeSessionCookie(cookieName, cookieValue);
		seg_URLs[0] = current_page;
		seg_labels[0] = current_label;
		writeBreadcrumb(0);
	}
	else
	{
		modCookie();
	}
}
// END initCookie

function modCookie() // call this in 'body onload' for all pages other than the default page for the website
{
	var numSegs = -1;	// number of path segments in visited_path
	var seg;			// data for one segment
	var segSplit;		// index of the first instance of the tag group splitting the segment
	var visited_path;	// to retain processed cookieValue
	var seg_URL;		// the path value in one segment
	var seg_label;		// the label value in one segment
	var labelIndex;		// index into seg of the string ' LABEL '

	cookieValue = getCookieValue(cookieName);		// retrieve the cookie value
	visited_path = cookieValue;						// copy cookieValue for processing
	var segBegin= visited_path.indexOf(tU);			// index of the first instance of the opening tag of the segment
	var segEnd = visited_path.indexOf(tL);			// index of the first instance of the closing tag of the segment
	while(segBegin!= -1)							// extract all segments, starting with the last one
	{
		seg = visited_path.substring(segBegin+ 5, segEnd);		// split a segment from visited_path
		visited_path = visited_path.substr(segEnd + 8);			// remove same segment from visited_path
		segSplit = seg.indexOf(tUL);							// index of the tag splitting the seg
		seg_URL = seg.substring(0, segSplit);					// extract the path part
		seg_label = seg.substr(segSplit + 13);					// extract the label part
		numSegs++;												// increment the number of segments found
		seg_URLs[numSegs] = seg_URL;							// store path in an array
		seg_labels[numSegs] = seg_label;						// store label in an array
		if(visited_path.length > 0)
		{
			segBegin= visited_path.indexOf(tU);			// update segBeginof the trimmed strring visited_path
			segEnd = visited_path.indexOf(tL);
		}
		else
		{
			segBegin= -1;
		}
	}
	if (numSegs == 0) // cases: (a) linked outside of first page opened in site and back; (b) user selects a new page
	{
		if(current_label == seg_labels[0])  // user is back to first page
		{
			cookieValue = tU + current_page + tUL + current_label + tL; // reinitialize the cookie value;
			writeSessionCookie(cookieName, cookieValue);
			writeBreadcrumb(0);
		}
		else
		{
			cookieValue += tU + current_page + tUL + current_label + tL; // add the current path to cookieValue
			writeSessionCookie(cookieName, cookieValue);
			numSegs++;
			seg_URLs[numSegs] = current_page;
			seg_labels[numSegs] = current_label;
			writeBreadcrumb(numSegs);
		}
	}
	else if(numSegs > 0) // only need to look at the last two to do the testing
	{
		if(current_label == seg_labels[numSegs]) // user must have linked out of the site and is back to the same page - rewrite same array
		{
			writeBreadcrumb(numSegs);
		}
		else
		{
			if(current_label == seg_labels[0])  // user is back to first page
			{
				cookieValue = tU + current_page + tUL + current_label + tL; // reinitialize the cookie value;
				writeSessionCookie(cookieName, cookieValue);
				writeBreadcrumb(0);
			}
			else if(current_label == seg_labels[numSegs-1]) // user has clicked the back button - trim the last segment from the path list
			{
				seg_URLs[numSegs]='';
				seg_labels[numSegs]='';
				numSegs--;
				cookieValue = cookieValue.substring(0,cookieValue.lastIndexOf(tU)); // remove last segment from cookieValue
				writeSessionCookie(cookieName, cookieValue);
				writeBreadcrumb(numSegs);
			}
			else // this is a new segment being visited - append it
			{
				numSegs++;
				seg_URLs[numSegs] = current_page;
				seg_labels[numSegs] = current_label;
				cookieValue += tU + current_page + tUL + current_label + tL; // add the new current_page to cookieValue
				writeSessionCookie(cookieName, cookieValue);
				writeBreadcrumb(numSegs);
			}
		}
	}
//debug
//	var msg = 'Number of Segments = ' + numSegs + '\n';
//	for(var i = 0; i<=numSegs; i++)
//	{
//		msg+= '  Segment' + i+1 + ', URL = ' + seg_URLs[i] + ', Label = ' + seg_labels[i] + '\n';
//	}
//	alert(msg);
}
// END modCookie

function testSessionCookie()
{
  document.cookie ="testSessionCookie=Enabled";
  if (getCookieValue("testSessionCookie")=="Enabled")
    return true 
  else
    return false;
}
// END testSessionCookie

function cookieExists(cName)
{
	var exp = new RegExp(encodeURI(cName) +"=");
	if (exp.test(document.cookie))
	{
		return true;
	}
	else
	{
		return false;
	}
}
function getCookieValue(cName)
{
	var exp = new RegExp(encodeURI(cName) + "=([^;]+)");
  	if (exp.test(document.cookie + ";"))
  	{
    	exp.exec(document.cookie + ";");
    	return decodeURI(RegExp.$1);
  	}
 	else
 	{
 		return false;
 	}
}
// END getCookieValue

function writeSessionCookie(cName, cValue)
{
	if (testSessionCookie())
		{
    document.cookie = encodeURI(cName) + "=" + encodeURI(cValue) + "; path=/";
    return true;
  }
  else return false;
}
// END writeSessionCookie

function writeBreadcrumb(numSegs)
{
	var breadcrumb = '';
	for(var i=0; i<numSegs; i++)
	{
		breadcrumb+= '<a href=\"' + seg_URLs[i] + '\">' + seg_labels[i] + '</a> > '
	}
	breadcrumb+= seg_labels[numSegs];
	doc.getElementById(breadcrumbID).innerHTML = breadcrumb;
}
// END writeBreadcrumb

