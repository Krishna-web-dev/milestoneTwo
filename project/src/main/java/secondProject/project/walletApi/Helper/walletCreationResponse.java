package secondProject.project.walletApi.Helper;

public class walletCreationResponse {

    private String status;
    private String error;
    private String mobile;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String phNo) {
        this.mobile = phNo;
    }

}
