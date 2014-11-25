<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp"%>
<div id="outlets-heading">
	<dl>
		<dt>
			<h2><pr:snippet name="find-outlets" group="outlets-map" defaultValue="Find Outlets" /></h2>
		</dt>
	</dl>
	<dl>
		<dt>
			<p>
				<pr:snippet name="outlets-map-description" group="outlets-map" defaultValue="If you can't find an outlet near you on the map here, let us
					know and we'll try to fix that. If you know the name of the outlet,
					use the main search box above. If not, enter your location below for
					suggestions." />
			</p>
		</dt>
	</dl>
</div>
<div id="outlets-container">
	<div id="outlets-search">
		<dl>
			<dt>
				<label class="outlets-search-label"><pr:snippet name="outlets-search-label" group="outlets-map" defaultValue="Your location:" /></label>
			</dt>
		</dl>
		<dl>
			<dt>
				<input class="outlets-search-input medium" type="text">
				<button onclick="geoCodingSearch()"><pr:snippet name="outlets-search-button" group="outlets-map" defaultValue="Find outlets" /></button>
			</dt>
		</dl>

		<div class="outlets-search-results">
			<div class="outlets-search-results-label">
				<dl>
					<dt>
						<label class="outlets-search-label"><pr:snippet name="outlet-select-label" group="outlets-map" defaultValue="Select an outlet:" /></label>
					</dt>
				</dl>
			</div>
			<div class="outlets-search-results-list">
				<ul id="outlet-list-parent"></ul>
			</div>
		</div>
	</div>
	<div id="outlets-map" style="padding-bottom:3px">
		<div class="map" id="outlets-map-display"></div>
		<div class="panorama" id="outlets-panorama-display">
			<button id="panorama-button" class="panorama-btn">&times;</button>
		</div>
	</div>
</div>
<script type="text/javascript">
var companiesJSON;
var map;
var infoWindow = new google.maps.InfoWindow();;
var hashMap = new Object();
var closestMarker;
$(document).ready(function() {
	$('.outlets-search-input').val('');
	$('.outlets-search-input').geocomplete();
	$('.outlets-search-results').hide();
	map = new GMaps({
		el : '#outlets-map-display',
		lat : -33.97883008368292,
		lng : 18.463618755340576,
		zoom : 10
	});

	GMaps.geolocate({
		success : function(position) {
			map.setCenter(position.coords.latitude,	position.coords.longitude);
			var myLocation = '<form style="min-width:80px; height:auto;">'
				+ '<div id="bodyContent">';
				myLocation = myLocation	+ '<p>' + "<pr:snippet name="you-are-here" group="outlets-map" escapeJavascript="true" defaultValue="Your are here"/>" + '</p>';
				myLocation = myLocation + '</div>';
				myLocation = myLocation + '</div>';
				myLocation = myLocation + '</form>';

				map.addMarker({
					lat : position.coords.latitude,
					lng : position.coords.longitude,
					title : 'You are here.',
					optimized : false,
					infoWindow : {
						content : myLocation
					}
				});
				requestCompaniesAjax(position.coords.latitude, position.coords.longitude);
		},
		error : function(error) {
			console.log("<pr:snippet name="geolocation-failed" group="outlets-map" escapeJavascript="true" defaultValue="Geolocation failed:"/>" + ' ' + error.message);
		},
		not_supported : function() {
			console.log("<pr:snippet name="geolocation-not-supported" group="outlets-map" escapeJavascript="true" defaultValue="Your browser does not support geolocation"/>");
		}
	});
});

function streetViewPanorama(latitude, longitude) {
	panorama = GMaps.createPanorama({
		el : '#outlets-panorama-display',
		lat : latitude,
		lng : longitude
	});
	google.maps.event.addListener(panorama, 'closeclick', closePanorama);
	$('#outlets-panorama-display').show();
	$('#outlets-map-display').hide();
}

$('#panorama-button').click(function() {
	closePanorama();	
});

function closePanorama() {
	$('#outlets-panorama-display').hide();
	$('#outlets-map-display').show();
}

$('.outlets-search-input').keyup(function(event){	 
	var keycode = (event.keyCode ? event.keyCode : event.which);
	if(keycode == '13') {
		geoCodingSearch();
	 	event.preventDefault();
	 	return false;
	}
});

function geoCodingSearch() {
	closePanorama();
	GMaps.geocode({
		address : $('.outlets-search-input').val(),
		callback : function(results, status) {
			if (status == 'OK') {
				map = new GMaps({
					el : '#outlets-map-display',
					lat : -33.97883008368292,
					lng : 18.463618755340576,
					zoom : 10
				});
				var latlng = results[0].geometry.location;
				map.setCenter(latlng.lat(), latlng.lng());
				map.addMarker({
					lat : latlng.lat(),
					lng : latlng.lng(),
					title : results[0].formatted_address,
					optimized : false,
					infoWindow : {
						content : '<p>' + results[0].formatted_address + '</p>'
					}
				});
				requestCompaniesAjax(latlng.lat(), latlng.lng());
			}
		}
	});
}
	
function requestCompaniesAjax(latitude, longitude) {
	$.ajax({
		type : "GET",
		url : "${ctx}/outlets/companies",
		data: {latitude: latitude, longitude: longitude, productId: ""},
		success : function(JSON) {
			companiesJSON = JSON.companies;
			addLocationsToMap(companiesJSON);
			addLocationList(companiesJSON);
		}
	});
}
	
