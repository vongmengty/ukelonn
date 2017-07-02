package no.priv.bang.ukelonn.bundle.postgresql.db.mocks;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

@SuppressWarnings("rawtypes")
public class MockLogService implements LogService {
    final String[] errorLevel = {"", "[ERROR] ", "[WARNING] ", "[INFO] ", "[DEBUG] "};

    public void log(int level, String message) {
        System.err.println(errorLevel[level] + message);
    }

    public void log(int level, String message, Throwable exception) {
        System.err.println(errorLevel[level] + message + "  Exception:");
        exception.printStackTrace();
    }

    public void log(ServiceReference sr, int level, String message) {
        // TODO Auto-generated method stub

    }

    public void log(ServiceReference sr, int level, String message, Throwable exception) {
        // TODO Auto-generated method stub

    }

}