<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<div id="product" class="product ui-helper-clearfix">
	<div class="wrapper">
		<div class="container-content">
			<h1><c:out value="${product.title}" /></h1>
			<div class="description"><c:out value="${product.subTitle}" /></div>
			<div id="product-detailed">
				<div class="image-panel">
					<pr:image product="${product}" width="160" height="250" />
					<a class="download-image" href="${ctx}/product/${product.id}/jacket-image" target="_blank"><pr:snippet name="downloadCoverImageLink" group="productDetail" defaultValue="Download high-res image"/></a>
				</div>
				<sec:authorize access="isAnonymous()">
				    <c:choose>
                        <c:when test="${product.availableForSale eq true}">
						<div class="anonymous-details">
							<div class="outlet-details">
							    <div class="ui-helper-clearfix">
	                                <c:set var="printoutAvailableIconClass" value="ui-icon-closethick" />
	                                <c:if test="${product.canPrint}">
	                                    <c:set var="printoutAvailableIconClass" value="ui-icon-check" />
	                                </c:if>
	                                <div>
	                                    <span style="float: left; clear: both; font-size: 12px;"><pr:snippet name="printoutAvailable" group="productDetailAnonymous" defaultValue="Print-out available" /></span>
	                                    <span class="ui-icon ${printoutAvailableIconClass}" style="float: left; clear: right;"></span>
	                                </div>
	                                <c:set var="canPhotocopyIconClass" value="ui-icon-closethick" />
	                                <c:if test="${product.canPhotocopy}">
	                                    <c:set var="canPhotocopyIconClass" value="ui-icon-check" />
	                                </c:if>
	                                <div style="padding-bottom: 50px">
	                                    <span style="float: left; clear: both; font-size: 12px;"><pr:snippet name="canPhotocopy" group="productDetailAnonymous" defaultValue="Can photocopy legally" /></span>
	                                    <span class="ui-icon ${canPhotocopyIconClass}" style="float: left; clear: right;"></span>
	                                </div>
	                            </div>
								<h3><pr:snippet name="heading" group="productDetailAnonymous" defaultValue="You can buy this book printed on demand at one of our outlets"/></h3>
								<div id="outlets-search" style="padding:0;">
								    <c:if test="${product.premium}">
	                                <div><pr:snippet name="headingPremium" group="productDetailAnonymous" defaultValue="This is only available from our Premium outlets. See the map for these outlets."/></div>
	                                <div>&nbsp;</div>
	                                </c:if>
									<dl>
										<dt>
											<label class="outlets-search-label"><pr:snippet name="outlets-search-label" group="product-outlets-map" defaultValue="Your location:" /></label>
										</dt>
									</dl>
									<dl>
										<dt>
											<input class="outlets-search-input small input-dims" type="text">
											<button onclick="geoCodingSearch()"><pr:snippet name="outlets-search-button" group="product-outlets-map" defaultValue="Find outlets" /></button>
										</dt>
									</dl>
							
									<div class="outlets-search-results">
										<div class="outlets-search-results-label">
											<dl>
												<dt>
													<label class="outlets-search-label"><pr:snippet name="outlet-select-label" group="product-outlets-map" defaultValue="Select an outlet:" /></label>
												</dt>
											</dl>
										</div>
										<div class="outlets-search-results-list">
											<select class="outlets-list-select" id="outlet-list-parent"></select>
										</div>
										<div>
											<label id="printing-cost" class="printing-cost"></label>
											<label id="printing-cost-message" class="printing-cost-detail"></label>
											<label id="printing-cost-contact" class="printing-cost-detail"></label>
											<button id="order-by-email" data-email-address="" style="margin-top: 10px"><pr:snippet name="order-by-email-button" group="product-outlets-map" escapeJavascript="true" defaultValue="Order by email"/></button>
										</div>
									</div>
								</div>
							</div>
							<div class="outlet-map">
								<div class="map" id="outlets-map-display"></div>
								<div class="panorama" id="outlets-panorama-display">
									<button id="panorama-button" class="panorama-btn">&times;</button>
								</div>
							</div>
						</div>
					    </c:when>
	                    <c:otherwise>
	                        <div class="anonymous-details" style="height: 240px;">
	                            <div class="publisher-note" style="margin-left: 20px; margin-top: 20px;">
	                                <h2><pr:snippet name="productNotAvailableHeader" group="productDetail" defaultValue="Want to buy this book?" /></h2>
	                                <p><pr:snippet name="productNotAvailableDescription" group="productDetail" defaultValue="Unfortunately this book is not for sale at present" /></p>
	                            </div>
	                        </div>
	                    </c:otherwise>
	                </c:choose>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
				<div class="details-top"></div>
				<div class="details">		
					<sec:authorize access="hasRole('ROLE_OUTLET')">
					<div class="tooltip">
						<div class="tooltip-message">
							<pr:snippet name="toolTip" group="productDetailLoggedIn" defaultValue="&lt;ol&gt;
