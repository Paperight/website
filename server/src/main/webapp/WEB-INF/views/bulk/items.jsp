<%@ include file="/WEB-INF/layouts/includes/taglibs.jsp" %>
<%@ taglib prefix="bulk" tagdir="/WEB-INF/tags/bulk" %>
<h4>Items</h4>
<table class="table table-hover">
    <thead>
        <tr>
            <th>Data</th>
            <th style="width:1px;">Status</th>
            <th style="width:1px;">Health</th>
            <th style="width:1px;"></th>
            <th style="width:1px;"></th>
        </tr>
    </thead>
    <tbody>
    <c:forEach items="${importItems}" var="importItem">
        <c:if test="${not importItem.cancelled }">
        <c:set var="healthClass" value="label-success" />
        <c:set var="statusClass" value="label-success" />
        <c:set var="rowClass" value = "" />
        
        <c:if test="${importItem.status eq 'NEW'}">
            <c:set var="statusClass" value="label-warning" />
        </c:if>
        <c:if test="${importItem.status eq 'CANCELLED'}">
            <c:set var="statusClass" value="label-danger health-error" />
        </c:if>
        <c:if test="${importItem.health eq 'ERROR'}">
            <c:set var="healthClass" value="label-danger health-error" />
            <c:set var="rowClass" value = "danger" />
        </c:if>
        <tr class="${rowClass}">
            <td>
                <dl class="dl-horizontal product-detail">
                    <dt>Title</dt>
                    <dd>${importItem.title}</dd>
                    <dt>Identifier</dt>
                    <dd>${importItem.identifier}</dd>
                    <c:if test="${not empty importItem.product}">
                        <dt>Product</dt>
                        <dd><a target="_blank" href='<pr:productLink product="${importItem.product}"/>' >View</a></dd>
                    </c:if>
                    
                    <c:if test="${not importItem.processed}">
                    
                        <c:if test="${importItem.hasPrintFormats}">
                            <dt>Files</dt>
                            <dd>
                            <c:if test="${not empty importItem.oneUpFilename}">
                               <button data-filename="${importItem.oneUpFilename}" type="button" class="btn btn-info btn-xs btn-preview-pdf">
                                    <span class="glyphicon glyphicon-eye-open"></span> One Up
                                </button>
                            </c:if>
                            <c:if test="${not empty importItem.twoUpFilename}">
                                <button data-filename="${importItem.twoUpFilename}" type="button" class="btn btn-info btn-xs btn-preview-pdf">
                                    <span class="glyphicon glyphicon-eye-open"></span> Two Up
                                </button>
                            </c:if>
                            <c:if test="${not empty importItem.a5Filename}">
                                <button data-filename="${importItem.a5Filename}" type="button" class="btn btn-info btn-xs btn-preview-pdf">
                                    <span class="glyphicon glyphicon-eye-open"></span> A5
                                </button>
                            </c:if>
                            </dd>
                        </c:if>
                        <dt>&nbsp;</dt>
                        <dd>
                            <button data-toggle="collapse" type="button" class="btn btn-warning btn-xs" data-target="#detail-${importItem.id}">
                                <span class="glyphicon glyphicon-sort"></span> Show/Hide Details
                            </button>
                        </dd>
                        <div id="detail-${importItem.id}" class="collapse">
                            <bulk:productDetailField label="Identifier type" value="${importItem.identifierType}"/>
                            <bulk:productDetailField label="Copyright status" value="${importItem.copyrightStatus}"/>
                            <bulk:productDetailField label="Publisher" value="${importItem.publisher}"/>
                            <bulk:productDetailField label="Subtitle" value="${importItem.subTitle}"/>
                            <bulk:productDetailField label="Alternative title" value="${importItem.alternativeTitle}"/>
                            <bulk:productDetailField label="Primary creators" value="${importItem.primaryCreators}"/>
                            <bulk:productDetailField label="Secondary creators" value="${importItem.secondaryCreators}"/>
                            <bulk:productDetailField label="Edition" value="${importItem.edition}"/>
                            <bulk:productDetailField label="Primary languages" value="${importItem.primaryLanguages}"/>
                            <bulk:productDetailField label="Secondary languages" value="${importItem.secondaryLanguages}"/>
                            <bulk:productDetailField label="Subject area" value="${importItem.subjectArea}"/>
                            <dt>Publication date</dt>
                            <dd><joda:format value="${importItem.publicationDate}" pattern="yyyy-MM-dd" /></dd>
                            <dt>Embargo date</dt>
                            <dd><joda:format value="${importItem.embargoDate}" pattern="yyyy-MM-dd" /></dd>
                            <bulk:productDetailField label="Licence fee in dollars" value="${importItem.licenceFeeInDollars}"/>
                            <bulk:productDetailField label="Short description" value="${importItem.shortDescription}"/>
                            <bulk:productDetailField label="Long description" value="${importItem.longDescription}"/>
                            <bulk:productDetailField label="Parent ISBN" value="${importItem.parentIsbn}"/>
                            <bulk:productDetailField label="Alternate ISBN" value="${importItem.alternateIsbn}"/>
                            <bulk:productDetailField label="Audience" value="${importItem.audience}"/>
                            <bulk:productDetailField label="Disallowed countries" value="${importItem.disallowedCountries}"/>
                            <bulk:productDetailField label="Tags" value="${importItem.tags}"/>
                            <bulk:productDetailField label="URL" value="${importItem.url}"/>
                            <bulk:productDetailField label="URL call to action" value="${importItem.urlCallToAction}"/>
                            <bulk:productDetailField label="Supports ads?" value="${importItem.supportsAds}"/>
                            <bulk:productDetailField label="Sample page range" value="${importItem.samplePageRange}"/>
                            <bulk:productDetailField label="Licence statement" value="${importItem.licenceStatement}"/>
                            <bulk:productDetailField label="Formats" value="${importItem.formats}"/>
                            <bulk:productDetailField label="CSV Data" value="${importItem.rawData}"/>
                        </div>
                    </c:if>
                </dl>
            </td>
            <td><span class="label ${statusClass}">${importItem.status}</span></td>
            <td><span class="health label ${healthClass}" data-toggle="tooltip" title="<c:out value="${importItem.error}" />">${importItem.health}</span></td>
            <td>
                <c:if test="${importItem.canCancel}">
                <button data-id="${importItem.id}" type="button" class="btn btn-danger btn-sm btn-cancel" data-toggle="confirmation-cancel">
                    <span class="glyphicon glyphicon-remove"></span> Cancel
                </button>
                </c:if>
            </td>
            <td>
                <c:if test="${importItem.canApply}">
                <button data-id="${importItem.id}" type="button" class="btn btn-success btn-sm btn-apply" data-toggle="confirmation-apply">
                    <span class="glyphicon glyphicon-ok"></span> Apply
                </button>
                </c:if>
            </td>
        </tr>
        </c:if>
    </c:forEach>
    </tbody>
