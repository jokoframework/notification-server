package py.com.sodep.notificationserver.interceptors;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class RequestParams {

    private String transactionId;

    public RequestParams() {
        //Default constructor
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "RequestParams{" +
                "transactionId='" + transactionId + '\'' +
                '}';
    }
}
