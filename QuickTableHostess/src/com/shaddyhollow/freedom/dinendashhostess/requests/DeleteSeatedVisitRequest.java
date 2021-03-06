package com.shaddyhollow.freedom.dinendashhostess.requests;

import java.util.UUID;

import com.shaddyhollow.areaeditor.Config;
import com.shaddyhollow.quicktable.models.removeQueuedVisitResponse;
import com.shaddyhollow.robospice.BaseRequest;
import com.shaddyhollow.util.FileOperations;

public class DeleteSeatedVisitRequest extends BaseRequest<Void> {
	// private final String url = Config.getServerRoot() +
	// "locations/{location_id}/seated_visits/{seated_visit_id}.json";
	private final String url = Config.getServerRoot() + "qt/api/seated/clear/?tenant_id={tenant_id}&visit_id={seated_visit_id}";

	private UUID seatedVisitID;
	private Integer tenantId;

	public DeleteSeatedVisitRequest(Integer tenantId, UUID seatedVisitID) {
		super(Void.class);
		this.seatedVisitID = seatedVisitID;
		this.tenantId = tenantId;
	}

	@Override
	public Void loadOnlineData() throws Exception {
		FileOperations.requestLog(url, seatedVisitID.toString(), null, 0);
		getRestTemplate().getForObject(url, removeQueuedVisitResponse.class, tenantId,
				seatedVisitID);
		return null;
	}

}