&lt;li&gt;Enter customer's full name.&lt;/li&gt;
&lt;li&gt;Select how many copies you will print for them.&lt;/li&gt;
&lt;li&gt;Select whether PDF is 1 or 2 pages per A4 side.&lt;/li&gt;
&lt;li&gt;Enter your total printing/binding/service charges.&lt;/li&gt;
&lt;li&gt;Click 'Buy now'.&lt;/li&gt;
&lt;/ol&gt;"/>					
						</div>
						<div class="tooltip-icon help-icon"></div>
					</div>
					</sec:authorize>
					
					<form id="purchase-licence-form" class="no-validate" action="" method="post">
						<input type="hidden" name="productId" value="${product.id}" />
						<input type="hidden" name="productPrice" value="${product.licenceFeeInDollars}" />
						<div class="form">
							<h3><pr:snippet name="printingDetailsAndCostsHeader" group="productDetailLoggedIn" defaultValue="Printing details and costs" /></h3>
							<dl>
								<dt><pr:snippet name="customerDetailsHeader" group="productDetailLoggedIn" defaultValue="Customer details" /></dt>
								<dd>
								<label style="display:none;" class="error" for="firstName"><pr:snippet name="customerDetailsFirstName" group="productDetailLoggedIn" defaultValue="Please enter a first name" /></label>
								<input type="text" id="firstName" name="firstName" value="" class="bg-input-detailed required clearfocus noplaceholder" />
								</dd>
							</dl>
							<dl>
								<dd>
								<label style="display:none;" class="error" for="lastName"><pr:snippet name="customerDetailsLastName" group="productDetailLoggedIn" defaultValue="Please enter a last name" /></label>
								<input type="text" id="lastName" name="lastName" value="" class="bg-input-detailed required clearfocus noplaceholder" />
								</dd>
							</dl>
							<dl>
								<dd>
								<label style="display:none;" class="error" for="phoneNumber"><pr:snippet name="customerDetailsPhoneNumber" group="productDetailLoggedIn" defaultValue="Please enter a phone number" /></label>
								<input type="text" id="phoneNumber" name="phoneNumber" value="" class="bg-input-detailed clearfocus" />
								</dd>
							</dl>
							<dl>
								<dt><pr:snippet name="customerDetailsNumberOfCopies" group="productDetailLoggedIn" defaultValue="How many copies do they need?" /></dt>
								<dd id="copiesui" class="buttonset">
									<button type="button" class="btn-minus"><span class="ui-button-icon-primary ui-icon ui-icon-minus"></span></button>
									<input autocomplete="off" class="bg-input-detailed-tiny" type="text" name="quantity" id="quantity" value="1" class="required number" maxlength="3" />
									<button type="button" class="btn-plus"><span class="ui-button-icon-primary ui-icon ui-icon-plus"></span></button>
								</dd>
							</dl>
							<dl id="layoutselection">
								<dt><pr:snippet name="customerDetailsLayout" group="productDetailLoggedIn" defaultValue="Select document format" /></dt>
								<dd id="layoutset">
									<label style="display:none;'" class="error" for="layout">Please select a layout</label>
									<c:if test="${not empty product.twoUpFilename}">
									<div class="layout-type">
										<span><input type="radio" name="layout" id="layout_two_up" value="TWO_UP"><label for="layout_two_up">&nbsp;<c:if test="${product.twoUpPageExtent gt 0}"><em>${product.twoUpPageExtent}</em> A4 pages (two per side) </c:if> ${product.twoUpFileSize} MB</label></span>
									</div>
									<div class="preview"><a href="${product.id}" data-layout="TWO_UP" class="btn-preview"><pr:snippet name="twoUpDownloadPreview" group="productDetailLoggedIn" defaultValue="Download preview" /></a></div>
									</c:if>
									<c:if test="${not empty product.oneUpFilename}">
									<div class="layout-type">
										<span><input type="radio" name="layout" id="layout_one_up" value="ONE_UP"><label for="layout_one_up">&nbsp;<c:if test="${product.oneUpPageExtent gt 0}"><em>${product.oneUpPageExtent}</em> A4 pages (one per side) </c:if> ${product.oneUpFileSize} MB</label></span>
									</div>
									<div class="preview"><a href="${product.id}" data-layout="ONE_UP" class="btn-preview"><pr:snippet name="oneUpDownloadPreview" group="productDetailLoggedIn" defaultValue="Download preview" /></a></div>
									</c:if>
									<c:if test="${not empty product.a5Filename}">
									<div class="layout-type">
										<span><input type="radio" name="layout" id="layout_a5" value="A5"><label for="layout_a5">&nbsp;<c:if test="${product.a5PageExtent gt 0}"><em>${product.a5PageExtent}</em> A5 pages (one per side) </c:if> ${product.a5FileSize} MB</label></span>
									</div>
									<div class="preview"><a href="${product.id}" data-layout="A5" class="btn-preview"><pr:snippet name="a5DownloadPreview" group="productDetailLoggedIn" defaultValue="Download preview" /></a></div>
									</c:if>
									<c:if test="${product.canPhotocopy}">
                                    <div class="layout-type">
                                        <span><input type="radio" name="layout" id="layout_photocopy" value="PHOTOCOPY"><label for="layout_photocopy">&nbsp;<pr:snippet name="layoutPhotocopy" group="productDetailLoggedIn" defaultValue="Photocopy licence only (no book PDF)" /></label></span>
                                    </div>
                                    </c:if>
								</dd>
							</dl>
						</div>
						
						<c:choose>
							<c:when test="${product.availableForSale eq true}">
							    <c:choose>
							        <c:when test="${restrictedPremium eq true}">
							            <div class="pricing">
                                            <div class="inner">
                                                <div class="publisher-note" style="margin-left:20px;">
                                                    <h2><pr:snippet name="pricePremiumnProductHeader" group="productDetailLoggedIn" defaultValue="Want to buy this book?" /></h2>
                                                    <p><pr:snippet name="pricePremiumnProductDescription" group="productDetailLoggedIn" defaultValue="Sorry, this is only available to Premium outlets" /></p>
                                                </div>
                                            </div>
                                        </div>
							        </c:when> 
								    <c:when test="${available eq true}">
										<sec:authorize access="hasRole('ROLE_OUTLET')">
										<div class="pricing">
											<div class="inner">
												<div class="sellingprice">
												<c:choose>
													<c:when test="${product.licenceFeeInDollars.unscaledValue() ne 0}">
													<span><pr:snippet name="priceYouPayCredits" group="productDetailLoggedIn" defaultValue="You pay in credits for this licence:" /></span><br />
													<em id="creditamount">${product.licenceFeeInCredits}</em>
													</c:when>
													<c:otherwise>
													<em id="creditamount">Free</em>
													</c:otherwise>
												</c:choose>
												</div>
												<div class="value"><pr:snippet name="priceApproximate" group="productDetailLoggedIn" defaultValue="Approx." />&nbsp;<em><pr:price amount="${product.licenceFeeInDollars}" /></em></div>
												<div class="outletcharges">
													<label><pr:snippet name="priceAddPrintCosts" group="productDetailLoggedIn" defaultValue="Add your printing/service charges" /> <span class="currencycode">(${company.currency.code})</span></label>
													<span class="currencysymbol">${company.currency.symbol}</span><input maxlength="12" type="text" id="outletcharges" name="outletcharges" value="${company.averagePrintingCost}" class="bg-input-detailed-small price required" />
												</div>
												<div class="total">
													<span><pr:snippet name="priceCustomerTotal" group="productDetailLoggedIn" defaultValue="Total your customer pays you:" /></span>
													<em><span class="amount" id="sellingprice"><pr:price amount="${product.licenceFeeInDollars}" /></span></em>
												</div>
												<button type="submit" class="btn-large"><pr:snippet name="buttonBuyNow" group="productDetailLoggedIn" defaultValue="buy now" /></button>
												<div class="licence-terms"><a href="${ctx}/terms/outlet" target="_blank" ><pr:snippet name="licenceTerms" group="productDetailLoggedIn" defaultValue="Read licence terms" /></a></div>
											</div>
										</div>
										</sec:authorize>
										
										<sec:authorize access="!hasRole('ROLE_OUTLET')">
										<div class="pricing">
											<div class="inner">
												<div class="publisher-note" style="margin-left:20px;">
													<h2><pr:snippet name="priceNotOutletHeader" group="productDetailLoggedIn" defaultValue="Want to buy this book?" /></h2>
													<p><pr:snippet name="priceNotOutletDescription" group="productDetailLoggedIn" defaultValue="You need to change your account registration to &lt;em&gt;Paperight Outlet&lt;/em&gt;." /></p>
													<button type="submit" onclick="window.location.href = '${ctx}/account/update'; return false;"><pr:snippet name="priceNotOutletButton" group="productDetailLoggedIn" defaultValue="Edit your details" /></button>
												</div>
											</div>
										</div>
										</sec:authorize>
									
									</c:when>
									<c:otherwise>
									
										<div class="pricing">
											<div class="inner">
												<div class="publisher-note" style="margin-left:20px;">
													<h2><pr:snippet name="priceNotAvailableHeader" group="productDetailLoggedIn" defaultValue="Want to buy this book?" /></h2>
													<p><pr:snippet name="priceNotAvailableDescription" group="productDetailLoggedIn" defaultValue="Unfortunately this is not available in your region. &lt;a target=&quot;_blank&quot; href=&quot;http://help.paperight.com/why-is-a-document-not-available-in-my-region/&quot;&gt;Find out why and report mistakes here.&lt;/a&gt;" /></p>
												</div>
											</div>
										</div>
									
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<div class="pricing">
									<div class="inner">
										<div class="publisher-note" style="margin-left:20px;">
											<h2><pr:snippet name="productNotAvailableHeader" group="productDetail" defaultValue="Want to buy this book?" /></h2>
											<p><pr:snippet name="productNotAvailableDescription" group="productDetail" defaultValue="Unfortunately this book is not for sale at present" /></p>
										</div>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
						
					</form>
					
				</div>
				<div class="details-bottom"></div>
				</sec:authorize>
			</div>
		</div>
	</div>
