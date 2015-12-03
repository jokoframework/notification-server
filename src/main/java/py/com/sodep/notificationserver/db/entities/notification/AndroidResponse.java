package py.com.sodep.notificationserver.db.entities.notification;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Json de respuesta de GCM
 * Created by gaby.lorely on 17/05/2015.
 */
@JsonAutoDetect
public class AndroidResponse {
    @JsonProperty("multicast_id")
    int multicast_id;
    @JsonProperty("success")
    int success;
    @JsonProperty("failure")
    int failure;
    @JsonProperty("canonical_ids")
    int canonical_ids;
    List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Integer getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(Integer canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public Integer getFailure() {
        return failure;
    }

    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(Integer multicast_id) {
        this.multicast_id = multicast_id;
    }



}
