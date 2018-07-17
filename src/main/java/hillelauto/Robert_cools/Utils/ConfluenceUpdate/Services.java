package hillelauto.Robert_cools.Utils.ConfluenceUpdate;

public enum Services {

    OfficeO365(50991158, "OfficeO365 new", "SKYF"),
    Salesforse(50991167, "Salesforce new", "SKYF"),
    Box(50991170, "Box new", "SKYF"),
    DropBox(50991213, "DropBox new", "SKYF"),
    AWSAmazon(50991188, "AWSAmazon new", "SKYF"),
    GoogleServices(50991180, "Google Services new", "SKYF");


    private final int pageId;
    private final String pageName;
    private final String spaceKey;


    Services(int pageId, String pageName, String spaceKey) {

        this.pageId = pageId;
        this.pageName = pageName;
        this.spaceKey = spaceKey;
    }

    public int getPageId() {
        return pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public String getSpaceKey() {
        return spaceKey;
    }
}