</table>

<script type="text/javascript">
$(function() {
    $('span.health-error').tooltip();
    
    $('[data-toggle="confirmation-cancel"]').confirmation({
        placement: "top",
        btnOkClass: "btn btn-sm btn-primary",
        btnOkLabel: "Yes",
        btnOkIcon: "glyphicon glyphicon-ok",
        btnCancelLabel: "No",
        onConfirm: cancel
    });
    
    $('[data-toggle="confirmation-apply"]').confirmation({
        placement: "left",
        btnOkClass: "btn btn-sm btn-primary",
        btnOkLabel: "Yes",
        btnOkIcon: "glyphicon glyphicon-ok",
        btnCancelLabel: "No",
        onConfirm: apply
    });
    
    function apply(event, element){
        var applyItemJob = function(id, params){
            $.ajax(
                $.extend({ 
                    timeout: paperight.ajax.timeout, 
                    cache: false, 
                    type: 'POST', 
                    url: paperight.contextPath + '/bulk/item/' + id + '/apply' 
                }, params));
            return this;
        }
        
        applyItemJob($(element).data('id'), {
            success: function(){ 
                paperight.loadView($(element).closest('.ui-view'));
                paperight.reload();
            }
        });
    }
    
    function cancel(event, element){
        
        var cancelItemJob = function(id, params){
            $.ajax(
                $.extend({ 
                    timeout: paperight.ajax.timeout, 
                    cache: false, 
                    type: 'POST', 
                    url: paperight.contextPath + '/bulk/item/' + id + '/cancel' 
                }, params));
            return this;
        }
        
        cancelItemJob($(element).data('id'), {
            success: function(){ 
                paperight.loadView($(element).closest('.ui-view'));
                paperight.reload();
            }
        });
    }
});
</script>