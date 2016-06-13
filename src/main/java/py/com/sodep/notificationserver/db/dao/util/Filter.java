package py.com.sodep.notificationserver.db.dao.util;


public class Filter {

    private String path;
    private String equals;
    private String greaterThan;
    private String lessThan;
    private Boolean sortAscending = false;
    private Boolean sortDescending = false;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEquals() {
        return equals;
    }

    public void setEquals(String equals) {
        this.equals = equals;
    }

    public String getGreaterThan() {
        return greaterThan;
    }

    public void setGreaterThan(String greaterThan) {
        this.greaterThan = greaterThan;
    }

    public String getLessThan() {
        return lessThan;
    }

    public void setLessThan(String lessThan) {
        this.lessThan = lessThan;
    }

    public Boolean getSortAscending() {
        return sortAscending;
    }

    public void setSortAscending(Boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    public Boolean getSortDescending() {
        return sortDescending;
    }

    public void setSortDescending(Boolean sortDescending) {
        this.sortDescending = sortDescending;
    }
}