function addLocationList(companies) {
	$('.outlets-search-results').show();
	var ulElem = $('#outlet-list-parent');
	ulElem.empty();
	if (companies == null
			|| (companies != null && companies.length == 0)) {
		return;
	}
	if (companies != null
			&& companies.length > 0) {
		for (var i = 0; i < 10; ++i) {
			var liElem = document.createElement("li");
			var anchorElem = document.createElement("a");
			var textElem;
			var premiumTextElem;
			textElem = document.createTextNode(companies[i].name);
			if(companies[i].mapDisplay == 'FEATURE') {
				premiumTextElem = document.createTextNode(" (" + "<pr:snippet name="premium-outlet" group="outlets-map" escapeJavascript="true" defaultValue="Premium Outlet"/>" + ")");
			} else {
				premiumTextElem = document.createTextNode("");
			}
			anchorElem.setAttribute("onclick", "focusLocation(" + companies[i].id + ")");
			anchorElem.appendChild(textElem);
			liElem.appendChild(anchorElem);
			liElem.appendChild(premiumTextElem);
			ulElem.append(liElem);
		}
	}
}
	
function focusLocation(companyId) {
	if (companiesJSON != null && companiesJSON.length > 0) {
		var selectedCompany = null;;
		for ( var i = 0; i < companiesJSON.length; ++i) {
			if (companiesJSON[i].id == companyId) {
				selectedCompany = companiesJSON[i];
				break;
			}
		}
		
		$('#outlets-panorama-display').hide();
		$('#outlets-map-display').show();
		
		if (selectedCompany == null) {
			return;
		}
		
		if (selectedCompany.mapDisplay == 'HIDDEN') {
			return;
		}
		
		var iconUrl = '';
		if (selectedCompany.mapDisplay == 'VISIBLE') {
			iconUrl = '${ctx}/img/marker-regular.png';
		} else if (selectedCompany.mapDisplay == 'FEATURE') {
			iconUrl = '${ctx}/img/marker-premium.png';
		} else {
			iconUrl = '${ctx}/img/marker-regular.png';
		}
		
		var marker = map.createMarker({
			lat : selectedCompany.latitude,
			lng : selectedCompany.longitude,
			title : selectedCompany.name,
			optimized : false,
			icon : iconUrl
		});	
		marker.hotspotid = selectedCompany.id;
		
		map.addMapMarker(marker);
		
		addMarkerListener(marker);
		
		if (infoWindow != null) {
			infoWindow.close();
		}
		
		infoWindow.setContent(hashMap[companyId]);
        infoWindow.open(map.map, marker); 
	}
}	
	
function addLocationsToMap(companies) {
	if (companies == null
			|| (companies != null && companies.length == 0)) {
		return;
	}
	if (companies != null
			&& companies.length > 0) {
		var i = 0;
		$.each(companies, function() {
			addOutletToMap(this);
			if (i == 0) {
				focusLocation(this.id);
			}
			i++;
		});
	}
}
	
function addOutletToMap(company) {
	if (company.mapDisplay == 'HIDDEN') {
		return;
	}
	var iconUrl = '';
	if (company.mapDisplay == 'VISIBLE') {
		iconUrl = '${ctx}/img/marker-regular.png';
	} else if (company.mapDisplay == 'FEATURE') {
		iconUrl = '${ctx}/img/marker-premium.png';
	} else {
		iconUrl = '${ctx}/img/marker-regular.png';
	}
	var locationDetails = '<form id="info-window-form" style="width:300px;">'
			+ '<h3 style="margin:0;padding:0;">'
			+ company.name
			+ '</h3>'
			+ '<div id="bodyContent">'
			+ '<div style="display:inline-block;">';
	if (company.mapDisplay == 'FEATURE') {
		locationDetails = locationDetails
				+ '<img style="float:left; padding-top:3px; padding-right:3px;" src="${ctx}/img/marker-star.png" />'
				+ '<p style="color:#6C9912;margin:0;padding:0;">' + "<pr:snippet name="premium-outlet" group="outlets-map" escapeJavascript="true" defaultValue="Premium Outlet"/>" + '</p>';
	}
	locationDetails = locationDetails
			+ '<h4 style="margin-top:15px;margin-bottom:0;padding:0">' + "<pr:snippet name="address" group="outlets-map" escapeJavascript="true" defaultValue="Address:"/>" + '</h4>';
	locationDetails = locationDetails
			+ '<p style="padding:0;margin:0;">'
			+ company.address
			+ '</p>';
	locationDetails = locationDetails
			+ '<div><span><a style="cursor: pointer;" onclick="streetViewPanorama('
			+ company.latitude
			+ ','
			+ company.longitude
			+ ');">' + "<pr:snippet name="street-view" group="outlets-map" escapeJavascript="true" defaultValue="Street View"/>" + '</a></span></div>';
	locationDetails = locationDetails
			+ '<h4 style="float:left;margin-top:15px;margin-bottom:0;padding:0;padding-right:5px;">' + "<pr:snippet name="telephone" group="outlets-map" escapeJavascript="true" defaultValue="Telephone:"/>" + '</h4>';
	locationDetails = locationDetails
			+ '<p style="padding:0;margin:0;margin-top:16px;">'
			+ company.phoneNumber
			+ '</p>';
	locationDetails = locationDetails
			+ '</div>';
	locationDetails = locationDetails
			+ '</div>';
	locationDetails = locationDetails
			+ '</form>';
	
	hashMap[company.id] = locationDetails;

	var marker = map.createMarker({
		lat : company.latitude,
		lng : company.longitude,
		title : company.name,
		optimized : false,
		icon : iconUrl
	});	
	marker.hotspotid = company.id;

	map.addMapMarker(marker);
	addMarkerListener(marker);
}

function addMarkerListener(marker) {
	google.maps.event.addListener(marker, 'click', function() {
		if (infoWindow) {
			infoWindow.close();
		}
		infoWindow.setContent(hashMap[marker.hotspotid]);
        infoWindow.open(map.map, marker); 
	}); 
}
</script>