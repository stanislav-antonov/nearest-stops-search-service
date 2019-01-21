package pse;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class Config {

    private static final String overridesFile = "./config/main.properties";
    private static final String defaultsFile = "resources/main.default.properties";

    private static final String propKeyPort = "server.port";
    private static final String propKeyLogFile = "server.log.file";
    private static final String propKeyStopDatasourceUrl = "datasource.stop.url";

    private final Properties mDefaults = new Properties();
    private final Properties mOverrides = new Properties();

    private boolean mOverridesLoaded;

    public static Config create() {
        return new Config();
    }

    public void load() throws IOException {
        loadDefaults();

        try {
            mOverridesLoaded = false;
            loadOverrides();
            mOverridesLoaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDefaults() throws IOException {
        InputStream in = null;
        final Properties props = new Properties();

        try {
            in = getClass().getResourceAsStream(defaultsFile);
            props.load(in);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        mDefaults.putAll(props);
    }

    public void loadOverrides() throws IOException {
        InputStream in = null;
        final Properties props = new Properties(mDefaults);

        try {
            in = new FileInputStream(overridesFile);
            props.load(in);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        mOverrides.putAll(props);
    }

    public boolean overridesLoaded() {
        return mOverridesLoaded;
    }

    public int getPort() {
        return Integer.valueOf(mOverrides.getProperty(propKeyPort, mDefaults.getProperty(propKeyPort)));
    }

    public String getLogFile() {
        return mOverrides.getProperty(propKeyLogFile, mDefaults.getProperty(propKeyLogFile));
    }

    public URI getStopDatasourceUrl() throws URISyntaxException {
        final String url = mOverrides.getProperty(propKeyStopDatasourceUrl, mDefaults.getProperty(propKeyStopDatasourceUrl));
        return url != null ? new URI(url) : null;
    }
}
