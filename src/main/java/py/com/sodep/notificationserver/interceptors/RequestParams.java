package py.com.sodep.notificationserver.interceptors;

import javax.enterprise.context.RequestScoped;

/**
 * User: duartm
 * Date: 20/11/13
 * Time: 05:53 PM
 */
@RequestScoped
public class RequestParams {

    private String transactionId;

    public RequestParams() {
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
