package com.shaddyhollow.freedom.dinendashhostess.requests;

import java.util.UUID;

import com.shaddyhollow.areaeditor.Config;
import com.shaddyhollow.quicktable.models.QueuedVisit;
import com.shaddyhollow.quicktable.models.SeatedVisit;
import com.shaddyhollow.robospice.BaseRequest;
import com.shaddyhollow.util.FileOperations;

public class AddSeatedVisitRequest extends BaseRequest<SeatedVisit>{
//    private final String url =
//    Config.getServerRoot() + "locations/{location_id}/seated_visits.json";
	private final String url =
			Config.getServerRoot() + "qt/api/seated/{location_id}/seated_visits/?tenant_id={tenant_id}";
    SeatedVisitRequest request;
    QueuedVisit queuedVisit;
    Integer locationId;
    Integer tenantId;
    
	public AddSeatedVisitRequest(Integer tenantId, Integer locationId, QueuedVisit queuedVisit, SeatedVisit seatedVisit) {
		super(SeatedVisit.class);
    	this.queuedVisit = queuedVisit;
    	this.tenantId = tenantId;
    	this.locationId = locationId;
    	this.request = new SeatedVisitRequest();
    	this.request.visit_id = seatedVisit.visit_id;
    	this.request.server_id = seatedVisit.server_id;
    	this.request.party_size = seatedVisit.party_size;;
    }

    private class SeatedVisitRequest {
    	@SuppressWarnings("unused")
		public UUID visit_id;
    	@SuppressWarnings("unused")
    	public UUID server_id;
    	@SuppressWarnings("unused")
    	public int party_size;
    }
    
	@Override
	public SeatedVisit loadOnlineData()
			throws Exception {
		FileOperations.requestLog(url, request.visit_id.toString(), request.server_id.toString(), request.party_size);
    	SeatedVisit visit =  getRestTemplate().postForObject(url, request, SeatedVisit.class, String.valueOf(locationId), String.valueOf(tenantId));
    	
    	return visit;
	}

}