</div>

<sec:authorize access="hasRole('ROLE_OUTLET')">
<div id="content" class="ui-helper-clearfix">
	<div class="wrapper">
		<div class="container-content">
			<h1>Licences</h1>
			<div id="product-licences" class="ui-view" view-id="${ctx}/product/${product.id}/licences.html"></div>
		</div>
	</div>
</div>
</sec:authorize>

<sec:authorize access="hasRole('ROLE_PUBLISHER')">
	<c:set var="companyId"><sec:authentication property="principal.actingUser.company.id"/></c:set>
	<c:set var="productOwnerId">${product.ownerCompany.id}</c:set>
	<c:if test="${companyId eq productOwnerId}">
	<div id="content" class="ui-helper-clearfix">
		<div class="wrapper">
			<div class="container-content">
				<h1>Earnings</h1>
				<div id="product-earnings" class="ui-view" view-id="${ctx}/product/${product.id}/publisher-earnings.html"></div>
			</div>
		</div>
	</div>
	</c:if>
</sec:authorize>

<div id="content" class="ui-helper-clearfix">
	<div class="wrapper">
		<div class="container-content">
			<div class="document-information">
				<h2><pr:snippet name="header" group="productDetailMetadata" defaultValue="Document information" /></h2>
				<c:if test="${not empty product.relatedProducts}">
					<h4><pr:snippet name="relatedProducts" group="productDetailMetadata" defaultValue="Related Products:" /></h4> 
					<dl>
						<dd>${product.relatedProducts}</dd>
					</dl>
				</c:if>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
				<dl>
					<dt><pr:snippet name="ownerCompany" group="productDetailMetadata" defaultValue="Owner Company:" /></dt>
					<dd>${product.ownerCompany.name} (${product.ownerCompany.companyAdmins[0].email})</dd>
				</dl>
				</sec:authorize>
				<c:if test="${not empty product.shortDescription}">
				<h4><pr:snippet name="shortDescription" group="productDetailMetadata" defaultValue="Summary:" /></h4> 
				<div class="description"><c:out value="${product.shortDescription}" /></div>
				</c:if>
				<c:if test="${not empty product.longDescription}">
				<h4><pr:snippet name="longDescription" group="productDetailMetadata" defaultValue="Description:" /></h4> 
				<div class="description"><c:out value="${product.longDescription}" /></div>
				</c:if>
				
				<div style="overflow:hidden">
					<dl style="width:50%; clear:none; float:left;">
						<c:if test="${not empty product.primaryCreators}">
						<dt><pr:snippet name="primaryCreators" group="productDetailMetadata" defaultValue="Primary Creators:" /></dt>
						<dd><c:out value="${product.primaryCreators}" /></dd>
						</c:if>
						<c:if test="${not empty product.secondaryCreators}">
						<dt><pr:snippet name="secondaryCreators" group="productDetailMetadata" defaultValue="Secondary Creators:" /></dt>
						<dd><c:out value="${product.secondaryCreators}" /></dd>
						</c:if>
						<c:if test="${not empty product.publisher}">
						<dt><pr:snippet name="publisher" group="productDetailMetadata" defaultValue="Publisher/owner:" /></dt>
						<dd><c:out value="${product.publisher}" /></dd>
						</c:if>
						<c:if test="${not empty product.publicationDate}">
						<dt><pr:snippet name="publicationDate" group="productDetailMetadata" defaultValue="First published:" /></dt>
						<dd><joda:format value="${product.publicationDate}" pattern="MMMM yyyy" /></dd>
						</c:if>
						<c:set var="identifierType" value="ISBN" />
						<c:if test="${not empty product.identifierType}">
							<c:set var="identifierType" value="${product.identifierType}" />
						</c:if>
						<c:if test="${not empty product.identifier}">
						<dt>${identifierType}:</dt>
						<dd><c:out value="${product.identifier}" /></dd>
						</c:if>
					</dl>
					<dl style="width:50%; clear:none; float:left;">
						<c:if test="${not empty product.primaryLanguages}">
						<dt><pr:snippet name="primaryLanguages" group="productDetailMetadata" defaultValue="Main language(s):" /></dt>
						<dd><c:out value="${product.primaryLanguages}" /></dd>
						</c:if>
						<c:if test="${not empty product.secondaryLanguages}">
						<dt><pr:snippet name="secondaryLanguages" group="productDetailMetadata" defaultValue="Other language(s):" /></dt>
						<dd><c:out value="${product.secondaryLanguages}" /></dd>
						</c:if>
						<c:if test="${not empty product.audience}">
						<dt><pr:snippet name="audience" group="productDetailMetadata" defaultValue="Audience:" /></dt>
						<dd><c:out value="${product.audience}" /></dd>
						</c:if>
						<c:if test="${not empty product.subjectArea}">
						<dt><pr:snippet name="subjectArea" group="productDetailMetadata" defaultValue="Subject Area:" /></dt>
						<dd><c:out value="${product.subjectArea}" /></dd>
						</c:if>
					</dl>
					<dl>
						<sec:authorize access="hasRole('ROLE_ADMIN')">
						<button type="submit" onclick="window.location.href = '${ctx}/product/update/${product.id}'; return false;"><pr:snippet name="updateButton" group="productDetailMetadata" defaultValue="Update" /></button>
						</sec:authorize>
					</dl>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	oneUpPageCount = <c:out value="${product.oneUpPageExtent}"/>;
	twoUpPageCount = <c:out value="${product.twoUpPageExtent}"/>;
	a5PageCount = <c:out value="${product.a5PageExtent}"/>;
	productId = <c:out value="${product.id}"/>;
	var productTitle = "<c:out value="${product.title}"/>";
	
	var map;
	var companiesJSON;
	var companiesHashMap = new Object();
	var infoWindow = new google.maps.InfoWindow();;
	var hashMap = new Object();
	var closestMarker;
	var markerSelected = false;
	var selectElem = $('#outlet-list-parent');
	$(document).ready(function(){
		if (document.getElementById('outlets-map-display') != null) {
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
						myLocation = myLocation	+ '<p>' + "<pr:snippet name="you-are-here" group="product-outlets-map" escapeJavascript="true" defaultValue="Your are here"/>" + '</p>';
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
					console.log("<pr:snippet name="geolocation-failed" group="product-outlets-map" escapeJavascript="true" defaultValue="Geolocation failed:"/>" + ' ' + error.message);
				},
				not_supported : function() {
					console.log("<pr:snippet name="geolocation-not-supported" group="product-outlets-map" escapeJavascript="true" defaultValue="Your browser does not support geolocation"/>");
				}
			});
		}
		
		$('.btn-cancel').live("click", function(){
			var btn = $(this);
			paperight.dialog('<p class="message"><pr:snippet name="dialog-cancel-licence-question" group="product-detail-licences" escapeJavascript="true" defaultValue="Remove this item?"/></p>', {
				title: "<pr:snippet name="dialog-cancel-licence-title" group="product-detail-licences" escapeJavascript="true" defaultValue="Cancel licence"/>", height: 145, modal: true,
				buttons: {
					"<pr:snippet name="dialog-cancel-licence-button-cancel" group="product-detail-licences" escapeJavascript="true" defaultValue="Cancel"/>":function(){ $(this).dialog("close"); },
					"<pr:snippet name="dialog-cancel-licence-button-ok" group="product-detail-licences" escapeJavascript="true" defaultValue="OK"/>": function(){
						$(this).dialog("close");
						btn.closest('tr').fadeTo('fast', 0.5, function(){

							paperight.cancelLicence(btn.attr('href'), {
								success: function(){ 
									paperight.loadView(btn.closest('.ui-view'));
									paperight.reload();
								}
							});
							
						});
					}
				}
			});
			return false;
		});
		
		$('.btn-reset').live("click", function(){
			var btn = $(this);
			paperight.dialog('<p class="message"><pr:snippet name="dialog-reset-licence-question" group="product-detail-licences" escapeJavascript="true" defaultValue="Reset this licence?"/></p>', {
				title: "<pr:snippet name="dialog-reset-licence-title" group="product-detail-licences" escapeJavascript="true" defaultValue="Reset licence"/>", height: 145, modal: true,
				buttons: {
					"<pr:snippet name="dialog-reset-licence-button-cancel" group="product-detail-licences" escapeJavascript="true" defaultValue="Cancel"/>":function(){ $(this).dialog("close"); },
					"<pr:snippet name="dialog-reset-licence-button-ok" group="product-detail-licences" escapeJavascript="true" defaultValue="OK"/>": function(){
						$(this).dialog("close");
						btn.closest('tr').fadeTo('fast', 0.5, function(){

							paperight.resetLicence(btn.attr('href'), {
								success: function(){ 
									paperight.loadView(btn.closest('.ui-view'));
									paperight.reload();
								}
							});
							
						});
					}
				}
			});
			return false;
		});
		
		$('.btn-download').live("click", function(){
			var btn = $(this);
			var modal = paperight.dialog('<p class="message"><pr:snippet name="dialog-generate-licence-generating" group="product-detail-licences" escapeJavascript="true" defaultValue="Generating licence..."/></p>', {
				title: "<pr:snippet name="dialog-generate-licence-title" group="product-detail-licences" escapeJavascript="true" defaultValue="Generating licence"/>", height: 180, modal: true, progressbar : { time: paperight.ajax.timeout, start: true }
			});
			var data = false;
			return paperight.generateLicence(btn.attr('value'), {
				data: data,
				success: function(data){
					if(typeof data == "object"){
						if (data.result == false) {
							modal.find(".message").text(data.message);
						} else {}
							modal.find(".message").text("<pr:snippet name="dialog-generate-licence-generated" group="product-detail-licences" escapeJavascript="true" defaultValue="Your file will now download."/>");
							modal.dialog('progressbar', {stop: true, value: 100});
							modal.dialog('addbutton', "<pr:snippet name="dialog-generate-licence-button-ok" group="product-detail-licences" escapeJavascript="true" defaultValue="OK"/>", function(){ $(this).dialog("close"); });
							paperight.downloadLicence(btn.attr('value'));
							setTimeout(function() {
								paperight.loadView(btn.closest('.ui-view'));
								paperight.reload();
								},
								1500);
					};
				},
				error: function(){
					modal.find(".message").text(data.message);
					modal.dialog('progressbar', {stop: true, value: 100});
					modal.dialog('addbutton', "<pr:snippet name="dialog-generate-licence-button-ok" group="product-detail-licences" escapeJavascript="true" defaultValue="OK"/>", function(){ $(this).dialog("close"); });
				}
			});
		});
		
		$('.btn-print-licence').live("click", function(){
            var btn = $(this);
            var modal = paperight.dialog('<p class="message"><pr:snippet name="dialog-generate-print-licence-generating" group="product-detail-licences" escapeJavascript="true" defaultValue="Generating print licence..."/></p>', {
                title: "<pr:snippet name="dialog-generate-print-licence-title" group="product-detail-licences" escapeJavascript="true" defaultValue="Generating print licence"/>", height: 180, modal: true, progressbar : { time: paperight.ajax.timeout, start: true }
            });
            var data = false;
            return paperight.generateLicence(btn.attr('value'), {
                data: data,
                success: function(data){
                    if(typeof data == "object"){
                        if (data.result == false) {
                            modal.find(".message").text(data.message);
                        } else {}
                            modal.find(".message").text("<pr:snippet name="dialog-generate-print-licence-generated" group="product-detail-licences" escapeJavascript="true" defaultValue="Your file will now download."/>");
                            modal.dialog('progressbar', {stop: true, value: 100});
                            modal.dialog('addbutton', "<pr:snippet name="dialog-generate-print-licence-button-ok" group="product-detail-licences" escapeJavascript="true" defaultValue="OK"/>", function(){ $(this).dialog("close"); });
                            paperight.downloadLicence(btn.attr('value'));
                            setTimeout(function() {
                                paperight.loadView(btn.closest('.ui-view'));
                                paperight.reload();
                                },
                                1500);
                    };
                },
                error: function(){
                    modal.find(".message").text(data.message);
                    modal.dialog('progressbar', {stop: true, value: 100});
                    modal.dialog('addbutton', "<pr:snippet name="dialog-generate-print-licence-button-ok" group="product-detail-licences" escapeJavascript="true" defaultValue="OK"/>", function(){ $(this).dialog("close"); });
                }
            });
        });
		
		/* $('.btn-print-licence').live('click', function(){
			console.log("btn-print-licence click");
	        var button = $(this);
	        var licenceId = button.attr('value');
	        paperight.dialog('<p class="message"><pr:snippet name="dialog-print-licence-message" group="product-detail-licences" escapeJavascript="true" defaultValue="Your print licence will download shortly. You may close this window at any time."/></p>', {
	            title: "<pr:snippet name="dialog-print-licence-title" group="product-detail-licences" escapeJavascript="true" defaultValue="Invoice"/>", height: 145, modal: true,
	            buttons: {
	                "<pr:snippet name="dialog-print-licence-button-ok" group="product-detail-licences" escapeJavascript="true" defaultValue="OK"/>": function(){ 
	                    button.find('span').removeClass('ui-icon-link').addClass('ui-icon-refresh');
	                    $(this).dialog("close"); 
	                }
	            }
	        });
	        paperight.downloadLicence(licenceId);
	        setTimeout(function() {
                paperight.loadView(button.closest('.ui-view'));
                paperight.reload();
                }, 5000);
	        return false;
	    }); */
		
		$('.btn-preview').live("click", function(){
			var btn = $(this);
			var layout = $(btn).data('layout');
			var productId = $(btn).attr('href');
			paperight.dialog('<p class="message"><pr:snippet name="dialog-preview-message" group="product-detail-licences" escapeJavascript="true" defaultValue="Preview will start shortly. You may close this window at any time."/></p>', {
				title: "<pr:snippet name="dialog-preview-title" group="product-detail-licences" escapeJavascript="true" defaultValue="Preview"/>", height: 145, modal: true,
				buttons: {
					"<pr:snippet name="dialog-preview-button-ok" group="product-detail-licences" escapeJavascript="true" defaultValue="OK"/>": function(){ 
						$(this).dialog("close"); 
					}
				}
			});
			paperight.previewProduct({ data: {productId: productId, layout: layout} });
			return false;
		});
		
		$('.btn-activate').live("click", function(){
			var modal = paperight.dialog('<p class="message"><pr:snippet name="dialog-confirm-email-message-sending" group="product-detail-licences" escapeJavascript="true" defaultValue="Sending confirmation email..."/></p>', {
				title: "<pr:snippet name="dialog-confirm-email-title" group="product-detail-licences" escapeJavascript="true" defaultValue="Confirmation email"/>", height: 180, modal: true, progressbar : { time: paperight.ajax.timeout, start: true }
			});
			var data = false;
			return paperight.sendActivateUser({
				data: data,
				success: function(data){
					if(typeof data == "object"){
						modal.find(".message").text((data.result == false) ? data.message : "<pr:snippet name="dialog-confirm-email-message-sent" group="product-detail-licences" escapeJavascript="true" defaultValue="Please check your email, we have sent you a confirmation message that you must click to confirm your email address."/>");
					};
					modal.dialog('addbutton', "<pr:snippet name="dialog-confirm-email-button-ok" group="product-detail-licences" escapeJavascript="true" defaultValue="OK"/>", function(){
						$(this).dialog("close");
					});
				},
				error: function(){
					modal.find(".message").text("Error");
					modal.dialog('progressbar', {stop: true, value: 100});
					modal.dialog('addbutton', "<pr:snippet name="dialog-confirm-email-button-ok" group="product-detail-licences" escapeJavascript="true" defaultValue="OK"/>", function(){ $(this).dialog("close"); });
				}
			});
		});
		
		var purchaseLicence = function(form){
			
			var modal = paperight.dialog('<p class="message"><pr:snippet name="dialog-purchasing-licence-message-purchasing" group="product-detail-licences" escapeJavascript="true" defaultValue="Purchasing please wait..."/></p>', {
				title: "<pr:snippet name="dialog-purchasing-licence-title" group="product-detail-licences" escapeJavascript="true" defaultValue="Purchase licence"/>", height: 180, modal: true, progressbar : { time: paperight.ajax.timeout, start: true }
			});
			
			var data = $(form).getFormValues();
			return paperight.purchaseLicence({
				data: data,
				success: function(data){
					modal.dialog('progressbar', {stop: true, value: 100});
					if(typeof data == "object"){
						modal.find(".message").text((data.result == false) ? data.message : "<pr:snippet name="dialog-purchasing-licence-message-purchased" group="product-detail-licences" escapeJavascript="true" defaultValue="Thank you! You may now download and print this. Scroll down to your Licences to download."/>");
						resetForm();
					};
					modal.dialog('addbutton', "<pr:snippet name="dialog-purchasing-licence-button-ok" group="product-detail-licences" escapeJavascript="true" defaultValue="OK"/>", function(){
						$(this).dialog("close");
						paperight.resetView('#product-licences');
					});
					paperight.reload();
				},
				error: function(){
					modal.find(".message").text("Error");
					modal.dialog('progressbar', {stop: true, value: 100});
					modal.dialog('addbutton', "<pr:snippet name="dialog-purchasing-licence-button-ok" group="product-detail-licences" escapeJavascript="true" defaultValue="OK"/>", function(){ $(this).dialog("close"); });
				}
			});
		};
		
		$('#purchase-licence-form').validate({
			ignore: "",
			submitHandler: function(form){
				try{
					purchaseLicence(form);
				}catch(e){
					console.error(e);
				}
				return false;
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
			data: {latitude: latitude, longitude: longitude, productId: productId},
			success : function(JSON) {
				companiesJSON = JSON.companies;
				addLocationsToMap(companiesJSON);
				addLocationList(companiesJSON);
			}
		});
	}
	
	function addLocationList(companies) {
		$('.outlets-search-results').show();
		selectElem.empty();
		if (companies == null
				|| (companies != null && companies.length == 0)) {
			return;
		}
		if (companies != null
				&& companies.length > 0) {
			for (var i = 0; i < 10; ++i) {
				addSingleLocationToList(companies[i]);
			}
		}
	}

	function addSingleLocationToList(company) {
		var optionElem = document.createElement("option");
		var textElem;
		if(company.mapDisplay == 'FEATURE') {
			textElem = document.createTextNode(company.name + " (" + "<pr:snippet name="premium-outlet" group="product-outlets-map" escapeJavascript="true" defaultValue="Premium Outlet"/>" + ")");
		} else {
			textElem = document.createTextNode(company.name);
		}
		optionElem.value = company.id;
		optionElem.appendChild(textElem);
		selectElem.append(optionElem);
	}
	
	$('#outlet-list-parent').change(function() {
		var companyId = $('#outlet-list-parent').val();
		focusLocation(companyId);
	});
	
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
			
			if (infoWindow) {
				infoWindow.close();
			}
			infoWindow.setContent(hashMap[marker.hotspotid]);
	        infoWindow.open(map, marker); 
			addPrintingCosts(selectedCompany);
		}
	}	
	
	function addPrintingCosts(company) {
		$('#printing-cost').html('&#8776; ' + company.printingCost);
		$('#printing-cost-message').text('<pr:snippet name="estimated-cost-pre" group="product-outlets-map" escapeJavascript="true" defaultValue="Estimated cost at"/>' + ' ' + company.name);
		$('#printing-cost-contact').text('<pr:snippet name="estimated-cost-post" group="product-outlets-map" escapeJavascript="true" defaultValue="To check, call them on"/>' + ' ' + company.phoneNumber);
		$('#order-by-email').attr("data-email-address", company.email);
	}
	
	$('#order-by-email').click(function() {
		var emailAddress = $('#order-by-email').attr("data-email-address");
		
		var subject = "<pr:snippet name="subject" group="order-by-email" escapeJavascript="true" defaultValue="Request to print a Paperight book"/>";
		var body = "<pr:snippet name="body" group="order-by-email" escapeJavascript="true" multiline="true" defaultValue="Hello. I would like to order a copy of [$productTitle$], which is on Paperight here: [$productUrl$]

Please send me details of pricing and how to pay and collect.

Thank you."/>";
	    body = body.replace("[$productTitle$]", productTitle);
	    body = body.replace("[$productUrl$]", window.location.href);
		var mailTo = "mailto:" + emailAddress + "?Subject=" + encodeURI(subject) + "&Body=" + encodeURI(body);
		console.log(mailTo);
	    window.location = mailTo;
    });
	
	
		
	function addLocationsToMap(companies) {
		if (companies == null
				|| (companies != null && companies.length == 0)) {
			return;
		}
		if (companies != null
				&& companies.length > 0) {
			var i = 0;
			$.each(companies, function() {
				companiesHashMap[this.id] = this;
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
		var locationDetails = '<form id="info-window-form" style="width:250px;">'
				+ '<h3 style="margin:0;padding:0;">'
				+ company.name
				+ '</h3>'
				+ '<div id="bodyContent">'
				+ '<div style="display:inline-block;">';
		if (company.mapDisplay == 'FEATURE') {
			locationDetails = locationDetails
					+ '<img style="float:left; padding-top:3px; padding-right:3px;" src="${ctx}/img/marker-star.png" />'
					+ '<p style="color:#6C9912;margin:0;padding:0;">' + "<pr:snippet name="premium-outlet" group="product-outlets-map" escapeJavascript="true" defaultValue="Premium Outlet"/>" + '</p>';
		}
		locationDetails = locationDetails
				+ '<h4 style="margin-top:15px;margin-bottom:0;padding:0">' + "<pr:snippet name="address" group="product-outlets-map" escapeJavascript="true" defaultValue="Address:"/>" + '</h4>';
		locationDetails = locationDetails
				+ '<p style="padding:0;margin:0;">'
				+ company.address
				+ '</p>';
		locationDetails = locationDetails
				+ '<div><span><a style="cursor: pointer;" onclick="streetViewPanorama('
				+ company.latitude
				+ ','
				+ company.longitude
				+ ');">' + "<pr:snippet name="street-view" group="product-outlets-map" escapeJavascript="true" defaultValue="Street View"/>" + '</a></span></div>';
		locationDetails = locationDetails
				+ '<h4 style="float:left;margin-top:15px;margin-bottom:0;padding:0;padding-right:5px;">' + "<pr:snippet name="telephone" group="product-outlets-map" escapeJavascript="true" defaultValue="Telephone:"/>" + '</h4>';
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
			var company = companiesHashMap[marker.hotspotid];
			var exists = false;
			$('#outlet-list-parent option').each(function(index, element){
			    if (parseInt(this.value) == marker.hotspotid) {
			        exists = true;
			        return false;
			    }
			});
			if (!exists) {
				addSingleLocationToList(company);
			}
			$('#outlet-list-parent').val(marker.hotspotid).change();
			if (infoWindow) {
				infoWindow.close();
			}
			infoWindow.setContent(hashMap[marker.hotspotid]);
	        infoWindow.open(map, marker); 
		}); 
	}
	
</script>
