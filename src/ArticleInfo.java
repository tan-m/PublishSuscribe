public class ArticleInfo {
    String type;
    String originator;
    String org;

    public ArticleInfo(String type, String originator, String org) {
        this.type = type;
        this.originator = originator;
        this.org = org;
    }

    @Override
    public boolean equals(Object obj1) {
        if(obj1 == null || !(obj1 instanceof ArticleInfo))
            return false;
        ArticleInfo obj = (ArticleInfo) obj1;
        return type.equals(obj.type) && originator.equals(obj.originator) && org.equals(obj.org);
    }

    @Override
    public int hashCode() {
        return type.hashCode() + originator.hashCode() + org.hashCode();
    }

    @Override
    public String toString() {
        return type + ";" + originator + ";" + org + ";";
    }
}