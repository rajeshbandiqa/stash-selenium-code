package selenium.helper;

public class ConfigFileReader {
    public String getReportConfigPath(){
        String currentDirectory = System.getProperty("user.dir");
        String configPath = currentDirectory+ "/src/test/java/selenium/helper/extent-config.xml";
        String reportConfigPath = configPath;
        if(reportConfigPath!= null) return reportConfigPath;
        else throw new RuntimeException("Report Config Path not specified in the Configuration.properties file for the Key:reportConfigPath");
    }
}
