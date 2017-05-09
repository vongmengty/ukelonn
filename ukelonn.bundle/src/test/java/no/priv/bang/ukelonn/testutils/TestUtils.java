package no.priv.bang.ukelonn.testutils;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.service.log.LogService;

import no.priv.bang.ukelonn.bundle.test.db.UkelonnDatabaseProvider;
import no.priv.bang.ukelonn.impl.ShiroFilterProvider;
import no.priv.bang.ukelonn.impl.UkelonnServletProvider;
import no.priv.bang.ukelonn.mocks.MockLogService;

/**
 * Contains static methods used in more than one unit test.
 *
 * @author Steinar Bang
 *
 */
public class TestUtils {

    private static UkelonnServletProvider ukelonnServletProvider;

    public static UkelonnServletProvider getUkelonnServletProvider() {
        return ukelonnServletProvider;
    }

    /**
     * Get a {@link File} referencing a resource.
     *
     * @param resource the name of the resource to get a File for
     * @return a {@link File} object referencing the resource
     * @throws URISyntaxException
     */
    public static File getResourceAsFile(String resource) throws URISyntaxException {
        return Paths.get(TestUtils.class.getResource(resource).toURI()).toFile();
    }

    /***
     * Fake injected OSGi services.
     */
    public static void setupFakeOsgiServices() {
        ukelonnServletProvider = new UkelonnServletProvider();
        UkelonnDatabaseProvider ukelonnDatabaseProvider = new UkelonnDatabaseProvider();
        DataSourceFactory derbyDataSourceFactory = new DerbyDataSourceFactory();
        ukelonnDatabaseProvider.setDataSourceFactory(derbyDataSourceFactory);
        LogService logservice = new MockLogService();
        ukelonnDatabaseProvider.setLogService(logservice);

        ukelonnServletProvider.setUkelonnDatabase(ukelonnDatabaseProvider.get());
        ukelonnServletProvider.setLogservice(logservice);

        ShiroFilterProvider shiroFilterProvider = new ShiroFilterProvider();
        shiroFilterProvider.setUkelonnDatabase(ukelonnDatabaseProvider.get());
    }

    /***
     * Clear any (fake or non-fake) injected OSGi services.
     *
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void releaseFakeOsgiServices() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        UkelonnServletProvider ukelonnService = (UkelonnServletProvider) UkelonnServletProvider.getInstance();
        if (ukelonnService != null) {
            ukelonnService.setUkelonnDatabase(null); // Release the database

            // Release the UkelonnService
            Field ukelonnServiceInstanceField = UkelonnServletProvider.class.getDeclaredField("instance");
            ukelonnServiceInstanceField.setAccessible(true);
            ukelonnServiceInstanceField.set(null, null);
        }

        dropTestDatabase();
    }

    public static void dropTestDatabase() {
        try {
            DriverManager.getConnection("jdbc:derby:memory:ukelonn;drop=true");
        } catch (SQLException e) {
            // Just eat any exceptions quietly. The database will be cleaned up
        }
    }

    public static void restoreTestDatabase() {
    	dropTestDatabase();
    	UkelonnDatabaseProvider ukelonnDatabaseProvider = (UkelonnDatabaseProvider) UkelonnServletProvider.getInstance().getDatabase();
        DataSourceFactory derbyDataSourceFactory = new DerbyDataSourceFactory();
        ukelonnDatabaseProvider.setDataSourceFactory(derbyDataSourceFactory);
    }

}
